package com.pessetto.origamismtp.filehandlers.inbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.pessetto.origamismtp.constants.Constants;

/** Represents the inbox
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class Inbox implements Serializable
{
	private static Inbox instance;
	private static final long serialVersionUID = -1686234852843453027L;
	private LinkedList<Message> messages;
	private transient List<NewMessageListener> newMessageListeners;
	private transient List<DeleteMessageListener> deleteMessageListeners;
        private transient int size;
	
	/** Creates new instance of inbox
	 */
	private Inbox()
	{
		messages = new LinkedList<Message>();
                size = 0;
	}
	
	/** Adds new message listener to inbox
	 * @param listener Class that implements the NewMessageListener
	 */
	public void addNewMessageListener(NewMessageListener listener)
	{
		if(newMessageListeners == null)
		{
			newMessageListeners = new ArrayList<NewMessageListener>();
		}
		newMessageListeners.add(listener);
	}
	
	/** Adds a delete message listener to inbox
	 * @param listener Class that implements DeleteMessageListener
	 */
	public void addDeleteMessageListener(DeleteMessageListener listener)
	{
		if(deleteMessageListeners == null)
		{
			deleteMessageListeners = new ArrayList<DeleteMessageListener>();
		}
		deleteMessageListeners.add(listener);
	}
	
	/** Adds a message to inbox and notifies all listeners
	 * @param msg The message received
	 */
	public void addMessage(Message msg)
	{
		if(newMessageListeners != null)
		{
                    notifyListenersOfNewMessage();
		}
                if(size > 0 && messages.size() == size)
                {
                    messages.removeLast();
                }
		messages.add(0,msg);
		this.serialize();
	}
	
        /** Clears all the messages out of the inbox
         */
        public void clearInbox()
        {
            while(messages.size() > 0)
            {
                deleteMessage(0);
            }
        }
        
	/** Deletes the message with the given id
	 * @param id The index of the email to delete
	 */
	public void deleteMessage(int id)
	{
		messages.remove(id);
		if(deleteMessageListeners != null)
		{
			this.notifyListenersOfDeletedMessage(id);
		}
		this.serialize();
	}
	
	/** Gets the inbox instance
	 * @return The inbox instance
	 */
	public static Inbox getInstance()
	{
		if(instance != null) return instance;
		File file = new File(Constants.INBOX_FILE);
		System.out.println("Checking for inbox file: " + Constants.INBOX_FILE);
		if(file.exists())
		{
			try
			{
				System.out.println("Inbox file exists");
				FileInputStream fin = new FileInputStream(file);
				ObjectInputStream oin = new ObjectInputStream(fin);
				instance = (Inbox)oin.readObject();
				oin.close();
				fin.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace(System.err);
			}
		}
		// if not just create a new instance
		else
		{
			System.out.println("Inbox file does not exist. Creating new.");
			instance = new Inbox();
		}
		return instance;
	}
	
	/** Gets the message at the specified index
	 * @param id The index of the message
	 * @return Message at the index
	 */
	public Message getMessage(int id)
	{
		return messages.get(id);
	}
	
	/** Gets how many messages are in the inbox
	 * @return The count of the message in inbox
	 */
	public int getMessageCount()
	{
		return messages.size();
	}
	
	/** Gets the last message to be received
	 * @return The last message received
	 */
	public Message getNewestMessage()
	{
		if(messages.size() == 0)
		{
                    return null;
		}
		return messages.get(0);
	}
        
        /** Gets the size of the inbox
         * @return The size the inbox can hold
         */
        public int getSize()
        {
            return size;
        }
        
        /** Sets the size of the inbox
         * @param s Size of the inbox
         */
        public void setSize(int s)
        {
            size = s;
            // Clear old messages if size is too small
            if(size > 0 && messages.size() > size)
            {
                int messageToDeleteCount = messages.size() - size;
                for(int i = 0; i < messageToDeleteCount; ++i)
                {
                    deleteMessage(messages.size() - 1);
                }
            }
        }
	
	/** Notifies the new message listeners of a new message
	 */
	private void notifyListenersOfNewMessage()
	{
		for(NewMessageListener listener : newMessageListeners)
		{
			listener.messageRecieved();
		}
	}
	
	/** Notifies the deleted message listener of a delete message
	 * @param index The index that was deleted
	 */
	private void notifyListenersOfDeletedMessage(int index)
	{
		for(DeleteMessageListener listener : deleteMessageListeners)
		{
			listener.removeEmail(index);
		}
	}
	
	/** Serializes the class to store on disk
	 */
	public void serialize()
	{
		try
		{
			File inboxFolder = new File(Constants.INBOX_FOLDER);
			if(!inboxFolder.exists())
			{
				inboxFolder.mkdirs();
			}
			File inboxFile = new File(Constants.INBOX_FILE);
			inboxFile.setWritable(true, false);	
			FileOutputStream fout = new FileOutputStream(inboxFile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(this);
			oout.close();
			fout.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
