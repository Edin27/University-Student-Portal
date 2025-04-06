package system_tests;

import controller.GuestController;
import controller.MenuController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;

public class ChooseTutorialOrLabForCourseSystemTests extends TUITest {

	@Test
	public void testChooseTutorial() throws URISyntaxException,
			IOException,
			ParseException {
		// Set up mock inputs for the entire flow
		setMockInput(
				"admin1", "admin1pass",              // Login credentials
				"3",                                 // MANAGE_COURSES
				"0",                                 // Add course
				"CS101",                             // Course code
				"Software Engineering and Professional",   // Name
				"SEPP",                             // Description
				"Y",                                 // require computers
				"Professor Johnson",                 // Course organiser name
				"johnson@university.edu",            // Course organiser email
				"Bob Roberts",                       // Course secretary name
				"bob@university.edu",                // Course secretary email
				"6",                                 // Required tutorials
				"6",                                 // Required labs
				// ADD Lecture
				"0",                                 // ADD Activity
				"0",                                 // ADD Lecture
				"2025-03-01",                        // Start date
				"09:00",                             // Start time
				"2025-06-30",                        // End date
				"10:30",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"Y",                                 // Is recorded

				// ADD Tutorial
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"11:00",                             // Start time
				"2025-06-30",                        // End date
				"12:00",                             // End time
				"Room 11",                           // Location
				"wed",                               // Day of week
				"10",                                 // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code

				"3",                                 // choose Activity
				"tutorial",							 // choose tutorial
				"CS101",  							 // course code
				"1",              					 // choose available tutorial
				"2",                                 // view Timetable
				"-1",                                // Return to main menu
				"-1"                                 // Exit
		);

		// Setup
		View view = new TextUserInterface();
		SharedContext context = new SharedContext(view);

		// Login as admin staff - using the same mock input stream
		GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
		guestController.login();

		// Start the menu controller to handle main menu
		MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());

		// Start capturing output
		startOutputCapture();

		// Execute the main menu which will go through our inputs
		menuController.mainMenu();

		assertOutputContains("Tutorials and labs not chosen for course: CS10!");
	}
}
