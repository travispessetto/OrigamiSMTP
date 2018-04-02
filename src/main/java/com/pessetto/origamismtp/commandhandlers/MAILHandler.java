package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

public class MAILHandler implements Validatable
{
	private String fromEmail;
	private String Response;
	private boolean valid;
	
	public MAILHandler(String fullCmd, Validatable ehloHandler)
	{
		valid = false;
		String[] parts = fullCmd.split(" ",2);
		if(ehloHandler == null)
		{
			Response = "503 Invalid Sequence of Commands;  Expected EHLO"+Constants.CRLF;
		}
		else if(parts.length > 1)
		{
			parts = parts[1].split(":",2);
			if(parts.length > 1)
			{
				fromEmail = parts[1];
				Response = "250 OK"+Constants.CRLF;
				valid = true;
			}
			else
			{
				Response = "501 Syntax Error could not seperate from and email given ("+fullCmd+")"+Constants.CRLF;
				valid = false;
			}
		}
		else
		{
			Response = "501 Syntax Error could net seperate cmd and cmdId" + Constants.CRLF;
			valid = false;
		}
	}
	
	public String GetResponse()
	{
		return Response;
	}
	
	public String GetSender()
	{
		return fromEmail;
	}

	@Override
	public Validatable ValidateOrNullify()
	{
		if(valid)
		{
			return this;
		}
		return null;
	}

}
