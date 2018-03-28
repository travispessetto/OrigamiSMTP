package com.pessetto.CommandHandlers;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.pessetto.Common.Variables;
import com.pessetto.Debug.ThreadLogger;
import com.pessetto.Status.AuthStatus;

public class AUTHHandler 
{
	private boolean used;
	private String cmd;
	private Scanner in;
	private DataOutputStream out;
	private AuthStatus authStatus;
	
	public AUTHHandler(String fullAuth)
	{
		used = false;
		cmd = fullAuth;
		authStatus = AuthStatus.START;
	}
	
	public String GetResponse()
	{
		String[] cmdSections = cmd.replace(Variables.CRLF, "").split("\\s");
		if(authStatus != AuthStatus.CONTINUE && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("plain"))
		{
			authStatus = AuthStatus.CONTINUE;
			return "334 Continue"+Variables.CRLF;
		}
		if(used)
		{
			authStatus = AuthStatus.FINISHED;
			return "503 Already authenticated"+Variables.CRLF;
		}
		else
		{
			authStatus = AuthStatus.FINISHED;
			used = true;
			return "235 AUTH SUCCESS" + Variables.CRLF;
		}
	}
	
	public AuthStatus getStatus()
	{
		return authStatus;
	}
}
