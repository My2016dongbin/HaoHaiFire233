package com.ruyiruyi.rylibrary.bus;

public class MessagePush {
    String message;

    public MessagePush() {
    }

    public MessagePush(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
