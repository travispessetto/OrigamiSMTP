package com.pessetto.origamismtp.commandhandlers;

import java.io.DataOutputStream;
import java.util.Scanner;
import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.status.AuthStatus;


/** Represents a handler for the AUTH command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class AUTHHandler 
{
	private boolean used;
	private String cmd;
	private AuthStatus authStatus;
	
	/** Creates an auth handler
	 * @param fullAuth the whole line of the auth command
	 */
	public AUTHHandler(String fullAuth)
	{
		used = false;
		cmd = fullAuth;
		authStatus = AuthStatus.START;
	}
	
	/** Gets the response to the client
	 * @return A string to be passed to the client
	 */
	public String getResponse()
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
	
	/** Gets the current state of authentication
	 * @return An Enum representing the current state of authentication
	 */
	public AuthStatus getStatus()
	{
		return authStatus;
	}
}
