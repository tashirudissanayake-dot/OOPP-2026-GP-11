package com.fot.tms.model;




public class UndergraduateUser extends User {

    public UndergraduateUser() {
        super();
        setRole("Undergraduate");
    }

    public UndergraduateUser(int userId, String username, String passwordHash, String email, String status) {
        super(userId, username, passwordHash, "Undergraduate", email, status);
    }

    @Override
    public boolean canChangeOwnPassword() {

        return false;
    }

    @Override
    public boolean canEditProfileNames() {

        return false;
    }

    @Override
    public String getRoleDisplayName() {
        return "Undergraduate Student";
    }
}
