package com.pessetto.FileHandlers.Inbox;

import java.io.Serializable;

public class Attachment implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String FileName;
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
	public Attachment(String fileName,byte[] content)
	{
		FileName = fileName;
		Content = content;
	}

}
