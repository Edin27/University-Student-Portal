package system_tests;

import external.EmailService;
import external.MockEmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MockEmailServiceTests extends TUITest {
    @Test
    public void testSendEmailFrequent() {
        EmailService email = new MockEmailService();
        startOutputCapture();
        String sender = "student1@hindeburg.ac.uk";
        String recipient = "teacher1@hindeburg.ac.uk";
        String subject = "email subject";
        String content = "email content";
        int status = email.sendEmail(sender, recipient, subject, content);
        assertOutputContains("Email from " + sender + " to " + recipient+"\n"+subject+"\n"+content);
        Assertions.assertEquals(0, status);
    }

    @Test
    public void testSendEmailIncorrectSender() {
        EmailService email = new MockEmailService();
        startOutputCapture();
        String sender = "student1athindeburg..uk";
        String recipient = "teacher1@hindeburg.ac.uk";
        String subject = "email subject";
        String content = "email content";
        int status = email.sendEmail(sender, recipient, subject, content);
        Assertions.assertEquals(1, status);
    }

    @Test
    public void testSendEmailIncorrectRecipient() {
        EmailService email = new MockEmailService();
        startOutputCapture();
        String sender = "student1@hindeburg.ac.uk";
        String recipient = "teache78&^";
        String subject = "email subject";
        String content = "email content";
        int status = email.sendEmail(sender, recipient, subject, content);
        Assertions.assertEquals(1, status);
    }
}
