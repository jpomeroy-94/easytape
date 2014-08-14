package com.jeffreypomeroy.easytape;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeffreypomeroy.util.DateUtil;
import com.jeffreypomeroy.util.DbUtil;
import com.jeffreypomeroy.util.FileUtil;
import com.jeffreypomeroy.util.GenUtil;
import com.jeffreypomeroy.util.ServerUtil;

public class VideoInfoActivity extends Activity implements View.OnClickListener {

	
	//--- constants
	
	private final String TABLENAME = "mediainfo";
	private final String KEYNAME = "mediainfoid";
/*
	private final String[] COLUMNNAMES = { "mediainfoid", "date", "time",
			"filename", "category", "albumname", "path", "displayname", "filetype", "description",
			"gps", "size", "youtubeid" };
	
	private final String SELECTCRITERIA = " ";

	private final String[] SORTCRITERIA = { " by-dsnd date, time ",
			" by title ", " by filetype, title " };
*/
	// CREATE TABLE byrdlandevents (_d INTEGER PRIMARY KEY AUTOINCREMENT,
	// eventdate TEXT NOT NULL,";

/*	private final String COLUMNDEFINITIONS = "mediainfoid INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "date TEXT NOT NULL, "
			+ "time TEXT NOT NULL, "
			+ "filename TEXT NOT NULL UNIQUE, "
			+ "category TEXT NOT NULL, "
			+ "albumname TEXT NOT NULL, "
			+ "path TEXT NOT NULL,"
			+ "title TEXT, "
			+ "filetype TEXT NOT NULL, "
			+ "filesize TEXT,"
			+ "description TEXT,"
			+ "gps TEXT," 
			+ "youtubeid TEXT";

	private final String CREATEQUERY = "CREATE TABLE " + TABLENAME + " ("
			+ COLUMNDEFINITIONS + ")";
*/
	private final String ACTIVEMSG = "Activate";
	
	private final String RECYCLEMSG = "Recycle";
	
	//--- layouts
	
	private TextView dateTv, timeTv, pathTv, fileNameTv, gpsTv, fileTypeTv,
			fileSizeTv;

	private EditText titleEt, descriptionEt;

	private Button fileExitBt, fileBt, playBt, recycleBt;

	//--- data control 
	
	private String mediaInfoId;

	HashMap<String, Object> dataPass;
	ArrayList<ContentValues> theRows;
	ContentValues aRow;
	
	//--- fields
	
	String thePath;
	String fileName;
	
	//--- static 
	
	public static String mediaFullPath;
	
	//--- temp 
	
	private int dmy;
	ContentValues tempCv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//xxxf: need to figure out why this works
		CameraActivity.setWentToInfo();
		dmy=1;
		try {
		setContentView(R.layout.activity_video_info);
		}
		catch (Exception e){
			dmy=1;
		}
		initVariables();
		setupFormFields();
		DbUtil.setContext(VideoInfoActivity.this);
		dmy = 1;
		//xxxf: below is debug table reset: 
		//DbUtil.dropTable(TABLENAME);//xxxf; temp override for testing
		Boolean isItThere = doesTableExist(TABLENAME);
		dmy = 1;
		if (!isItThere) {
			// was created so have to populate it with all media/
			ServerUtil.runServerStack("easytapeinit");
			HashMap<String, Object> dataPass = new HashMap<String, Object>();
			dataPass.put("status", "getmedianames");
			dataPass = FileUtil.getMediaNames(dataPass);
			String[] mediaList = (String[]) dataPass.get("medianames");
			if (mediaList.length > 0) {
				dmy = 1;// xxxf: blow up below
				buildDbRows(dataPass);
				dmy = 2;
				getLatestMediaEntry();
			}
		} else {
			// You may be here from Video Capture, or Video List
			dmy=1;
			switch (GenUtil.getFromLocation()){
			case GenUtil.CAMEFROMVIDEOLIST:
				break;
			case GenUtil.CAMEFROMCAMERA:
				tempCv = CameraActivity.getCameraData();
			//xxxf: wip
				dmy=1;
				thePath = tempCv.getAsString("path");
				dmy=1;
				fileName=tempCv.getAsString("filename");
				dmy=1;
				buildRow(thePath, fileName);
				dmy=1;
				break;
			case GenUtil.CAMEFROMCAMCORDER:
				thePath = CaptureActivity.outPutPath;
				dmy = 1;
				fileName = GenUtil.getFileFromPath(thePath);
				dmy = 1;
				buildRow(thePath, fileName);
				dmy = 1;
				break;
			}
			getLatestMediaEntry();
		}
	}

	private void initVariables() {
		dataPass = new HashMap<String, Object>();
		theRows = new ArrayList<ContentValues>();
		aRow = new ContentValues();
	}

	private void setupFormFields() {
		dmy=1;
		dateTv = (TextView) findViewById(R.id.date_tv);
		dmy=1;
		timeTv = (TextView) findViewById(R.id.time_tv);
		pathTv = (TextView) findViewById(R.id.path_tv);
		fileNameTv = (TextView) findViewById(R.id.filename_tv);
		gpsTv = (TextView) findViewById(R.id.gps_tv);
		fileTypeTv = (TextView) findViewById(R.id.filetype_tv);
		fileSizeTv = (TextView) findViewById(R.id.filesize_tv);
dmy=1;
try {
		titleEt = (EditText) findViewById(R.id.title_et);
} catch (Exception e){
	dmy=1;
}
		dmy=1;
		descriptionEt = (EditText) findViewById(R.id.description_et);
dmy=1;
		fileBt = (Button) findViewById(R.id.file_bt);
		fileExitBt = (Button) findViewById(R.id.fileexit_bt);
		playBt = (Button) findViewById(R.id.play_bt);
		recycleBt = (Button) findViewById(R.id.recycle_bt);
dmy=1;
		fileBt.setOnClickListener(this);
		fileExitBt.setOnClickListener(this);
		playBt.setOnClickListener(this);
		recycleBt.setOnClickListener(this);
		dmy=1;
		String currentCategory = VideoListActivity.getCurrentCategory();
		if ("recycle".equalsIgnoreCase(currentCategory)){
			recycleBt.setText(ACTIVEMSG);
		}
		else {
			recycleBt.setText(RECYCLEMSG);
		}
		
	}

	private boolean doesTableExist(String TABLENAME) {
		dmy = 1;
		Boolean itExists = false;
		try {
			itExists = DbUtil.checkTable(TABLENAME);
		} catch (Exception e) {
			dmy = 1;
		}
		dmy = 1;
		return itExists;
	}

	private void buildDbRows(HashMap<String, Object> dataPass) {

		dmy = 1;
		String[] mediaList = (String[]) dataPass.get("medianames");
		String basePath = (String) dataPass.get("basepath");
		dmy = 1;
		for (int lp = 0; lp < mediaList.length; lp++) {
			String usePath = basePath + mediaList[lp];
			buildRow(usePath, mediaList[lp]);
		}
		dmy = 1;
	}

	
	private void buildRow(String pathName, String fileName) {
		String theDate = DateUtil.getTodayDate();
		String theTime = DateUtil.getTodayTime();
		dmy = 1;// xxxf: theType comes up unknown
		String theType = GenUtil.getFileType(fileName);
		Long fileSize = FileUtil.getFileSize(pathName);
		String fileSizeStr = String.valueOf(fileSize);
		aRow.clear();
		aRow.put("date", theDate);
		aRow.put("time", theTime);
		aRow.put("filetype", theType);
		aRow.put("filename", fileName);
		aRow.put("path", pathName);
		aRow.put("filesize", fileSize);
		aRow.put("category", "active");
		aRow.put("albumname", "init");
		theRows.clear();
		theRows.add(aRow);
		dataPass.clear();
		dataPass.put("therows", theRows);
		dataPass.put("tablename", TABLENAME);
		dataPass.put("keyname", KEYNAME);
		dmy=1;
		dataPass=DbUtil.writeDb(dataPass);
		dmy=1;
	}

	private void getLatestMediaEntry() {
		dmy = 1;
		// xxxf: not done here
		Long keyId = DbUtil.getLastKeyId();
		dmy = 1;
		dataPass.clear();
		dataPass.put("tablename", "mediainfo");
		dataPass.put("status", "read a row");
		dataPass.put("selectname1", "mediainfoid");
		dataPass.put("selectvalue1", String.valueOf(keyId));
		dmy = 1;
		dataPass = DbUtil.readDb(dataPass);
		dmy = 1;
		theRows = (ArrayList<ContentValues>) dataPass.get("therows");
		dmy = 1;
		aRow.clear();
		aRow = theRows.get(0);
		String theDate = aRow.getAsString("date");
		String theTime = aRow.getAsString("time");
		String theFileName = aRow.getAsString("filename");
		String theFileType = aRow.getAsString("filetype");
		String thePath = aRow.getAsString("path");
		String theTitle = aRow.getAsString("title");
		String theDesc = aRow.getAsString("description");
		String fileSize = aRow.getAsString("filesize");
		dateTv.setText(theDate);
		timeTv.setText(theTime);
		pathTv.setText(thePath);
		fileNameTv.setText(theFileName);
		fileTypeTv.setText(theFileType);
		// need to get gps and put in here
		String fileSizeDisplay = GenUtil.convertToMeg(fileSize);
		fileSizeTv.setText(fileSizeDisplay);
		titleEt.setText(theTitle);
		descriptionEt.setText(theDesc);
		mediaFullPath=thePath;
		dmy = 1;
	}
	public static String getMediaPath(){
		return mediaFullPath;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.video_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		dmy=1;
		int theId = item.getItemId();
		dmy=1;
		switch (theId){
		case R.id.menu_info_home:
			VideoListActivity.setLastSelectedRow(-1);
			MainActivity.setGoHome();
			finish();
			break;
		case R.id.menu_info_list:
			VideoListActivity.setLastSelectedRow(-1);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		dmy=1;
		super.onResume();
		if (MainActivity.doIGoHome()){
			dmy=1;
			//xxxf: blows up when doing finish
			finish();
			dmy=1;
		}
		dmy=1;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dmy=1;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dmy=1;
	}

	@Override
	public void onClick(View theView) {
		// TODO Auto-generated method stub
		String theTitle, theDescription, mediaInfoId;
		Long theKeyId;
		dmy=1;
		int clickType = theView.getId();
		switch (clickType) {
		case R.id.file_bt:
			theRows.clear();
			theTitle = titleEt.getText().toString();
			theDescription = descriptionEt.getText().toString();
			theKeyId = DbUtil.getLastKeyId();
			mediaInfoId = String.valueOf(theKeyId);
			aRow.put("mediainfoid", mediaInfoId);
			aRow.put("title", theTitle);
			aRow.put("description", theDescription);
			theRows.add(aRow);
			dmy=1;
			dataPass.put("tablename", TABLENAME);
			dataPass.put("keyname", "mediainfoid");
			dataPass.put("therows", theRows);
			dataPass = DbUtil.writeDb(dataPass);
			DbUtil.storeUpdatedRow(aRow);
			dmy = 1;
			finish();
			break;
		case R.id.play_bt:
			Class ourClass;
			dmy=1;
			try {
				//String runClass="com.jeffreypomeroy.easytape.PlaybackActivity";
				String runClass="com.jeffreypomeroy.easytape.PlayerActivity";
				ourClass = Class.forName(runClass);
				Intent ourIntent = new Intent(VideoInfoActivity.this, ourClass);
				startActivity(ourIntent);
				dmy=1;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.fileexit_bt:
			theRows.clear();
			theTitle = titleEt.getText().toString();
			theDescription = descriptionEt.getText().toString();
			theKeyId = DbUtil.getLastKeyId();
			mediaInfoId = String.valueOf(theKeyId);
			aRow.put("mediainfoid", mediaInfoId);
			aRow.put("title", theTitle);
			aRow.put("description", theDescription);
			theRows.add(aRow);
			dmy=1;
			dataPass.put("tablename", TABLENAME);
			dataPass.put("keyname", "mediainfoid");
			dataPass.put("therows", theRows);
			dataPass = DbUtil.writeDb(dataPass);
			DbUtil.storeUpdatedRow(aRow);
			dmy = 1;
			VideoListActivity.setLastSelectedRow(-1);
			MainActivity.setGoHome();
			finish();
			break;
		case R.id.recycle_bt:
			dmy=1;
			theKeyId = DbUtil.getLastKeyId();
			mediaInfoId = String.valueOf(theKeyId);
			theTitle = titleEt.getText().toString();
			theDescription = descriptionEt.getText().toString();
			String theCurrentCategory=VideoListActivity.getCurrentCategory();
			String theOtherCategory;
			if ("recycle".equalsIgnoreCase(theCurrentCategory)){
				theOtherCategory="active";
			}
			else {
				theOtherCategory="recycle";
			}
			dmy=1;
			aRow.clear();
			aRow.put("mediainfoid", mediaInfoId);
			//theOtherCategory="activee";//xxxf
			aRow.put("category", theOtherCategory);
			aRow.put("title", theTitle);
			aRow.put("description", theDescription);
			theRows.clear();
			theRows.add(aRow);
			dmy=1;
			dataPass.put("tablename", TABLENAME);
			dataPass.put("keyname", "mediainfoid");
			dataPass.put("therows", theRows);
			dataPass = DbUtil.writeDb(dataPass);
			dmy=1;
			DbUtil.storeUpdatedRow(aRow);
			dmy = 1;
			finish();
		}
	}
}
