package com.example.whats_up_app.Classes;

public class RetriveFriendSearchClass {

    private String userName ;
    private String sentDate;

    public RetriveFriendSearchClass() {
    }

    public RetriveFriendSearchClass(String userName, String sentDate) {
        this.userName = userName;
        this.sentDate = sentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }
}

