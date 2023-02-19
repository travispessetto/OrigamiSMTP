package com.pessetto.origamismtp.accounts;

import com.pessetto.origamismtp.constants.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Travis Pessetto
 */
public class Account implements Serializable{
    private transient final int Iterations = 65536;
    private transient final int KeyLength = 512;
    private byte[] PasswordHash;
    private byte[] Salt;
    private static final long serialVersionUID = -1686234852843453027L;
    
    
    public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecureRandom random = new SecureRandom();
        Salt = new byte[16];
        random.nextBytes(Salt);
        
        KeySpec spec = new PBEKeySpec(password.toCharArray(),Salt,Iterations,KeyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PasswordHash = factory.generateSecret(spec).getEncoded();        
    }
    
    public boolean isPasswordValid(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        KeySpec spec = new PBEKeySpec(password.toCharArray(),Salt,Iterations,KeyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        var confirmPasswordHash = factory.generateSecret(spec).getEncoded();    
        return Arrays.equals(PasswordHash,confirmPasswordHash);
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
                    File settingsFile = new File(settingsFolder+"test.ser");
                    settingsFile.setWritable(true, false);	
                    FileOutputStream fout = new FileOutputStream(settingsFile);
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
