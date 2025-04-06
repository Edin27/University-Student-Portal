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

    private static Course course1;

    @Test
    @DisplayName("View timetable standard")
    public void testViewTimetableStandard() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "admin1", "admin1pass",              // Login credentials
                "3",                                 // MANAGE_COURSES
                "0",                                 // Add course
                "001",                               // Course code
                "course1",                           // Name
                "dec1",                              // Description
                "n",                                 // require computers
                "name",                              // Course organiser name
                "email",                             // Course organiser email
                "name2",                             // Course secretary name
                "email",                             // Course secretary email
                "0",                                 // Required tutorials
                "0",                                 // Required labs
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:00",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "-1",                                //Return to manage courses
                "-1",                                // Return to main menu
                "0",                                 // LOGOUT
                "0",                                 // LOGIN
                "student1", "student1pass",          // Login credentials
                "5",                                 // MANAGE_TIMETABLE
                "1",                                 // Add Course to Timetable
                "001",                               // Enter the course code
                "2",                                 //view timetable
                "-1",                                // Return to main menu
                "-1"                                 // Exit
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
                "admin1", "admin1pass",              // Login credentials
                "3",                                 // MANAGE_COURSES
                "0",                                 // Add course
                "001",                               // Course code
                "course1",                           // Name
                "dec1",                              // Description
                "n",                                 // require computers
                "name",                              // Course organiser name
                "email",                             // Course organiser email
                "name2",                             // Course secretary name
                "email",                             // Course secretary email
                "0",                                 // Required tutorials
                "0",                                 // Required labs
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:00",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:10",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "-1",                                //Return to manage courses
                "-1",                                // Return to main menu
                "0",                                 // LOGOUT
                "0",                                 // LOGIN
                "student1", "student1pass",          // Login credentials
                "5",                                 // MANAGE_TIMETABLE
                "1",                                 // Add Course to Timetable
                "001",                               // Enter the course code
                "2",                                 //view timetable
                "-1",                                // Return to main menu
                "-1"                                 // Exit
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
                "admin1", "admin1pass",              // Login credentials
                "3",                                 // MANAGE_COURSES
                "0",                                 // Add course
                "001",                               // Course code
                "course1",                           // Name
                "dec1",                              // Description
                "n",                                 // require computers
                "name",                              // Course organiser name
                "email",                             // Course organiser email
                "name2",                             // Course secretary name
                "email",                             // Course secretary email
                "6",                                 // Required tutorials
                "6",                                 // Required labs
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:00",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "-1",                                // Return to manage courses
                "-1",                                // Return to main menu
                "0",                                 // LOGOUT
                "0",                                 // LOGIN
                "student1", "student1pass",          // Login credentials
                "5",                                 // MANAGE_TIMETABLE
                "1",                                 // Add Course to Timetable
                "001",                               // Enter the course code
                "2",                                 //view timetable
                "-1",                                // Return to main menu
                "-1"                                 // Exit
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
                "admin1", "admin1pass",              // Login credentials
                "3",                                 // MANAGE_COURSES
                "0",                                 // Add course
                "001",                               // Course code
                "course1",                           // Name
                "dec1",                              // Description
                "n",                                 // require computers
                "name",                              // Course organiser name
                "email",                             // Course organiser email
                "name2",                             // Course secretary name
                "email",                             // Course secretary email
                "6",                                 // Required tutorials
                "0",                                 // Required labs
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:00",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "-1",                                // Return to manage courses
                "-1",                                // Return to main menu
                "0",                                 // LOGOUT
                "0",                                 // LOGIN
                "student1", "student1pass",          // Login credentials
                "5",                                 // MANAGE_TIMETABLE
                "1",                                 // Add Course to Timetable
                "001",                               // Enter the course code
                "2",                                 //view timetable
                "-1",                                // Return to main menu
                "-1"                                 // Exit
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
                "admin1", "admin1pass",              // Login credentials
                "3",                                 // MANAGE_COURSES
                "0",                                 // Add course
                "001",                               // Course code
                "course1",                           // Name
                "dec1",                              // Description
                "n",                                 // require computers
                "name",                              // Course organiser name
                "email",                             // Course organiser email
                "name2",                             // Course secretary name
                "email",                             // Course secretary email
                "0",                                 // Required tutorials
                "6",                                 // Required labs
                "0",                                 // ADD Activity
                "0",                                 // ADD Lecture
                "2025-01-01",                        // Start date
                "12:00",                             // Start time
                "2025-12-15",                        // End date
                "13:00",                             // End time
                "location",                          // Location
                "mon",                               // Day of week
                "Y",                                 // Is recorded
                "-1",                                // Return to manage courses
                "-1",                                // Return to main menu
                "0",                                 // LOGOUT
                "0",                                 // LOGIN
                "student1", "student1pass",          // Login credentials
                "5",                                 // MANAGE_TIMETABLE
                "1",                                 // Add Course to Timetable
                "001",                               // Enter the course code
                "2",                                 //view timetable
                "-1",                                // Return to main menu
                "-1"                                 // Exit
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
