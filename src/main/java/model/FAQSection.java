package model;

import java.util.LinkedList;
import java.util.List;

public class FAQSection {
    private final String topic;
    private final List<FAQItem> items = new LinkedList<>();
    private FAQSection parent;
    private final List<FAQSection> subsections = new LinkedList<>();

    public FAQSection(String topic) {
        this.topic = topic;
    }

    public void addSubsection(FAQSection section) {
        subsections.add(section);
        section.parent = this;
    }

    public void removeSubsection(FAQSection section) {
        for (FAQSection subsection : subsections) {
            addSubsection(subsection);
        }
        subsections.remove(section);
    }

    public boolean removeItem(int id) {
        for(FAQItem item : items) {
            if (item.getId() == id) {
                items.remove(item);
                return true;
            }
        }
        return false;
    }

    public List<FAQSection> getSubsections() {
        return subsections;
    }

    public String getTopic() {
        return topic;
    }

    public List<FAQItem> getItems() {
        return items;
    }

    public FAQSection getParent() {
        return parent;
    }

    public void setParent(FAQSection parent) {
        this.parent = parent;
    }
}
