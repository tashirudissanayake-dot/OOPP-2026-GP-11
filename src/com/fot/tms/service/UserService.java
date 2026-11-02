package com.fot.tms.service;

import com.fot.tms.dao.ProfileDAO;
import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.Profile;
import com.fot.tms.model.User;
import com.fot.tms.util.ValidationUtil;

import java.util.List;




public class UserService {
    private final UserDAO userDAO;
    private final ProfileDAO profileDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.profileDAO = new ProfileDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public boolean createUser(User user, Profile profile, String plainPassword) {
        if (user == null || !ValidationUtil.isNotEmpty(user.getUsername()) || !ValidationUtil.isNotEmpty(plainPassword)) {
            return false;
        }
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false; 
            
        }
        return userDAO.create(user, profile, plainPassword);
    }

    public boolean updateUser(User user, Profile profile) {
        if (user == null) return false;
        return userDAO.update(user, profile);
    }

    public boolean updateStatus(int userId, String status) {
        return userDAO.updateStatus(userId, status);
    }

    public boolean resetPassword(int userId, String newPassword) {
        if (!ValidationUtil.isNotEmpty(newPassword)) return false;
        return userDAO.resetPassword(userId, newPassword);
    }

    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }

    public boolean updateContactAndPhoto(int userId, String contactNo, String photoPath) {
        return profileDAO.updateContactAndPhotoOnly(userId, contactNo, photoPath);
    }

    public boolean updateProfile(Profile profile) {
        return profileDAO.updateProfile(profile);
    }
}
