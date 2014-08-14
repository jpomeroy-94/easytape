package com.jeffreypomeroy.easytape;

import java.io.IOException;
import java.io.InputStream;

import com.jeffreypomeroy.util.FileUtil;
import com.jeffreypomeroy.util.GenUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CameraActivity extends Activity implements View.OnClickListener {
	// ImageButton ib;
	// ImageView iv;
	// Button b;
	private Intent i;
	private Bitmap bmp;
	private final static int cameraData = 0;
	private int dmy = 0;
	private static ContentValues cameraInfoCv;
	private static boolean wentToInfo = false;
	private boolean fromOnCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.photo);
		fromOnCreate = true;
		wentToInfo=false;
		initialize();
		// InputStream is =
		// getResources().openRawResource(R.drawable.ic_launcher);
		// bmp = BitmapFactory.decodeStream(is);
		dmy=1;
		callCamera();

	}

	private void initialize() {
		// iv = (ImageView) findViewById(R.id.ivReturnedPic);
		// ib = (ImageButton) findViewById(R.id.ibTakePic);
		// b = (Button) findViewById(R.id.bSetWall);
		// b.setOnClickListener(this);
		// ib.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * switch(v.getId()){ case R.id.bSetWall: try {
		 * getApplicationContext().setWallpaper(bmp); } catch (IOException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } break; case
		 * R.id.ibTakePic: i = new
		 * Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 * startActivityForResult(i,cameraData); break; }
		 */
	}

	@Override
	protected void onResume() {
		dmy = 1;
		// TODO Auto-generated method stub
		super.onResume();
		dmy = 1;
		if (MainActivity.doIGoHome()) {
			dmy = 1;
			// xxxf: blows up when doing finish
			finish();
			dmy = 1;
		} else {
			dmy = 1;
			// wentToInfo doesnt get reset to false if you go home
			if (wentToInfo) {
				dmy = 1;
				wentToInfo = false;
				callCamera();
			} else {
				dmy = 1;
				// fromOnCreate=false;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		dmy = 1;
		super.onActivityResult(requestCode, resultCode, data);
		dmy = 1;
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			dmy = 1;// good
			bmp = (Bitmap) extras.get("data");
			cameraInfoCv = FileUtil.saveImage(bmp);
			dmy = 1;// good
			GenUtil.setFromLocation(GenUtil.CAMEFROMCAMERA);
			dmy = 1;// good
			Class ourClass;
			try {
				ourClass = Class.forName(this.getPackageName()
						+ ".VideoInfoActivity");
				dmy = 1;
				Intent ourIntent = new Intent(CameraActivity.this, ourClass);
				dmy = 1;
				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dmy = 1;
			// iv.setImageBitmap(bmp);
		} else {
			dmy = 1;
		}
	}

	// ------------
	private void callCamera() {
		dmy = 1;
		//Intent i2 = new Intent(android.provider.MediaStore.)
		i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i, cameraData);
	}

	// --- static routines
	public static ContentValues getCameraData() {
		return cameraInfoCv;
	}

	public static void setWentToInfo() {
		wentToInfo = true;
	}
}