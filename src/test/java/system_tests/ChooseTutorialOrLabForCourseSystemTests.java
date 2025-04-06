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
	public void testOnlyChooseTutorial() throws URISyntaxException,
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
				"1",                                 // Required tutorials
				"2",                                 // Required labs
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
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
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

		assertOutputContains("You have successfully chosen all required Tutorials for CS101!");

		assertOutputContains("Time: 11:00 -> 12:00");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: CHOSEN");
		assertOutputContains("Labs not chosen for course: CS101!");
	}

	@Test
	public void testChoose2TutorialsAndLab() throws URISyntaxException,
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
				"2",                                 // Required tutorials
				"1",                                 // Required labs
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

				// ADD Tutorial1
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"11:00",                             // Start time
				"2025-06-30",                        // End date
				"12:00",                             // End time
				"Room 11",                           // Location
				"wed",                               // Day of week
				"10",                                 // Capacity

				// ADD Tutorial2
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"13:30",                             // Start time
				"2025-06-30",                        // End date
				"15:30",                             // End time
				"Room 111",                           // Location
				"thu",                               // Day of week
				"10",                                 // Capacity

				// ADD Lab
				"0",                                 // ADD Activity
				"2",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"16:00",                             // Start time
				"2025-06-30",                        // End date
				"17:30",                             // End time
				"Room 101",                           // Location
				"thu",                               // Day of week
				"10",                                 // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code

				// choose available tutorial1
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
				"1",              					 // choose available tutorial1

				// choose available tutorial2
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
				"1",              					 // choose available tutorial2

				// choose Lab
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"lab",							     // choose lab
				"1",              					 // choose available lab


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

		assertOutputContains("You still need to choose 1 more tutorials for CS101");
		assertOutputContains("You have successfully chosen all required Tutorials for CS101!");

		assertOutputContains("You have successfully chosen all required Labs for CS101!");
		assertOutputContains("All required activities for CS101 have been chosen!");

		assertOutputContains("Time: 11:00 -> 12:00");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: CHOSEN");

		assertOutputContains("Time: 13:30 -> 15:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: CHOSEN");

		assertOutputContains("Time: 16:00 -> 17:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Lab");
		assertOutputContains("Status: CHOSEN");

	}

	@Test
	public void testChooseTutorialAnd2Labs() throws URISyntaxException,
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
				"1",                                 // Required tutorials
				"2",                                 // Required labs
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

				// ADD Lab1
				"0",                                 // ADD Activity
				"2",                                 // ADD Lab
				"2025-03-01",                        // Start date
				"13:30",                             // Start time
				"2025-06-30",                        // End date
				"15:30",                             // End time
				"Room 111",                           // Location
				"thu",                               // Day of week
				"10",                                 // Capacity

				// ADD Lab2
				"0",                                 // ADD Activity
				"2",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"16:00",                             // Start time
				"2025-06-30",                        // End date
				"17:30",                             // End time
				"Room 101",                           // Location
				"thu",                               // Day of week
				"10",                                 // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code

				// choose available Tutorial1
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
				"1",              					 // choose available tutorial1

				// choose available Lab1
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"lab",							 // choose tutorial
				"1",              					 // choose available tutorial2

				// choose Lab2
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"lab",							     // choose lab
				"1",              					 // choose available lab


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

		assertOutputContains("You have successfully chosen all required Tutorials for CS101!");

		assertOutputContains("You still need to choose 1 more labs for CS101");
		assertOutputContains("You have successfully chosen all required Labs for CS101!");
		assertOutputContains("All required activities for CS101 have been chosen!");

		assertOutputContains("Time: 11:00 -> 12:00");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: CHOSEN");

		assertOutputContains("Time: 13:30 -> 15:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Lab");
		assertOutputContains("Status: CHOSEN");

		assertOutputContains("Time: 16:00 -> 17:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Lab");
		assertOutputContains("Status: CHOSEN");

	}

	@Test
	public void testChooseTutorialHasConflict() throws URISyntaxException,
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
				"1",                                 // Required tutorials
				"1",                                 // Required labs

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
				"10:00",                             // Start time
				"2025-06-30",                        // End date
				"11:30",                             // End time
				"Room 11",                           // Location
				"mon",                               // Day of week
				"10",                                 // Capacity

				// ADD Lab
				"0",                                 // ADD Activity
				"2",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"16:00",                             // Start time
				"2025-06-30",                        // End date
				"17:30",                             // End time
				"Room 101",                           // Location
				"thu",                               // Day of week
				"10",                                 // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code

				// choose available tutorial
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
				"1",              					 // choose available tutorial1

				// choose Lab
				"3",                                 // choose Activity
				"CS101",  							 // course code
				"lab",							     // choose lab
				"1",              					 // choose available lab


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

		assertOutputContains("You have successfully chosen all required Tutorials for CS101!");

		assertOutputContains("You have successfully chosen all required Labs for CS101!");
		assertOutputContains("All required activities for CS101 have been chosen!");

		assertOutputContains("Clashes with another activity!");
		assertOutputContains("Time: 10:00 -> 11:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: CHOSEN");
		assertOutputContains("Clashes with another activity!");

		assertOutputContains("Time: 16:00 -> 17:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Lab");
		assertOutputContains("Status: CHOSEN");

	}

	@Test
	public void testChooseActivityBeforeAddCourseToTimetable() throws URISyntaxException,
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
				"1",                                 // Required tutorials
				"1",                                 // Required labs
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
				"10",                                // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE

				"3",                                 // choose Activity
				"CS101",  							 // course code
				"tutorial",							 // choose tutorial
				"2",  								 // view Timetable
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

		assertOutputContains("No timetable found for student student1@hindeburg.ac.uk");
		assertOutputContains("You have not added any courses to your timetable.");

	}


}
