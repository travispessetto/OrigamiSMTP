package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

public class EHLOHandler implements Validatable
{
	private String ClientDomain;
	private String Response;
	
	public EHLOHandler(String fullEHLO)
	{
		String[] parts = fullEHLO.split(" ");
		if(parts.length == 2)
		{
			ClientDomain = parts[1];
			if(Constants.ENABLE_START_TLS)
			{
				
				Response = "250-localhost WELCOME TLS ENABLED"+Constants.CRLF;
				Response += "250-AUTH PLAIN" + Constants.CRLF;
				Response += "250 STARTTLS"+Constants.CRLF;
			}
			else
			{
				Response = "250-localhost WELCOME" + Constants.CRLF;
				Response += "250 AUTH PLAIN" + Constants.CRLF;
			}
		}
		else
		{
			Response = "501 Syntax Error"+Constants.CRLF;
		}
	}
	
	public EHLOHandler(String fullEHLO, boolean secure)
	{
		String[] parts = fullEHLO.split(" ");
		if(parts.length == 2)
		{
			ClientDomain = parts[1];
			if(Constants.ENABLE_START_TLS && !secure)
			{
				
				Response = "250-localhost WELCOME TLS ENABLED"+Constants.CRLF;
				Response += "250-AUTH PLAIN" + Constants.CRLF;
				Response += "250 STARTTLS"+Constants.CRLF;
			}
			else
			{
				Response += "250-localhost WELCOME" + Constants.CRLF;
				Response = "250 AUTH PLAIN"+Constants.CRLF;
			}
		}
		else
		{
			Response = "501 Syntax Error"+Constants.CRLF;
		}
	}
	
	public String GetResponse()
	{
		return Response;
	}

	@Override
	public Validatable ValidateOrNullify() {
		return this;
	}

}
