package com.pessetto.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class SSLModeHandler 
{
	public static void  EncryptedMode(SSLServerSocket socket) throws IOException
	{
		System.out.println("ENCRYPTED AWAIT");
		Socket connection = socket.accept();
		System.out.println("ENCRYPTED ACCEPT");
		DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
		Scanner inFromClient = new Scanner(connection.getInputStream());
		while(inFromClient.hasNextLine())
		{
			System.out.println(inFromClient.nextLine());
		}
	}
}
