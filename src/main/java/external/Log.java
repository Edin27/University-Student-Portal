package external;

import model.SharedContext;
import org.tinylog.Logger;

public class Log {
    private static String userID = "Guest";
    public static void AddLog(ActionName action, String inputs, Status status) {

        // adds line to log file
        Logger.info(userID + " - " + action + " - " + inputs + " - " + status);
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
        ADD_COURSE_TO_TIMETABLE,
        VIEW_COURSES,
        CHOOSE_TUTORIAL_OR_lAB,
        ADD_COURSE,
        // ect, ect
    }
}
