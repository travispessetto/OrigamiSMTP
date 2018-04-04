package com.pessetto.origamismtp;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pessetto.origamismtp.status.StatusListener;
import com.pessetto.origamismtp.threads.ConnectionHandler;
import java.io.*;


/** The OrigamiSMTP main class
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class OrigamiSMTP{

	private ServerSocket smtpSocket;
	private List<StatusListener> statusListeners;
	private int port;
	
	/** Creates an instance opened to the specified port
	 * @param port The port to open SMTP on
	 */
	public OrigamiSMTP(int port)
	{
		this.port = port;
		statusListeners = new ArrayList<StatusListener>();
	}
	
	
	/** Starts the server.  Here for command line usage.
	 * @param args
	 * @throws Exception
	 */
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
	
	/**
	 * Adds a status listener
	 * @param sl A class that implements StatusListener
	 */
	public void addStatusListener(StatusListener sl)
	{
		statusListeners.add(sl);
	}
	
	/** Closes the SMTP connection
	 */
	public void closeSMTP()
	{
		try {
			smtpSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Starts the SMTP server
	 * @throws BindException
	 */
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
	
	/** Notifies the status listeners of SMTP start
	 */
	private void notifyStarted()
	{
		for(StatusListener listener : statusListeners)
		{
			listener.smtpStarted();
		}
	}
	
	/** Notifies the status listeners of SMTP stopped
	 */
	private void notifyStopped()
	{
		for(StatusListener listener : statusListeners)
		{
			listener.smtpStopped();
		}
	}
}
