/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pessetto.origamismtp.testing;

import com.pessetto.origamismtp.accounts.Account;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 *
 * @author Travis Pessetto
 */
public class AccountTest {
    
    public AccountTest(){}
    
    @Test
    public void testAccountPassword() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        var account = new Account();
        account.setPassword("foobarbaz");
        assertTrue(account.isPasswordValid("foobarbaz"));
    }
    
    @Test
    public void testBadAccountPassword() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        var account = new Account();
        account.setPassword("foobar");
        assertFalse(account.isPasswordValid("foobarbaZ"));
    }
}
