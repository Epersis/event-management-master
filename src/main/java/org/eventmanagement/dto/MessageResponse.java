package org.eventmanagement.dto;


import java.io.Serializable;

public class MessageResponse implements Serializable {

    public MessageResponse(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
