package com.pessetto.origamismtp.commandhandlers;

import com.pessetto.origamismtp.Settings;
import com.pessetto.origamismtp.accounts.Accounts;
import com.pessetto.origamismtp.commandhandlers.interfaces.IAUTHHandler;
import java.io.DataOutputStream;
import java.util.Scanner;
import com.pessetto.origamismtp.constants.Constants;
import com.pessetto.origamismtp.status.AuthStatus;
import java.util.Base64;


/** Represents a handler for the AUTH command
 * @author Travis Pessetto
 * @author pessetto.com
 */
public class AUTHHandler implements IAUTHHandler
{
	private boolean used;
	private String cmd;
	private int loginCount;
        private AuthType authType;
        private String userName;
        private int step;

	private AuthStatus authStatus;
	
	/** Creates an auth handler
	 * @param fullAuth the whole line of the auth command
	 */
	public AUTHHandler()
	{
		used = false;
		authStatus = AuthStatus.START;
		loginCount = 0;
                authType = AuthType.INIT;
                step = 0;
	}
	
	
	/** Gets the response to the client
	 * @return A string to be passed to the client
	 */
	public String getResponse(String cmd)
	{
		String[] cmdSections = cmd.replace(Constants.CRLF, "").split("\\s");
                var settings = Settings.getInstance();
		System.out.println("Command sections: "+cmdSections.length);
		System.out.println("Auth Status: " + authStatus);
		System.out.println("CMD: " + cmd);
                if((authStatus == AuthStatus.CONTINUE) && cmd.trim().equals("*"))
                {
                    authStatus = AuthStatus.START;
                    return "501";
                }
                else if(authStatus == AuthStatus.CONTINUE && authType == AuthType.LOGIN)
                {
                    if(step == 1)
                    {
                        // get username
                        System.out.println("USERNAME given is: "+cmd.trim());
                        userName = new String(Base64.getDecoder().decode(cmd.trim()));
                        // prompt for password
                        ++step;
                        return "334 UGFzc3dvcmQ6"+Constants.CRLF;
                    }
                    else
                    {
                        // check user name and password
                        var password = new String(Base64.getDecoder().decode(cmd));
                        var accounts = Accounts.getInstance();
                        var account = accounts.getAccount(userName);
                        try
                        {
                            if(!settings.getRequireSignIn() || (account != null && account.isPasswordValid(password)))
                            {
                                ++loginCount;
                                authStatus = AuthStatus.FINISHED;
                                return "235 2.7.0 Authentication successful"+Constants.CRLF;
                            }
                            else
                            {
                                authStatus = AuthStatus.START;
                                return "535 5.7.8  Authentication credentials invalid"+Constants.CRLF;
                            }
                        }
                        catch(Exception ex)
                        {
                             authStatus = AuthStatus.START;
                             return "535 5.7.8  Authentication credentials invalid"+Constants.CRLF;
                        }
                        finally
                        {
                            step = 0;
                        }
                    }
                }
                else if(authStatus != AuthStatus.CONTINUE && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("plain"))
		{
			authStatus = AuthStatus.CONTINUE;
                        authType = AuthType.PLAIN;
			return "334 Continue"+Constants.CRLF;
		}
		else if(authStatus == AuthStatus.START && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("login"))
		{
			authStatus = AuthStatus.CONTINUE;
                        authType = AuthType.LOGIN;
                        ++step;
                        // prompt username (base64 encoded)
			return "334 VXNlcm5hbWU6" + Constants.CRLF;   
		}
		else if(authStatus == AuthStatus.CONTINUE && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("login") && loginCount == 0)
		{
			++loginCount;
			authStatus = AuthStatus.CONTINUE;
			return "334 Continue" + Constants.CRLF;
		}
		else if(authStatus == AuthStatus.CONTINUE && cmdSections.length == 2 && cmdSections[1].toLowerCase().equals("login") && loginCount == 1)
		{
			++loginCount;
			authStatus = AuthStatus.FINISHED;
			return "235 AUTH SUCCESS" + Constants.CRLF;
		}
		if(used)
		{
			authStatus = AuthStatus.FINISHED;
			return "503 Already authenticated"+Constants.CRLF;
		}
		else
		{
			authStatus = AuthStatus.FINISHED;
			used = true;
			return "235 AUTH SUCCESS" + Constants.CRLF;
		}
	}
	
	/** Gets the current state of authentication
	 * @return An Enum representing the current state of authentication
	 */
	public AuthStatus getStatus()
	{
		return authStatus;
	}
}
