package com.jeffreypomeroy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.graphics.Bitmap;

public class FileUtil {
	public static final String VIDEOBASEPATH = "/storage/emulated/0/Pictures/CameraSample/";
	public static int dmy;

	public static HashMap<String,Object> getMediaNames(HashMap<String,Object> dataPass) {
		File thePath = new File(VIDEOBASEPATH);
		String[] mediaList = thePath.list();
		dataPass.put("basepath", VIDEOBASEPATH);
		dataPass.put("medianames", mediaList);
		return dataPass;
	}
	public static Long getFileSize(String thePath){
		Long fileSize;
		File fileWork = new File(thePath);
		fileSize = fileWork.length();
		return fileSize;
	}
	public static void deleteFile(String thePath){
		dmy=1;
		File thePathFile = new File(thePath);
		thePathFile.delete();
		dmy=1;
	}
	public static ContentValues saveImage(Bitmap bmp){
		//xxxf: do I need to convert bmp to png format here?
		String theDate = DateUtil.getTodayDate(DateUtil.SORT);
		String theTime = DateUtil.getTodayTime(DateUtil.SECONDOFDAY);
		String bmpName = "IMG"+"_"+theDate+"_"+theTime+".png";
		String thePath = VIDEOBASEPATH.concat(bmpName);
		try {
			FileOutputStream out = new FileOutputStream(thePath);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			 out.close();
		} catch (Exception e){
			dmy=1;
		}
		dmy=1;
		ContentValues returnCv = new ContentValues();
		returnCv.put("path", thePath);
		returnCv.put("filename", bmpName);
		return returnCv;
	}
}
