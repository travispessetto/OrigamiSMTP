package com.pessetto.CommandHandlers;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import com.pessetto.Common.Variables;
import com.pessetto.FileHandlers.EmailHandler;

public class CommandHandler
{
	private DataHandler data;
	private EHLOHandler ehlo;
	private MAILHandler mail;
	private RCPTHandler rcpt;
	private STARTTLSHandler tls;
	private DataOutputStream outToClient;
	private Scanner inFromClient;
	private boolean secure;
	
	public CommandHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		this.outToClient = outToClient;
		this.inFromClient = inFromClient;
		secure = false;
	}
	
	public void HandleData()
	{
		data = new DataHandler();
		HandleResponse(data.GetResponse());
		data.ProcessMessage(inFromClient);
		HandleResponse(data.GetResponse());
		EmailHandler email = new EmailHandler(mail, rcpt, data);
	}
	
	public void HandleEHLO(String fullEHLO)
	{
		ehlo = new EHLOHandler(fullEHLO,secure);
		HandleResponse(ehlo.GetResponse());
	}
	
	public void HandleMAIL(String fullMAIL)
	{
		mail = new MAILHandler(fullMAIL);
		HandleResponse(mail.GetResponse());
	}
	
	public void HandleRCPT(String fullCmd)
	{
		rcpt = new RCPTHandler(fullCmd);
		HandleResponse(rcpt.GetResponse());
	}
	
	public void HandleRSET()
	{
		RSETHandler rset = new RSETHandler(ehlo);
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
	}
	
	public void HandleNotImplemented(String cmd)
	{
		System.err.println("NOT IMPLEMENTED: " + cmd);
		HandleResponse("502 Command Not Implemented"+Variables.CRLF);
	}
	
	public void HandleResponse(String response)
	{
		try 
		{
			System.out.print("S: "+response);
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
