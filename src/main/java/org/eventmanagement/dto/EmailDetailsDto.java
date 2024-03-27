package org.eventmanagement.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EmailDetailsDto implements Serializable {

    private String to;

    private String subject;

    private String templateName;

    Map<String, Object> model = new HashMap<>();

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
