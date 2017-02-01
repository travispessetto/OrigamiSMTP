package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class RSETHandler 
{
	public RSETHandler(EHLOHandler ehlo)
	{
		ehlo = null;
	}
	
	public String GetResponse()
	{
		return "250 OK" + Variables.CRLF;
	}
}
