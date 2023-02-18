/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pessetto.origamismtp.accounts;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author plow_
 */
public class Account {
    private int Iterations = 65536;
    private int KeyLength = 512;
    private String UserName;
    private byte[] PasswordHash;
    private byte[] Salt;
    
    
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
}
