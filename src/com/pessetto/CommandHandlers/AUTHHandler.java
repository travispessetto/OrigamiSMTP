package com.pessetto.CommandHandlers;

import com.pessetto.Common.Variables;

public class AUTHHandler 
{
	private boolean used;
	
	public AUTHHandler()
	{
		used = false;
	}
	
	public String GetResponse()
	{
		if(used)
		{
			return "503 Already authenticated";
		}
		else
		{
			used = true;
			return "235 AUTH SUCCESS" + Variables.CRLF;
		}
	}
}
