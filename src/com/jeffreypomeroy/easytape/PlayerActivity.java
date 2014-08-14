package com.jeffreypomeroy.easytape;

import java.io.IOException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

public class PlayerActivity extends Activity implements
		android.view.View.OnClickListener {
	String uriString;
	int dmy;
	// - Audio
	private TextureView mPlaybackView;
	private TextView mAttribView = null;
	private TextView videoPlayed, debug;
	// - Video
	VideoView video_player_view;
	DisplayMetrics dm;
	SurfaceView sur_View;
	MediaController media_Controller;
	MediaPlayer mediaPlayer;
	ProgressBar theProgBar;
	SeekBar theSeekBar;
	Button playerPlayBt, playerPauseBt;
	Uri myUri;
	String theState = "init";
	int videoLength = 0;
	private Handler theHandler = new Handler();

	// - override methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// System.out.println("enter oncreate");
		dmy = 1;
		setContentView(R.layout.activity_player);
		dmy = 1;
		mPlaybackView = (TextureView) findViewById(R.id.PlaybackView);
		mAttribView = (TextView) findViewById(R.id.AttribView);
		theProgBar = (ProgressBar) findViewById(R.id.ProgressbarWait);
		theSeekBar = (SeekBar) findViewById(R.id.SeekBarProgress);
		playerPlayBt = (Button) findViewById(R.id.player_playbt);
		//playerPauseBt = (Button) findViewById(R.id.player_pausebt);
		videoPlayed = (TextView) findViewById(R.id.TextViewPlayed);
		dmy=1;
		debug = (TextView) findViewById(R.id.debug);
		playerPlayBt.setOnClickListener(this);
		//playerPauseBt.setOnClickListener(this);
		video_player_view = (VideoView) findViewById(R.id.video_player_view);
		dmy = 1;
		media_Controller = new MediaController(this);

		dmy = 1;
		theHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateSeekBar();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.playback, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_exit:
			dmy = 1;
			// xxxf: need to clean things up here
			if (mediaPlayer != null){
				mediaPlayer.stop();
				dmy=1;
				mediaPlayer.release();
				mediaPlayer=null;
			}
			dmy=1;
			if (video_player_view != null){
				video_player_view.stopPlayback();
			}
			dmy=1;
			//xxxf: not sure if I should do some kind of videopayerview release here
			finish();
			break;
		case R.id.menu_home:
			MainActivity.setGoHome();
			// xxxf: need to clean things up here
			dmy=1;
			if (mediaPlayer != null){
				//xxxf: blows up below
					dmy=1;
					try {
					mediaPlayer.stop();
					dmy=1;
					mediaPlayer.release();
					dmy=1;
					mediaPlayer=null;
					} catch (Exception e){
						dmy=1;
					}
				dmy=1;
			}
			dmy=1;
			if (video_player_view != null){
				dmy=1;
				video_player_view.stopPlayback();
				video_player_view=null;
			}
			dmy=1;
			finish();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View theView) {
		// TODO Auto-generated method stub
		dmy = 1;
		int theId = theView.getId();
		switch (theId) {
		case R.id.player_playbt:
			dmy = 1;
			theProgBar.setVisibility(theProgBar.INVISIBLE);
			if ("init".equalsIgnoreCase(theState)) {
				theState = "playing";
				playerPlayBt.setVisibility(playerPlayBt.INVISIBLE);
				startPlayback();
			} else {
				video_player_view.resume();
				mediaPlayer.start();
			}
			break;
/*		case R.id.player_pausebt:
			if ("playing".equalsIgnoreCase(theState)) {
				theProgBar.setVisibility(theProgBar.VISIBLE);
				video_player_view.pause();
				mediaPlayer.pause();
			}
*/
		}
	}

	// ==================================
	private void updateSeekBar() {
		if (video_player_view != null){
		if (video_player_view.isPlaying()) {
			if (videoLength == 0) {
				videoLength = video_player_view.getDuration();
			}
			dmy = 1;
			int curPos = video_player_view.getCurrentPosition();
			dmy = 1;
			theSeekBar
					.setProgress((int) (((float) curPos / videoLength) * 100));
			dmy = 1;
			//
			String curPosStr = String.valueOf(curPos);
			//xxxf: need to hide below
			debug.setText("updseekbar: " + String.valueOf(curPos) + "/"
					+ String.valueOf(videoLength));
			dmy = 1;
		}
		}
		theHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateSeekBar();
			}

		}, 1000);
		dmy = 1;

	}

	private void startPlayback() {
		dmy = 1;
		debug.setText("startplayback");
		/*
		theSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar theSeekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				updateSeekBar();
				if (fromUser){ mediaPlayer.seekTo(progress * 1000);}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				updateSeekBar();
				dmy=1;
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				updateSeekBar();
				dmy=1;
			}

		});
		*/
		startAudio();
		startVideo();
	}

	private void startVideo() {

		uriString = VideoInfoActivity.getMediaPath();
		dmy = 1;
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		dmy = 1;
		try {
			video_player_view.setMinimumWidth(width);
		} catch (Exception e) {
			dmy = 1;
		}
		dmy = 1;
		video_player_view.setMinimumHeight(height);
		video_player_view.setMediaController(media_Controller);
		dmy = 1;
		video_player_view.setVideoPath(uriString);
		video_player_view.start();
	}

	private void startAudio() {
		uriString = VideoInfoActivity.getMediaPath();
		myUri = Uri.parse(uriString);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), myUri);
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dmy = 1;
		// need to listen to when it starts
		try {
			mediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							// TODO Auto-generated method stub
							try {
								mp.start();
							} catch (Exception e) {
								dmy = 1;
							}
						}

					});
		} catch (Exception e) {
			dmy = 1;
		}
		try {
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							theProgBar.setVisibility(theProgBar.VISIBLE);
							theState = "init";
							mp.stop();
							//mp = null;
						}
					});
		} catch (Exception e) {
			dmy = 1;
		}

	}

}