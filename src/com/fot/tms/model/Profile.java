package com.fot.tms.model;

import java.io.Serializable;




public class Profile implements Serializable {
    private int profileId;
    private int userId;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String profilePicturePath;

    public Profile() {}

    public Profile(int profileId, int userId, String firstName, String lastName, String contactNo, String profilePicturePath) {
        this.profileId = profileId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.profilePicturePath = profilePicturePath;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo != null ? contactNo : "";
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getProfilePicturePath() {
        return profilePicturePath != null ? profilePicturePath : "";
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getFullName() {
        String fn = getFirstName().trim();
        String ln = getLastName().trim();
        if (fn.isEmpty() && ln.isEmpty()) return "";
        if (fn.isEmpty()) return ln;
        if (ln.isEmpty()) return fn;
        return fn + " " + ln;
    }
}
