/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pessetto.origamismtp.testing;

import com.pessetto.origamismtp.accounts.Account;
import com.pessetto.origamismtp.accounts.Accounts;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


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
    
    @Test
    public void testAddAccount() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        var accounts = Accounts.getInstance();
        // make sure account is not setup
        accounts.removeAccount("travis");
        accounts.addAccount("travis", "foobar");
        var account = accounts.getAccount("travis");
        assertNotNull(account);
        accounts.removeAccount("travis");
    }
    
    @Test
    public void testRemoveALlAcounts() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
         var accounts = Accounts.getInstance();
         accounts.addAccount("johndoe", "barb@z");
         assertTrue(accounts.getAccountCount() > 0);
         accounts.removeAllAccounts();
         assertEquals(accounts.getAccountCount(),0);
    }
}
