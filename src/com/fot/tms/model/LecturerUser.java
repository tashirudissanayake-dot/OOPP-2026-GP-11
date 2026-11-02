package com.fot.tms.model;




public class LecturerUser extends User {

    public LecturerUser() {
        super();
        setRole("Lecturer");
    }

    public LecturerUser(int userId, String username, String passwordHash, String email, String status) {
        super(userId, username, passwordHash, "Lecturer", email, status);
    }

    @Override
    public boolean canChangeOwnPassword() {


        return false;
    }

    @Override
    public boolean canEditProfileNames() {
        return true;
    }

    @Override
    public String getRoleDisplayName() {
        return "Lecturer / Academic Staff";
    }
}
