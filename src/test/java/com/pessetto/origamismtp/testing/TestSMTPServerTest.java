package com.pessetto.origamismtp.testing;

import static org.junit.Assert.fail;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.Test;


public class TestSMTPServerTest {

	public TestSMTPServerTest()
	{
		
	}
	
	@Test
	public void testGetRecievedMessage() 
	{
		TestSMTPServer server = new TestSMTPServer(2525);	
		try 
		{
			sendTestMessage();
			Future<com.pessetto.origamismtp.filehandlers.inbox.Message> messageFuture = server.getLatestMessageFuture();
			com.pessetto.origamismtp.filehandlers.inbox.Message latestMessage = messageFuture.get(10,TimeUnit.SECONDS);
			server.closeServer();
			if(latestMessage.getSubject().equals("Test email"))
			{
				return;
			}
			else
			{
				fail("No message recieved");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Thread interupted");
		} catch (ExecutionException e) {
			e.printStackTrace();
			fail("Execution Fail");
		} catch (TimeoutException e) {
			e.printStackTrace();
			fail("Message not recieved");
		} catch (AddressException e) {
			fail("Address not recieved");
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
			fail("Sending message exception");
		}
	}
	
	private void sendTestMessage() throws AddressException, MessagingException
	{
            Properties props = System.getProperties();
            props.put("mail.smtp.host","localhost");
            props.put("mail.smtp.port","2525");
            props.put("mail.smtp.socketFactory.port","2525");
            //props.put("mail.smtp.starttls.enable","true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("test@example.com"));
            msg.setRecipient(jakarta.mail.Message.RecipientType.TO,new InternetAddress("testTo@example.com"));
            msg.setSubject("Test email");
            msg.setText("Hello!");
            Transport.send(msg);
	}

}
