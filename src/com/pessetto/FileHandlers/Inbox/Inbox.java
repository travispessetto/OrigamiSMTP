package com.pessetto.FileHandlers.Inbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.pessetto.Variables.InboxVariables;

public class Inbox implements Serializable
{
	private static Inbox instance;
	private static final long serialVersionUID = -1686234852843453027L;
	private LinkedList<Message> messages;
	private transient List<NewMessageListener> newMessageListeners;
	private transient List<DeleteMessageListener> deleteMessageListeners;
	
	private Inbox()
	{
		messages = new LinkedList<Message>();
	}
	
	public void addNewMessageListener(NewMessageListener listener)
	{
		if(newMessageListeners == null)
		{
			newMessageListeners = new ArrayList<NewMessageListener>();
		}
		newMessageListeners.add(listener);
	}
	
	public void addDeleteMessageListener(DeleteMessageListener listener)
	{
		if(deleteMessageListeners == null)
		{
			deleteMessageListeners = new ArrayList<DeleteMessageListener>();
		}
		deleteMessageListeners.add(listener);
	}
	
	public void addMessage(Message msg)
	{
		if(newMessageListeners != null)
		{
			notifyListenersOfNewMessage();
		}
		messages.add(0,msg);
		this.serialize();
	}
	
	public void deleteMessage(int id)
	{
		messages.remove(id);
		if(deleteMessageListeners != null)
		{
			this.notifyListenersOfDeletedMessage(id);
		}
		this.serialize();
	}
	
	public static Inbox getInstance()
	{
		if(instance != null) return instance;
		File file = new File(InboxVariables.inboxFile);
		if(file.exists())
		{
			try
			{
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
			instance = new Inbox();
		}
		return instance;
	}
	
	public Message getMessage(int id)
	{
		return messages.get(id);
	}
	
	public int getMessageCount()
	{
		return messages.size();
	}
	
	public Message getNewestMessage()
	{
		return messages.get(0);
	}
	
	private void notifyListenersOfNewMessage()
	{
		for(NewMessageListener listener : newMessageListeners)
		{
			listener.messageRecieved();
		}
	}
	
	private void notifyListenersOfDeletedMessage(int index)
	{
		for(DeleteMessageListener listener : deleteMessageListeners)
		{
			listener.removeEmail(index);
		}
	}
	
	public void serialize()
	{
		try
		{
			File inboxFile = new File(InboxVariables.inboxFile);
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
