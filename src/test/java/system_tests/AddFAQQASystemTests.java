package system_tests;

import controller.MenuController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;

import java.io.IOException;
import java.net.URISyntaxException;

public class AddFAQQASystemTests extends TUITest {

    @Test
    @DisplayName("Standard add faq with no course tag")
    public void testAddFAQNoTag() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "", "Topic", "q", "a", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Created new FAQ item");
    }

    @Test
    @DisplayName("try to add faq with no topic")
    public void testAddFAQNoTopic() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "", "", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Topic cannot be empty!");
    }

    @Test
    @DisplayName("Try to add faq with no question")
    public void testAddFAQNoQuestion() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "", "Topic", "", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Question cannot be empty!");
    }

    @Test
    @DisplayName("Try to add faq with no answer")
    public void testAddFAQNoAnswer() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "", "Topic", "q", "", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Answer cannot be empty!");
    }

    @Test
    public void testAddCourseTagExists() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getCourseManager().getCourses().add(new Course(new TextUserInterface(), "code", "name",
                "desc", false, "co", "coe", "cs", "cse",
                0, 0, null, null, null));
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "code", "Topic", "q", "a", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Created new FAQ item");
    }

    @Test
    public void testAddCourseTagFail() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "no course", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Course code does not exist");
    }
}
