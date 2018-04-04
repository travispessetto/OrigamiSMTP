package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

/** Represents a handler for the RCPT command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class RCPTHandler implements Validatable {
	
	private String response;
	private String toEmail;
	private boolean valid;
	private MAILHandler mail;
	
	/** Creates an instance of the RCPTHandler
	 * @param fullCmd The full line of the RCPT command
	 * @param mailHandler The current MAILHandler
	 */
	public RCPTHandler(String fullCmd,MAILHandler mailHandler)
	{
		toEmail = "";
		mail = mailHandler;
		addAddress(fullCmd);
	}
	
	/** Adds an email address
	 * @param fullCmd The full RCPT command
	 */
	public void addAddress(String fullCmd)
	{
		valid = false;
		if(mail == null)
		{
			response = "503 Sender required before recipient"+Constants.CRLF;
			valid = false;
		}
		String[] parts = fullCmd.split(":",2);
		if(parts.length > 1)
		{
			if(!toEmail.equals(""))
			{
				toEmail += ", ";
			}
			toEmail += parts[1];
			response = "250 OK"+Constants.CRLF;
			valid = true;
		}
		else
		{
			response = "501 Syntax Error could net seperate cmd and cmdId" + Constants.CRLF;
			valid = false;
		}
	}

	/** Gets the recipient this is going to
	 * @return The string representing the recipient
	 */
	public String getRecipient()
	{
		return toEmail;
	}
	
	/** Gets the response of the handler
	 *  @return The string representing the response
	 */
	public String getResponse()
	{
		return response;
	}

	/** Validates the RCPT handler or nullifies it
	 */
	@Override
	public Validatable validateOrNullify() 
	{
		return valid ? this : null;
	}
}
