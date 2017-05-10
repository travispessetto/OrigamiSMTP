package com.pessetto.FileHandlers.Inbox;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.pessetto.Variables.InboxVariables;

public class Message implements Serializable
{
	private static final long serialVersionUID = -6718647229973886137L;
	private String from;
	private String to;
	private String subject;
	private String message;
	private String processedMessage;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
		processMessage();
	}
	
	public String getProcessedMessage()
	{
		return processedMessage;
	}
	
	public void processMessage()
	{
		try
		{
			Session session = Session.getDefaultInstance(new Properties());
			InputStream inputStream = new ByteArrayInputStream(message.getBytes());
			MimeMessage mimeMessage = new MimeMessage(session,inputStream);
			if(mimeMessage.isMimeType(InboxVariables.plainMime))
			{
				processedMessage = mimeMessage.getContent().toString();
			}
			else if(mimeMessage.isMimeType(InboxVariables.multipartMime))
			{
				MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
				processedMessage = getTextFromMimeMultipart(mimeMultipart);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace(System.err);
		}
	}
	
	private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws Exception{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/html") || bodyPart.isMimeType("text/plain")) {
	            String html = (String) bodyPart.getContent();
	            result = org.jsoup.Jsoup.parse(html).html();
	        }
	    }
	    return result;
	}
	
	
	
}
