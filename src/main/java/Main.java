import external.*;
import model.SharedContext;
import controller.MenuController;
import org.json.simple.parser.ParseException;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        View view = new TextUserInterface();
        try {
            AuthenticationService auth = new MockAuthenticationService();
            EmailService email = new MockEmailService();
            SharedContext sharedContext = new SharedContext(view);
            MenuController menus = new MenuController(sharedContext, view, auth, email);
            menus.mainMenu();
        } catch (URISyntaxException | IOException | ParseException | NullPointerException e) {
            view.displayException(e);
        }
    }
}
