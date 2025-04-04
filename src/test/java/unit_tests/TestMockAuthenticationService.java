package unit_tests;

import external.MockAuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import system_tests.TUITest;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

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
//		assertFalse(result.contains("error"), "Login with valid credentials should " +
//				"return user data without error");
		assertEquals("{\"password\":\"" + password + "\",\"role\":\"TeachingStaff\"," +
				"\"email\":\"" + username + "@hindeburg.ac.uk\",\"username\":\"" + username + "\"}",
				result, "Login with valid credentials should return user data without " +
						"error");
	}

	@Test
	@DisplayName("Login with invalid username returns error message")
	public void inValidUserNameReturnError(){
		String username = "studnet2";
		String password = "student2pass";
		String result = authService.login(username, password);
//		assertTrue(result.contains("error"), "Login with invalid username should " +
//				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result,
				"Login with invalid username should return error");
	}

	@Test
	@DisplayName("Login with wrong password returns error message")
	public void wrongPasswordReturnError(){
		String username = "student2";
		String password = "student2passs";
		String result = authService.login(username, password);
//		assertTrue(result.contains("error"), "Login with wrong password should " +
//				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result,
				"Login with wrong password should return error");
	}

	@Test
	@DisplayName("Login with empty username returns error message")
	public void emptyUserNameReturnError(){
		String username = "";
		String password = "student2pass";
		String result = authService.login(username, password);
//		assertNotNull(username, "Login with empty username should " +
//				"return error");
//		assertTrue(result.contains("error"), "Login with empty username should " +
//				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result,
				"Login with empty username should return error");
	}

	@Test
	@DisplayName("Login with empty password returns error message")
	public void emptyPasswordReturnError(){
		String username = "student2";
		String password = "";
		String result = authService.login(username, password);
//		assertNotNull(password, "Login with empty password should " +
//				"return error");
//		assertTrue(result.contains("error"), "Login with empty password should " +
//				"return error");
		assertEquals("{\"error\":\"Wrong username or password\"}", result,
				"Login with empty password should return error");
	}

	@Test
	@DisplayName("NullPointerException thrown when resource file is missing")
	public void nullResourceThrowsNPE() {
		assertThrows(NullPointerException.class, () -> {
			new MockAuthenticationService() {
				@Override
				protected URL getUserDataResource() {
					return null; // Simulate missing resource file
				}
			};
		}, "NullPointerException should be thrown when resource file is missing");
	}

	@Test
	@DisplayName("ParseException thrown when data file is invalid")
	void invalidResourceThrowParseException() {
		assertThrows(ParseException.class, () -> {
			new MockAuthenticationService(){
				@Override
				protected URL getUserDataResource() {
					return getClass().getResource("/InvalidMockUserData.json");
				}
			};
		}, "ParseException should be thrown when data file contains invalid JSON");
	}

	@Test
	@DisplayName("ParseException thrown when data file has missing field")
	void missingFieldThrowParseException() {
		assertThrows(ParseException.class, () -> {
			new MockAuthenticationService(){
				@Override
				protected URL getUserDataResource() {
					return getClass().getResource("/MissingFieldMockUserData.json");
				}
			};
		}, "ParseException should be thrown when data file has missing field");
	}

	@Test
	@DisplayName("ParseException thrown when data file has empty value")
	void emptyValueThrowParseException() {
		assertThrows(ParseException.class, () -> {
			new MockAuthenticationService(){
				@Override
				protected URL getUserDataResource() {
					return getClass().getResource("/EmptyValueMockUserData.json");
				}
			};
		}, "ParseException should be thrown when data file has empty value");
	}

	@Test
	@DisplayName("URISyntaxException thrown when file URI is invalid")
	public void invalidURIThrowURISyntaxExcep() {
		assertThrows(URISyntaxException.class, () -> {
			new MockAuthenticationService() {
				@Override
				protected File getDataFileFromURL(URL url) throws URISyntaxException {
					try {
						URL invalidUrl = new URL("http://example.com/|invalid|path");
						return Paths.get(invalidUrl.toURI()).toFile();
					} catch (MalformedURLException e) {
						throw new URISyntaxException(e.getMessage(), "Forced MalformedURLException");
					}
				}
			};
		}, "URISyntaxException should be thrown when file URI is invalid");
	}

	@Test
	@DisplayName("IOException thrown when file cannot be opened")
	public void testIOExceptionThrown() {
		assertThrows(IOException.class, () -> {
			new MockAuthenticationService() {
				@Override
				protected File getDataFileFromURL(URL url) {
					// Return a File object pointing to a non-existent file
					return new File("nonexistent/path/to/file.json");
				}
				@Override
				protected FileReader createFileReader(File file) throws IOException {
					return new FileReader(file);
				}
			};
		}, "IOException should be thrown when file cannot be opened");
	}

}