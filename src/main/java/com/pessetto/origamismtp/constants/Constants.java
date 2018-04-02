package com.pessetto.origamismtp.constants;

public class Constants {
	public final static char CR = (char)0x0D;
	public final static char LF = (char)0x0A;
	public final static String CRLF = "" + CR + LF;
	//public final static String MessageFolder = "messages";
	public final static boolean ENABLE_START_TLS = true;
	public final static String LOG_FOLDER = System.getProperty("user.dir")+"/logs/";
	public static String INBOX_FILE = System.getProperty("user.dir")+"/Origami SMTP/inbox.ser";
	public static String PLAIN_MIME = "text/plain";
	public static String MULTIPART_MIME = "multipart/*";
}
