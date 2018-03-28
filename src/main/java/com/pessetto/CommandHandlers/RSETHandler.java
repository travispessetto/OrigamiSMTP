package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

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
		return "250 OK" + Variables.CRLF;
	}
}
