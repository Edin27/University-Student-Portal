package unit_tests;

import external.EmailService;
import external.MockEmailService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import system_tests.TUITest;

public class TestMockEmailService extends TUITest {

	@Test
	@DisplayName("Standard email returns success")
	public void testSendEmailFrequent() {
		EmailService email = new MockEmailService();
		startOutputCapture();
		String sender = "student1@hindeburg.ac.uk";
		String recipient = "teacher1@hindeburg.ac.uk";
		String subject = "email subject";
		String content = "email content";
		int status = email.sendEmail(sender, recipient, subject, content);
		assertOutputContains("Email from " + sender + " to " + recipient);
		assertEquals(EmailService.STATUS_SUCCESS, status);
	}

	@Test
	@DisplayName("Unusual email addresses returns success")
	public void testSendEmailBoundary() {
		EmailService email = new MockEmailService();
		startOutputCapture();
		String sender = "stu_&dent+1@hindeburg.ac.uk";
		String recipient = "t.e.a.c.h.e.r.2@hindeburg.ac.uk";
		String subject = "email subject";
		String content = "email content";
		int status = email.sendEmail(sender, recipient, subject, content);
		assertOutputContains("Email from " + sender + " to " + recipient);
		assertEquals(EmailService.STATUS_SUCCESS, status);
	}

	@Test
	@DisplayName("Invalid sender email returns error")
	public void testSendEmailIncorrectSender() {
		EmailService email = new MockEmailService();
		startOutputCapture();
		String sender = "student1athindeburg..uk";
		String recipient = "teacher1@hindeburg.ac.uk";
		String subject = "email subject";
		String content = "email content";
		int status = email.sendEmail(sender, recipient, subject, content);
		assertEquals(EmailService.STATUS_INVALID_SENDER_EMAIL, status);
	}

	@Test
	@DisplayName("Invalid recipient email returns error")
	public void testSendEmailIncorrectRecipient() {
		EmailService email = new MockEmailService();
		startOutputCapture();
		String sender = "student1@hindeburg.ac.uk";
		String recipient = "teache78&^";
		String subject = "email subject";
		String content = "email content";
		int status = email.sendEmail(sender, recipient, subject, content);
		assertEquals(EmailService.STATUS_INVALID_RECIPIENT_EMAIL, status);
	}
}
