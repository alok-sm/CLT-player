package com.alok.clt.activities;

import java.io.File;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.content.Intent;
import android.os.Environment;

import com.alok.clt.R;

public class PdfViewActivity extends PdfViewerActivity {
	public static final String cachePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.clt";

	public int getPreviousPageImageResource() {
		return R.drawable.left_arrow;
	}

	public int getNextPageImageResource() {
		return R.drawable.right_arrow;
	}

	public int getZoomInImageResource() {
		return R.drawable.zoom_in;
	}

	public int getZoomOutImageResource() {
		return R.drawable.zoom_out;
	}

	public int getPdfPasswordLayoutResource() {
		return R.layout.pdf_file_password;
	}

	public int getPdfPageNumberResource() {
		return R.layout.dialog_pagenumber;
	}

	public int getPdfPasswordEditField() {
		return R.id.etPassword;
	}

	public int getPdfPasswordOkButton() {
		return R.id.btOK;
	}

	public int getPdfPasswordExitButton() {
		return R.id.btExit;
	}

	public int getPdfPageNumberEditField() {
		return R.id.pagenum_edit;
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

