package external;

import model.Lab;
import model.SharedContext;
import org.tinylog.Logger;

public class Log {
    public static void AddLog(String mail, ActionName action, String inputs, Status status) {
        if (mail == null) {mail = "Guest";}
        // adds line to log file
        Logger.info(mail + " - " + action + " - " + inputs + " - " + status);
    }

    public enum Status {
        SUCCESS,
        FAILURE
    }

    public enum ActionName {
        LOGIN,
        LOGOUT,
        CONSULT_FAQ,
        CONTACT_STAFF,
        MANAGE_RECEIVED_QUERIES,
        VIEW_COURSES,
        ADD_COURSE,
        ADD_COURSE_TO_TIMETABLE,
        CHOOSE_TUTORIAL_OR_lAB,
        VIEW_TIMETABLE,
        REMOVE_COURSE,
        ADD_FAQ,
        REMOVE_FAQ,
        // ect, ect
    }
}
