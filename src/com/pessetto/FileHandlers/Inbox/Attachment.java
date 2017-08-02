package com.pessetto.FileHandlers.Inbox;

import java.io.Serializable;
import java.util.Arrays;

public class Attachment implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] content;
	
	public Attachment(String fileName, byte[] content)
	{
		this.fileName = fileName;
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment other = (Attachment) obj;
		if (!Arrays.equals(content, other.content))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
