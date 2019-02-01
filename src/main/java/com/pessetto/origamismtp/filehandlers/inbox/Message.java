package com.pessetto.origamismtp.filehandlers.inbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.pessetto.origamismtp.constants.Constants;
import com.sun.mail.util.BASE64DecoderStream;
import javafx.beans.property.SimpleBooleanProperty;

/** Represents a message
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class Message implements Serializable
{

  private static final long serialVersionUID = -3256213945590604962L;
  private String from;
  private String to;
  private String subject;
  private String message;
  private String plainMessage;
  private String htmlMessage;
  private boolean read;
  private transient SimpleBooleanProperty isRead;
  private final LinkedList<Attachment> attachments;

  /** Constructs a new instance of Message
   */
  public Message()
  {
    attachments = new LinkedList<>();
    read = false;
    isRead = new SimpleBooleanProperty(read);
  }

  /** Gets the read flag.  It is read if true.
   * @return SimpleBooleanProperty if the message has been read or not
   */
  public SimpleBooleanProperty isRead()
  {
    if (isRead == null)
    {
      System.out.println("Creating simple boolean set to " + read
              + " for email read");
      isRead = new SimpleBooleanProperty(read);
    }
    return isRead;
  }

  /** Sets the read flag.  true is yes; fase is no.
   * @param isRead The boolean value for yes or no
   */
  public void setRead(boolean isRead)
  {
    if (this.isRead == null)
    {
      System.out.println("Creating simple boolean set to " + read
              + " for email read");
      this.isRead = new SimpleBooleanProperty(read);
    }
    System.out.println("Setting message read flag to: " + isRead);
    read = isRead;
    this.isRead.set(read);
  }

  /** Returns who the message is from
   * @return who the message is from
   */
  public String getFrom()
  {
    return from;
  }

  /** Sets who the message is from
   * @param from The address the message is from
   */
  public void setFrom(String from)
  {
    this.from = from;
  }

  /** Gets who the message is from
   * @return The address the message is to
   */
  public String getTo()
  {
    return to;
  }

  /** Gets who the message is to
   * @param to The recipient of the message
   */
  public void setTo(String to)
  {
    this.to = to;
  }

  /** Gets the message subject
   * @return String of the subject
   */
  public String getSubject()
  {
    return subject;
  }

  /** Sets the subject of the message
   * @param subject The subject of the message
   */
  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  /** Gets the message as a string
   * @return String representing the message body
   */
  public String getMessage()
  {
    return message;
  }

  /** Sets the message
   * @param message The message body
   */
  public void setMessage(String message)
  {
    this.message = message;
    processMessage();
  }

  /** Gets the message formatted as HTML
   * @return String
   */
  public String getHTMLMessage()
  {
    return htmlMessage;
  }

  /** Gets the plain message of the message
   * @return String
   */
  public String getPlainMessage()
  {
    return plainMessage;
  }

  /** Processes the message
   */
  public void processMessage()
  {
    System.out.println("Process message");
    try
    {
      Session session = Session.getDefaultInstance(new Properties());
      InputStream inputStream = new ByteArrayInputStream(message.getBytes());
      MimeMessage mimeMessage = new MimeMessage(session, inputStream);
      if (mimeMessage.isMimeType(Constants.PLAIN_MIME))
      {
        plainMessage = mimeMessage.getContent().toString();
      }
      else if (mimeMessage.isMimeType(Constants.MULTIPART_MIME))
      {
        MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
        processMimeMultipart(mimeMultipart);
      }
      subject = mimeMessage.getSubject();
      System.out.println("Message processed");
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
  }

  /** Converts a Byte[] array to a byte[] array
   * @param array
   * @return byte[]
   */
  private byte[] convertByteObjectArrayToPrimativeByteArray(Byte[] array)
  {
	  byte[] bytes = new byte[array.length];
	  for(int i = 0; i < array.length; ++i)
	  {
		  bytes[i] = array[i];
	  }
	  return bytes;
  }
  
  /** Processes the multipart part of the message
   * @param mimeMultipart
   * @throws Exception
   */
  private void processMimeMultipart(
          MimeMultipart mimeMultipart) throws Exception
  {
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++)
    {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      String fileName = this.getFileName(bodyPart.getContentType());
      if(fileName == null)
	  {
    	  bodyPart.getFileName();
	  }
      if (fileName == null && bodyPart.isMimeType("text/plain"))
      {
        if (plainMessage == null)
        {
          String html = (String) bodyPart.getContent();
          plainMessage = org.jsoup.Jsoup.parse(html).html();
        }
        else
        {
          addPlainTextAttachment(bodyPart);
        }
      }
      else if (fileName == null && bodyPart.isMimeType("text/html"))
      {
        if (htmlMessage == null)
        {
          String html = (String) bodyPart.getContent();
          htmlMessage = org.jsoup.Jsoup.parse(html).html();
        }
        else
        {
          addPlainTextAttachment(bodyPart);
        }

      }
      else if (bodyPart.isMimeType("multipart/alternative"))
      {
        processMimeMultipart((MimeMultipart) bodyPart.getContent());
      }
      else if (bodyPart.isMimeType("text/*"))
      {
        String content = (String) bodyPart.getContent();
        int size = bodyPart.getSize();
        Attachment attach = new Attachment(fileName, content.getBytes(), size);
        attachments.add(attach);
      }
      else
      {
        BASE64DecoderStream ds = (BASE64DecoderStream) bodyPart.getContent();
        ArrayList<Byte> contentList = new ArrayList<Byte>();
        int intVal;
        while((intVal = ds.read()) >= 0)
        {
        	byte byteVal = (byte)intVal;
        	contentList.add(byteVal);
        }
        int size = contentList.size();
        Byte[] ByteContent = contentList.toArray(new Byte[contentList.size()]);
        byte[] content = this.convertByteObjectArrayToPrimativeByteArray(ByteContent);
        Attachment attach = new Attachment(fileName, content, size);
        attachments.add(attach);
      }
    }

  }

  /** Returns list of attachments
   * @return LinkedList Linked list of attachments
   */
  public LinkedList<Attachment> getAttachments()
  {
    return attachments;
  }

  /** Gets how many attachments the message has
   * @return int
   */
  public int getAttachmentCount()
  {
    return attachments.size();
  }

  /** Gets the file name
   * @param details
   * @return String of filename
   */
  private String getFileName(String details)
  {
    Pattern p = Pattern.compile("name=\"(.+?)\"");
    Matcher m = p.matcher(details);
    if (m.find())
    {
      if (m.groupCount() > 0)
      {
        return m.group(1);
      }
    }
    return null;
  }

  /** Adds a plain text attachment
   * @param BodyPart
   */
  private void addPlainTextAttachment(BodyPart b)
  {
    try
    {
      System.out.println("Adding plain text attachment");
      String fileName = getFileName(b.getContentType());
      String content = (String) b.getContent();
      int size = b.getSize();
      Attachment attach = new Attachment(fileName, content.getBytes(), size);
      attachments.add(attach);
    }
    catch (MessagingException | IOException e)
    {
      System.err.println("Could not get file name or read file content");
      e.printStackTrace();
    }
  }

}
