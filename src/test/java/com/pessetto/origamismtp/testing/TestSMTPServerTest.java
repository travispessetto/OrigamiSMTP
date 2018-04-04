import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import com.pessetto.origamismtp.testing.*;
import org.junit.Test;


class TestSMTPServerTest {

	@Test
	void testGetRecievedMessage() 
	{
		TestSMTPServer server = new TestSMTPServer(2525);
		Thread smtpServerThread = new Thread(server);
		smtpServerThread.start();
		
		Inbox inbox = Inbox.getInstance();
		TestSMTPNewMessageListener newMessageListener = new TestSMTPNewMessageListener();
		inbox.addNewMessageListener(newMessageListener);
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> messageFuture = executor.submit(newMessageListener);
		try 
		{
			sendTestMessage();
			Boolean messageRecieved = messageFuture.get(10,TimeUnit.SECONDS);
			if(messageRecieved.booleanValue())
			{
				com.pessetto.origamismtp.filehandlers.inbox.Message latestMessage = inbox.getNewestMessage();
				if(latestMessage.getSubject().equals("Test email"))
				{
					return;
				}
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
        msg.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress("testTo@example.com"));
        msg.setSubject("Test email");
        msg.setText("Hello!");
        Transport.send(msg);
	}

}
