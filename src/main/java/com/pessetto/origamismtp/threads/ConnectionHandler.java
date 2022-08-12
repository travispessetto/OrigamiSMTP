package com.pessetto.origamismtp.threads;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLHandshakeException;

import com.pessetto.origamismtp.commandhandlers.CommandHandler;
import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.status.AuthStatus;

/** Represents a class to handle connections
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class ConnectionHandler implements Runnable {

	private Socket connectionSocket;
	
	/** Creates new instance of the ConnectionHandler on socket
	 * @param connectionSocket The socket to use
	 */
	public ConnectionHandler(Socket connectionSocket)
	{
		this.connectionSocket = connectionSocket;
	}

	/** Runs the connection in a new thread
	 */
	@Override
	public void run() {
		try
		{
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
			CommandHandler commandHandler = new CommandHandler(outToClient,inFromClient);
			inFromClient.useDelimiter(Constants.CRLF);
			String welcome = "220 local.origamimail.us SMTP Ready"+Constants.CRLF;
			outToClient.writeBytes(welcome);
			String cmd = "";
			boolean quit = false;
			AuthStatus authStatus = AuthStatus.START;
			while(!Thread.currentThread().isInterrupted() && !quit && (cmd = getFullCmd(inFromClient)) != "QUIT")
			{
				String cmdId = getCmdIdentifier(cmd).toLowerCase();
				System.out.println(cmd);
				System.out.println("Command Handler auth Status: " + authStatus);
				if(authStatus == AuthStatus.CONTINUE)
				{
					authStatus = commandHandler.handleAuth(cmd);
				}
				else if(cmdId.equals("auth"))
				{
					authStatus = commandHandler.handleAuth(cmd);
				}
				else if(cmdId.equals("data"))
				{
					commandHandler.handleData();
				}
				else if(cmdId.equals("ehlo") || cmdId.equals("helo"))
				{
					commandHandler.handleEHLO(cmd);
				}
				else if(cmdId.equals("mail"))
				{
				
					commandHandler.handleMAIL(cmd);
				}
				else if(cmdId.equals("rset") || cmd.equals("reset"))
				{
					commandHandler.handleRSET();
				}
				else if(cmdId.equals("rcpt"))
				{
					commandHandler.handleRCPT(cmd);
				}
				else if(cmdId.equals("starttls"))
				{
					connectionSocket = commandHandler.handleSTARTTLS(connectionSocket);
					outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					inFromClient = new Scanner(connectionSocket.getInputStream());
					commandHandler.setInAndOutFromClient(inFromClient, outToClient);
				}
				else if(cmdId.equals("quit"))
				{
					commandHandler.handleQuit();
                                        connectionSocket.close();
					quit = true;
				}

				else
				{
					commandHandler.handleNotImplemented(cmd);
				}
			}
		}
		catch(SSLHandshakeException ex)
		{
			System.out.println("SSL Handshake failed");
			ex.printStackTrace(System.err);
		}
		catch(Exception ex)
		{
			System.out.println("Client Disconnect");
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		
	}
	
	/** Gets the full command
	 * @param inFromClient The stream in from client
	 * @return The string represnetation of the command
	 */
	private static String getFullCmd(Scanner inFromClient)
	{
		String raw = "QUIT";
		if(inFromClient.hasNextLine())
		{
			raw = inFromClient.nextLine();
		}
		return raw;
	}
	
	/** Gets the command identifier
	 * @param cmd The full command
	 * @return The command identifier
	 */
	private static String getCmdIdentifier(String cmd)
	{
		String[] parts = cmd.split(" ");
		return parts[0].toLowerCase();
	}

}
