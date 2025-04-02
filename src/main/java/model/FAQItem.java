package model;

public class FAQItem {
    private final int id;
    private final String question;
    private final String answer;
    private final String tag; 

    public FAQItem(int id, String question, String answer, String tag) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tag = tag;;
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

    public String getTag() {
    return tag;
    }
    public int getId() { return id; }
} 


