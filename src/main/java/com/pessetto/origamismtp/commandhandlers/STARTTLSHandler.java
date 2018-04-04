package com.pessetto.origamismtp.commandhandlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import com.pessetto.origamismtp.constants.Constants;

/** Represents a handler for the STARTTLS command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class STARTTLSHandler 
{
	String response;
	SSLServerSocketFactory socketFactory;
	SSLSocket newSocket;
	SSLContext sslContext;
	KeyStore keyStore;
	KeyStore trustStore;
	KeyManagerFactory keyManagerFactory;
	TrustManagerFactory trustFactory;
	
	/** Creates a new instance of the STARTTLSHandler
	 * @param old The socket that is currently in use (not encrypted)
	 */
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
				response = "454 TLS not available due to temporary reason: TLS already active";
			}
			else
			{
				response = "220 Ready to start TLS" + Constants.CRLF;
			}
		}
		catch(Exception e)
		{
			System.err.println("Fatal Error: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/** Enables SSL on the socket
	 * @param old The old (unencrypted) socket
	 * @return A new encrypted socket
	 * @throws IOException
	 */
	public SSLSocket enableTLS(Socket old) throws IOException
	{
		response = null;
		try
		{
			newSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(old, null,old.getPort(),false);
			newSocket.setEnabledProtocols(newSocket.getSupportedProtocols());
			newSocket.setEnabledCipherSuites(newSocket.getSupportedCipherSuites());
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
	
	/** The response to send to the client
	 * @return The string response to the client
	 */
	public String getResponse()
	{
		return response;
	}
	
	
}

