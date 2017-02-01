package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class STARTTLSHandler 
{
	String Response;
	
	public STARTTLSHandler()
	{
		Response = "200 Go Ahead" + Variables.CRLF;
	}
	
	public String GetResponse()
	{
		return Response;
	}
}
