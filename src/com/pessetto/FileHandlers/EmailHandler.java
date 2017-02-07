package com.pessetto.FileHandlers;

import java.io.File;
import java.io.*;

import com.pessetto.CommandHandlers.DataHandler;
import com.pessetto.CommandHandlers.MAILHandler;
import com.pessetto.CommandHandlers.RCPTHandler;
import com.pessetto.Common.Variables;

import java.util.Calendar;
import java.util.Scanner;

public class EmailHandler
{
	public EmailHandler(MAILHandler mail, RCPTHandler rcpt, DataHandler data)
	{
		if(mail != null && rcpt != null && data != null)
		{
			File messageFolder = new File(Variables.MessageFolder);
			messageFolder.mkdir();
			try
			{
				BufferedWriter emailFile = new BufferedWriter(new FileWriter(messageFolder+"/"+Integer.toString(GetUnixTime())+".mht"));
				emailFile.write("TO: " + rcpt.GetRecipient() + Variables.CRLF);
				emailFile.write("FROM: "+ mail.GetSender() + Variables.CRLF);
				emailFile.write("===MESSAGE==="+Variables.CRLF+data.GetData()+Variables.CRLF+"===/MESSAGE===");
				emailFile.close();
			}
			catch(Exception e)
			{
				System.err.println("Could not write email to file");
			}
		}
	}
	
	private int GetUnixTime()
	{
		return (int)(System.currentTimeMillis() / 1000L);
	}
}
