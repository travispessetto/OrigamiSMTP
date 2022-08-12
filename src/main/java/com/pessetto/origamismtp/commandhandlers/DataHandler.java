package com.pessetto.origamismtp.commandhandlers;

import java.util.Scanner;

import com.pessetto.origamismtp.commandhandlers.interfaces.Validatable;
import com.pessetto.origamismtp.constants.Constants;


/** Represents and handler for the Data command
 * @author Travis Pessetto
 * @author pessetto.com
 *
 */
public class DataHandler implements Validatable
{
	private String data;
	private String response;
	private boolean valid;
	
	/** Creates a new instance of DataHandler
	 * @param rcpt The instance of RCPT handler to verify
	 */
	public DataHandler(RCPTHandler rcpt)
	{
		valid = false;
		if(rcpt == null)
		{
			response = "503 Invalid Sequence of Commands;  Must use MAIL and RCPT before DATA" + Constants.CRLF;
			valid = false;
		}
		else
		{
			response = "354 Start mail input; end with <CRLF>.<CRLF>"+Constants.CRLF;
			valid = true;
		}
	}
	
	/** Processes the data message
	 * @param inFromClient The input stream from the client
	 */
	public void processMessage(Scanner inFromClient)
	{
		inFromClient.useDelimiter(""+Constants.CRLF+"."+Constants.CRLF);
		if(inFromClient.hasNext())
		{
			data = inFromClient.next();
			// Clear out buffer
			//inFromClient.nextLine();
			//inFromClient.nextLine();
			response = "250 OK" + Constants.CRLF;
		}
		else
		{
			response = "501 Syntax Error no lines" + Constants.CRLF;
		}
		
		
	}
	
	/** Gets the data passed from client to server
	 * @return A string representing the data
	 */
	public String getData()
	{
		return data;
	}
	
	/** Gets the response to send to client
	 */
	public String getResponse()
	{
		return response;
	}

	/** Validates the DataHandler or nullifies it if invalid
	 */
	@Override
	public Validatable validateOrNullify() {
		return valid ? this : null;
	}
}
