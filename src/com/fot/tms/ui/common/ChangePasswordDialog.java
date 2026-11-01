package com.fot.tms.ui.common;

import com.fot.tms.model.User;
import com.fot.tms.service.UserService;
import com.fot.tms.util.PasswordUtil;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;
import com.fot.tms.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;



public class ChangePasswordDialog extends JDialog {
    private final User user;
    private final UserService userService;
    private JPasswordField txtCurrentPass;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;

    public ChangePasswordDialog(Frame parent, User user) {
        super(parent, "Change Password", true);
        this.user = user;
        this.userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setSize(420, 320);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        txtCurrentPass = UIHelper.createStyledPasswordField(15);
        mainPanel.add(txtCurrentPass, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        txtNewPass = UIHelper.createStyledPasswordField(15);
        mainPanel.add(txtNewPass, gbc);


        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        txtConfirmPass = UIHelper.createStyledPasswordField(15);
        mainPanel.add(txtConfirmPass, gbc);

        add(mainPanel, BorderLayout.CENTER);


        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UIConstants.BG_TABLE_HEADER);

        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        JButton btnUpdate = UIHelper.createPrimaryButton("Update Password");
        btnUpdate.addActionListener(e -> performPasswordChange());

        btnPanel.add(btnCancel);
        btnPanel.add(btnUpdate);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void performPasswordChange() {
        String currentPass = new String(txtCurrentPass.getPassword()).trim();
        String newPass = new String(txtNewPass.getPassword()).trim();
        String confirmPass = new String(txtConfirmPass.getPassword()).trim();

        if (!ValidationUtil.isNotEmpty(currentPass) || !ValidationUtil.isNotEmpty(newPass)) {
            UIHelper.showWarning(this, "Please fill in all password fields.", "Missing Information");
            return;
        }


        User fresh = userService.getUserById(user.getUserId());
        if (fresh == null || !PasswordUtil.verifyPassword(currentPass, fresh.getPasswordHash())) {
            UIHelper.showError(this, "The current password entered is incorrect.", "Authentication Failed");
            return;
        }

        if (newPass.length() < 6) {
            UIHelper.showWarning(this, "New password must be at least 6 characters long.", "Weak Password");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            UIHelper.showWarning(this, "The new password and confirmation do not match.", "Password Mismatch");
            return;
        }

        boolean success = userService.resetPassword(user.getUserId(), newPass);
        if (success) {
            UIHelper.showInfo(this, "Password has been updated successfully!", "Success");
            dispose();
        } else {
            UIHelper.showError(this, "Failed to update password. Please try again.", "Error");
        }
    }
}
