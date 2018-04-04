package com.pessetto.origamismtp.filehandlers.inbox;

/** Listener for a new message */
public interface NewMessageListener
{
	/** Command to let listener know of new message*/
	void messageRecieved();
}
