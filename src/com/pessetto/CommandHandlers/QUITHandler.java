package com.pessetto.CommandHandlers;

import java.io.DataOutputStream;
import java.util.Scanner;

import com.pessetto.CommandHandlers.Interfaces.Validatable;
import com.pessetto.Common.Variables;

public class QUITHandler implements Validatable
{
	DataOutputStream outToClient;
	Scanner inFromClient;
	public QUITHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		this.outToClient = outToClient;
		this.inFromClient = inFromClient;
	}

	@Override
	public String GetResponse() 
	{
		return "221 Closing connection; Goodbye;"+Variables.CRLF;
	}

	@Override
	public Validatable ValidateOrNullify() 
	{
		// Always go to null and close
		try
		{
			outToClient.close();
			inFromClient.close();
		}
		catch(Exception ex)
		{
			System.err.println("Fatal Error");
		}
		return null;
	}
	
	

}
