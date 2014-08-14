package com.jeffreypomeroy.easytape;

import java.util.ArrayList;

import com.jeffreypomeroy.easytape.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	int dmy;
	static int dmy2;
	private static String userName = "guest";
	
	String[] classes = {"LoginActivity","CaptureActivity","CameraActivity","VideoListActivity","finish"};
	String[] classNames = {"Logon","Camcorder","Camera","Media","Exit"};
	private static final String packagePrefix = "com.jeffreypomeroy.easytape.";
	TextView topDisplay;
	static protected boolean goHome = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		topDisplay = (TextView) findViewById(R.id.usernamedisplaytv);
//works here	
		ListView theListView = (ListView) findViewById(R.id.optionslv);
		dmy=1;
		final ArrayList<String> theList = new ArrayList<String>();
		
		for (int theListCtr = 0; theListCtr < classes.length; theListCtr++) {
			theList.add(classNames[theListCtr]);
		}
//works here		
		dmy=1;
		theListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.activity_main_listview, theList));
		dmy=1;

		 // fails here
		try {
		theListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int selectionNo,
					long arg3) {
				String classSuffix = classes[selectionNo];
				if (classSuffix.equalsIgnoreCase("finish")){
					finish();
				}
				else {
				try {
					Class ourClass = Class.forName(packagePrefix + classSuffix);
					Intent ourIntent = new Intent(MainActivity.this, ourClass);
					startActivity(ourIntent);
					dmy=1;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				}
			}
			
		});
		} catch (Exception e){
			dmy=1;
		}
	}
	
	@Override
	protected void onResume() {
		dmy=1;
		try {
			//getting a null pointer exception here
			//topDisplay is null
			topDisplay.setText(userName);
		} catch (Exception e){
			dmy=1;
		}
		dmy=1;
		goHome=false;
		super.onResume();
	}

	public static String getUserName(){
		return userName;
	}
	public static void setUserName(String newUserName){
		userName=newUserName;
	}
	protected static void setGoHome(){
		goHome=true;
		dmy2=1;
	}
	protected static boolean doIGoHome(){
		dmy2=1;
		return goHome;
	}
}