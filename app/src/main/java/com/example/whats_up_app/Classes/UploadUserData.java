package com.example.whats_up_app.Classes;

public class UploadUserData {

    String fullName , dateOfBarth , profilePicUrl;

    public UploadUserData() {
    }

    public UploadUserData(String fullName, String dateOfBarth, String profilePicUrl) {
        this.fullName = fullName;
        this.dateOfBarth = dateOfBarth;
        this.profilePicUrl = profilePicUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBarth() {
        return dateOfBarth;
    }

    public void setDateOfBarth(String dateOfBarth) {
        this.dateOfBarth = dateOfBarth;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
