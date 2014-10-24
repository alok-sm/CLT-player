package com.alok.clt.activities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.alok.clt.R;

public class IntroActivity extends Activity 
{
	private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
	private static final int logoTime = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		playVideo(); //NORMAL EXECUTION
//		showLogos(); //SKIP INTRO VIDEO
//		goToRegistration(); //SKIP INTRO VIDEO AND LOGOS
		
	}
	private void showLogos()
	{
		setContentView(R.layout.layout_logo);
		Runnable task = new Runnable() 
		{
			public void run() 
			{
				goToRegistration();
		    }
		};
		worker.schedule(task, logoTime, TimeUnit.SECONDS);
	}
	private void goToRegistration()
	{
		Intent i = new Intent(IntroActivity.this,RegistrationActivity.class);
		startActivity(i);
	}
	private void playVideo()
	{
		VideoView v = (VideoView) findViewById(R.id.video);
		v.setMediaController(null);
		
		v.setOnCompletionListener
		(
			new MediaPlayer.OnCompletionListener() 
			{
				@Override
				public void onCompletion(MediaPlayer mp) 
				{
//					showLogos();ENABLE TO SHOW LOGOS
					goToRegistration();					
				}
			}
		);
		
		
		v.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.intro );
		v.start();
	}
}
