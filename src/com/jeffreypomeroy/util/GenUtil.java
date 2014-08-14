package com.jeffreypomeroy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;

public class GenUtil {
	public static final String FORWARDSLASH = "/";
	public static final String PERIOD = ".";
	
	public static int fromLocation = 0;
	
	public static final int CAMEFROMVIDEOLIST = 1;
	public static final int CAMEFROMCAMCORDER = 2;
	public static final int CAMEFROMCAMERA = 3;
	
	public static int dmy;
	private static ArrayList<String> theAryLst;
	private static Context utilContext;
	
	
	public static void setFromLocation(int setFromLocation){
		fromLocation = setFromLocation;
	}
	public static int getFromLocation(){
		return fromLocation;
	}
		
	public static String getFileType(String theFile){
		String theType = "unknown";
		dmy=1;
		try {
			String[] theFileAry = theFile.split("\\.");

		dmy=1;//xxxf: above line does not create good ary
		int theLen=theFileAry.length;
		if (theLen>1){
			theType=theFileAry[(theLen-1)];
		}
		dmy=1;
		theType=theType.toLowerCase(Locale.US);
		}
		catch (Exception e){
			dmy=1;
		}
		return theType;
	}

	public static String getFileFromPath(String thePath){
		String fileName = "unknown";
		String[] thePathAry = thePath.split(FORWARDSLASH);
		if (thePathAry.length>1){
			fileName = thePathAry[(thePathAry.length-1)];
		}
		return fileName;
	}
	public static String convertToMeg(String fileSize){
		int fileSizeInt = Integer.valueOf(fileSize);
		int fileSizeMegInt = fileSizeInt/1000000;
		if (fileSizeMegInt>0){
			fileSize=String.valueOf(fileSizeMegInt)+"M";
		}
		else {
			int fileSizeKilInt = fileSizeInt/1000;
			fileSize=String.valueOf(fileSizeKilInt)+"K";
		}
		return fileSize;
	}
	static void doSplit(String theLine, String theDelim) {
		theAryLst = new ArrayList<String>();
		String theFieldStr = "";
		int startPos, endPos, theDelimLen;
		startPos = 0;
		theDelimLen = theDelim.length();
		Boolean dontGetOut = true;

		while (dontGetOut) {
			endPos = theLine.indexOf(theDelim, startPos);
			if (endPos > -1) {
				theFieldStr = theLine.substring(startPos, endPos);
				try {
					theAryLst.add(theFieldStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				endPos += theDelimLen;
				startPos = endPos;
			} else {
				theFieldStr = theLine.substring(startPos, theLine.length());
				theAryLst.add(theFieldStr);
				dontGetOut = false;
			}

		}
	}

	static String getTheField(int entryNo) {
		String theReturn = theAryLst.get(entryNo).toString();
		return theReturn;
	}
	
	//--------------- error display
	
	static void errorDisplay(String errorMsg){
		
		AlertDialog.Builder messageBox = new AlertDialog.Builder(utilContext);
		messageBox.setMessage(errorMsg);
		messageBox.setPositiveButton("ok", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
			
		});
		
		messageBox.show();
	}

	static class JsonLib {

		// --- generic defs

		static JSONObject jsonObj, jsonRowObj;
		static JSONArray jsonAry, jsonRowAry;

		// --- table meta defs

		static JSONObject tableMetaInfoObj;
		static JSONObject allTablesMetaInfoObj = new JSONObject();

		static void getFromJson(String theJson) throws JSONException {

			try {
				// object xxxf
				jsonAry = new JSONArray(theJson); // seems to always be
													// byrdlandevents json
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		static int getLength(int posNo) {
			return jsonAry.length();// jsonAry is null here xxxf
		}

		static void getRow(int posNo, int rowNo) {
			try {
				// jsonRowAry = jsonAry.getJSONArray(rowNo);//this aborts
				jsonRowObj = jsonAry.getJSONObject(rowNo);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}

		static String getSqlColsStr(String tableName, int posNo)
				throws JSONException {
			String theReturn;
			Object theColNameObj, theColValueObj;
			String theColName, theColValue, theColType;
			String theColNames = "(";
			String theColValues = "(";
			String delim = "";
			String sqt = "'";
			String nsqt = "";
			String useSqt;
			tableMetaInfoObj = allTablesMetaInfoObj.getJSONObject(tableName);
			Iterator<?> theColNamesItr = jsonRowObj.keys();// blows up around here
			while (theColNamesItr.hasNext()) {
				// column names
				theColNameObj = theColNamesItr.next();
				theColName = theColNameObj.toString();
				// - need to check out column name to see its type
				theColType = tableMetaInfoObj.getString(theColName);
				if (!theColType.equalsIgnoreCase("serial")) {
					theColNames += (delim + theColName);
					// column values
					theColValueObj = jsonRowObj.get(theColName);
					theColValue = theColValueObj.toString();
					if (theColType.equalsIgnoreCase("varchar")
							|| theColType.equalsIgnoreCase("date")
							|| theColType.equalsIgnoreCase("time")) {
						useSqt = sqt;
					} else {
						useSqt = nsqt;
					}
					theColValues += (delim + useSqt + theColValue + useSqt);
					delim = ",";
				}
			}
			// theReturn+=theColName+",";
			theReturn = theColNames + ") values " + theColValues + ")";
			return theReturn;
		}

		static void saveMeta(String tableName, String tableMeta)
				throws JSONException {
			tableMetaInfoObj = new JSONObject(tableMeta);
			allTablesMetaInfoObj.put(tableName, tableMetaInfoObj);
		}

		static String getSqlValsStr(int posNo) {

			String theReturn = "values (";

			return theReturn;
		}
	}
}