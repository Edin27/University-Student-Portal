package system_tests;

import controller.GuestController;
import controller.MenuController;
import controller.ViewerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.CourseManager;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public class ViewCoursesSystemTests extends TUITest{

    Course course1 = new Course(new TextUserInterface(), "1", "course1", "desc1", false, "teacher1", "teacher1@hindeburg.ac.uk", "teacher2", "teacher2@hindeburg.ac.uk", 0, 0, null, null, null);
    Course course2 = new Course(new TextUserInterface(), "2", "course2", "desc2", false, "teacher1", "teacher1@hindeburg.ac.uk", "teacher2", "teacher2@hindeburg.ac.uk", 0, 0, null, null, null);
    Course course3 = new Course(new TextUserInterface(), "3", "course3", "desc3", false, "teacher1", "teacher1@hindeburg.ac.uk", "teacher2", "teacher2@hindeburg.ac.uk", 0, 0, null, null, null);

    @Test
    @DisplayName("View courses when no courses are added")
    public void testViewCoursesNoCourses() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        ViewerController viewerController = new ViewerController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());;
        startOutputCapture();
        viewerController.viewCourses();
        assertOutputContains("no courses");
    }

    @Test
    @DisplayName("View courses with three courses added")
    public void testViewCoursesStandard() throws URISyntaxException, IOException, ParseException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getCourseManager().getCourses().add(course1);
        context.getCourseManager().getCourses().add(course2);
        context.getCourseManager().getCourses().add(course3);
        ViewerController viewerController = new ViewerController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        viewerController.viewCourses();
        assertOutputContains("Course Name: course1, Course Code: 1");
        assertOutputContains("Course Name: course2, Course Code: 2");
        assertOutputContains("Course Name: course3, Course Code: 3");
        context.getCourseManager().getCourses().remove(course1);
        context.getCourseManager().getCourses().remove(course2);
        context.getCourseManager().getCourses().remove(course3);
    }

    @Test
    @DisplayName("View valid course returns course info")
    public void testViewCourseStandard() throws URISyntaxException, IOException, ParseException {
        setMockInput("2");
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getCourseManager().getCourses().add(course1);
        context.getCourseManager().getCourses().add(course2);
        context.getCourseManager().getCourses().add(course3);
        ViewerController viewerController = new ViewerController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        viewerController.viewSpecificCourse();
        assertOutputContains("Course Code: 2");
        context.getCourseManager().getCourses().remove(course1);
        context.getCourseManager().getCourses().remove(course2);
        context.getCourseManager().getCourses().remove(course3);
    }

    @Test
    @DisplayName("View invalid course returns error")
    public void testViewCourseFailure() throws URISyntaxException, IOException, ParseException {
        setMockInput("jfcds");
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getCourseManager().getCourses().add(course1);
        context.getCourseManager().getCourses().add(course2);
        context.getCourseManager().getCourses().add(course3);
        ViewerController viewerController = new ViewerController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        viewerController.viewSpecificCourse();
        assertOutputContains("This course does not exist!");
        context.getCourseManager().getCourses().remove(course1);
        context.getCourseManager().getCourses().remove(course2);
        context.getCourseManager().getCourses().remove(course3);
    }
}
