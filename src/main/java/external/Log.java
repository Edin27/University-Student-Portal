package external;

import model.Lab;
import model.SharedContext;
import view.View;
import org.tinylog.Logger;

public class Log {
    private static String userID = "Guest";

    /**
     * uses the tinylog library to add a line/log to the file log.txt
     * @param action names the action that is logged
     * @param inputs the user input that led to the log
     * @param status is this a log of a system success or failure
     */
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
