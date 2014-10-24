package com.alok.clt.activities;

import java.io.File;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.alok.clt.R;
import com.alok.clt.util.FileEncryptionHandler;

public class PlayVideoActivity extends Activity 
{
	private boolean delete;
	MediaController  mc;
	private boolean flag;
	public static final String cachePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.clt";
	public static String encPath;
	View decorView;

	public boolean execDone = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Intent i = getIntent();
		encPath = i.getStringExtra("FilePath");
		super.onCreate(savedInstanceState);
		flag = false;

		setContentView(R.layout.activity_play_video);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.abs_layout);
		SharedPreferences pref = getSharedPreferences("CLTprefs", Context.MODE_PRIVATE);
		TextView textViewTitle = (TextView) findViewById(R.id.mytext);
		textViewTitle.setText(pref.getString("school", ""));
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		encPlay();
	}
	
	private void encPlay()
	{
		( new Thread() 
		{ 
			public void run() 
			{ 
				playonui(extract());
			} 
			
			public String extract()
			{
				Log.e("CACHEPATH:",cachePath);
			    File tempDir = new File(cachePath);
			    FileEncryptionHandler fh = new FileEncryptionHandler();
			    fh.decrypt(encPath,cachePath);
			    Log.e("ENCPATH",encPath);
			    Log.e("CACHEEXIST",""+tempDir.exists());
			    String dest = null;
			    if(!tempDir.exists() || tempDir.list() == null || tempDir.list()[0]==null){
			    	PlayVideoActivity.this.runOnUiThread
					(
						new Runnable() 
						{
							@Override
							public void run() 
							{
								Intent intent = getIntent();
								finish();
								startActivity(intent);
							}
						}
					);
			    }
			    dest = cachePath+"/"+tempDir.list()[0];
			    
			    execDone = true;
			    return dest;
			}
			
			public void playonui(final String decPath)
			{
				PlayVideoActivity.this.runOnUiThread
				(
					new Runnable() 
					{
						@Override
						public void run() 
						{
							play(decPath);
						}
					}
				);
			}
		} 
	).start(); 
	}
	
	public void play(String path)
	{
		File f = new File(path);
		if(f.isDirectory())
		{
			delete = false;
			Intent i = new Intent(this,QuizActivity.class);
	        i.putExtra("path", path+"/html5.html");
	        String parentDir = new File(encPath).getParent();
			i.putExtra("ParentPath", parentDir);
	        startActivity(i);
		}
		else if(path.endsWith(".pdf"))
		{
			delete = false;
			Intent intent = new Intent(this, PdfViewActivity.class);
			intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,path);
			String parentDir = new File(encPath).getParent();
			intent.putExtra("ParentPath", parentDir);
			startActivity(intent);
		}
		else 
		{
			delete = true;
			ProgressBar p = (ProgressBar)findViewById(R.id.progressBar1);
			p.setVisibility(View.INVISIBLE);
			decorView = getWindow().getDecorView();
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			playVideo(path);
		}
		
	}
	
	private void playVideo(String FilePath)
	{
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		mc = new MediaController(this); 
		hideNav();
		mc.setAnchorView(videoView);
		videoView.setMediaController(mc);
		
		videoView.setOnCompletionListener
		(
			new MediaPlayer.OnCompletionListener() 
			{
				@Override
				public void onCompletion(MediaPlayer mp) 
				{
					PlayVideoActivity.this.finish();
				}
			}
		);
		
		videoView.setOnTouchListener
		(
			new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) 
				{
					hideNav();
					return false;
				}
			}
		);
		
		videoView.setVideoURI(Uri.parse(FilePath));
		videoView.start();
	}
	
	@Override
	protected void onPause()
	{

		super.onPause();
		if(delete)
		{
			delete(new File(cachePath));
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if(delete)
		{
			delete(new File(cachePath));
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(delete)
		{
			delete(new File(cachePath));
		}
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		finish();
	}
	
	public static boolean delete(File file) 
	{
		if (file != null) 
		{
		    if (file.isDirectory()) 
		    {
		    	String[] children = file.list();
		    	for (int i = 0; i < children.length; i++)
		    	{
		    		boolean success = delete(new File(file, children[i]));
		    		if (!success) 
		    		{
		    			return false;
		    		}
		    	}
		    }
		    return file.delete();
		}
		return false;
	}
	
	private void hideNav()
	{
		if(flag)
		{
			
			getActionBar().hide();

			mc.show();
		}
		else
		{
			
			
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LOW_PROFILE;
			decorView.setSystemUiVisibility(uiOptions);
			getActionBar().show();

			mc.hide();
		}
		flag = !flag;
	}
	
	
}
