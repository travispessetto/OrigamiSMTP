package com.pessetto.origamismtp.testing;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import com.pessetto.origamismtp.filehandlers.inbox.Message;
import com.pessetto.origamismtp.filehandlers.inbox.NewMessageListener;

/** Thread to wait for a new message
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class TestSMTPMessageListener  implements Callable<Message>, NewMessageListener{

	private AtomicBoolean messageRecieved;
	
	public TestSMTPMessageListener()
	{
		messageRecieved = new AtomicBoolean(false);
	}

	/** Listens for a message to be received
	 */
	@Override
	public void messageRecieved() 
	{
		messageRecieved.set(true);
	}
	
	/** Gets if the message was recieved
	 * @return AtomicBoolean of message recieved
	 */
	public AtomicBoolean getMessageRecieved()
	{
		return messageRecieved;
	}

	/** Returns the newest message
	 * @return Message The latest message
	 */
	@Override
	public Message call() throws Exception {
		while(!messageRecieved.get())
		{
			Thread.sleep(100);
		}
		Inbox inbox = Inbox.getInstance();
		return inbox.getNewestMessage();
	}


}
