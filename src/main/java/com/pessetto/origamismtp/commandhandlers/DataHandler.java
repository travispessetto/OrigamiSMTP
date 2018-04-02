package com.pessetto.origamismtp.commandhandlers;

import java.util.Scanner;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;


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
			Response = "503 Invalid Sequence of Commands;  Must use MAIL and RCPT before DATA" + Constants.CRLF;
			valid = false;
		}
		else
		{
			Response = "354 Start mail input; end with <CRLF>.<CRLF>"+Constants.CRLF;
			valid = true;
		}
	}
	
	public void ProcessMessage(Scanner inFromClient)
	{
		inFromClient.useDelimiter(""+Constants.CRLF+"."+Constants.CRLF);
		if(inFromClient.hasNext())
		{
			Data = inFromClient.next();
			// Clear out buffer
			inFromClient.nextLine();
			inFromClient.nextLine();
			Response = "250 OK" + Constants.CRLF;
		}
		else
		{
			Response = "501 Syntax Error no lines" + Constants.CRLF;
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
