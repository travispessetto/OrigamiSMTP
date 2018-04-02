package com.pessetto.origamismtp.commandhandlers;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.filehandlers.EmailHandler;
import com.pessetto.origamismtp.status.AuthStatus;


public class CommandHandler
{
	private AUTHHandler auth;
	private DataHandler data;
	private EHLOHandler ehlo;
	private MAILHandler mail;
	private RCPTHandler rcpt;
	private STARTTLSHandler tls;
	private DataOutputStream outToClient;
	private Scanner inFromClient;
	private boolean secure;
	private AuthStatus authStatus;
	
	public CommandHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		this.outToClient = outToClient;
		this.inFromClient = inFromClient;
		secure = false;
		authStatus = AuthStatus.START;
	}
	
	public AuthStatus HandleAuth(String fullAuth)
	{
		if(auth == null)
		{
			auth = new AUTHHandler(fullAuth);
		}
		HandleResponse(auth.GetResponse());
		return auth.getStatus();
	}
	
	public void HandleData()
	{
		data = new DataHandler(rcpt);
		HandleResponse(data.GetResponse());
		data = (DataHandler)data.ValidateOrNullify();
		if(data != null)
		{
			data.ProcessMessage(inFromClient);
			HandleResponse(data.GetResponse());
			EmailHandler email = new EmailHandler(mail, rcpt, data);
		}
	}
	
	public void HandleEHLO(String fullEHLO)
	{
		ehlo = new EHLOHandler(fullEHLO,secure);
		HandleResponse(ehlo.GetResponse());
		ehlo = (EHLOHandler)ehlo.ValidateOrNullify();
	}
	
	public void HandleMAIL(String fullMAIL)
	{
		mail = new MAILHandler(fullMAIL,ehlo);
		HandleResponse(mail.GetResponse());
		mail = (MAILHandler) mail.ValidateOrNullify();
	}
	
	public void HandleRCPT(String fullCmd)
	{
		if(rcpt == null)
		{
			rcpt = new RCPTHandler(fullCmd,mail);
		}
		else
		{
			rcpt.AddAddress(fullCmd);
		}
		HandleResponse(rcpt.GetResponse());
		rcpt = (RCPTHandler) rcpt.ValidateOrNullify();
	}
	
	public void HandleRSET()
	{
		RSETHandler rset = new RSETHandler(mail,rcpt,data);
		HandleResponse(rset.GetResponse());
	}
	
	public SSLSocket HandleSTARTTLS(Socket old) throws IOException
	{
		tls = new STARTTLSHandler(old);
		HandleResponse(tls.GetResponse());
		SSLSocket ssocket = tls.EnableTLS(old);
		System.out.println("Secure socket setup");
		secure = true;
		return ssocket;
	}
	
	public void HandleQuit()
	{
		QUITHandler quitH = new QUITHandler(outToClient,inFromClient);
		HandleResponse(quitH.GetResponse());
		//quitH = (QUITHandler)quitH.ValidateOrNullify();
	}
	
	public void HandleNotImplemented(String cmd)
	{
		HandleResponse("502 Command Not Implemented"+Constants.CRLF);
	}
	
	public void HandleResponse(String response)
	{
		try 
		{
			outToClient.writeBytes(response);
		}
		catch (IOException e) 
		{
			System.err.println("Fatal Error");
			e.printStackTrace();
		}
	}
	
	public void setInAndOutFromClient(Scanner in, DataOutputStream out)
	{
		inFromClient = in;
		outToClient = out;
	}

}
