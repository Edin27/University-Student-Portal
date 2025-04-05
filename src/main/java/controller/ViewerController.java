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
        Log.AddLog(Log.ActionName.VIEW_COURSES, "", Log.Status.SUCCESS);
    }

    public void viewSpecificCourse() {
        String code = view.getInput("Input course code: ");
        Course course = sharedContext.courseManager.findCourse(code);
        if (course != null) {
            view.displayCourse(course);
            Log.AddLog(Log.ActionName.VIEW_COURSES, code, Log.Status.SUCCESS);
        } else {
            view.displayError("This course does not exist!");
            Log.AddLog(Log.ActionName.VIEW_COURSES, code, Log.Status.FAILURE);
        }
    }
    }


