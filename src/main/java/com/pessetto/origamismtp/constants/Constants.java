package com.pessetto.origamismtp.constants;

/** Class containing all constants for Origami SMTP */
public class Constants {
	/** Constant for carriage return */
	public final static char CR = (char)0x0D;
	/** Constant for line feed */
	public final static char LF = (char)0x0A;
	/** Constant for Carriage return line feed */
	public final static String CRLF = "" + CR + LF;
	/** Constant to enable SSL */
	public final static boolean ENABLE_START_TLS = true;
	/** Constant for the log folder */
	public final static String LOG_FOLDER = System.getProperty("user.dir")+"/logs/";
	/** Constant for the inbox folder*/
	public final static String INBOX_FOLDER = System.getProperty("user.dir")+"/Origami SMTP/";
	/** Constant for the inbox file*/
	public final static String INBOX_FILE = INBOX_FOLDER + "inbox.ser";
        /** Constant for the settings folder**/
        public final static String SETTINGS_FOLDER = System.getProperty("user.dir") + "/Origami SMTP/";
        /** Constant for the settings file **/
        public final static String SETTINGS_FILE = SETTINGS_FOLDER + "settings.ser";
        /** Constant for the accounts file **/
        public final static String ACCOUNT_FILE = SETTINGS_FOLDER + "accounts.ser";
	/** Constant representing plain mime type */
	public final static String PLAIN_MIME = "text/plain";
	/** Constant representing the multipart mime type */
	public final static String MULTIPART_MIME = "multipart/*";
}
