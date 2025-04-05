package model;

import view.View;

import java.util.*;

public class SharedContext {
    protected final View view;
    public static final String ADMIN_STAFF_EMAIL = "inquiries@hindeburg.ac.nz";
    public User currentUser;
    public final List<Inquiry> inquiries;
    public final FAQ faq;
    public final CourseManager courseManager;


    public SharedContext(View view) {
        this.view = view;
        this.currentUser = new Guest();
        this.inquiries = new ArrayList<>();
        faq = new FAQ();
        courseManager = new CourseManager(view);
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
