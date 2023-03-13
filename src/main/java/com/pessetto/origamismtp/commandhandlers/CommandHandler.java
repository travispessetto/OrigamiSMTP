package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.commandhandlers.interfaces.IAUTHHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.net.ssl.SSLSocket;
import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.filehandlers.EmailHandler;
import com.pessetto.origamismtp.status.*;


/** Represents a handler able to pass commands to other classes
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class CommandHandler
{
	private IAUTHHandler auth;
	private DataHandler data;
	private EHLOHandler ehlo;
	private MAILHandler mail;
	private RCPTHandler rcpt;
	private STARTTLSHandler tls;
	private DataOutputStream outToClient;
	private Scanner inFromClient;
	private boolean secure;
	
	/** Creates a new CommandHandler
	 * @param outToClient A stream that goes out to the client
	 * @param inFromClient A stream that comes in from the client
	 */
	public CommandHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		this.outToClient = outToClient;
		this.inFromClient = inFromClient;
		secure = false;
	}
	
	/** Handles the AUTH command
	 * @param fullAuth The full auth command 
	 * @return an Enum value of the current authentication status
	 */
	public AuthStatus handleAuth(String fullAuth)
	{
		if(auth == null)
		{
                    auth = new AUTHHandler();
		}
		handleResponse(auth.getResponse(fullAuth));
		return auth.getStatus();
	}
	
	/** Handles the DATA command
	 */
	public void handleData()
	{
		data = new DataHandler(rcpt);
		handleResponse(data.getResponse());
		data = (DataHandler)data.validateOrNullify();
		if(data != null)
		{
			data.processMessage(inFromClient);
			handleResponse(data.getResponse());
                        EmailHandler emailHandler = new EmailHandler(this.mail,this.rcpt,this.data);
		}
	}
	
	/** Handles the EHLO command
	 * @param fullEHLO The full line of the command from client
	 */
	public void handleEHLO(String fullEHLO)
	{
		ehlo = new EHLOHandler(fullEHLO,secure);
		handleResponse(ehlo.getResponse());
		ehlo = (EHLOHandler)ehlo.validateOrNullify();
	}
	
	/** Handles the Mail command
	 * @param fullMAIL The full line of the command from the client
	 */
	public void handleMAIL(String fullMAIL)
	{
		mail = new MAILHandler(fullMAIL,ehlo);
		handleResponse(mail.getResponse());
		mail = (MAILHandler) mail.validateOrNullify();
	}
	
	/** Handles the RCPT command
	 * @param fullCmd The full line of the RCPT command
	 */
	public void handleRCPT(String fullCmd)
	{
		if(rcpt == null)
		{
			rcpt = new RCPTHandler(fullCmd,mail);
		}
		else
		{
			rcpt.addAddress(fullCmd);
		}
		handleResponse(rcpt.getResponse());
		rcpt = (RCPTHandler) rcpt.validateOrNullify();
	}
	
	/** Handles the RSET command
	 */
	public void handleRSET()
	{
		RSETHandler rset = new RSETHandler(mail,rcpt,data);
		handleResponse(rset.getResponse());
	}
	
	/** Handles the STARTTLS command
	 * @param old The old non-ssl socket
	 * @return The new encrypted socket
	 * @throws IOException Input or Output failed
	 */
	public SSLSocket handleSTARTTLS(Socket old) throws IOException
	{
		tls = new STARTTLSHandler(old);
		handleResponse(tls.getResponse());
		SSLSocket ssocket = tls.enableTLS(old);
		System.out.println("Secure socket setup");
		secure = true;
		return ssocket;
	}
	
	/** Handles the QUIT command
	 */
	public void handleQuit()
	{
		QUITHandler quitH = new QUITHandler(outToClient,inFromClient);
		handleResponse(quitH.getResponse());
		//quitH = (QUITHandler)quitH.ValidateOrNullify();
	}
	
	/** Handles any command not implemented (missing) or not valid
	 * @param cmd The command that is not implemented
	 */
	public void handleNotImplemented(String cmd)
	{
		handleResponse("502 Command Not Implemented"+Constants.CRLF);
	}
	
	/** Handles the response to the client
	 * @param response The response to send to the client
	 */
	public void handleResponse(String response)
	{
		try 
		{
			System.out.println(response);
			outToClient.writeBytes(response);
		}
		catch (IOException e) 
		{
			System.err.println("Fatal Error");
			e.printStackTrace();
		}
	}
	
	/** Sets the in and out streams to and from the client
	 * @param in The stream in from the client
	 * @param out The stream out to the client
	 */
	public void setInAndOutFromClient(Scanner in, DataOutputStream out)
	{
		inFromClient = in;
		outToClient = out;
	}

}
