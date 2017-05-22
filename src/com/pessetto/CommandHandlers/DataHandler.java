package com.pessetto.CommandHandlers;

import java.util.Scanner;

import com.pessetto.CommandHandlers.Interfaces.Validatable;
import com.pessetto.Common.Variables;

public class DataHandler implements Validatable
{
	private String Data;
	private String Response;
	private boolean valid;
	public DataHandler(RCPTHandler rcpt)
	{
		valid = false;
		if(rcpt == null)
		{
			Response = "503 Invalid Sequence of Commands;  Must use MAIL and RCPT before DATA" + Variables.CRLF;
			valid = false;
		}
		else
		{
			Response = "354 Start mail input; end with <CRLF>.<CRLF>"+Variables.CRLF;
			valid = true;
		}
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

	@Override
	public Validatable ValidateOrNullify() {
		return valid ? this : null;
	}
}
