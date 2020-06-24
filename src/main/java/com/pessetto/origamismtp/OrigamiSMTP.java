package com.pessetto.origamismtp;

import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pessetto.origamismtp.status.StatusListener;
import com.pessetto.origamismtp.threads.ConnectionHandler;
import java.io.*;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


/** The OrigamiSMTP main class
 * @author Travis Pessetto
 * @author pessetto.com
 */
@Command(name = "Origami SMTP", mixinStandardHelpOptions = true, version = "2.0.2",
         description = "Starts a local SMTP server.")
public class OrigamiSMTP implements Callable<Integer>{

	private ServerSocket smtpSocket;
	private List<StatusListener> statusListeners;
        @Option(names = {"-p", "--port"}, description = "Port to bind to (default 2525)")
	private int port = 2525;
        
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
		this.port = port;
		statusListeners = new ArrayList<StatusListener>();
                Inbox inbox = Inbox.getInstance();
                inbox.setSize(maxInboxSize);
	}
	
	
	/** Starts the server.  Here for command line usage.
	 * @param args Program arguments
	 * @throws Exception Anything that could go wrong with the connection
	 */
	public static void main(String args[]) throws Exception
	{
            int exitCode = new CommandLine(new OrigamiSMTP()).execute(args);  
            System.exit(exitCode);
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

    @Override
    public Integer call() throws Exception {
        OrigamiSMTP console = new OrigamiSMTP();
        console.startSMTP();
        return 0;
    }
}
