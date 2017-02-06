// https://tools.ietf.org/html/rfc2821#section-4.1.1
// http://stackoverflow.com/questions/11985896/can-a-java-server-accept-both-ssl-and-plaintext-connections-on-one-port
package com.pessetto.main;

import java.net.*;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;

import com.pessetto.CommandHandlers.CommandHandler;
import com.pessetto.Common.Variables;

import java.io.*;

public class ConsoleMain{

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
			boolean quit = false;
			System.out.println("AWAIT CONNECTION");
			Socket connectionSocket = smtpSocket.accept();;
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
			CommandHandler commandHandler = new CommandHandler(outToClient,inFromClient);
			inFromClient.useDelimiter(Variables.CRLF);
			String welcome = "220 127.0.0.1 SMTP Ready"+Variables.CRLF;
			outToClient.writeBytes(welcome);
			System.out.print(welcome);
			try
			{
				String cmd = "";
				while((cmd = GetFullCmd(inFromClient)) != "QUIT")
				{
					String cmdId = GetCmdIdentifier(cmd).toLowerCase();
					if(cmdId.equals("data"))
					{
						commandHandler.HandleData();
					}
					else if(cmdId.equals("ehlo") || cmdId.equals("helo"))
					{
						commandHandler.HandleEHLO(cmd);
					}
					else if(cmdId.equals("mail"))
					{
					
						commandHandler.HandleMAIL(cmd);
					}
					else if(cmdId.equals("rset") || cmd.equals("reset"))
					{
						commandHandler.HandleRSET();
					}
					else if(cmdId.equals("rcpt"))
					{
						commandHandler.HandleRCPT(cmd);
					}
					else if(cmdId.equals("starttls"))
					{
						connectionSocket = commandHandler.HandleSTARTTLS(connectionSocket);
						System.out.println("connectionSocket now set to TLS");
						outToClient = new DataOutputStream(connectionSocket.getOutputStream());
						inFromClient = new Scanner(connectionSocket.getInputStream());
						commandHandler.setInAndOutFromClient(inFromClient, outToClient);
					}
					else if(cmdId.equals("quit"))
					{
						commandHandler.HandleQuit();
						quit = true;
					}

					else
					{
						commandHandler.HandleNotImplemented(cmd);
					}
				}
			}
			catch(Exception ex)
			{
				System.err.println("Client Disconnect");
				System.err.println(ex.getMessage());
				ex.printStackTrace(System.err);
			}
		}
	}
	
	public static String GetFullCmd(Scanner inFromClient)
	{
		String raw = "QUIT";
		if(inFromClient.hasNextLine())
		{
			raw = inFromClient.nextLine();
			System.out.println("C: " + raw);
		}
		return raw;
	}
	
	public static String GetCmdIdentifier(String cmd)
	{
		String[] parts = cmd.split(" ");
		return parts[0];
	}
}
