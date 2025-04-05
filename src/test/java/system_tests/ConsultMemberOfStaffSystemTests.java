package system_tests;

import controller.InquirerController;
import external.AuthenticationService;
import external.EmailService;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;
import java.io.IOException;
import java.net.URISyntaxException;

public class ConsultMemberOfStaffSystemTests extends TUITest {

    @Test
    public void testInquiryNoTagSuccess() throws URISyntaxException, IOException, ParseException {
        setMockInput("student1@hindeburg.ac.uk", "inquiry topic", "This is an inquiry", "");
        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        AuthenticationService auth = new MockAuthenticationService();
        EmailService email = new MockEmailService();
        InquirerController inquirerController = new InquirerController(sharedContext, view, auth, email);
        startOutputCapture();
        inquirerController.contactStaff();
        assertOutputContains("Your inquiry has been recorded. Someone will be in touch via email soon!");
    }

    @Test
    public void testInquiryNoTagInvalidEmail() throws URISyntaxException, IOException, ParseException {
        setMockInput("wrongemail.com");
        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        AuthenticationService auth = new MockAuthenticationService();
        EmailService email = new MockEmailService();
        InquirerController inquirerController = new InquirerController(sharedContext, view, auth, email);
        startOutputCapture();
        inquirerController.contactStaff();
        assertOutputContains("Invalid email address! Please try again");
    }

    @Test
    public void testInquiryNoTagBlankSubject() throws URISyntaxException, IOException, ParseException {
        setMockInput("student1@hindeburg.ac.uk", "", "");
        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        AuthenticationService auth = new MockAuthenticationService();
        EmailService email = new MockEmailService();
        InquirerController inquirerController = new InquirerController(sharedContext, view, auth, email);
        startOutputCapture();
        inquirerController.contactStaff();
        assertOutputContains("Inquiry subject cannot be blank! Please try again");
    }

    @Test
    public void TestInquiryTagSuccess() throws URISyntaxException, IOException, ParseException {
        setMockInput("student1@hindeburg.ac.uk", "inquiry topic", "This is an inquiry", "1");
        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        AuthenticationService auth = new MockAuthenticationService();
        EmailService email = new MockEmailService();
        InquirerController inquirerController = new InquirerController(sharedContext, view, auth, email);
        sharedContext.getCourseManager().getCourses().add(new Course(view, "1", "course1",
                "description", false, "org", "teacher1@hindeburg.ac.uk",
                "sec", "teacher2@hindeburg.ac.uk", 0, 0, null, null, null));
        startOutputCapture();
        inquirerController.contactStaff();
        assertOutputContains("teacher1@hindeburg.ac.uk");
        assertOutputContains("Your inquiry has been recorded. Someone will be in touch via email soon!");
    }

    @Test
    public void TestInquiryTagFailure() throws URISyntaxException, IOException, ParseException {
        setMockInput("student1@hindeburg.ac.uk", "inquiry topic", "This is an inquiry", "2");
        View view = new TextUserInterface();
        SharedContext sharedContext = new SharedContext(view);
        AuthenticationService auth = new MockAuthenticationService();
        EmailService email = new MockEmailService();
        InquirerController inquirerController = new InquirerController(sharedContext, view, auth, email);
        sharedContext.getCourseManager().getCourses().add(new Course(view, "1", "course1",
                "description", false, "org", "teacher1@hindeburg.ac.uk",
                "sec", "teacher2@hindeburg.ac.uk", 0, 0, null, null, null));
        startOutputCapture();
        inquirerController.contactStaff();
        assertOutputContains("This course code does not exist!");
    }
}
