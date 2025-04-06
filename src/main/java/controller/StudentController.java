package controller;

import external.AuthenticationService;
import external.EmailService;
import external.Log;
import model.*;
import view.View;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StudentController extends Controller {
    public StudentController(SharedContext sharedContext, View view,
                             AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void manageTimetable(){

        while (true) {
            view.displayInfo("[1] Add Course to Timetable");
            view.displayInfo("[2] View Timetable");
            view.displayInfo("[3] Choose Activity For Course");
            view.displayInfo("[-1] Return to main menu");

            String input = view.getInput("Please choose an option: ");
            if (input == null || input.trim().isEmpty()) {
                view.displayError("No option selected. Please enter a valid number.");
                continue;
            }

            try {
                int optionNo = Integer.parseInt(input);

                if (optionNo == -1) {
                    break;
                } else if (optionNo == 1) {
                    addCourseToTimetable();
                } else if (optionNo == 2) {
                    viewTimetable();
                } else if (optionNo == 3) {
                    chooseActivityForCourse();
                } else {
                    view.displayError("Invalid option: " + input);
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid option: " + input);
            }
        }
    }

    private void addCourseToTimetable(){
        view.displayInfo("===Add Course To Timetable===");
        String courseCode = view.getInput("Enter the course code: ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.addCourseToStudentTimetable(email,courseCode);


    }



    private void removeCourseFromTimetable(){}
    private void viewTimetable(){
        if (!(sharedContext.currentUser instanceof AuthenticatedUser)) {
            view.displayError("You must be logged in to view the timetable.");
            return;
        }

        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();

        // 直接从 CourseManager 获取 Timetable 对象
        Timetable studentTimetable = courseManager.getTimetableByEmail(email);

        if (studentTimetable == null) {
            view.displayWarning("You have not added any courses to your timetable.");
            Log.AddLog(Log.ActionName.VIEW_TIMETABLE, "null", Log.Status.FAILURE);
        } else {
            view.displayTimetable(sharedContext);
        }
    }
    private void chooseActivityForCourse(){
        view.displayInfo("===Choose Activity For Course===");
        String courseCode = view.getInput("Enter the course code: ");
        String activityType = view.getInput("Enter the activityType (E.g Tutorial,Lab): ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.chooseActivityForCourse(email,courseCode, activityType);
    }
}
