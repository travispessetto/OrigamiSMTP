// https://tools.ietf.org/html/rfc2821#section-4.1.1
// http://stackoverflow.com/questions/11985896/can-a-java-server-accept-both-ssl-and-plaintext-connections-on-one-port
package com.pessetto.main;

import java.net.*;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import com.pessetto.CommandHandlers.CommandHandler;
import com.pessetto.Common.Variables;

import java.io.*;

public class ConsoleMain{

	public static void main(String args[]) throws Exception
	{
		System.out.println("Starting SMTP");
		SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		SSLServerSocket smtpSocket = (SSLServerSocket)serverSocketFactory.createServerSocket(2525);
		smtpSocket.setUseClientMode(false);
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
						commandHandler.HandleSTARTTLS(connectionSocket);
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
			System.out.println(raw);
		}
		return raw;
	}
	
	public static String GetCmdIdentifier(String cmd)
	{
		String[] parts = cmd.split(" ");
		return parts[0];
	}
}
