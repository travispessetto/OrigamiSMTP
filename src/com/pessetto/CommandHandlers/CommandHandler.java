package com.pessetto.CommandHandlers;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

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
	
	public CommandHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		this.outToClient = outToClient;
		this.inFromClient = inFromClient;
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
		ehlo = new EHLOHandler(fullEHLO);
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
	
	public void HandleSTARTTLS()
	{
		tls = new STARTTLSHandler();
		HandleResponse(tls.GetResponse());
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
			System.out.print(response);
			outToClient.writeBytes(response);
		}
		catch (IOException e) 
		{
			System.err.println("Fatal Error");
			e.printStackTrace();
		}
	}

}
