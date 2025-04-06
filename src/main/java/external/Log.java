package external;

import model.Lab;
import model.SharedContext;
import view.View;
import org.tinylog.Logger;

public class Log {

    private static String userID = "Guest";
    public static void AddLog(ActionName action, String inputs, Status status) {
        Logger.info(userID + " - " + action + " - " + inputs + " - " + status);
    }

    public static void setUserID(String userID) {
        Log.userID = userID;
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
        ADD_ACTIVITY,
        ADD_COURSE_TO_TIMETABLE,
        CHOOSE_TUTORIAL_OR_lAB,
        VIEW_TIMETABLE,
        REMOVE_COURSE,
        ADD_FAQ,
        CHOOSE_ACTIVITY,
        REMOVE_FAQ,
        // ect, ect
    }
}
