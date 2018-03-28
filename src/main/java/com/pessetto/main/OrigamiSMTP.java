// https://tools.ietf.org/html/rfc2821#section-4.1.1
// http://stackoverflow.com/questions/11985896/can-a-java-server-accept-both-ssl-and-plaintext-connections-on-one-port
package com.pessetto.main;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLSocket;

import com.pessetto.CommandHandlers.CommandHandler;
import com.pessetto.Common.Variables;
import com.pessetto.Threads.ConnectionHandler;
import com.pessetto.FileHandlers.Inbox.*;
import com.pessetto.Status.StatusListener;

import java.io.*;

public class OrigamiSMTP{

	private ServerSocket smtpSocket;
	private List<StatusListener> statusListeners;
	private int port;
	public static void main(String args[]) throws Exception
	{
		int bindPort = 2525;
		if(args.length == 1)
		{
			System.out.println("Setting port to " + args[0]);
			bindPort = Integer.parseInt(args[0]);
		}
		else
		{
			System.out.println("Default to 2525");
		}
		OrigamiSMTP console = new OrigamiSMTP(bindPort);
		console.startSMTP();
	}
	
	// To also be able to use it in a library
	public OrigamiSMTP(int port)
	{
		this.port = port;
		statusListeners = new ArrayList<StatusListener>();
	}
	
	public void addStatusListener(StatusListener sl)
	{
		statusListeners.add(sl);
	}
	
	public void closeSMTP()
	{
		try {
			smtpSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startSMTP() throws BindException
	{
		try
		{
			ExecutorService threadPool = Executors.newWorkStealingPool();
			java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
			java.lang.System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
			Socket ssls = null;
			System.out.println("Starting SMTP");
			InetSocketAddress bindAddress = new InetSocketAddress(port);
			smtpSocket = new ServerSocket();
			smtpSocket.setReuseAddress(true);
			smtpSocket.bind(bindAddress);
			System.out.println("Socket Opened");
			notifyStarted();
			while(!Thread.interrupted()  || !Thread.currentThread().isInterrupted())
			{
				System.out.println("AWAIT CONNECTION");
				Socket connectionSocket = smtpSocket.accept();
				ConnectionHandler connectionHandler = new ConnectionHandler(connectionSocket);
				threadPool.submit(connectionHandler);
				System.out.println("Connection sent to thread");
			}
			if(Thread.interrupted() || Thread.currentThread().isInterrupted())
			{
				notifyStopped();
				System.out.println("Quit due to interupted thread");
			}
		}
		catch(BindException ex)
		{
			notifyStopped();
			System.err.println("Could not bind to port");
			throw ex;
		}
		catch(Exception ex)
		{
			notifyStopped();
			System.err.println("Failed to open socket");
			ex.printStackTrace(System.err);
		}
	}
	
	private void notifyStarted()
	{
		for(StatusListener listener : statusListeners)
		{
			listener.smtpStarted();
		}
	}
	
	private void notifyStopped()
	{
		for(StatusListener listener : statusListeners)
		{
			listener.smtpStopped();
		}
	}
}
