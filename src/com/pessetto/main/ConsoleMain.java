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

import java.io.*;

public class ConsoleMain{

	public static void main(String args[]) throws Exception
	{
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
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
		java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		java.lang.System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
		Socket ssls = null;
		System.out.println("Starting SMTP");
		InetSocketAddress bindAddress = new InetSocketAddress(2525);
		ServerSocket smtpSocket = new ServerSocket();
		smtpSocket.setReuseAddress(true);
		smtpSocket.bind(bindAddress);
		System.out.println("Socket Opened");
		while(true)
		{
			System.out.println("AWAIT CONNECTION");
			Socket connectionSocket = smtpSocket.accept();
			ConnectionHandler connectionHandler = new ConnectionHandler(connectionSocket);
			threadPool.submit(connectionHandler);
			System.out.println("Connection sent to thread");
		}
	}
}
