package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.constants.Constants;

/** Represents a handler for the RSET command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class RSETHandler 
{
	/** Creates a new instance of the RSET Handler
	 * @param mail Current mail handler to be nullfied
	 * @param rcpt Current RCPT handler to be nullified
	 * @param data Current DATA handler to be nullifiedS
	 */
	public RSETHandler(MAILHandler mail, RCPTHandler rcpt, DataHandler data)
	{
		mail = null;
		rcpt = null;
		data = null;
	}
	
	/** Handles the response to the client
	 * @return String response for the client
	 */
	public String getResponse()
	{
		return "250 OK" + Constants.CRLF;
	}
}
