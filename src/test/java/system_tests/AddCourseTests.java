package system_tests;


import controller.AdminStaffController;
import external.AuthenticationService;
import external.EmailService;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import view.TextUserInterface;
import view.View;
import java.io.IOException;
import java.net.URISyntaxException;



public class AddCourseTests extends TUITest{

	@Test
	@DisplayName("Add course successfully")
	public void testAddCourseSuccessfully() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
						"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
						"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "2025-09" +
						"-12", "12:00","2025-12-10", "13:00", "Appleton Tower", "mon",
						"y", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Course has been successfully created");
	}

	@Test
	@DisplayName("Test when an integer but unavailable MANAGE_COURSES menu option is " +
			"inputted")
	public void testManageCoursesOptionUnavailable() throws URISyntaxException,
			IOException,
			ParseException {
		setMockInput("3", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid option: ");

	}

	@Test
	@DisplayName("Test when non-integer MANAGE_COURSES menu option is inputted")
	public void testManageCoursesOptionInvalid() throws URISyntaxException,
			IOException,
			ParseException {
		setMockInput("/", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid option: ");
	}

	@Test
	@DisplayName("Test non-integer required tutorials")
	public void testNonIntReqTutorials() throws URISyntaxException, IOException, ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
						"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
						"sarah@hindeburg.ac.uk", "a", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid input: ");
	}

	@Test
	@DisplayName("Test non-integer required labs")
	public void testNonIntReqLabs() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "a", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid input: ");
	}

	@Test
	@DisplayName("Test some required fields empty")
	public void testRequiredFieldEmpty() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "", "", "Students will learn " + "software engineering " +
						"through a series of group assignments", "y", "Dr James",
						"james@hindeburg.ac.uk", "Sarah", "sarah@hindeburg.ac.uk", "7",
						"5", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Required course info not provided");
	}

	@Test
	@DisplayName("Test invalid course code (not alphanumeric)")
	public void testInvalidCourseCode() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF-001", "Software Engineering", "Students will learn " +
						"software engineering " +
						"through a series of group assignments", "y", "Dr James",
				"james@hindeburg.ac.uk", "Sarah", "sarah@hindeburg.ac.uk", "7",
				"5", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Provided course code is invalid");
	}

	@Test
	@DisplayName("Test course code already exist in system")
	public void testDuplicatedCourseCode() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "2025-09" +
						"-12", "12:00","2025-12-10", "13:00", "Appleton Tower", "mon",
				"y", "-1", "0", "INF001", "Data Science", "Students will learn " +
						"data analysis through few assignments",
				"y", "Dr John", "john@hindeburg.ac.uk", "Lily",
				"lily@hindeburg.ac.uk", "8", "6", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Course with that code already exists");
	}

	@Test
	@DisplayName("Test unavailable Add Activity menu option")
	public void testUnavailableActOption() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "9", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid option: ");

	}

	@Test
	@DisplayName("Test invalid Add Activity menu option (non-integer)")
	public void testInvalidActOption() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "a", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Invalid option: ");

	}

	@Test
	@DisplayName("Test empty activity field")
	public void testEmptyActivityField() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "2025-09" +
						"-12", "12:00","  ", "", "Appleton Tower", "mon",
				"y", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Required activity info not provided");
	}

	@Test
	@DisplayName("Test unavailable activity type")
	public void testUnavailableActivityType() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "9", "2025-09" +
						"-12", "12:00","2025-12-01", "13:00", "Appleton Tower", "mon",
				"y", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Activity type provided is invalid");
	}

	@Test
	@DisplayName("Test invalid activity type (non-integer)")
	public void testInvalidActivityType() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "a", "2025-09" +
						"-12", "12:00","2025-12-01", "13:00", "Appleton Tower", "mon",
				"y", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Activity type provided is invalid");
	}

	@Test
	@DisplayName("Test invalid date")
	public void testInvalidDate() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "30122025", "12:00",
				"2025-12-01", "13:00", "Appleton Tower", "mon", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Start date or end date provided is invalid");
	}

	@Test
	@DisplayName("Test invalid time")
	public void testInvalidTime() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "2025-09-01", "12pm",
				"2025-12-01", "13:00", "Appleton Tower", "mon", "y" ,"-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Start time or end time provided is invalid");
	}

	@Test
	@DisplayName("Test end date earlier than start date")
	public void testEndDateBeforeStartDate() throws URISyntaxException, IOException,
			ParseException {
		setMockInput("0", "INF001", "Software Engineering", "Students will learn " +
						"software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "7", "5", "0", "0", "2025-12-01", "12pm",
				"2025-09-01", "13:00", "Appleton Tower", "mon", "-1", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("End date cannot be earlier than start date");
	}













}
