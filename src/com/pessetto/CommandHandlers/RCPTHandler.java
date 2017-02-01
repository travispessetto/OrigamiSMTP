package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class RCPTHandler {
	
	private String Response;
	private String ToEmail;
	public RCPTHandler(String fullCmd)
	{
		String[] parts = fullCmd.split(" ",2);
		if(parts.length > 1)
		{
			parts = parts[1].split(":",2);
			if(parts.length > 1)
			{
				ToEmail = parts[1];
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

	public String GetRecipient()
	{
		return ToEmail;
	}
	
	public String GetResponse()
	{
		return Response;
	}
}
