package com.pessetto.CommandHandlers;

import com.pessetto.CommandHandlers.Interfaces.Validatable;
import com.pessetto.Common.Variables;

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
			Response = "503 Invalid Sequence of Commands;  Expected EHLO"+Variables.CRLF;
		}
		else if(parts.length > 1)
		{
			parts = parts[1].split(":",2);
			if(parts.length > 1)
			{
				fromEmail = parts[1];
				Response = "250 OK"+Variables.CRLF;
				valid = true;
			}
			else
			{
				Response = "501 Syntax Error could not seperate from and email"+Variables.CRLF;
				valid = false;
			}
		}
		else
		{
			Response = "501 Syntax Error could net seperate cmd and cmdId" + Variables.CRLF;
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
