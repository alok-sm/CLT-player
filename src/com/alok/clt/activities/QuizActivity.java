package com.alok.clt.activities;

import java.io.File;

import com.alok.clt.R;
import com.alok.clt.R.id;
import com.alok.clt.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

public class QuizActivity extends Activity {

	public static final String cachePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.clt";
	private WebView webView;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		Intent acint = getIntent();
		String path = acint.getStringExtra("path");
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file://"+path);
	}
	@Override
	protected void onPause()
	{

		super.onPause();
		delete(new File(cachePath));
		
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		delete(new File(cachePath));
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		delete(new File(cachePath));
		
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
	
	@Override
    public void onBackPressed()
    {
    	Intent intent = new Intent(this,FileChooserActivity.class);
    	Intent thisIntent = getIntent();
    	String passPath = thisIntent.getStringExtra("ParentPath");
    	intent.putExtra("PassPath", passPath);
    	delete(new File(cachePath));
    	startActivity(intent);
    }
}
