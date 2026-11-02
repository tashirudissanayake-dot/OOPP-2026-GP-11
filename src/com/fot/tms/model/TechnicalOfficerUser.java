package com.fot.tms.model;

public class TechnicalOfficerUser extends User
    {

    public TechnicalOfficerUser()
    {
        super();
        setRole("TO");
    }

    public TechnicalOfficerUser(int userId, String username, String passwordHash, String email, String status) 
    {
        super(userId, username, passwordHash, "TO", email, status);
    }

    @Override
    public boolean canChangeOwnPassword()
    {

        return false;
    }

    @Override
    public boolean canEditProfileNames() 
    {
        return true;
    }

    @Override
    public String getRoleDisplayName() {
        return "Technical Officer (TO)";
    }
}
