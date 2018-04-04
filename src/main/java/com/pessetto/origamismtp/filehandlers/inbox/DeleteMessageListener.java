package com.pessetto.origamismtp.filehandlers.inbox;

/** An interface for any class that wants to listen for deletes
 * @author Travis Pessetto
 * @author pessetto.com
 */
public interface DeleteMessageListener
{
	/** Method to let listener know of deleted message*/
	void removeEmail(int index);
}
