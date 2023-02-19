package com.pessetto.origamismtp.accounts;

import com.pessetto.origamismtp.Settings;
import com.pessetto.origamismtp.accounts.Account;
import com.pessetto.origamismtp.constants.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;


/**
 *
 * @author Travis Pessetto
 */
public class Accounts implements Serializable{
    private static final long serialVersionUID = -1686234852843453027L;
    private transient static Accounts Instance;
    private HashMap<String,Account> Accounts;
    
    private Accounts()
    {
        Accounts = new HashMap<String,Account>();
    }
    
    public static Accounts getInstance()
    {
        if(Instance == null)
        {
             File file = new File(Constants.ACCOUNT_FILE);
            if(file.exists())
            {
                try
                {
                    FileInputStream fin = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fin);
                    Instance = (Accounts)oin.readObject();
                    oin.close();
                    fin.close();
                }
                catch(Exception ex)
                {
                    System.err.println("Failed to deserialize accounts");
                }
            }
            else
            {
                Instance = new Accounts();
            }
        }
        return Instance;
    }
    
    public boolean addAccount(String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        if(Accounts.containsKey(userName.toUpperCase()))
        {
            return false;
        }
        Account account = new Account();
        account.setPassword(password);
        Accounts.put(userName.toUpperCase(), account);
        serialize();
        return true;
    }
    
    public boolean removeAccount(String userName)
    {
        if(Accounts.containsKey(userName.toUpperCase()))
        {
            Accounts.remove(userName.toUpperCase());
            serialize();
            return true;
        }
        return false;
    }
    
    public Account getAccount(String userName)
    {
        if(Accounts.containsKey(userName.toUpperCase()))
        {
            return Accounts.get(userName.toUpperCase());
        }
        return null;
    }

    public void removeAllAccounts()
    {
        Accounts = new HashMap<String,Account>();
        serialize();
    }
    
    public int getAccountCount()
    {
        return Accounts.size();
    }
    
        /** Serializes the class to store on disk
	 */
	public void serialize()
	{
		try
		{
                    File settingsFolder = new File(Constants.SETTINGS_FOLDER);
                    if(!settingsFolder.exists())
                    {
                            settingsFolder.mkdirs();
                    }
                    File accountsFile = new File(Constants.ACCOUNT_FILE);
                    accountsFile.setWritable(true, false);	
                    FileOutputStream fout = new FileOutputStream(accountsFile);
                    ObjectOutputStream oout = new ObjectOutputStream(fout);
                    oout.writeObject(this);
                    oout.close();
                    fout.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
