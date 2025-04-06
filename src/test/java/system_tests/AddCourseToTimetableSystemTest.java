package system_tests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import controller.AdminStaffController;
import controller.GuestController;
import controller.MenuController;
import external.AuthenticationService;
import external.EmailService;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.StudentController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.CourseManager;
import model.SharedContext;
import model.Timetable;
import view.TextUserInterface;
import view.View;

public class AddCourseToTimetableSystemTest extends TUITest {

	@Test
	public void testAddLectureToTimetable() throws URISyntaxException, IOException,
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
				"2025-12-31",                        // End date
				"10:30",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"Y",                                 // Is recorded

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code
				"2",								 // view Timetable
				"-1",                                // Return to main menu
				"-1"                                 // Exit
		);

		// Setup
		TextUserInterface view = new TextUserInterface();
		SharedContext context = new SharedContext(view);

		// Login as admin staff - using the same mock input stream
		GuestController guestController = new GuestController(context, view, new MockAuthenticationService(), new MockEmailService());
		guestController.login();

		// Start the menu controller to handle main menu
		MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
		//menuController.mainMenu();
		// Start capturing output
		startOutputCapture();

		// Execute the main menu which will go through our inputs
		menuController.mainMenu();

		// Verify output contains success message
		assertOutputContains("You have to choose 6 tutorials for this course");
		assertOutputContains("You have to choose 6 labs for this course");
		assertOutputContains("The course was successfully added to your timetable");
		assertOutputContains("Time: 09:00 -> 10:30");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Lecture");
		assertOutputContains("Status: CHOSEN");
		assertOutputContains("Tutorials and labs not chosen for course: CS101!");
	}

	@Test
	public void testAddLectureAndTutorialToTimetable() throws URISyntaxException,
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
				"2025-12-31",                        // End date
				"10:30",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"Y",                                 // Is recorded

				// ADD Tutorial
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"11:00",                             // Start time
				"2025-12-31",                        // End date
				"12:00",                             // End time
				"Room 11",                           // Location
				"wed",                               // Day of week
				"10",								 // Capacity

				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS101",                             // Enter the course code
				"2",								 // view Timetable
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

		// Verify output contains success message
		assertOutputContains("You have to choose 6 tutorials for this course");
		assertOutputContains("You have to choose 6 labs for this course");
		assertOutputContains("The course was successfully added to your timetable");
		assertOutputContains("Time: 11:00 -> 12:00");
		assertOutputContains("Course code: CS101");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: UNCHOSEN");
		assertOutputContains("Tutorials and labs not chosen for course: CS101!");
	}

	@Test
	public void testUnRecordedLectureConflict() throws URISyntaxException,
			IOException,
			ParseException {
		// Set up mock inputs for the entire flow
		setMockInput(
				"admin1", "admin1pass",              // Login credentials
				"3",                                 // MANAGE_COURSES
				"0",                                 // Add course
				"CS102",                             // Course code
				"Foundation of Data science",        // Name
				"FDS",                               // Description
				"Y",                                 // require computers
				"Professor H",                 // Course organiser name
				"H@university.edu",            // Course organiser email
				"Mary Jane",                       // Course secretary name
				"MJ@university.edu",                // Course secretary email
				"4",                                 // Required tutorials
				"4",                                 // Required labs

				// ADD Lecture
				"0",                                 // ADD Activity
				"0",                                 // ADD Lecture
				"2025-03-01",                        // Start date
				"10:00",                             // Start time
				"2025-12-31",                        // End date
				"12:00",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"n",                                 // Is recorded

				// ADD Tutorial
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"11:00",                             // Start time
				"2025-12-31",                        // End date
				"12:00",                             // End time
				"Room 11",                           // Location
				"mon",                               // Day of week
				"10",								 // Capacity


				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS102",                             // Enter the course code
				"2",								 // view Timetable
				"-1",                                //Return to main menu
				"-1"                                 // Exit
		);

		// Setup
		TextUserInterface view = new TextUserInterface();
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

		// Verify output contains success message
		assertOutputContains("You have at least one clash with an unrecorded lecture. " +
				"The course cannot be added to your timetable");
	}

	@Test
	public void testRecordedLectureConflict() throws URISyntaxException,
			IOException,
			ParseException {
		// Set up mock inputs for the entire flow
		setMockInput(
				"admin1", "admin1pass",              // Login credentials
				"3",                                 // MANAGE_COURSES
				"0",                                 // Add course
				"CS102",                             // Course code
				"Foundation of Data science",        // Name
				"FDS",                               // Description
				"Y",                                 // require computers
				"Professor H",                       // Course organiser name
				"H@university.edu",                  // Course organiser email
				"Mary Jane",                       // Course secretary name
				"MJ@university.edu",                // Course secretary email
				"4",                                 // Required tutorials
				"4",                                 // Required labs

				// ADD Lecture
				"0",                                 // ADD Activity
				"0",                                 // ADD Lecture
				"2025-03-01",                        // Start date
				"10:00",                             // Start time
				"2025-12-31",                        // End date
				"12:00",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"Y",                                 // Is recorded

				// ADD Tutorial
				"0",                                 // ADD Activity
				"1",                                 // ADD Tutorial
				"2025-03-01",                        // Start date
				"11:00",                             // Start time
				"2025-12-31",                        // End date
				"12:00",                             // End time
				"Room 11",                           // Location
				"mon",                               // Day of week
				"10",								 // Capacity


				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS102",                             // Enter the course code
				"2",								 // view Timetable
				"-1",                                //Return to main menu
				"-1"                                 // Exit
		);

		// Setup
		TextUserInterface view = new TextUserInterface();
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

		// Verify output contains success message
		assertOutputContains("Some activities have clashes, please choose carefully");
		assertOutputContains("You have to choose 4 tutorials for this course");
		assertOutputContains("You have to choose 4 labs for this course");
		assertOutputContains("The course was successfully added to your timetable");

		assertOutputContains("Time: 10:00 -> 12:00");
		assertOutputContains("Course code: CS102");
		assertOutputContains("Activity: Lecture");
		assertOutputContains("Status: CHOSEN");

		assertOutputContains("Time: 11:00 -> 12:00");
		assertOutputContains("Course code: CS102");
		assertOutputContains("Activity: Tutorial");
		assertOutputContains("Status: UNCHOSEN");

		assertOutputContains("Tutorials and labs not chosen for course: CS102!");
	}

	@Test
	public void testAddInvalidCourseCodeToTimetable() throws URISyntaxException,
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
				"0",                                 // ADD Activity
				"0",                                 // ADD Lecture
				"2025-03-01",                        // Start date
				"09:00",                             // Start time
				"2025-12-31",                        // End date
				"10:30",                             // End time
				"Room 101",                          // Location
				"mon",                               // Day of week
				"Y",                                 // Is recorded
				"-1",                                //Return to manage courses
				"-1",                                // Return to main menu
				"0",                                 // LOGOUT
				"0",                                 // LOGIN
				"student1", "student1pass",          // Login credentials
				"5",                                 // MANAGE_TIMETABLE
				"1",                                 // Add Course to Timetable
				"CS102",                             // Enter the course code
				"2",								 // viewTimetable
				"-1",                                // Return to main menu
				"-1"                                 // Exit
		);

		// Setup
		TextUserInterface view = new TextUserInterface();
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

		// Verify output contains success message
		assertOutputContains("Incorrect course code provided");
		assertOutputContains("You have not added any courses to your timetable.");
	}


}