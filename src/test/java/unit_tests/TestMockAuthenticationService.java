package unit_tests;

import external.MockAuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import system_tests.TUITest;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.simple.parser.ParseException;

class TestMockAuthenticationService extends TUITest {

	private static MockAuthenticationService authService;

	@BeforeAll
	static void setUp() throws URISyntaxException, IOException, ParseException, NullPointerException {
		authService = new MockAuthenticationService();
	}

	@Test
	@DisplayName("Login with valid credentials returns user data")
	public void validCredentialsNoError() {
		String username = "teacher1";
		String password = "teacher1pass";
		String result = authService.login(username, password);
		assertFalse(result.contains("error"), "Login with valid credentials should " +
				"return user data without error");
		assertEquals("{\"password\":\"" + password + "\",\"role\":\"TeachingStaff\"," +
				"\"email\":\"" + username + "@hindeburg.ac.uk\",\"username\":\"" + username + "\"}",
				result);
	}

	@Test
	@DisplayName("Login with invalid username returns error message")
	public void inValidUserNameReturnError(){
		String username = "studnet2";
		String password = "student2pass";
		String result = authService.login(username, password);
		assertTrue(result.contains("error"), "Login with invalid username should " +
				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result);
	}

	@Test
	@DisplayName("Login with wrong password returns error message")
	public void wrongPasswordReturnError(){
		String username = "student2";
		String password = "student2passs";
		String result = authService.login(username, password);
		assertTrue(result.contains("error"), "Login with wrong password should " +
				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result);
	}

	


}