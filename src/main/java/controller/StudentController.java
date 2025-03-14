package controller;

import external.AuthenticationService;
import external.EmailService;
import model.AuthenticatedUser;
import model.Inquiry;
import model.SharedContext;
import view.View;

import java.util.List;

public class StudentController extends Controller {
    public StudentController(SharedContext sharedContext, View view,
                             AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }
}
