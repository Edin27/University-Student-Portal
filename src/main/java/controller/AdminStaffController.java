package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class AdminStaffController extends StaffController {
    public AdminStaffController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }
    
    public void manageFAQ() {
        FAQSection currentSection = null;

        while (true) {
            if (currentSection == null) {
                view.displayFAQ(sharedContext.getFAQ());
                view.displayInfo("[-1] Return to main menu");
            } else {
                view.displayFAQSection(currentSection);
                view.displayInfo("[-1] Return to " + (currentSection.getParent() == null ? "FAQ" : currentSection.getParent().getTopic()));
            }
            view.displayInfo("[-2] Add FAQ item");
            String input = view.getInput("Please choose an option: ");
            try {
                int optionNo = Integer.parseInt(input);

                if (optionNo == -2) {
                    addFAQItem(currentSection);
                } else if (optionNo == -1) {
                    if (currentSection == null) {
                        break;
                    } else {
                        currentSection = currentSection.getParent();
                    }
                } else {
                    try {
                        if (currentSection == null) {
                            currentSection = sharedContext.getFAQ().getSections().get(optionNo);
                        } else {
                            currentSection = currentSection.getSubsections().get(optionNo);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        view.displayError("Invalid option: " + optionNo);
                    }
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid option: " + input);
            }
        }
    }

    private void addFAQItem(FAQSection currentSection) {
        // When adding an item at root of FAQ, creating a section is mandatory
        boolean createSection = (currentSection == null);
        if (!createSection) {
            createSection = view.getYesNoInput("Would you like to create a new topic for the FAQ item?");
        }

        if (createSection) {
            String newTopic = view.getInput("Enter new topic title: ");
            FAQSection newSection = new FAQSection(newTopic);
            if (currentSection == null) {
                if (sharedContext.getFAQ().getSections().stream().anyMatch(section -> section.getTopic().equals(newTopic))) {
                    view.displayWarning("Topic '" + newTopic + "' already exists!");
                    newSection = sharedContext.getFAQ().getSections().stream().filter(section -> section.getTopic().equals(newTopic)).findFirst().orElseThrow();
                } else {
                    sharedContext.getFAQ().addSection(newSection);
                    view.displayInfo("Created topic '" + newTopic + "'");
                }
            } else {
                if (currentSection.getSubsections().stream().anyMatch(section -> section.getTopic().equals(newTopic))) {
                    view.displayWarning("Topic '" + newTopic + "' already exists under '" + currentSection.getTopic() + "'!");
                    newSection = currentSection.getSubsections().stream().filter(section -> section.getTopic().equals(newTopic)).findFirst().orElseThrow();
                } else {
                    currentSection.addSubsection(newSection);
                    view.displayInfo("Created topic '" + newTopic + "' under '" + currentSection.getTopic() + "'");
                }
            }
            currentSection = newSection;
        }

        String question = view.getInput("Enter the question for new FAQ item: ");
        String answer = view.getInput("Enter the answer for new FAQ item: ");
        currentSection.getItems().add(new FAQItem(question, answer));

        String emailSubject = "FAQ topic '" + currentSection.getTopic() + "' updated";
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("Updated Q&As:");
        for (FAQItem item : currentSection.getItems()) {
            emailContentBuilder.append("\n\n");
            emailContentBuilder.append("Q: ");
            emailContentBuilder.append(item.getQuestion());
            emailContentBuilder.append("\n");
            emailContentBuilder.append("A: ");
            emailContentBuilder.append(item.getAnswer());
        }
        String emailContent = emailContentBuilder.toString();

        email.sendEmail(
                ((AuthenticatedUser) sharedContext.currentUser).getEmail(),
                SharedContext.ADMIN_STAFF_EMAIL,
                emailSubject,
                emailContent
        );

        view.displaySuccess("Created new FAQ item");
    }

    public void manageInquiries() {
        String[] inquiryTitles = getInquiryTitles(sharedContext.inquiries);

        while (true) {
            view.displayInfo("Pending inquiries");
            int selection = selectFromMenu(inquiryTitles, "Back to main menu");
            if (selection == -1) {
                return;
            }
            Inquiry selectedInquiry = sharedContext.inquiries.get(selection);

            while (true) {
                view.displayDivider();
                view.displayInquiry(selectedInquiry);
                view.displayDivider();
                String[] followUpOptions = { "Redirect inquiry", "Respond to inquiry" };
                int followUpSelection = selectFromMenu(followUpOptions, "Back to all inquiries");

                if (followUpSelection == -1) {
                    break;
                } else if (followUpOptions[followUpSelection].equals("Redirect inquiry")) {
                    redirectInquiry(selectedInquiry);
                } else if (followUpOptions[followUpSelection].equals("Respond to inquiry")) {
                    respondToInquiry(selectedInquiry);
                    inquiryTitles = getInquiryTitles(sharedContext.inquiries); // required to remove responded inquiry from titles
                    break;
                }
            }
        }
    }

    private void redirectInquiry(Inquiry inquiry) {
        inquiry.setAssignedTo(view.getInput("Enter assignee email: "));
        email.sendEmail(
                SharedContext.ADMIN_STAFF_EMAIL,
                inquiry.getAssignedTo(),
                "New inquiry from " + inquiry.getInquirerEmail(),
                "Subject: " + inquiry.getSubject() + "\nPlease log into the Self " +
                        "Service Portal to review and respond to the inquiry."
        );
        view.displaySuccess("Inquiry has been reassigned");
    }

    public void manageCourses(){
        while (true) {
            view.displayInfo("[0] Add course");
            view.displayInfo("[1] Remove course");
            view.displayInfo("[-1] Return to main menu");

            String input = view.getInput("Please choose an option: ");
            try {
                int optionNo = Integer.parseInt(input);

                if (optionNo == 0) {
                    addCourse();
                }else if (optionNo == 1){
                    removeCourse();
                }else if (optionNo == -1) {
                    break;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid option: " + input);
            }
        }

    }

    private void addCourse(){
        view.displayInfo("===Add Course===");
        String courseCode = view.getInput("Enter the course code: ");
        String name = view.getInput("Enter the course name: ");
        String description = view.getInput("Enter the course description: ");
        Boolean requiresComputers = view.getYesNoInput("Does it require computers?");
        String courseOrganiserName = view.getInput("Enter the course organiser's name: ");
        String courseOrganiserEmail = view.getInput("Enter the course organiser's " +
                "email: ");
        String courseSecretaryName = view.getInput("Enter the course secretary's name: ");
        String courseSecretaryEmail = view.getInput("Enter the course secretary's " +
                "email: ");
        String requiredTutorials = view.getInput("Enter the number of required " +
                "tutorials: ");
        String requiredLabs = view.getInput("Enter the number of required labs: ");
        Integer reqTutorials = null;
        Integer reqLabs = null;
        try{
            reqTutorials = Integer.parseInt(requiredTutorials);
        }catch(NumberFormatException e){
            view.displayError("Invalid input: " + requiredTutorials);
        }
        try{
            reqLabs = Integer.parseInt(requiredLabs);
        }catch(NumberFormatException e){
            view.displayError("Invalid input: " + requiredLabs);
        }

        String currentUserEmail = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        boolean added = courseManager.addCourse(courseCode, name, description,
                requiresComputers, courseOrganiserName,courseOrganiserEmail,
                courseSecretaryName, courseSecretaryEmail,reqTutorials, reqLabs);

        if(added){
            email.sendEmail(sharedContext.ADMIN_STAFF_EMAIL, courseOrganiserEmail
                ,"Course Created-" + courseCode, String.format("A course has been " +
                            "provided with the following details: \nCourse code: " +
                            "%s\nCourse name: %s\nDescription: %s\nRequires computers: " +
                            "%s\nCourse organiser's name: %s\nCourse oraganiser's " +
                            "email: %s\nCourse secretary's name: %s\nCourse " +
                            "secretary's email: %s\nNumber of required tutorials: " +
                            "%s\nNumber of required labs: %s", courseCode, name,
                            description, requiresComputers, courseOrganiserName,
                            courseOrganiserEmail, courseSecretaryName,
                            courseSecretaryEmail, reqTutorials, reqLabs));
        }

    }

    private void removeCourse(){}


}
