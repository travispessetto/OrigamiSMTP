package com.pessetto.Debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pessetto.Common.Variables;

public class ThreadLogger 
{
	private BufferedWriter logOutput;
	public ThreadLogger()
	{
		try 
		{
			Path logFolder = Paths.get(Variables.LogFolder);
			if(!Files.isDirectory(logFolder))
			{
				File directory = new File(Variables.LogFolder);
				directory.mkdir();
			}
			long mills = System.currentTimeMillis();
			String logName = Variables.LogFolder+"log-protocol-"+mills+".log";
			logOutput = new BufferedWriter(new FileWriter(logName));
		} catch (FileNotFoundException e) {
			// TODO: Handle cannot write to log file
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logMessage(boolean client,String message)
	{
		try
		{
			String start = client ? "Client > " : "Server > ";
			String[] lines = message.split("\\r?\\n");
			for(String line : lines)
			{
				logOutput.write(start + line + Variables.CRLF);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void closeLog()
	{
		try 
		{	
			logOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
