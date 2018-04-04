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
	
	/** Creates new instance of inbox
	 */
	private Inbox()
	{
		messages = new LinkedList<Message>();
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
	
	/** Adds a delte message listener to inbox
	 * @param listener Class that implments DeleteMessageListener
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
	 * @param msg The message recieved
	 */
	public void addMessage(Message msg)
	{
		if(newMessageListeners != null)
		{
			notifyListenersOfNewMessage();
		}
		messages.add(0,msg);
		this.serialize();
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
	
	/** Gets the last message to be recieved
	 * @return The last message recieved
	 */
	public Message getNewestMessage()
	{
		if(messages.size() == 0)
		{
			return null;
		}
		return messages.get(0);
	}
	
	/** Notifies the new message listners of a new message
	 */
	private void notifyListenersOfNewMessage()
	{
		for(NewMessageListener listener : newMessageListeners)
		{
			listener.messageRecieved();
		}
	}
	
	/** Notifies the deleted message listender of a delete message
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
			System.out.println("Saving inbox to " + Constants.INBOX_FILE);
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
