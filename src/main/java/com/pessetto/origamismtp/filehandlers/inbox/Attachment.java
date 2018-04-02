package com.pessetto.origamismtp.filehandlers.inbox;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;

public class Attachment implements Serializable
{
	private static final long serialVersionUID = 2L;
	private String FileName;
	private String MimeType;
	private int Size;
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public byte[] getContent() {
		return Content;
	}
	public void setContent(byte[] content) {
		Content = content;
	}
	private byte[] Content;
	public Attachment(String fileName,byte[] content, int size)
	{
		try
		{
			FileName = fileName;
			Content = content;
			Size = size;
			InputStream is = new BufferedInputStream(new ByteArrayInputStream(content));
			MimeType = URLConnection.guessContentTypeFromStream(is);
			if(MimeType == null)
			{
				MimeType = "application/octet-stream";
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}

	}
	public int getSize() {
		return Size;
	}
	public void setSize(int size) {
		Size = size;
	}
	public String getMimeType() {
		return MimeType;
	}
	public void setMimeType(String mimeType) {
		MimeType = mimeType;
	}

}
