package com.alok.clt.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alok.clt.R;
import com.alok.clt.activities.FileChooserActivity;
import com.alok.clt.activities.IntroActivity;


public class FileArrayHandler extends ArrayAdapter<FileHandler>
{
	private String quizext = ".enf";
	private String pdfext = ".enp";
	private String vidext = ".enc";

	private Context c;
	private int id;
	private List<FileHandler>items;
	
	public FileArrayHandler(Context context, int textViewResourceId, List<FileHandler> objects) 
	{
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		items = objects;
	}
	public FileHandler getItem(int i)
	{
		return items.get(i);
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
        if (v == null) 
        {
        	LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final FileHandler o = items.get(position);
        if (o != null) 
        {
        	TextView t1 = (TextView) v.findViewById(R.id.TextView01);
	        ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
	        
	        String filename = o.getPath();
	        if(
	        	filename.indexOf(pdfext)!=-1 ||
	        	filename.indexOf(vidext)!=-1 ||
	        	filename.indexOf(quizext)!=-1 
	        )
	        {
	        	File file = new File(filename.replaceAll("\\.[0-9a-z]+", ".png"));
	        	if(!file.exists())file = new File("android.resource://" + 
	        			FileChooserActivity.PACKAGE_NAME + "/" +
	        			R.raw.file_icon);
	        	imageCity.setImageURI(Uri.fromFile(file));
	        }
	        else
	        {
	        	
	        	String uri = "drawable/" + o.getImage();
		        int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
		        Drawable image = c.getResources().getDrawable(imageResource);
	        	imageCity.setImageDrawable(image);
	        }
                  
            if(t1!=null)
            	t1.setText(o.getName().replaceAll("\\.[0-9a-z]+", ""));
                       
        }
        return v;
	}
}
