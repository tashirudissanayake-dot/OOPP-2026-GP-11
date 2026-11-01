package com.fot.tms.service;

import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.User;




public class AuthService {
    private static volatile AuthService instance;
    private final UserDAO userDAO;
    private User currentUser;

    private AuthService() {
        this.userDAO = new UserDAO();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        User user = userDAO.authenticate(username.trim(), password);
        if (user != null && "Active".equalsIgnoreCase(user.getStatus())) {
            this.currentUser = user;
            return user;
        }
        return null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().equalsIgnoreCase(role);
    }
}
