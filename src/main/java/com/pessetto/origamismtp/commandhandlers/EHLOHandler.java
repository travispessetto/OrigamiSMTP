package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;

/** Represents a handler for the EHLO command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class EHLOHandler implements Validatable
{
	private String response;
	
	/** Creates a new instance of the EHLOHandler
	 * @param fullEHLO The full line of the EHLO command
	 */
	public EHLOHandler(String fullEHLO)
	{
		String[] parts = fullEHLO.split(" ");
		if(parts.length == 2)
		{
			if(Constants.ENABLE_START_TLS)
			{
				
				response = "250-localhost WELCOME TLS ENABLED"+Constants.CRLF;
				response += "250-AUTH LOGIN PLAIN" + Constants.CRLF;
				response += "250 STARTTLS"+Constants.CRLF;
			}
			else
			{
				response = "250-localhost WELCOME" + Constants.CRLF;
				response += "250-AUTH LOGIN PLAIN" + Constants.CRLF;
			}
		}
		else
		{
			response = "501 Syntax Error"+Constants.CRLF;
		}
	}
	
	/** Creates a new instance of the EHLO command that allows secure if secure is true
	 * @param fullEHLO The full EHLO command
	 * @param secure Whether to allow secure connections or not
	 */
	public EHLOHandler(String fullEHLO, boolean secure)
	{
		String[] parts = fullEHLO.split(" ");
		if(parts.length == 2)
		{
			if(Constants.ENABLE_START_TLS && !secure)
			{
				
				response = "250-localhost WELCOME TLS ENABLED"+Constants.CRLF;
				response += "250-AUTH LOGIN PLAIN" + Constants.CRLF;
				response += "250 STARTTLS"+Constants.CRLF;
			}
			else
			{
				response += "250-localhost WELCOME" + Constants.CRLF;
				response = "250 AUTH PLAIN"+Constants.CRLF;
			}
		}
		else
		{
			response = "501 Syntax Error"+Constants.CRLF;
		}
	}
	
	/** Gets the response to send to client
	 *  @return The string for the client
	 */
	public String getResponse()
	{
		return response;
	}

	/** Validates the handler or nullifies the handler
	 */
	@Override
	public Validatable validateOrNullify() {
		return this;
	}

}
