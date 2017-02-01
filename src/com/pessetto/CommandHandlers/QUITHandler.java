package com.pessetto.CommandHandlers;

import java.io.DataOutputStream;
import java.util.Scanner;

public class QUITHandler
{
	
	public QUITHandler(DataOutputStream outToClient, Scanner inFromClient)
	{
		try
		{
			outToClient.close();
			inFromClient.close();
		}
		catch(Exception ex)
		{
			System.err.println("Fatal Error");
		}
		
	}

}
