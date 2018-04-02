package com.pessetto.origamismtp.filehandlers;

import java.io.File;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

import com.pessetto.origamismtp.commandhandlers.DataHandler;
import com.pessetto.origamismtp.commandhandlers.MAILHandler;
import com.pessetto.origamismtp.commandhandlers.RCPTHandler;
import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import com.pessetto.origamismtp.filehandlers.inbox.Message;

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
