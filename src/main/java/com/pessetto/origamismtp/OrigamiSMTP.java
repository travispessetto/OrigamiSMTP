package com.pessetto.origamismtp;

import com.pessetto.origamismtp.accounts.Accounts;
import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pessetto.origamismtp.status.StatusListener;
import com.pessetto.origamismtp.threads.ConnectionHandler;
import java.io.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import java.util.concurrent.Callable;


/** The OrigamiSMTP main class
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class OrigamiSMTP implements Callable<Integer>{

	private ServerSocket smtpSocket;
	private List<StatusListener> statusListeners;
	private int Port = 2525;
        
        /** Creates an instance opened to the specified port
         * @param port  The port to open SMTP on
         */
        public OrigamiSMTP(int port)
        {
            this(port,0);
        }
        
        /** Creates an instance of Origami SMTP meant to be used via CLI
         * 
         */
        public OrigamiSMTP()
        {
            statusListeners = new ArrayList<StatusListener>();
            Inbox inbox = Inbox.getInstance();
        }
	
	/** Creates an instance opened to the specified port
	 * @param port The port to open SMTP on
         * @param maxInboxSize size how many messages the inbox will hold before
         * deleting old messages
	 */
	public OrigamiSMTP(int port, int maxInboxSize)
	{
		Port = port;
		statusListeners = new ArrayList<StatusListener>();
                Inbox inbox = Inbox.getInstance();
                inbox.setSize(maxInboxSize);
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
	 * @throws BindException Failure to bind to port
	 */
	public void startSMTP() throws BindException
	{
		try
		{
			ExecutorService threadPool = Executors.newWorkStealingPool();
			java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
			java.lang.System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
			Socket ssls = null;
			System.out.println("Starting SMTP on PORT "+Port);
			InetSocketAddress bindAddress = new InetSocketAddress(Port);
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

    @Override
    public Integer call() throws Exception {
        OrigamiSMTP console = new OrigamiSMTP();
        console.startSMTP();
        return 0;
    }

	public static void main(String[] args) throws BindException
	{
            var settings = Settings.getInstance();
            var accounts = Accounts.getInstance();
            if(args.length == 0)
            {
		OrigamiSMTP server = new OrigamiSMTP(settings.getPort());
		server.startSMTP();
            }
            else
            {
                for(int i = 0; i < args.length; ++i)
                {
                    if(args[i].indexOf("--") == 0 && i< args.length)
                    {
                        var arg = args[i].substring(2).toUpperCase();
                        switch(arg)
                        {
                            case "PORT":
                                if(i + 1 < args.length)
                                {
                                    var port = parseInt(args[i+1]);
                                    System.out.println("Port set to "+port);
                                    settings.setPort(port);
                                }
                                break;
                            case "REQUIRE-VALID-SIGNIN":
                                if(i + 1 < args.length)
                                {
                                    var validSignInRequired = parseBoolean(args[i+1]);
                                    System.out.println("Sign in required set to  "+validSignInRequired);
                                    settings.requireValidSignIn(validSignInRequired);
                                }
                                break;
                            case "ADD-ACCOUNT":
                                if(i + 2 < args.length)
                                {
                                    var userName = args[i+1];
                                    var password = args[i+2];
                                    try
                                    {
                                        if(accounts.addAccount(userName, password))
                                        {
                                            System.out.println("Account added");
                                        }
                                        else
                                        {
                                            System.out.println("Account could not be added");
                                        }
                                    }
                                    catch(Exception ex)
                                    {
                                        System.out.println("Could not add account:" + ex.getLocalizedMessage());
                                    }
                                    
                                }
                                break;
                            case "REMOVE-ACCOUNT":
                                if(i+1 < args.length)
                                {
                                    var userName = args[i+1];
                                    accounts.removeAccount(userName);
                                    System.out.println("Account removed");
                                }
                                break;
                            case "LIST-SETTINGS":
                                System.out.println("PORT: "+settings.getPort());
                                System.out.print("Sign in In Required: " + settings.getRequireSignIn());
                                break;
                        }
                    }
                }
            }
	}
}
