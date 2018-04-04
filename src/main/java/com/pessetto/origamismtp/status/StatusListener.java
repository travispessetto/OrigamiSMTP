package com.pessetto.origamismtp.status;

/** Interface to listen if SMTP is started or stopped
 * @author Travis Pessetto
 * @author pessetto.com
 */
public interface StatusListener
{
	public void smtpStarted();
	public void smtpStopped();
}
