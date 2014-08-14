package com.jeffreypomeroy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUtil {
	private static final int DATABASE_VERSION = 1;
	private static String dbName = "workDb";
	private static final String KEY_ROWID = "_d";
	static ArrayList<String> theMembersDesc = new ArrayList<String>();
	private static DbOpen dbOpenHelper;
	private static Context activityContext;
	private static SQLiteDatabase theDatabase;
	private static long lastKeyId;
	private static ContentValues lastRowUpdated;
	private static int dmy;
	private static String tableName, keyName, selectName1, selectValue1,
		sortName1, sortName2;
	// --- define the db open helper class

	private static class DbOpen extends SQLiteOpenHelper {

		private DbOpen(Context context) {
			super(context, dbName, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// db.execSQL(DROPEVENTTABLE);
			// onCreate(db);
		}

	}

	// --- constructor for main class

	public static void setContext(Context sentContext) {
		activityContext = sentContext;
	}

	// --- initialize the database
	// - I guess you only get 1 db so dont need db name to open db

	private static void initDb() {
		dmy=1;
		if (dbOpenHelper == null) {

			// activityContext is not defined since becoming a static class
			dmy=1;//xxxf: possibly bad context below - it is null at this point - fixed
			dbOpenHelper = new DbOpen(activityContext);
			dmy=1;
		}
		if (theDatabase == null) {
			dmy=1;//xxxf blows up below - fixed
			theDatabase = dbOpenHelper.getWritableDatabase();
			dmy=1;
		}
	}

	// --- check if table exists in database and if not then create it

	public static Boolean checkTable(String tableNameStr,
			String tableCreateSqlStr) throws SQLException {
		dmy=1;
		Boolean itExists = false;
		initDb();
		dmy=1;
		Cursor cursor = theDatabase.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableNameStr + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				itExists = true;
				dmy=1;
				cursor.close();
			} else {
				if (!tableCreateSqlStr.isEmpty()) {
					dmy=1;//xxxf need to make the filename be unique
					theDatabase.execSQL(tableCreateSqlStr);
					dmy=1;
				}
				cursor.close();
			}
		}
		return itExists;
	}

	public static Boolean checkTable(String tableNameStr){
	dmy=1;
	Boolean itExists = false;
	initDb();
	dmy=1;
	Cursor cursor = theDatabase.rawQuery(
			"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
					+ tableNameStr + "'", null);
	if (cursor != null) {
		if (cursor.getCount() > 0) {
			itExists = true;
			dmy=1;
			cursor.close();
		} else {
			itExists=false;
			cursor.close();
		}
	}
	return itExists;
	}
	public static Boolean createTable(String tableNameStr){
		try {
			GenUtil.JsonLib.getFromJson(tableNameStr);
			int noItems = GenUtil.JsonLib.getLength(0);
			int retrieveLp;

			for (retrieveLp = 0; retrieveLp < noItems; retrieveLp++) {
				GenUtil.JsonLib.getRow(0, retrieveLp);// hangs on
														// this line
				//String queryCols = GenUtil.JsonLib.getSqlColsStr(theParam1,0);
				//String queryLine = "insert into " + theParam1 + " "	+ queryCols;

				//DbUtil.runUpdateQuery(queryLine);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	// --- clear out complete table

	public static void clearTable(String tableNameStr) throws SQLException {
		initDb();

		theDatabase.execSQL("DELETE FROM " + tableNameStr);
	}

	// --- delete table

	public static void dropTable(String tableNameStr) throws SQLException {
		initDb();
		try {
			theDatabase.execSQL("drop table " + tableNameStr);
		} catch (Exception e) {
			// who cares
		}
	}
	public static void storeUpdatedRow(ContentValues aRow){
		dmy=1;
		lastRowUpdated=aRow;
	}
	public static ContentValues getLastUpdatedRow(){
		return lastRowUpdated;
	}
	//--- writeDb
	
	public static HashMap<String, Object> writeDb(HashMap<String, Object> dataPass){
		ArrayList<ContentValues> theRows = (ArrayList<ContentValues>) dataPass
				.get("therows");
		initDb();
		String keyValue = "none";
		String keyName = (String) dataPass.get("keyname");
		String tableName = (String) dataPass.get("tablename");
		String theStatus = (String) dataPass.get("status");
		int noRows = theRows.size();
		dmy=1;//xxxf
		if (noRows>1){
		ContentValues row1 = theRows.get(0);//xxxf:???
		ContentValues row2 = theRows.get(1);//xxxf:??
		dmy=1;
		}
		dmy=1;
		for (int rowLp = 0; rowLp < noRows; rowLp++) {
			ContentValues aRow = theRows.get(rowLp);
			keyValue="none";
			if (aRow.containsKey(keyName)){
				keyValue = aRow.getAsString(keyName);
			}
			dmy=1;
			//xxxf: blows up below
			if (keyValue.equals("none")) {
				// do insert
				theStatus = "insert";
				dmy=1;
				lastKeyId=theDatabase.insert(tableName, null, aRow);
				dmy=1;
			} else {
				// do update
				//need to remove the keyvalue because wont be updating it
				aRow.remove(keyName);
				theStatus = "update";
				String whereClause=keyName+" = ?";
				String[] keyValues = {keyValue};
				dmy=1;
try {
	dmy=1;
				theDatabase.update(tableName, aRow, whereClause, keyValues);
				dmy=1;
}
catch (Exception e){
	dmy=1;
}
				dmy=1;
			}
		}
		dmy=1;
		dataPass.put("status", theStatus);
		return dataPass;
	}
	//- get Last Key Id
	public static long getLastKeyId(){
		return lastKeyId;
	}
	
	public static void setLastKeyId(long lastKeyIdOverride){
		dmy=1;
		lastKeyId=lastKeyIdOverride;
		dmy=1;
	}

	// --- read db
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> readDb(HashMap<String, Object> dataPass) {
		dmy=1;
		initDb();
		String tableName;
		String columnList = "none";
		String selectName1 = "none";
		String selectValue1 = "none";
		String sortName1 = "none";
		String sortName1Direction = "asc";
		String sortName2 = "none";
		String sortName2Direction = "asc";
		if (dataPass.containsKey("selectvalue1")){
			selectValue1 = (String) dataPass.get("selectvalue1");
		}
		else {
			selectValue1 = null;
		}
		//- get stuff
		tableName = (String) dataPass.get("tablename");
		if (dataPass.containsKey("columnlist")){
			columnList=(String)dataPass.get("columnlist");
		}
		if (dataPass.containsKey("selectname1")){
			selectName1 = (String) dataPass.get("selectname1");

		}
		if (dataPass.containsKey("sortname1")){
			sortName1 = (String) dataPass.get("sortname1");
			if (dataPass.containsKey("sortname1direction")){
				sortName1Direction = (String) dataPass.get("sortname1direction");
			}
		}
		if (dataPass.containsKey("sortname2")){
			sortName2 = (String) dataPass.get("sortname2");
			if (dataPass.containsKey("sortname2direction")){
				sortName2Direction = (String) dataPass.get("sortname2direction");
			}
		}

		String[] selectionArgs = {selectValue1};
		if (selectValue1 == "none"){
			selectionArgs = null;
		}
		//- build query
		String theQuery = "select ";
		if (!columnList.equalsIgnoreCase("none")){
			theQuery+=columnList;
		}
		else {
			theQuery+=" * ";
		}
		theQuery+=" from " + tableName;
		if (!selectName1.equalsIgnoreCase("none")){
			theQuery += " where " + selectName1 + " =  ?";
		}
		if (!sortName1.equalsIgnoreCase("none")){
			theQuery+=" order by "+sortName1;
			if (sortName1Direction == "desc"){
				theQuery+=" desc";
			}
		}
		if (!sortName2.equalsIgnoreCase("none")){
			theQuery+=", "+sortName2;
			if (sortName2Direction == "desc"){
				theQuery+=" desc";
			}
		}
		ArrayList<ContentValues> theRows = new ArrayList<ContentValues>();
		try {
		dmy=1;

			Cursor cursor = theDatabase.rawQuery(theQuery, selectionArgs);

		dmy=1;
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				String[] theColNames = cursor.getColumnNames();
				int colNo = theColNames.length;

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					ContentValues aRow = new ContentValues();
					for (int colLp = 0; colLp < colNo; colLp++) {
						String colName = theColNames[colLp];
						int colPos = cursor.getColumnIndex(colName);
						String colValue = cursor.getString(colPos);
						aRow.put(colName, colValue);
					}
					theRows.add(aRow);
				}
				dataPass.put("status", "ok");
			} else {
				dataPass.put("status", "no rows selected");
			}
		} else {
			dataPass.put("status", "cursor came back null");
		}
		cursor.close();
		}
		catch (Exception e){
			dmy=1;
		}
		dataPass.put("therows", theRows);
		dmy=1;

		return dataPass;
	}
	public static int deleteDb(HashMap dataPass){
		int noDeleted=0;
		tableName=(String) dataPass.get("tablename");
		selectName1=(String) dataPass.get("selectname1");
		selectValue1=(String) dataPass.get("selectvalue1");
		String whereClause=selectName1+" = ?s";
		String[] whereArgs = {selectValue1};
		dmy=1;
		try {
			noDeleted=theDatabase.delete(tableName, whereClause, whereArgs);
		}
		catch (Exception e){
			dmy=1;
		}
		dmy=1;
		return noDeleted;
	}

	// --- run query

	public static void runUpdateQuery(String theQuery) throws SQLException {
		initDb();
		// xxxf need to have update query validation checking here
		theDatabase.execSQL(theQuery);
		//theDatabase.insert(table, nullColumnHack, values)
	}


	// --- close database
	// xxxf: why not called?
	@SuppressWarnings("unused")
	private void close() {
		dbOpenHelper.close();
	}

}
