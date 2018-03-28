package com.pessetto.Threads;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.pessetto.CommandHandlers.CommandHandler;
import com.pessetto.Common.Variables;
import com.pessetto.Debug.ThreadLogger;
import com.pessetto.Status.AuthStatus;

public class ConnectionHandler implements Runnable {

	private Socket connectionSocket;
	public ConnectionHandler(Socket connectionSocket)
	{
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {
		ThreadLogger threadLogger = new ThreadLogger();
		try
		{
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
			CommandHandler commandHandler = new CommandHandler(outToClient,inFromClient,threadLogger);
			inFromClient.useDelimiter(Variables.CRLF);
			String welcome = "220 127.0.0.1 SMTP Ready"+Variables.CRLF;
			outToClient.writeBytes(welcome);
			String cmd = "";
			boolean quit = false;
			AuthStatus authStatus = AuthStatus.START;
			while(!Thread.currentThread().isInterrupted() && !quit && (cmd = GetFullCmd(inFromClient)) != "QUIT")
			{
				String cmdId = GetCmdIdentifier(cmd).toLowerCase();
				threadLogger.logMessage(true, cmd);
				if(authStatus == AuthStatus.CONTINUE)
				{
					authStatus = commandHandler.HandleAuth(cmd);
				}
				else if(cmdId.equals("auth"))
				{
					authStatus = commandHandler.HandleAuth(cmd);
				}
				else if(cmdId.equals("data"))
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
			threadLogger.closeLog();
		}
		catch(Exception ex)
		{
			threadLogger.closeLog();
			System.err.println("Client Disconnect");
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		
	}
	
	private static String GetFullCmd(Scanner inFromClient)
	{
		String raw = "QUIT";
		if(inFromClient.hasNextLine())
		{
			raw = inFromClient.nextLine();
		}
		return raw;
	}
	
	private static String GetCmdIdentifier(String cmd)
	{
		String[] parts = cmd.split(" ");
		return parts[0].toLowerCase();
	}

}
