package com.pessetto.origamismtp.testing;

import java.net.BindException;

import com.pessetto.origamismtp.OrigamiSMTP;
import com.pessetto.origamismtp.filehandlers.inbox.NewMessageListener;

/** Represents a test thread for the SMTP server
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class OrigamiSMTPServerThread implements Runnable
{
	private OrigamiSMTP server;
	
	public OrigamiSMTPServerThread(int port)
	{
		server = new OrigamiSMTP(port);
	}
	

	/** Runs the server thread */
	@Override
	public void run() 
	{
		try 
		{
			server.startSMTP();
		} catch (BindException e)
		{
			// This is OK
		}	
	}

}
