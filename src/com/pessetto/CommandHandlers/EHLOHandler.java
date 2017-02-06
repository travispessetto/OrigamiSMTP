package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class EHLOHandler 
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
				
				Response = "250-127.0.0.1 WELCOME TLS ENABLED"+Variables.CRLF;
				Response += "250 STARTTLS"+Variables.CRLF;
			}
			else
			{
				Response = "250 127.0.0.1"+Variables.CRLF;
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
				
				Response = "250-127.0.0.1 WELCOME TLS ENABLED"+Variables.CRLF;
				Response += "250 STARTTLS"+Variables.CRLF;
			}
			else
			{
				Response = "250 127.0.0.1"+Variables.CRLF;
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

}
