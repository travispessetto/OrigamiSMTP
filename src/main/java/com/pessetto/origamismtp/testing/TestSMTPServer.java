package com.pessetto.origamismtp.testing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import com.pessetto.origamismtp.filehandlers.inbox.Message;

/** A test server for testing email
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class TestSMTPServer
{
	private Thread origamiThread;
	private TestSMTPMessageListener messageListener;
	
	/** Creates a new instance of a test server
	 * @param port The Port to open the server on
	 */
	public TestSMTPServer(int port)
	{
		Inbox inbox = Inbox.getInstance();
		messageListener = new TestSMTPMessageListener();
		inbox.addNewMessageListener(messageListener);
		OrigamiSMTPServerThread osst = new OrigamiSMTPServerThread(port);
		origamiThread = new Thread(osst);
		origamiThread.start();
	}
	
	/** Closes the SMTP server
	 */
	public void closeServer()
	{
		origamiThread.interrupt();
	}
	
	/** Gets the message future for the latest message
	 * @return The future of the message
	 */
	public Future<Message> getLatestMessageFuture()
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		return executor.submit(messageListener);
	}
	

}
