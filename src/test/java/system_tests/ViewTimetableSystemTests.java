package system_tests;

import controller.GuestController;
import controller.MenuController;
import controller.StudentController;
import controller.ViewerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Activity;
import model.Course;
import model.Lecture;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ViewTimetableSystemTests extends TUITest{

    @Test
    @DisplayName("View timetable standard")
    public void testViewTimetableStandard() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass", "3", "0", "001", "course1", "dec1", "n", "name", "email", "name2", "email",                             // Course secretary email
                "0", "0", "0", "0", "2025-01-01", "12:00", "2025-12-15", "13:00", "location", "mon", "Y", "-1", "-1",                                // Return to main menu
                "0", "0", "student1", "student1pass", "5", "1", "001", "2", "-1", "-1"                                 // Exit
        );

        TextUserInterface view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
        guestController.login();
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();

        assertOutputContains("student1@hindeburg.ac.uk Timetable");
        assertOutputContains("MONDAY   [2025-04-14]");
        assertOutputContains("Time: 12:00 -> 13:00");
        assertOutputContains("Course code: 001");
        assertOutputContains("Activity: Lecture");
    }

    @Test
    @DisplayName("View timetable clash warning")
    public void testViewTimetableClashWarning() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass", "3", "0", "001", "course1", "dec1", "n", "name", "email", "name2", "email",                             // Course secretary email
                "6", "6", "0", "0", "2025-01-01", "12:00", "2025-12-15", "13:00", "location", "mon", "Y",
                "0", "0", "2025-01-01", "12:10", "2025-12-15", "13:00", "location", "mon", "Y", "-1", "-1",                                // Return to main menu
                "0", "0", "student1", "student1pass", "5", "1", "001", "2", "-1", "-1"                                 // Exit
        );

        TextUserInterface view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
        guestController.login();
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();

        assertOutputContains("student1@hindeburg.ac.uk Timetable");
        assertOutputContains("MONDAY   [2025-04-14]");
        assertOutputContains("Time: 12:00 -> 13:00");
        assertOutputContains("Course code: 001");
        assertOutputContains("Activity: Lecture");
        assertOutputContains("Clashes with another activity!");
        assertOutputContains("Time: 12:10 -> 13:00");
    }

    @Test
    @DisplayName("View timetable tutorial and lab warning")
    public void testViewTutorialLabWarning() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass", "3", "0", "001", "course1", "dec1", "n", "name", "email", "name2", "email",                             // Course secretary email
                "6", "6", "0", "0", "2025-01-01", "12:00", "2025-12-15", "13:00", "location", "mon", "Y", "-1", "-1",                                // Return to main menu
                "0", "0", "student1", "student1pass", "5", "1", "001", "2", "-1", "-1"                                 // Exit
        );

        TextUserInterface view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
        guestController.login();
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();

        assertOutputContains("student1@hindeburg.ac.uk Timetable");
        assertOutputContains("Tutorials and labs not chosen for course: 001!");
    }

    @Test
    @DisplayName("View timetable tutorial warning")
    public void testViewTutorialWarning() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass", "3", "0", "001", "course1", "dec1", "n", "name", "email", "name2", "email",                             // Course secretary email
                "6", "0", "0", "0", "2025-01-01", "12:00", "2025-12-15", "13:00", "location", "mon", "Y", "-1", "-1",                                // Return to main menu
                "0", "0", "student1", "student1pass", "5", "1", "001", "2", "-1", "-1"                                 // Exit
        );

        TextUserInterface view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
        guestController.login();
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();

        assertOutputContains("student1@hindeburg.ac.uk Timetable");
        assertOutputContains("Tutorials not chosen for course: 001!");
    }

    @Test
    @DisplayName("View timetable lab warning")
    public void testViewLabWarning() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass", "3", "0", "001", "course1", "dec1", "n", "name", "email", "name2", "email",                             // Course secretary email
                "0", "6", "0", "0", "2025-01-01", "12:00", "2025-12-15", "13:00", "location", "mon", "Y", "-1", "-1",                                // Return to main menu
                "0", "0", "student1", "student1pass", "5", "1", "001", "2", "-1", "-1"                                 // Exit
        );

        TextUserInterface view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
        guestController.login();
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();

        assertOutputContains("student1@hindeburg.ac.uk Timetable");
        assertOutputContains("Labs not chosen for course: 001!");
    }
}
