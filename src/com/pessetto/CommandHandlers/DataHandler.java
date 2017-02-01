package com.pessetto.CommandHandlers;

import java.util.Scanner;

import com.pessetto.Common.Variables;

public class DataHandler
{
	private String Data;
	private String Response;
	public DataHandler()
	{
		Response = "354 Start mail input; end with <CRLF>.<CRLF>"+Variables.CRLF;
	}
	
	public void ProcessMessage(Scanner inFromClient)
	{
		inFromClient.useDelimiter(""+Variables.CRLF+"."+Variables.CRLF);
		if(inFromClient.hasNext())
		{
			Data = inFromClient.next();
			// Clear out buffer
			inFromClient.nextLine();
			inFromClient.nextLine();
			Response = "250 OK" + Variables.CRLF;
		}
		else
		{
			Response = "501 Syntax Error no lines" + Variables.CRLF;
		}
		
		
	}
	
	public String GetData()
	{
		return Data;
	}
	
	public String GetResponse()
	{
		return Response;
	}
}
