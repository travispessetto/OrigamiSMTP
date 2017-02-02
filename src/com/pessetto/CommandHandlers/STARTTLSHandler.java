// https://tools.ietf.org/html/rfc3207
package com.pessetto.CommandHandlers;

import java.io.IOException;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.pessetto.Common.Variables;

public class STARTTLSHandler 
{
	String Response;
	SSLServerSocketFactory socketFactory;
	SSLSocket newSocket;
	
	public STARTTLSHandler()
	{
		Response = "200 Go Ahead" + Variables.CRLF;
	}
	
	public SSLSocket EnableTLS(Socket old) throws IOException
	{
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		newSocket = (SSLSocket) factory.createSocket(old,null,old.getPort(),false);		
		newSocket.setUseClientMode(false);
		newSocket.startHandshake();
		return newSocket;
	}
	
	public String GetResponse()
	{
		return Response;
	}
}

class MyHandshakeListener implements HandshakeCompletedListener {
	  public void handshakeCompleted(HandshakeCompletedEvent e) {
	    System.out.println("Handshake succesful!");
	    System.out.println("Using cipher suite: " + e.getCipherSuite());
	  }
	}
