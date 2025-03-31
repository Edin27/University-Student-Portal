package view;

import model.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements View {
    private final Scanner scanner = new Scanner(System.in);
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public boolean getYesNoInput(String prompt) {
        System.out.println(prompt + " [Y/n]");
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
            return true;
        } else if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
            return false;
        }
        return Boolean.parseBoolean(line);
    }

    @Override
    public void displayInfo(String text) {
        System.out.println(text);
    }

    @Override
    public void displaySuccess(String text) {
        System.out.println(ANSI_GREEN + text + ANSI_RESET);
    }

    @Override
    public void displayWarning(String text) {
        System.out.println(ANSI_YELLOW + text + ANSI_RESET);
    }

    @Override
    public void displayError(String text) {
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    @Override
    public void displayException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        displayError(sw.toString());
    }

    @Override
    public void displayDivider() {
        System.out.println("-------------------------");
    }

    @Override
    public void displayFAQ(FAQ faq) {
        System.out.println("Frequently Asked Questions");
        displayDivider();
        int i = 0;
        for (FAQSection section : faq.getSections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(section.getTopic());
        }
    }

    @Override
    public void displayFAQSection(FAQSection section) {
        System.out.println(section.getTopic());
        displayDivider();
        for (FAQItem item : section.getItems()) {
            System.out.println("[Course: " + item.getCourse() + "]");
            System.out.println(item.getQuestion());
            System.out.print("> ");
            System.out.println(item.getAnswer());
        }

        System.out.println("Subsections:");
        int i = 0;
        for (FAQSection subsection : section.getSubsections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(subsection.getTopic());
        }
    }

    @Override
    public void displayInquiry(Inquiry inquiry) {
        System.out.println("Inquirer: " + inquiry.getInquirerEmail());
        System.out.println("Created at: " + inquiry.getCreatedAt());
        System.out.println("Assigned to: " + (inquiry.getAssignedTo() == null ? "No one" : inquiry.getAssignedTo()));
        System.out.println("Query:");
        System.out.println(inquiry.getContent());
    }

    @Override
    public void displayCourses(CourseManager courseManager) {
        Collection<Course> courses = courseManager.getCourses();
        if (courses.isEmpty()) {
            System.out.println("no courses");
        } else{
            for (Course course : courseManager.getCourses()) {
                System.out.println("Course Name: " + course.getName() + "   Course Code: " + course.getCourseCode());
            }
        }
    }

    @Override
    public void displayCourse(Course course) {
        System.out.println("Course Code: " + course.getCourseCode());
        System.out.println("Name: " + course.getName());
        System.out.println("Description: " + course.getDescription());
        System.out.println("Requires Computers: " + course.getRequiresComputers());
        System.out.println("Organiser Name: " + course.getCourseOrganiserName());
        System.out.println("Organiser Email: " + course.getCourseOrganiserEmail());
        System.out.println("Secretary Name: " + course.getCourseSecretaryName());
        System.out.println("Secretary Email: " + course.getCourseSecretaryEmail());
        System.out.println("Required Tutorials: " + course.getRequiredTutorials());
        System.out.println("Required Labs: " + course.getRequiredLabs());
        System.out.println("Lectures");
        displayActivities(course.getLectures());
        System.out.println("Tutorials");
        displayActivities(course.getTutorials());
        System.out.println("Labs");
        displayActivities(course.getLabs());

    }

    private void displayActivities(List<Activity> activities) {
        if (activities.isEmpty()) {System.out.println("none");return;}
        for (Activity activity: activities) {
            System.out.println("["+activity.getStartDate()+" -> "+activity.getEndDate());
            System.out.println(" "+activity.getDay().toString().toLowerCase()+", "+activity.getStartTime()+" -> "+activity.getEndTime()+"]");
        }
    }

    @Override
    public void displayTimetable(Timetable timetable) {
        System.out.println(timetable.getStudentEmail() + " Timetable");
        List<Timetable.TimeSlot> timeSlots = timetable.getTimeSlots();
        for (Timetable.TimeSlot timeSlot : timeSlots) {
            System.out.println(timeSlot);
        }
    }

    @Override
    public void displayFAQ(FAQ faq, String tagFilter) {
        System.out.println("Frequently Asked Questions");
        displayDivider();
        int i = 0;
        for (FAQSection section : faq.getSections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(section.getTopic());
        }

        // Also show filtered items at root level (optional)
        for (FAQSection section : faq.getSections()) {
            displayFAQSection(section, tagFilter);
        }
    }

    @Override
    public void displayFAQSection(FAQSection section, String tagFilter) {
        System.out.println(section.getTopic());
        displayDivider();

        for (FAQItem item : section.getItems()) {
            if (tagFilter == null ||
                item.getQuestion().toLowerCase().contains(tagFilter.toLowerCase()) ||
                item.getAnswer().toLowerCase().contains(tagFilter.toLowerCase())) {
                System.out.println(item.getQuestion());
                System.out.print("> ");
                System.out.println(item.getAnswer());
            }
        }

        System.out.println("Subsections:");
        int i = 0;
        for (FAQSection subsection : section.getSubsections()) {
            System.out.print("[");
            System.out.print(i++);
            System.out.print("] ");
            System.out.println(subsection.getTopic());
        }
    }
}
