package model;

public class FAQItem {
    private final String question;
    private final String answer;

    public FAQItem(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return id + ". Q: " + question + " A: " + answer;
    }
} 


