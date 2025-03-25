package controller;

import external.AuthenticationService;
import external.EmailService;
import external.Log;
import model.*;
import view.View;

import java.util.Iterator;

public class ViewerController extends Controller {
    public ViewerController(SharedContext sharedContext, View view,
                            AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void viewCourses() {
        view.displayCourses(sharedContext.getCourseManager());
    }

    public void viewSpecificCourse() {
        String code = view.getInput("Input course code: ");
        if (sharedContext.courseManager.getCourses() != null) {
            Iterator<Course> courses = sharedContext.courseManager.getCourses().iterator();
            while (courses.hasNext()) {
                Course course = courses.next();
                if (course.getCourseCode().equals(code)) {
                    view.displayCourse(course);
                }
            }
        }
    }

}
