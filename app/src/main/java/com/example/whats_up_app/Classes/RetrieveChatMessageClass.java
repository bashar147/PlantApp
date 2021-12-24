package com.example.whats_up_app.Classes;

public class RetrieveChatMessageClass {
    private String from , message , time;

    public RetrieveChatMessageClass() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public RetrieveChatMessageClass(String from, String message, String time) {
        this.from = from;
        this.message = message;
        this.time = time;
    }
}
