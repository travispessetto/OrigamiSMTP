package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.constants.Constants;

public class RSETHandler 
{
	public RSETHandler(MAILHandler mail, RCPTHandler rcpt, DataHandler data)
	{
		mail = null;
		rcpt = null;
		data = null;
	}
	
	public String GetResponse()
	{
		return "250 OK" + Constants.CRLF;
	}
}
