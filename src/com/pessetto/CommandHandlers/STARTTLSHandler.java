// https://tools.ietf.org/html/rfc3207
// http://blog.trifork.com/2009/11/10/securing-connections-with-tls/
// http://docs.oracle.com/javase/7/docs/technotes/guides/security/jsse/ReadDebug.html
package com.pessetto.CommandHandlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.pessetto.Common.Variables;

public class STARTTLSHandler 
{
	String Response;
	SSLServerSocketFactory socketFactory;
	SSLSocket newSocket;
	SSLContext sslContext;
	KeyStore keyStore;
	KeyStore trustStore;
	KeyManagerFactory keyManagerFactory;
	TrustManagerFactory trustFactory;
	
	public STARTTLSHandler(Socket old)
	{
		try
		{
			keyStore  = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream ksIs = STARTTLSHandler.class.getClassLoader().getResourceAsStream("keys");
			keyStore.load(ksIs,"password".toCharArray());
			if(ksIs != null)
			{
				ksIs.close();
			}
			
			keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, "password".toCharArray());
			
			InputStream trustStoreIs = STARTTLSHandler.class.getClassLoader().getResourceAsStream("truststore");
			trustStore = KeyStore.getInstance("JKS");
			trustStore.load(trustStoreIs, "password".toCharArray());
			trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(trustStore);
			
			sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(keyManagerFactory.getKeyManagers(),trustFactory.getTrustManagers(), null);
			
			if(old instanceof SSLSocket)
			{
				Response = "454 TLS not available due to temporary reason: TLS already active";
			}
			else
			{
				Response = "220 Ready to start TLS" + Variables.CRLF;
			}
		}
		catch(Exception e)
		{
			System.err.println("Fatal Error: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public SSLSocket EnableTLS(Socket old) throws IOException
	{
		Response = null;
		try
		{
			newSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(old, null,old.getPort(),false);
			newSocket.setEnabledProtocols(newSocket.getSupportedProtocols());
			DisplayProtocols(newSocket.getSupportedProtocols());
			
			newSocket.setEnabledCipherSuites(newSocket.getSupportedCipherSuites());
			DisplayCiphers(newSocket.getSupportedCipherSuites());
			newSocket.setUseClientMode(false);
			Thread.sleep(1000);
			newSocket.startHandshake();
			if(newSocket.getNeedClientAuth())
			{
				try
				{
					Certificate[] peerCerts = newSocket.getSession().getPeerCertificates();
					
				}
				catch(SSLPeerUnverifiedException e)
				{
					System.out.println("PEERS UNVERIFIED; IGNORE"); 
				}
			}
			return newSocket;
		}
		catch(SSLHandshakeException ex)
		{
			System.err.println("Handshake failed!");
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		catch(Exception ex)
		{
			System.out.println("SSL FAILED");
			ex.printStackTrace(System.err);
		}
		return null;
	}
	
	public String GetResponse()
	{
		return Response;
	}
	
	
	private void DisplayArrayAsString(String[] arr)
	{
		for(String str : arr)
		{
			System.out.println("- " + str);
		}
	}
	
	private void DisplayCiphers(String[] ciphers)
	{
		System.out.println("Chiphers: ");
		DisplayArrayAsString(ciphers);
	}
	
	private void DisplayProtocols(String[] protocols)
	{
		System.out.println("Protocols:");
		DisplayArrayAsString(protocols);
	}
}

