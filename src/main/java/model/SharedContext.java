package model;

import java.util.*;

public class SharedContext {
    public static final String ADMIN_STAFF_EMAIL = "inquiries@hindeburg.ac.nz";
    public User currentUser;

    public final List<Inquiry> inquiries;
    public final FAQ faq;

    public final CourseManager courseManager;

    public SharedContext() {
        this.currentUser = new Guest();
        this.inquiries = new ArrayList<>();
        faq = new FAQ();
        courseManager = CourseManager.getCourseManager();
    }

    public FAQ getFAQ() {
        return faq;
    }

    public String getCurrentUserRole(){
        String userRole = null;
        if(currentUser instanceof Guest){
            userRole = "Guest";
        }
        else if(currentUser instanceof AuthenticatedUser){
            userRole = ((AuthenticatedUser) currentUser).getRole();
        }
        return userRole;
    }

    public String getCurrentUserEmail(){
        String userEmail = null;
       if(currentUser instanceof AuthenticatedUser){
            userEmail = ((AuthenticatedUser) currentUser).getEmail();
        }
        return userEmail;
    }

    public CourseManager getCourseManager(){
        return courseManager;
    }
}
