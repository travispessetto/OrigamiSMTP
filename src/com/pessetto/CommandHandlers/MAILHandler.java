package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class MAILHandler 
{
	private String fromEmail;
	private String Response;
	
	public MAILHandler(String fullCmd)
	{
		String[] parts = fullCmd.split(" ",2);
		if(parts.length > 1)
		{
			parts = parts[1].split(":",2);
			if(parts.length > 1)
			{
				fromEmail = parts[1];
				Response = "250 OK"+Variables.CRLF;
			}
			else
			{
				Response = "501 Syntax Error could not seperate from and email"+Variables.CRLF;
			}
		}
		else
		{
			Response = "501 Syntax Error could net seperate cmd and cmdId" + Variables.CRLF;
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

}
