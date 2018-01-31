package com.pessetto.FileHandlers.Inbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.sun.mail.util.*;

import com.pessetto.Variables.InboxVariables;

public class Message implements Serializable
{
	
	private static final long serialVersionUID = -3256213945590604962L;
	private String from;
	private String to;
	private String subject;
	private String message;
	private String plainMessage;
	private String htmlMessage;
	private LinkedList<Attachment> attachments;
	
	public Message()
	{
		attachments = new LinkedList<Attachment>();
	}
	
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
	
	public String getHTMLMessage()
	{
		return htmlMessage;
	}
	
	public String getPlainMessage()
	{
		return plainMessage;
	}
	
	public void processMessage()
	{
		System.out.println("Process message");
		try
		{
			Session session = Session.getDefaultInstance(new Properties());
			InputStream inputStream = new ByteArrayInputStream(message.getBytes());
			MimeMessage mimeMessage = new MimeMessage(session,inputStream);
			if(mimeMessage.isMimeType(InboxVariables.plainMime))
			{
				plainMessage = mimeMessage.getContent().toString();
			}
			else if(mimeMessage.isMimeType(InboxVariables.multipartMime))
			{
				MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
				processMimeMultipart(mimeMultipart);
			}
			subject = mimeMessage.getSubject();
			System.out.println("Message processed");
		}
		catch(Exception ex)
		{
			ex.printStackTrace(System.err);
		}
	}

	
	private void processMimeMultipart(
	        MimeMultipart mimeMultipart) throws Exception{
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        String fileName = this.getFileName(bodyPart.getContentType());
	        if (fileName == null && bodyPart.isMimeType("text/plain")) {
	        	if(plainMessage == null)
	        	{
	        		String html = (String) bodyPart.getContent();
	        		plainMessage = org.jsoup.Jsoup.parse(html).html();
	        	}
	        	else
	        	{
	        		addPlainTextAttachment(bodyPart);
	        	}
	        }
	        else if(fileName == null && bodyPart.isMimeType("text/html"))
	        {
	        	if(htmlMessage == null)
	        	{
		        	String html = (String) bodyPart.getContent();
		        	htmlMessage = org.jsoup.Jsoup.parse(html).html();
	        	}
	        	else
	        	{
	        		addPlainTextAttachment(bodyPart);
	        	}
	        	
	        }
	        else if(bodyPart.isMimeType("multipart/alternative"))
	        {
	        	processMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	        else if(bodyPart.isMimeType("text/*"))
	        {
	        	String content = (String) bodyPart.getContent();
	        	int size = bodyPart.getSize();
	        	Attachment attach = new Attachment(fileName,content.getBytes(),size);
	        	attachments.add(attach);
	        }
	        else
	        {
	        	BASE64DecoderStream ds = (BASE64DecoderStream)bodyPart.getContent();
	        	byte[] content = new byte[bodyPart.getSize()];
	        	ds.read(content);
	        	int size = bodyPart.getSize();
	        	Attachment attach = new Attachment(fileName,content,size);
	        	attachments.add(attach);
	        }
	    }
	    
	}

	
    public LinkedList<Attachment> getAttachments() {
		return attachments;
	}
    
    public int getAttachmentCount()
    {
    	return attachments.size();
    }

	private String getFileName(String details)
    {
    	Pattern p = Pattern.compile("name=\"(.+?)\"");
    	Matcher m = p.matcher(details);
    	if(m.find())
    	{
    		if(m.groupCount() > 0)
    		{
    			return m.group(1);
    		}
    	}
    	else
    	{
    		System.out.println("Could not use regex to find file name");
    	}
    	return null;
    }
	
	private void addPlainTextAttachment(BodyPart b)
	{
		try {
			System.out.println("Adding plain text attachment");
			String fileName = getFileName(b.getContentType());
			String content = (String)b.getContent();
			int size = b.getSize();
			Attachment attach = new Attachment(fileName,content.getBytes(),size);
			attachments.add(attach);
		} catch (MessagingException | IOException e) {
			System.err.println("Could not get file name or read file content");
			e.printStackTrace();
		}
	}
	
}
