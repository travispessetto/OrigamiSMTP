/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pessetto.origamismtp;

import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Travis Pessetto
 */
public class Settings implements Serializable{
    private static Settings Instance;
    private int Port;
    private boolean RequireValidSignIn;
    private static final long serialVersionUID = -1686234852843453027L;
    
    private Settings()
    {
        Port = 2525;
        RequireValidSignIn = false;
    }
    
    public static Settings getInstance()
    {
        if(Instance == null)
        {
            // check for file
            File file = new File(Constants.SETTINGS_FILE);
            if(file.exists())
            {
                try
                {
                    FileInputStream fin = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fin);
                    Instance = (Settings)oin.readObject();
                    oin.close();
                    fin.close();
                }
                catch(Exception ex)
                {
                        ex.printStackTrace(System.err);
                }
            }
            else
            {
                Instance = new Settings();
            }
        }
        return Instance;
    }
    
    public void setPort(int port)
    {
        Port = port;
        serialize();
    }
    
    public int getPort()
    {
        return Port;
    }
    
    public void requireValidSignIn(boolean require)
    {
        RequireValidSignIn = require;
        serialize();
    }
    
    public boolean getRequireSignIn()
    {
        return RequireValidSignIn;
    }
    
    /** Serializes the class to store on disk
	 */
	public void serialize()
	{
		try
		{
			File settingsFolder = new File(Constants.SETTINGS_FOLDER);
			if(!settingsFolder.exists())
			{
				settingsFolder.mkdirs();
			}
			File settingsFile = new File(Constants.SETTINGS_FILE);
			settingsFile.setWritable(true, false);	
			FileOutputStream fout = new FileOutputStream(settingsFile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(this);
			oout.close();
			fout.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
