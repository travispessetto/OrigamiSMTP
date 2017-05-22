package com.pessetto.CommandHandlers;

import com.pessetto.CommandHandlers.Interfaces.Validatable;
import com.pessetto.Common.Variables;

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
			if(Variables.EnableStartTLS)
			{
				
				Response = "250-localhost WELCOME TLS ENABLED"+Variables.CRLF;
				Response += "250-AUTH PLAIN" + Variables.CRLF;
				Response += "250 STARTTLS"+Variables.CRLF;
			}
			else
			{
				Response = "250-localhost WELCOME" + Variables.CRLF;
				Response += "250 AUTH PLAIN" + Variables.CRLF;
			}
		}
		else
		{
			Response = "501 Syntax Error"+Variables.CRLF;
		}
	}
	
	public EHLOHandler(String fullEHLO, boolean secure)
	{
		String[] parts = fullEHLO.split(" ");
		if(parts.length == 2)
		{
			ClientDomain = parts[1];
			if(Variables.EnableStartTLS && !secure)
			{
				
				Response = "250-localhost WELCOME TLS ENABLED"+Variables.CRLF;
				Response += "250-AUTH PLAIN" + Variables.CRLF;
				Response += "250 STARTTLS"+Variables.CRLF;
			}
			else
			{
				Response += "250-localhost WELCOME" + Variables.CRLF;
				Response = "250 AUTH PLAIN"+Variables.CRLF;
			}
		}
		else
		{
			Response = "501 Syntax Error"+Variables.CRLF;
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
