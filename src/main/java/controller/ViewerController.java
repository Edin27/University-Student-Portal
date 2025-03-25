package controller;

import external.AuthenticationService;
import external.EmailService;
import external.Log;
import model.AuthenticatedUser;
import model.CourseManager;
import model.Inquiry;
import model.SharedContext;
import view.View;

public class ViewerController extends Controller {
    public ViewerController(SharedContext sharedContext, View view,
                            AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void viewCourses() {
        view.displayCourses(sharedContext.getCourseManager());
    }

    public void viewSpecificCourse() {

    }

}
