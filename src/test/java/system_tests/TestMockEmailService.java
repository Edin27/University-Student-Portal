package system_tests;

import external.EmailService;
import external.MockEmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMockEmailService extends TUITest {
    @Test
    public void testSendEmailFrequent() {
        EmailService email = new MockEmailService();
        startOutputCapture();
        String sender = "student1@hindeburg.ac.uk";
        String recipient = "teacher1@hindeburg.ac.uk";
        String subject = "email subject";
        String content = "email content";
        int status = email.sendEmail(sender, recipient, subject, content);
        assertOutputContains("Email from " + sender + " to " + recipient);
        Assertions.assertEquals(EmailService.STATUS_SUCCESS, status);
    }

    @Test
    public void testSendEmailBoundary() {
        EmailService email = new MockEmailService();
        startOutputCapture();
        String sender = "stu_&dent+1@hindeburg.ac.uk";
        String recipient = "t.e.a.c.h.e.r.2@hindeburg.ac.uk";
        String subject = "email subject";
        String content = "email content";
        int status = email.sendEmail(sender, recipient, subject, content);
        assertOutputContains("Email from " + sender + " to " + recipient);
        Assertions.assertEquals(EmailService.STATUS_SUCCESS, status);
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
        Assertions.assertEquals(EmailService.STATUS_INVALID_SENDER_EMAIL, status);
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
        Assertions.assertEquals(EmailService.STATUS_INVALID_RECIPIENT_EMAIL, status);
    }
}
