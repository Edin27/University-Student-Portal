package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import view.View;

import java.util.List;

public class StudentController extends Controller {
    public StudentController(SharedContext sharedContext, View view,
                             AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void manageTimetable(){

        while (true) {
            view.displayInfo("[1] Add Course to Timetable");
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
    private void viewTimetable(){}
    private void chooseActivityForCourse(){}
}
