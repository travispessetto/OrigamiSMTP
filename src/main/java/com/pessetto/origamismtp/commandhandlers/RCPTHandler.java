package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

public class RCPTHandler implements Validatable {
	
	private String Response;
	private String ToEmail;
	private boolean valid;
	private MAILHandler mail;
	public RCPTHandler(String fullCmd,MAILHandler mailHandler)
	{
		ToEmail = "";
		mail = mailHandler;
		AddAddress(fullCmd);
	}
	
	public void AddAddress(String fullCmd)
	{
		valid = false;
		if(mail == null)
		{
			Response = "503 Sender required before recipient"+Constants.CRLF;
			valid = false;
		}
		String[] parts = fullCmd.split(":",2);
		if(parts.length > 1)
		{
			if(!ToEmail.equals(""))
			{
				ToEmail += ", ";
			}
			ToEmail += parts[1];
			Response = "250 OK"+Constants.CRLF;
			valid = true;
		}
		else
		{
			Response = "501 Syntax Error could net seperate cmd and cmdId" + Constants.CRLF;
			valid = false;
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

	@Override
	public Validatable ValidateOrNullify() 
	{
		return valid ? this : null;
	}
}
