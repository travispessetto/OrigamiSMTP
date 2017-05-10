package com.pessetto.FileHandlers;

import java.io.File;
import java.io.*;

import com.pessetto.CommandHandlers.DataHandler;
import com.pessetto.CommandHandlers.MAILHandler;
import com.pessetto.CommandHandlers.RCPTHandler;
import com.pessetto.Common.Variables;
import com.pessetto.FileHandlers.Inbox.Inbox;
import com.pessetto.FileHandlers.Inbox.Message;

import java.util.Calendar;
import java.util.Scanner;

public class EmailHandler
{
	public EmailHandler(MAILHandler mail, RCPTHandler rcpt, DataHandler data)
	{
		if(mail != null && rcpt != null && data != null)
		{
			Inbox inbox = Inbox.getInstance();
			Message message = new Message();
			message.setFrom(mail.GetSender());
			message.setTo(rcpt.GetRecipient());
			message.setSubject(Integer.toString(GetUnixTime()));
			message.setMessage(data.GetData());
			inbox.addMessage(message);
		}
	}
	
	private int GetUnixTime()
	{
		return (int)(System.currentTimeMillis() / 1000L);
	}
}
