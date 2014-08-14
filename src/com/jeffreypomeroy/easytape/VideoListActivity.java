package com.jeffreypomeroy.easytape;

import java.util.ArrayList;
import java.util.HashMap;

import com.jeffreypomeroy.util.DbUtil;
import com.jeffreypomeroy.util.FileUtil;
import com.jeffreypomeroy.util.GenUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class VideoListActivity extends Activity {

	//--- layout objects
	
	TextView currentCategoryTv;
	ListView videoListLv;
	
	//--- data control objects
	
	//ArrayList<String> videoList;
	ArrayList<String> videoKeyList;
	HashMap<String, Object> dataPass;
	ArrayList<ContentValues> videoListInfo;
	ArrayAdapter<ContentValues> videoListAdapter;
	ContentValues videoInfo;
	
	//--- static objects
	
	static int lastSelectedRow = -1;

	static String currentCategory = "active";
	
	//--- debug
	
	int dmy;

	//--- overrides
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		DbUtil.setContext(VideoListActivity.this);
		dmy = 1;
		currentCategory="active";//xxxf
		setupMenuDisplay();
		dmy=1;
		populateVideoList();
		dmy=1;
		attachVideoList();
		dmy=1;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		dmy=1;
		super.onResume();
		if (MainActivity.doIGoHome()){
			lastSelectedRow=-1;
			dmy=1;
			finish();
		}
		else {
		dmy = 1;
		if (lastSelectedRow > -1) {
			//xxxf: below creates lastRowUpdated as null
			ContentValues lastRowUpdated = DbUtil.getLastUpdatedRow();
			//xxxf: blows up below
			dmy=1;
			String theCategory = lastRowUpdated.getAsString("category");
			dmy=1;
			boolean doit = false;
			//either changed from active>recycle or recycle>active so delete from display array
			if (!currentCategory.equalsIgnoreCase(theCategory)){
				dmy=1;
				videoListInfo.remove(lastSelectedRow);
				videoKeyList.remove(lastSelectedRow);
				dmy=1;
			} else {
				videoListInfo.set(lastSelectedRow, lastRowUpdated);
				dmy=1;
			}
			dmy = 1;
			refreshVideoList();
			lastSelectedRow = -1;
			dmy = 1;
		}
		dmy=1;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		case R.id.menu_list_delete:
			emptyRecyclingBin();
			if ("recycle".equalsIgnoreCase(currentCategory)){
				videoListInfo.clear();
				videoKeyList.clear();
				this.populateVideoList();
				this.attachVideoList();
				dmy=1;
				this.refreshVideoList();
			}
			break;
		case R.id.menu_list_recycle:
			this.toggleCurrentCategory();
			this.populateVideoList();
			this.attachVideoList();
			//this.refreshVideoList();
			if (this.currentCategory.equalsIgnoreCase("recycle")){
				item.setTitle("Display Active Videos");
			}
			else {
				item.setTitle("Display Recycle Bin");
			}
			break;
		case R.id.menu_list_exit:
			finish();
			break;
		}
		return true;
	}
	//--- static
	
	protected static void setLastSelectedRow(int updateRowNo) {
		lastSelectedRow = updateRowNo;
	}
	
	protected static String getCurrentCategory(){
		return currentCategory;
	}
	//--- utilities
	
	private void refreshVideoList(){
		dmy=1;
		try {
			videoListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			dmy = 1;
		}
	}
		
	protected void toggleCurrentCategory(){
		if ("active".equalsIgnoreCase(currentCategory)){
			currentCategory="recycle";
		}
		else {
			currentCategory="active";
		}
		currentCategoryTv.setText(currentCategory);
	}

	private void setupMenuDisplay() {
		dmy = 1;// below videoListLv is null
		videoListLv = (ListView) findViewById(R.id.videoListlv);
		currentCategoryTv = (TextView) findViewById(R.id.currentcategorytv);
		dmy = 1;
		//videoList = new ArrayList<String>();
		videoKeyList = new ArrayList<String>();
		videoListInfo = new ArrayList<ContentValues>();
		videoInfo = new ContentValues();
		dataPass = new HashMap<String, Object>();
	}
	private void populateVideoList(){
		//
		dataPass.clear();
		dataPass.put("tablename", "mediainfo");
		dataPass.put("keyname", "mediainfoid");
		dataPass.put("selectname1", "category");
		dataPass.put("selectvalue1", currentCategory);
		dataPass.put("sortname1", "date");
		dataPass.put("sortname2", "time");
		dataPass.put("sortname1direction", "desc");
		dataPass.put("sortname2direction", "desc");
		dataPass.put("columnlist",
				"mediainfoid, title, filename, filesize, date, time");
		dmy=1;
		dataPass = DbUtil.readDb(dataPass);
		//
		dmy = 1;
		videoListInfo.clear();
		//videoList.clear();
		videoKeyList.clear();
		videoListInfo = (ArrayList<ContentValues>) dataPass.get("therows");

		dmy=1;
		int noVideos = videoListInfo.size();
		dmy = 1;
		for (int videoLp = 0; videoLp < noVideos; videoLp++) {
			videoInfo=videoListInfo.get(videoLp);
			videoKeyList.add(videoInfo.getAsString("mediainfoid"));
		}
		dmy=1;
	}
	private void attachVideoList(){
		dmy = 1;
		try {
			videoListAdapter = new NewAdapter(this,	videoListInfo);
			dmy=1;
			videoListLv.setAdapter(videoListAdapter);
			dmy=1;
		} catch (Exception e) {
			dmy = 1;
		}
		videoListLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int selectedRow, long arg3) {
				// TODO Auto-generated method stub
				dmy = 1;
				lastSelectedRow = selectedRow;
				GenUtil.setFromLocation(GenUtil.CAMEFROMVIDEOLIST);
				String lastKeyId = videoKeyList.get(selectedRow);
				// below sets lastKeyIdLong as null
				Long lastKeyIdLong = Long.parseLong(lastKeyId);
				dmy = 1;// blows up below
				try {
					DbUtil.setLastKeyId(lastKeyIdLong);
				} catch (Exception e) {
					dmy = 1;
				}
				dmy = 1;
				Class ourClass;
				try {
					ourClass = Class
							.forName("com.jeffreypomeroy.easytape.VideoInfoActivity");
					Intent ourIntent = new Intent(VideoListActivity.this,
							ourClass);
					startActivity(ourIntent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		dmy = 1;
	}
	

	private int emptyRecyclingBin(){
		int noDeleted=0;
		String thePath;
		ContentValues aRow;
		dataPass.clear();
		dataPass.put("tablename", "mediainfo");
		dataPass.put("keyname", "mediainfoid");
		dataPass.put("selectname1", "category");
		dataPass.put("selectvalue1", "recycle");
		dataPass = DbUtil.readDb(dataPass);
		videoListInfo = (ArrayList<ContentValues>) dataPass.get("therows");
		noDeleted=videoListInfo.size();
		for (int delLp=0; delLp<noDeleted;delLp++){
			aRow=(ContentValues) videoListInfo.get(delLp);
			thePath=aRow.getAsString("path");
			FileUtil.deleteFile(thePath);
			dmy=1;
		}
		dmy=1;
		String theQuery="update mediainfo set category = \"delete\" where category=\"recycle\"";
		DbUtil.runUpdateQuery(theQuery);
		dmy=1;
		return noDeleted;
	}
	private class NewAdapter extends ArrayAdapter<ContentValues>{
		  
		private final Context context;
		private final ArrayList<ContentValues> values;
		private ContentValues theContentValues;

		public NewAdapter(Context context, ArrayList<ContentValues> values) {
		    super(context, R.layout.activity_video_list_listview, values);
		    this.context = context;
		    this.values = values;
		    dmy=1;
		  }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			dmy=1;
			View rowView = null;
			  LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  dmy=1;
			  try {
				  //xxxf: blows up below
				  dmy=1;
				   rowView = inflater.inflate(R.layout.activity_video_list_listview, parent, false);

				    dmy=1;
				    TextView theDateTv = (TextView) rowView.findViewById(R.id.videolistdatetv);
				    TextView theTimeTv = (TextView) rowView.findViewById(R.id.videolisttimetv);
				    TextView theFileSizeTv = (TextView) rowView.findViewById(R.id.videolistfilesizetv);
				    TextView theTitleTv = (TextView) rowView.findViewById(R.id.videolisttitletv);
				    
				    //
				    dmy=1;
				    videoInfo=values.get(position);
					String theDate = videoInfo.getAsString("date");
					String theTime = videoInfo.getAsString("time");
					String theTitle = videoInfo.getAsString("title");
					String theFileName = videoInfo.getAsString("filename");
					String fileSize = videoInfo.getAsString("filesize");
					String fileSizeDisplay = "("+GenUtil.convertToMeg(fileSize)+")";
					dmy=1;
					if ("".equalsIgnoreCase(theTitle) || theTitle == null) {
						dmy=1;
						theTitle=theFileName;
					}
					dmy=1;
					//
					theDateTv.setText(theDate);
					theTimeTv.setText(theTime);
					theFileSizeTv.setText(fileSizeDisplay);
					theTitleTv.setText(theTitle);
					dmy = 1;
			  } catch (Exception e){
				  dmy=1;
			  }
				    /*
				    TextView textView = (TextView) rowView.findViewById(R.id.label);
				    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
				    textView.setText(values[position]);
				    // change the icon for Windows and iPhone
				    String s = values[position];
				    if (s.startsWith("iPhone")) {
				      imageView.setImageResource(R.drawable.no);
				    } else {
				      imageView.setImageResource(R.drawable.ok);
				    }
					*/
				    return rowView;
				  }
		}
		
	}
