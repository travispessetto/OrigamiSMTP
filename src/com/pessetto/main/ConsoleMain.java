// https://tools.ietf.org/html/rfc2821#section-4.1.1
// http://stackoverflow.com/questions/11985896/can-a-java-server-accept-both-ssl-and-plaintext-connections-on-one-port
package com.pessetto.main;

import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLSocket;

import com.pessetto.CommandHandlers.CommandHandler;
import com.pessetto.Common.Variables;
import com.pessetto.Threads.ConnectionHandler;
import com.pessetto.FileHandlers.Inbox.*;

import java.io.*;

public class ConsoleMain{

	private ServerSocket smtpSocket;
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
		ConsoleMain console = new ConsoleMain(bindPort);
		console.startSMTP();
	}
	
	// To also be able to use it in a library
	public ConsoleMain(int port)
	{
		this.port = port;
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
	
	public void startSMTP()
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
				System.out.println("Quit due to interupted thread");
			}
		}
		catch(Exception ex)
		{
			System.err.println("Failed to open socket");
			ex.printStackTrace(System.err);
		}
	}
}
