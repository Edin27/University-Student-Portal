package system_tests;

import controller.AdminStaffController;
import controller.ViewerController;
import external.AuthenticationService;
import external.EmailService;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import view.TextUserInterface;
import view.View;
import java.io.IOException;
import java.net.URISyntaxException;

public class RemoveCourseSystemTests extends TUITest {

	@Test
	@DisplayName("Remove course successfully")
	public void testRemoveCourseSuccessfully() throws URISyntaxException, IOException,
			ParseException {
		// Add INF001 into course list, then remove INF001
		setMockInput("0", "INF001", "Software Engineering",
					"Students will learn software engineering through a series of group assignments",
					"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
					"sarah@hindeburg.ac.uk", "1", "1", "0", "0", "2025-03-03",
					"12:00","2025-05-05", "13:00", "Appleton Tower", "mon",
					"y", "-1", "1", "INF001", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Course has been successfully created");
		assertOutputContains("Course has been successfully removed");

		// Check if removed from view course
		ViewerController viewerController = new ViewerController(sharedContext, view,
				auth, email);
		startOutputCapture();
		viewerController.viewCourses();
		assertOutputContains("no courses");

	}

	@Test
	@DisplayName("Course code not exist")
	public void testCourseCodeNotExist() throws URISyntaxException, IOException,
			ParseException {
		// Add INF001 into course list, then remove INF002
		setMockInput("0", "INF001", "Software Engineering",
				"Students will learn software engineering through a series of group assignments",
				"y", "Dr James", "james@hindeburg.ac.uk", "Sarah",
				"sarah@hindeburg.ac.uk", "1", "1", "0", "0", "2025-09-12",
				"12:00","2025-12-10", "13:00", "Appleton Tower", "mon",
				"y", "-1", "1", "INF002", "-1");
		View view = new TextUserInterface();
		SharedContext sharedContext = new SharedContext(view);
		AuthenticationService auth = new MockAuthenticationService();
		EmailService email = new MockEmailService();
		AdminStaffController adminStaffController = new AdminStaffController(sharedContext, view, auth, email);
		startOutputCapture();
		adminStaffController.manageCourses();
		assertOutputContains("Course has been successfully created");
		assertOutputContains("Course code provided does not exist in the system");

	}


}
