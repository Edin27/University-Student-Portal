package system_tests;

import controller.MenuController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.FAQItem;
import model.FAQSection;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;

import java.io.IOException;
import java.net.URISyntaxException;

public class RemoveFAQQAPairSystemTests extends TUITest{

    static FAQSection faqSection1 = null;
    static FAQSection faqSubSection1 = null;
    static FAQSection faqSection2 = null;

    @BeforeAll
    static void setUp() {
        faqSection1 = new FAQSection("Topic1");
        faqSection1.getItems().add(new FAQItem(1, "Question1", "Answer1", "course1"));
        faqSection1.getItems().add(new FAQItem(2, "Question2", "Answer2", ""));

        faqSubSection1 = new FAQSection("Topic1.1");
        faqSubSection1.getItems().add(new FAQItem(3, "Question3", "Answer3", "course1"));
        faqSubSection1.getItems().add(new FAQItem(4, "Question4", "Answer4", "course2"));
        faqSection1.addSubsection(faqSubSection1);

        faqSection2 = new FAQSection("Topic2");
        faqSection2.getItems().add(new FAQItem(5, "Question5", "Answer5", "course1"));
        faqSection2.getItems().add(new FAQItem(6, "Question6", "Answer6", "course1"));
    }

    @Test
    @DisplayName("remove a single item successfully")
    public void testRemoveItem() throws URISyntaxException, IOException, ParseException, NullPointerException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getFAQ().addSection(faqSection1);context.getFAQ().addSection(faqSection2);
        setMockInput("0", "admin1", "admin1pass", "2", "0", "-3", "1", "-1", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("FAQ item removed.");
    }

    @Test
    @DisplayName("remove a section with a subsection")
    public void testRemoveSection() throws URISyntaxException, IOException, ParseException, NullPointerException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getFAQ().addSection(faqSection1);context.getFAQ().addSection(faqSection2);
        setMockInput("0", "admin1", "admin1pass", "2", "0", "-3", "1","-3", "2", "-1", "-1", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("FAQ item removed.");
        assertOutputContains("[1] Topic1.1");
    }

    @Test
    @DisplayName("No item found with itemId")
    public void testNoItemFound() throws URISyntaxException, IOException, ParseException, NullPointerException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getFAQ().addSection(faqSection1);context.getFAQ().addSection(faqSection2);
        setMockInput("0", "admin1", "admin1pass", "2", "0", "-3", "10", "-1", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("No item found with ID 10");
    }

    @Test
    @DisplayName("Item id with invalid format")
    public void testInvalidID() throws URISyntaxException, IOException, ParseException, NullPointerException {
        SharedContext context = new SharedContext(new TextUserInterface());
        context.getFAQ().addSection(faqSection1);context.getFAQ().addSection(faqSection2);
        setMockInput("0", "admin1", "admin1pass", "2", "0", "-3", "%#", "-1", "-1", "-1");
        MenuController menuController = new MenuController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Invalid ID: %#");
    }
}
