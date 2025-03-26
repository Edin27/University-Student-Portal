package controller;

import external.AuthenticationService;
import external.EmailService;
import external.Log;
import model.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InquirerController extends Controller {
    public InquirerController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void consultFAQ() {
        FAQSection currentSection = null;
        String userEmail;
        if (sharedContext.currentUser instanceof AuthenticatedUser) {
            userEmail = ((AuthenticatedUser) sharedContext.currentUser).getEmail();
        } else {
            userEmail = null;
        }

        int optionNo = 0;
        while (currentSection != null || optionNo != -1) {
            if (currentSection == null) {
                view.displayFAQ(sharedContext.getFAQ());
                view.displayInfo("[-1] Return to main menu");
            } else {
                view.displayFAQSection(currentSection);
                view.displayInfo("[-1] Return to " + (currentSection.getParent() == null ? "FAQ" : currentSection.getParent().getTopic()));
            }

            String input = view.getInput("Please choose an option: ");

            try {
                optionNo = Integer.parseInt(input);

                if (optionNo != -1 && optionNo != -2 && optionNo != -3) {
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

    public void contactStaff() {
        String inquirerEmail;
        if (sharedContext.currentUser instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) sharedContext.currentUser;
            inquirerEmail = user.getEmail();
        } else {
            inquirerEmail = view.getInput("Enter your email address: ");
            // From https://owasp.org/www-community/OWASP_Validation_Regex_Repository
            if (!inquirerEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                view.displayError("Invalid email address! Please try again");
                Log.AddLog(Log.ActionName.CONTACT_STAFF, inquirerEmail, Log.Status.FAILURE);
                return;
            }
        }

        String subject = view.getInput("Describe the topic of your inquiry in a few words: ");
        if (subject.strip().isBlank()) {
            view.displayError("Inquiry subject cannot be blank! Please try again");
            Log.AddLog(Log.ActionName.CONTACT_STAFF, subject, Log.Status.FAILURE);
            return;
        }

        String text = view.getInput("Write your inquiry:" + System.lineSeparator());
        if (text.strip().isBlank()) {
            view.displayError("Inquiry content cannot be blank! Please try again");
            Log.AddLog(Log.ActionName.CONTACT_STAFF, text, Log.Status.FAILURE);
            return;
        }


        String course = view.getInput("Course code (optional): " );
        // No course code given
        if (course.strip().isBlank()) {
            Inquiry inquiry = new Inquiry(inquirerEmail, subject, text);
            sharedContext.inquiries.add(inquiry);
            email.sendEmail(
                    SharedContext.ADMIN_STAFF_EMAIL,
                    SharedContext.ADMIN_STAFF_EMAIL,
                    "New inquiry from " + inquirerEmail,
                    "Subject: " + subject + System.lineSeparator() + "Please log into the Self Service Portal to review and respond to the inquiry."
            );
            view.displaySuccess("Your inquiry has been recorded. Someone will be in touch via email soon!");
            Log.AddLog(Log.ActionName.CONTACT_STAFF, "", Log.Status.SUCCESS);
        } else {
            // Course code is given - find course in course manager
            if (sharedContext.getCourseManager() != null) {
                Iterator<Course> courses = sharedContext.getCourseManager().getCourses().iterator();
                while (courses.hasNext()) {
                    Course tempCourse = courses.next();
                    if (tempCourse.hasCode(course)) {
                        Inquiry inquiry = new Inquiry(inquirerEmail, subject, text);
                        sharedContext.inquiries.add(inquiry);
                        inquiry.setAssignedTo(tempCourse.getCourseOrganiserEmail());
                        email.sendEmail(
                                SharedContext.ADMIN_STAFF_EMAIL,
                                inquiry.getAssignedTo(),
                                "New inquiry from " + inquirerEmail,
                                "Subject: " + subject + System.lineSeparator() + "Please log into the Self Service Portal to review and respond to the inquiry."
                        );
                        view.displaySuccess("Your inquiry has been recorded. Someone will be in touch via email soon!");
                        Log.AddLog(Log.ActionName.CONTACT_STAFF, course, Log.Status.SUCCESS);
                        return;
                    }
                }
            }

            view.displayError("This course code does not exist");
            Log.AddLog(Log.ActionName.CONTACT_STAFF, course, Log.Status.FAILURE);
        }
    }
}
