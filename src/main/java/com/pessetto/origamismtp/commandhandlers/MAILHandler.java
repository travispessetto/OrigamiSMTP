package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

/** Represents a handler for the MAIL command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class MAILHandler implements Validatable
{
	private String fromEmail;
	private String response;
	private boolean valid;
	
	/** Creates a new instance of MailHandler
	 * @param fullCmd The full line of the command
	 * @param ehloHandler The current EHLOHandler
	 */
	public MAILHandler(String fullCmd, Validatable ehloHandler)
	{
		valid = false;
		String[] parts = fullCmd.split(" ",2);
		if(ehloHandler == null)
		{
			response = "503 Invalid Sequence of Commands;  Expected EHLO"+Constants.CRLF;
		}
		else if(parts.length > 1)
		{
			parts = parts[1].split(":",2);
			if(parts.length > 1)
			{
				fromEmail = parts[1];
				response = "250 OK"+Constants.CRLF;
				valid = true;
			}
			else
			{
				response = "501 Syntax Error could not seperate from and email given ("+fullCmd+")"+Constants.CRLF;
				valid = false;
			}
		}
		else
		{
			response = "501 Syntax Error could net seperate cmd and cmdId" + Constants.CRLF;
			valid = false;
		}
	}
	
	/** Gets the response to send to the client
	 * @return The string to sent to client
	 */
	public String getResponse()
	{
		return response;
	}
	
	/** Gets the sender
	 * @return The string of the sender
	 */
	public String getSender()
	{
		return fromEmail;
	}

	/** Validates or nullifies the MAILHandler
	 */
	@Override
	public Validatable validateOrNullify()
	{
		if(valid)
		{
			return this;
		}
		return null;
	}

}
