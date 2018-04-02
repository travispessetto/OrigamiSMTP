package com.pessetto.origamismtp.commandhandlers;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.status.AuthStatus;


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
		String[] cmdSections = cmd.replace(Constants.CRLF, "").split("\\s");
		if(authStatus != AuthStatus.CONTINUE && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("plain"))
		{
			authStatus = AuthStatus.CONTINUE;
			return "334 Continue"+Constants.CRLF;
		}
		if(used)
		{
			authStatus = AuthStatus.FINISHED;
			return "503 Already authenticated"+Constants.CRLF;
		}
		else
		{
			authStatus = AuthStatus.FINISHED;
			used = true;
			return "235 AUTH SUCCESS" + Constants.CRLF;
		}
	}
	
	public AuthStatus getStatus()
	{
		return authStatus;
	}
}
