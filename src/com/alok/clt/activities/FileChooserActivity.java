package com.alok.clt.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.alok.clt.R;
import com.alok.clt.util.FileArrayHandler;
import com.alok.clt.util.FileHandler;

public class FileChooserActivity extends ListActivity 
{
	private static String extension = ".enc";
	private static String pdfextension = ".enp";
	private static String quizextension = ".enf";
	private File currentDir;
    private FileArrayHandler adapter;
    private File parentDir = null;
    public static String PACKAGE_NAME;
    private File thisDir;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        currentDir = new File(getsd());
        PACKAGE_NAME = getPackageName();
        Intent thisIntent = getIntent();
        String passPath = thisIntent.getStringExtra("PassPath");
        if(passPath != null)
        	fill(new File(passPath));	
        else
        	fill(currentDir); 
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
            	
            	fill(parentDir);
            		
            		
            	
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private String getsd()
    {
    	String str = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/CLT";
    	String[] locs = {str, "/storage/sdcard0/CLT", "/mnt/external_sd/CLT", "/mnt/extsd/CLT"};
    	
    	for(String loc:locs)
    	{
    		File f = new File(loc);
    		if(f.exists())return loc;
    	}
    	
    	return locs[0];
    }
    
    private void fill(File f)
    {
    	thisDir = f;
    	if(f.getAbsolutePath().toString().equalsIgnoreCase(getsd()))
    	{
    		getActionBar().setHomeButtonEnabled(false);
    		getActionBar().setDisplayHomeAsUpEnabled(false);
    	}
    	else
    	{
    		getActionBar().setHomeButtonEnabled(true);
    		getActionBar().setDisplayHomeAsUpEnabled(true);
    	}
    	File[]dirs = f.listFiles();
    	String title = f.getPath().replace(getsd(), "").replace("/", ">");
    	if(title.equals(""))title = " CLT";
		this.setTitle(title.substring(1));
		List<FileHandler>dir = new ArrayList<FileHandler>();
		List<FileHandler>fls = new ArrayList<FileHandler>();
		try
		{
			for(File ff: dirs)
			{ 
				if(ff.isDirectory() && !ff.isHidden())
				{
					File[] fbuf = ff.listFiles(); 
					int buf = 0;
					if(fbuf != null)
					{ 
						for(File file:fbuf)
						{
							if(
									!file.isHidden()&&
									(
											file.isDirectory() ||
											file.getName().endsWith(extension) ||
											file.getName().endsWith(pdfextension) ||
											file.getName().endsWith(quizextension)
									)
									
							  )
							  buf++;
						}
					} 
					else buf = 0; 
					String num_item = String.valueOf(buf);
					if(buf == 0) num_item = num_item + " item";
					else num_item = num_item + " items";
					dir.add(new FileHandler(ff.getName(),num_item,null,ff.getAbsolutePath(),"directory_icon")); 
				}
				else if(
					ff.getName().endsWith(extension) ||
					ff.getName().endsWith(pdfextension) ||
					ff.getName().endsWith(quizextension)
				)
				{
					
					fls.add(new FileHandler(ff.getName(),ff.length() + " Byte", null, ff.getAbsolutePath(),"file_icon"));
				}
			}
		}
		catch(Exception e){ }
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if(!f.getAbsolutePath().toString().equalsIgnoreCase(getsd()))
		{
			parentDir = new File(f.getParent());
		}
		else
		{
			parentDir = null;
		}
		adapter = new FileArrayHandler(FileChooserActivity.this,R.layout.file_view,dir);
		this.setListAdapter(adapter); 
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
    {
		super.onListItemClick(l, v, position, id);
		FileHandler o = adapter.getItem(position);
		if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up"))
		{
			currentDir = new File(o.getPath());
			fill(currentDir);
		}
		else
		{
			onFileClick(o);
		}
	}
    
    private void onFileClick(FileHandler o)
    {
    	Intent intent = new Intent(this, PlayVideoActivity.class);
//    	intent.putExtra("Directory", thisDir.getAbsolutePath());
        intent.putExtra("FilePath",o.getPath());
        startActivity(intent);
    }
    
    
    @Override
    public void onBackPressed()
    {
    	
    	if(thisDir.getAbsolutePath().toString().equalsIgnoreCase(getsd()) || thisDir == null)
    	{
    		Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
    	}
    	else
    	{
    		fill(parentDir);
    	}
    }
}
