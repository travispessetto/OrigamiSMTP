package com.pessetto.origamismtp.filehandlers.inbox;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;

/** Represents an attachment
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class Attachment implements Serializable
{
	private byte[] content;
	private static final long serialVersionUID = 2L;
	private String fileName;
	private String mimeType;
	private int size;
	
	/** Creates a new attachment
	 * @param fileName The file name
	 * @param content The content (in bytes of the attachment)
	 * @param size The size of the attachment
	 */
	public Attachment(String fileName,byte[] content, int size)
	{
		try
		{
			this.fileName = fileName;
			this.content = content;
			this.size = size;
			InputStream is = new BufferedInputStream(new ByteArrayInputStream(content));
			mimeType = URLConnection.guessContentTypeFromStream(is);
			if(mimeType == null)
			{
				mimeType = "application/octet-stream";
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}

	}
	
	/** Gets the file name
	 * @return The file name of the attachment
	 */
	public String getfileName() {
		return fileName;
	}
	
	/** Sets the file name of the attachment
	 * @param fileName The name of the file
	 */
	public void setfileName(String fileName) {
		this.fileName = fileName;
	}
	
	/** Gets the content in (bytes)
	 * @return The content in bytes
	 */
	public byte[] getcontent() {
		return content;
	}
	
	/** Sets the content
	 * @param content The content in bytes
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	/** Gets the size of the attachment
	 * @return The size of the attachment
	 */
	public int getsize() {
		return size;
	}
	
	/** Sets the size of the attachment
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/** Gets the mime type of the attachment
	 * @return The string of the mime type
	 */
	public String getMimeType() {
		return mimeType;
	}
	
	/** Sets the mime type of the attachment
	 * @param mimeType The mime type of the attachment
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
