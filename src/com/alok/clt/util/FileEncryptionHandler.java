package com.alok.clt.util;

import net.lingala.zip4j.core.ZipFile;

public class FileEncryptionHandler 
{
    public void decrypt(String source, String destination)
    {
    	String password = "72 degrees, warm and sunny!";
        try
        {
            ZipFile zipFile = new ZipFile(source);
            zipFile.setPassword(password);
            zipFile.extractAll(destination);
        } 
        catch (Exception e) {}
    }
}

