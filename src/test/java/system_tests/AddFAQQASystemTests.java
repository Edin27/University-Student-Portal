package system_tests;

import controller.MenuController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;

import java.io.IOException;
import java.net.URISyntaxException;

public class AddFAQQASystemTests extends TUITest {

    @Test
    public void testAddCourseNoTag() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        setMockInput("0", "admin1", "admin1pass", "2", "-2", "", "Topic", "q", "a", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Created new FAQ item");
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
