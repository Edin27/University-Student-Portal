package view;

import model.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
        for (FAQItem item : section.getItems()) { {
            System.out.println(item.getId() + ") [Course: " + item.getTag() + "]");
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
    }}

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
                System.out.println("Course Name: " + course.getName() + ", Course Code: " + course.getCourseCode());
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
        if (activities == null || activities.isEmpty()) {System.out.println("none");return;}
        for (Activity activity: activities) {
            System.out.println("["+activity.getStartDate()+" -> "+activity.getEndDate());
            System.out.println(" "+activity.getDay().toString().toLowerCase()+", "+activity.getStartTime()+" -> "+activity.getEndTime()+"]");
        }
    }


    public List<Timetable.TimeSlot> displayAvailableTimeSlots(
            Timetable timetable,
            Course course,
            String courseCode,
            String activityType
    ) {
        displayInfo("Available " + activityType + "s for " + courseCode + ":");
        List<Timetable.TimeSlot> availableSlots = new ArrayList<>();
        int index = 1;

        List<Activity> activities = "Tutorial".equalsIgnoreCase(activityType) ? course.getTutorials() : course.getLabs();

        for (Timetable.TimeSlot slot : timetable.getTimeSlots()) {
            if (slot.getCourseCode().equals(courseCode) &&
                    slot.getStatus() == Timetable.Status.UNCHOSEN &&
                    slot.activityType.equalsIgnoreCase(activityType)) {
                for (Activity activity : activities) {
                    if (activity.getId() == slot.getActivityId() &&
                            activity.getStartDate().equals(slot.getStartDate()) &&
                            activity.getStartTime().equals(slot.getStartTime()) &&
                            activity.getEndDate().equals(slot.getEndDate()) &&
                            activity.getEndTime().equals(slot.getEndTime())) {
                        String slotInfo = "[" + index + "] " + slot.getStartDate() + " " +
                                slot.getStartTime() + " - " + slot.getEndDate() + " " +
                                slot.getEndTime() + " (ID: " + slot.getActivityId() + ")";
                        displayInfo(slotInfo);
                        availableSlots.add(slot);
                        index++;
                        break;
                    }
                }
            }
        }

        if (availableSlots.isEmpty()) {
            displayError("No available " + activityType.toLowerCase() + "s for " + courseCode);
        }

        return availableSlots;
    }

    @Override
    public void displayTimetable(SharedContext sharedContext) {
        ArrayList<String> courseList = new ArrayList<>();
        CourseManager courseManager = sharedContext.getCourseManager();
        Timetable timetable = courseManager.getTimetableByEmail(sharedContext.getCurrentUserEmail());
        System.out.println(timetable.getStudentEmail() + " Timetable");
        List<Timetable.TimeSlot> timeSlots = timetable.getTimeSlots();
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .plusWeeks(1);
        LocalDate nextFriday = nextMonday.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        LocalDate currentDate = nextMonday;
        Map<LocalDate, List<Timetable.TimeSlot>> nextWeekTimetable = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            nextWeekTimetable.put(currentDate, new ArrayList<>());
            currentDate = currentDate.plusDays(1);
        }

        // Only display next week's timetable
        for (Timetable.TimeSlot timeSlot : timeSlots) {
            LocalDate startDate = timeSlot.getStartDate();
            LocalDate endDate = timeSlot.getEndDate();
            DayOfWeek day = timeSlot.getDay();

            if(!startDate.isAfter(nextFriday) && !endDate.isBefore(nextMonday)){
                // Get date of activity next week
                LocalDate date = today
                        .with(TemporalAdjusters.nextOrSame(day))
                        .plusWeeks(1);

                nextWeekTimetable
                        .get(date)
                        .add(timeSlot);

            }
        }
        nextWeekTimetable.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    LocalDate date = entry.getKey();
                    List<Timetable.TimeSlot> slots = entry.getValue();
                    System.out.println(date.getDayOfWeek().toString() + "   [" + date + "]");
                    if (slots.isEmpty()) {
                        System.out.println("   No activity");
                    }
                    else {
                        for (Timetable.TimeSlot slot1 : slots) {
                            // display activity
                            System.out.println("   " + slot1);
                            if (!courseList.contains(slot1.getCourseCode())) {
                                courseList.add(slot1.getCourseCode());
                            }
                            // check for clashes
                            for (Timetable.TimeSlot slot2 : slots) {
                                if (!slot1.equals(slot2) && slot1.getStatus() == Timetable.Status.CHOSEN && slot2.getStatus() == Timetable.Status.CHOSEN) {
                                    if (slot1.overlaps(slot2.getStartDate(),
                                            slot2.getStartTime(), slot2.getEndDate(),
                                            slot2.getEndTime(), slot2.getDay(),
                                            slot2.getStatus())) {
                                        if (courseManager.findCourse(slot1.getCourseCode()).isUnrecordedLecture(slot1.getActivityId())) {
                                            displayError("Unrecorded Lecture clash!\n");
                                        } else {
                                            displayWarning("Clashes with another activity!\n");
                                        }
                                    }
                                }
                            }
                        }

                    }
                    displayDivider();
                });
        for (String code : courseList) {
            boolean labsFull = courseManager.checkChosenLabs(code, timetable);
            boolean tutorialsFull = courseManager.checkChosenTutorials(code, timetable);

            if (!labsFull && !tutorialsFull) {
                displayWarning(String.format("Tutorials and labs not chosen for course: %s!", code));
            } else if (!labsFull) {
                displayWarning(String.format("Labs not chosen for course: %s!", code));
            } else if (!tutorialsFull) {
                displayWarning(String.format("Tutorials not chosen for course: %s!", code));
            }
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
        boolean empty = true;

        for (FAQItem item : section.getItems()) {
            if (tagFilter == null || (item.getTag() != null && item.getTag().toLowerCase().contains(tagFilter.toLowerCase()))) {
                System.out.println(item.getQuestion());
                System.out.print("> ");
                System.out.println(item.getAnswer());
                empty = false;
            }
        }

        if (empty) {
            System.out.println("No faq items of this tag!");
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
