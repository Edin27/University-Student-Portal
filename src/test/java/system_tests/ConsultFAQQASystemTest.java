package system_tests;

import controller.InquirerController;
import external.AuthenticationService;
import external.EmailService;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.FAQItem;
import model.FAQSection;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;

public class ConsultFAQQASystemTest extends TUITest {

    FAQSection faqSection1 = new FAQSection("Topic1");
    boolean a = faqSection1.getItems().add(new FAQItem(1, "Question1", "Answer1", "course1"));
    boolean b = faqSection1.getItems().add(new FAQItem(2, "Question2", "Answer2", ""));

    FAQSection faqSection2 = new FAQSection("Topic2");
    boolean c = faqSection1.getItems().add(new FAQItem(1, "Question1", "Answer1", "course1"));
    boolean d = faqSection1.getItems().add(new FAQItem(2, "Question2", "Answer2", "course1"));

    @Test
    @DisplayName("Consult the faq with no tag filter")
    public void testConsultFAQNoFilter() throws URISyntaxException, IOException, ParseException {
        setMockInput("", "0", "-1", "-1");
        SharedContext sharedContext = new SharedContext(new TextUserInterface());
        InquirerController inquirerController = new InquirerController(sharedContext, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        sharedContext.getFAQ().addSection(faqSection1);
        startOutputCapture();
        inquirerController.consultFAQ();
        assertOutputContains("0) [Course: course1]\n" + "Question1\n" + "> Answer1");
        assertOutputContains("1) [Course: None]\n" + "Question2\n" + "> Answer2");
    }

    @Test
    @DisplayName("Consult the faq with a tag filter")
    public void testConsultFAQFilter() throws URISyntaxException, IOException, ParseException {
        setMockInput("course1", "0", "-1", "-1");
        SharedContext sharedContext = new SharedContext(new TextUserInterface());
        InquirerController inquirerController = new InquirerController(sharedContext, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        sharedContext.getFAQ().addSection(faqSection1);
        startOutputCapture();
        inquirerController.consultFAQ();
        assertOutputContains("Question1\n" + "> Answer1\n" + "Subsections:\n" + "[-1]");
    }

    @Test
    @DisplayName("Consult the faq with a tag filter that empties topic")
    public void testConsultFAQFilterEmpty() throws URISyntaxException, IOException, ParseException {
        setMockInput("course1", "0", "-1", "-1");
        SharedContext sharedContext = new SharedContext(new TextUserInterface());
        InquirerController inquirerController = new InquirerController(sharedContext, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        sharedContext.getFAQ().addSection(faqSection2);
        startOutputCapture();
        inquirerController.consultFAQ();
        assertOutputContains("No faq items of this tag!");
    }
}
