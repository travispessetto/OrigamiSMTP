package com.pessetto.origamismtp.testing;

import java.net.BindException;

import com.pessetto.origamismtp.OrigamiSMTP;
import com.pessetto.origamismtp.filehandlers.inbox.NewMessageListener;

/** Represents a test thread for the SMTP server
 * @author travis.pessetto
 */
public class TestSMTPServer implements Runnable
{
	private OrigamiSMTP server;
	
	public TestSMTPServer(int port)
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
