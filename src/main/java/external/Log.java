package external;

import model.SharedContext;
import org.tinylog.Logger;

public class Log {
    public static void AddLog(SharedContext sharedContext, ActionName action, String inputs, Status status) {
        String userID;
        if (sharedContext.getCurrentUserRole() == "Guest") {
            userID = "Guest";
        } else {
            userID = sharedContext.getCurrentUserEmail();
        }
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
        VIEW_COURSES,
        // ect, ect
    }
}
