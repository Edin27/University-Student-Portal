package model;

public class FAQItem {
    private final String question;
    private final String answer;
    private final String course;
    private final int id;

    public FAQItem(String question, String answer, String course, int id) {
        this.question = question;
        this.answer = answer;
        this.course = course;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCourse() { return course; }

    public int getId() { return id; }
}
