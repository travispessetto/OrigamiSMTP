
package com.pessetto.origamismtp.testing;
import com.pessetto.origamismtp.filehandlers.inbox.Inbox;
import com.pessetto.origamismtp.filehandlers.inbox.Message;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InboxTest {
    
    public InboxTest()
    {
        
    }
    
    @Test
    public void testInboxSetSizeReduction()
    {
        
        String msgTwoSub = "Goodbye John";
        Message one = new Message();
        one.setFrom("james.doe@example.com");
        one.setTo("jane.doe@example.com");
        one.setSubject("Hello World!");
        one.setMessage("Dear John,\nIt has been awhile since we last spoke.");
        
        Message two = new Message();
        two.setFrom("james.doe@example.com");
        two.setTo("jane.doe@example.com");
        two.setSubject(msgTwoSub);
        two.setMessage("Dear John,\nI am breaking up with you.");
        assertEquals(msgTwoSub,two.getSubject());
        
        Inbox inbox = Inbox.getInstance();
        // make sure inbox is large
        inbox.setSize(5);
        inbox.clearInbox();
        inbox.addMessage(one);
        inbox.addMessage(one);
        inbox.addMessage(two);
        inbox.setSize(3);
        
        // make sure inbox currently has 3 messages
        assertEquals(3,inbox.getMessageCount());
        // resize the inbox to one
        inbox.setSize(1);
        assertEquals(1,inbox.getSize());
        // check to make sure it's the newest message
        assertEquals(msgTwoSub, inbox.getNewestMessage().getSubject());        
    }
    
    @Test
    public void testInboxDoesNotExceedLimit()
    {
        Inbox inbox = Inbox.getInstance();
        inbox.clearInbox();
        inbox.setSize(1);
        
        String msgTwoSub = "Goodbye John";
        Message one = new Message();
        one.setFrom("james.doe@example.com");
        one.setTo("jane.doe@example.com");
        one.setSubject("Hello World!");
        one.setMessage("Dear John,\nIt has been awhile since we last spoke.");
        
        Message two = new Message();
        two.setFrom("james.doe@example.com");
        two.setTo("jane.doe@example.com");
        two.setSubject(msgTwoSub);
        two.setMessage("Dear John,\nI am breaking up with you.");
        
        inbox.addMessage(one);
        inbox.addMessage(two);
        assertEquals(1,inbox.getMessageCount());
        assertEquals(msgTwoSub,inbox.getMessage(0).getSubject());
        
    }
    
    @Test
    public void testInboxSizeZeroHasNoLimit()
    {
         Inbox inbox = Inbox.getInstance();
        inbox.clearInbox();
        inbox.setSize(0);
        
        String msgTwoSub = "Goodbye John";
        Message one = new Message();
        one.setFrom("james.doe@example.com");
        one.setTo("jane.doe@example.com");
        one.setSubject("Hello World!");
        one.setMessage("Dear John,\nIt has been awhile since we last spoke.");
        
        for(int i = 0; i < 999; ++i)
        {
           inbox.addMessage(one);
        }
        assertEquals(999,inbox.getMessageCount());
    }
}
