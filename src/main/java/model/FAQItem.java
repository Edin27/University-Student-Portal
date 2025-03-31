package model;

public class FAQItem {
    private final String question;
    private final String answer;
    private final String course;

    public FAQItem(String question, String answer, String course) {
        this.question = question;
        this.answer = answer;
        this.course = course;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCourse() { return course; }
}
