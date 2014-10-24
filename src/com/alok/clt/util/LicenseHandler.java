package com.alok.clt.util;

import android.content.Context;
import android.provider.Settings.Secure;
public class LicenseHandler 
{
	public static String getID(Context c)
	{
		return Secure.getString(c.getContentResolver(),Secure.ANDROID_ID).toUpperCase(); 
	}
	
	public static boolean checkLicense(String key, Context c)
	{
		return key.equalsIgnoreCase(encID(getID(c)));
	}
	
	public static String encID(String id)
	{
		String t = "";
		String x;
		for(int i = 0; i<id.length()-1; i+=2)
		{
			x = getnum(id.charAt(i));
			t = x + t;
		}
		return t;
	}
	
	public static String getnum(char c)
	{
		int x;
		if(Character.isDigit(c))
		{
			x = 9 - Integer.parseInt(""+c);
		} 
		else 
		{
			x = 70 - ((int) c);
		}
		return ""+x;
	}
}
