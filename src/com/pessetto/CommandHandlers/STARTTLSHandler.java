// https://tools.ietf.org/html/rfc3207
package com.pessetto.CommandHandlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
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
	SSLContext sslContext;
	KeyStore keyStore;
	KeyManagerFactory keyManagerFactory;
	
	public STARTTLSHandler()
	{
		try
		{
			System.setProperty("javax.net.ssl.keyStore", "./keys");
			System.setProperty("javax.net.ssl.keyStorePassword", "password");
			keyStore  = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream ksIs = new FileInputStream("./keys");
			keyStore.load(ksIs,"password".toCharArray());
			if(ksIs != null)
			{
				ksIs.close();
			}
			
			keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, "password".toCharArray());
			sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(keyManagerFactory.getKeyManagers(),null, null);
			Response = "200 STARTLS" + Variables.CRLF;
		}
		catch(Exception e)
		{
			System.err.println("Fatal Error: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public SSLSocket EnableTLS(Socket old) throws IOException
	{
		//SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		newSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(old, null,old.getPort(),false);
		newSocket.addHandshakeCompletedListener(new MyHandshakeListener());
		newSocket.setEnabledProtocols(new String[]{"TLSv1.1","TLSv1.2"});
		newSocket.setUseClientMode(false);
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
