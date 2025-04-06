package system_tests;

import controller.InquirerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.FAQItem;
import model.FAQSection;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;

import java.io.IOException;
import java.net.URISyntaxException;

public class ConsultFAQQASystemTests extends TUITest {

    FAQSection faqSection1 = new FAQSection("Topic1");
    boolean a = faqSection1.getItems().add(new FAQItem(1, "Question1", "Answer1", "course1"));
    boolean b = faqSection1.getItems().add(new FAQItem(2, "Question2", "Answer2", ""));

    FAQSection faqSection2 = new FAQSection("Topic2");
    boolean c = faqSection2.getItems().add(new FAQItem(1, "Question1", "Answer1", "course1"));
    boolean d = faqSection2.getItems().add(new FAQItem(2, "Question2", "Answer2", "course1"));

    @Test
    @DisplayName("Consult the faq with no tag filter")
    public void testConsultFAQNoFilter() throws URISyntaxException, IOException, ParseException {
        setMockInput("", "0", "-1", "-1");
        SharedContext sharedContext = new SharedContext(new TextUserInterface());
        InquirerController inquirerController = new InquirerController(sharedContext, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        sharedContext.getFAQ().addSection(faqSection1);
        startOutputCapture();
        inquirerController.consultFAQ();
        assertOutputContains("Question1");
        assertOutputContains("Question2");
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
        assertOutputContains("Question1");
    }

    @Test
    @DisplayName("Consult the faq with a tag filter that empties topic")
    public void testConsultFAQFilterEmpty() throws URISyntaxException, IOException, ParseException {
        setMockInput("course2", "0", "-1", "-1");
        SharedContext sharedContext = new SharedContext(new TextUserInterface());
        InquirerController inquirerController = new InquirerController(sharedContext, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        sharedContext.getFAQ().addSection(faqSection2);
        startOutputCapture();
        inquirerController.consultFAQ();
        assertOutputContains("No faq items of this tag!");
    }
}
