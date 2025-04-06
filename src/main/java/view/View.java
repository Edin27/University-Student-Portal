package view;

import model.*;

import java.util.List;

public interface View {
    String getInput(String prompt);
    boolean getYesNoInput(String prompt);
    void displayInfo(String text);
    void displaySuccess(String text);
    void displayWarning(String text);
    void displayError(String text);
    void displayException(Exception e);
    void displayDivider();
    void displayFAQ(FAQ faq);
    void displayFAQSection(FAQSection section);
    void displayInquiry(Inquiry inquiry);
    void displayCourses(CourseManager courseManager);
    void displayCourse(Course course);
    List<Timetable.TimeSlot> displayAvailableTimeSlots(
            Timetable timetable,
            Course course,
            String courseCode,
            String activityType
    );
    void displayTimetable(Timetable timetable);
    void displayFAQ(FAQ faq, String tagFilter);
    void displayFAQSection(FAQSection section, String tagFilter);
}
