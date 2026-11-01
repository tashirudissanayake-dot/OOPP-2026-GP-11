package com.fot.tms.ui;

import com.fot.tms.model.User;
import com.fot.tms.service.AuthService;
import com.fot.tms.ui.admin.AdminDashboardFrame;
import com.fot.tms.ui.lecturer.LecturerDashboardFrame;
import com.fot.tms.ui.to.TODashboardFrame;
import com.fot.tms.ui.undergraduate.UndergraduateDashboardFrame;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnClear;
    private JComboBox<String> cmbQuickFill;

    public LoginFrame() {
        setTitle("Faculty of Technology Management System (FoT-TMS) - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel container = new JPanel(new GridLayout(1, 2));


        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(UIConstants.PRIMARY_COLOR);
        leftPanel.setLayout(new BorderLayout(20, 20));
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel brandContent = new JPanel();
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        brandContent.setOpaque(false);

        JLabel lblUniv = new JLabel("UNIVERSITY OF RUHUNA");
        lblUniv.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUniv.setForeground(UIConstants.ACCENT_COLOR);

        JLabel lblFaculty = new JLabel("Faculty of Technology");
        lblFaculty.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblFaculty.setForeground(Color.WHITE);

        JLabel lblSys = new JLabel("Management System (FoT-TMS)");
        lblSys.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSys.setForeground(new Color(203, 213, 225));

        JLabel lblDesc = new JLabel("<html><br>Course: ICT2132 - Object Oriented Programming Practicum<br>" +
                "Compliant with UGC Circular No. 12-2024.<br><br>" +
            "• Role-Based Access Control (RBAC)<br>" +
            "• 15 Theory & 15 Practical Attendance Engine<br>" +
            "• Continuous Assessment & Exam Eligibility<br>" +
            "• SGPA & CGPA Calculation Platform</html>");
        lblDesc.setFont(UIConstants.FONT_REGULAR);
        lblDesc.setForeground(new Color(226, 232, 240));

        brandContent.add(lblUniv);
        brandContent.add(Box.createRigidArea(new Dimension(0, 10)));
        brandContent.add(lblFaculty);
        brandContent.add(lblSys);
        brandContent.add(Box.createRigidArea(new Dimension(0, 15)));
        brandContent.add(lblDesc);

        JLabel lblFooter = new JLabel("Group G11 - University of Ruhuna © 2026");
        lblFooter.setFont(UIConstants.FONT_SMALL);
        lblFooter.setForeground(new Color(148, 163, 184));

        leftPanel.add(brandContent, BorderLayout.CENTER);
        leftPanel.add(lblFooter, BorderLayout.SOUTH);


        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(UIConstants.BG_MAIN);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(35, 45, 35, 45));

        JPanel formCard = UIHelper.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblLogin = new JLabel("Account Login");
        lblLogin.setFont(UIConstants.FONT_TITLE);
        lblLogin.setForeground(UIConstants.TEXT_PRIMARY);
        formCard.add(lblLogin, gbc);

        gbc.gridy = 1;
        JLabel lblPrompt = new JLabel("Enter your credentials to access your dashboard.");
        lblPrompt.setFont(UIConstants.FONT_REGULAR);
        lblPrompt.setForeground(UIConstants.TEXT_MUTED);
        formCard.add(lblPrompt, gbc);


        gbc.gridy = 2;
        JPanel quickPanel = new JPanel(new BorderLayout(5, 5));
        quickPanel.setOpaque(false);
        quickPanel.add(new JLabel("Quick Demo User:"), BorderLayout.NORTH);
        cmbQuickFill = new JComboBox<>(new String[]{
            "-- Select Demo Account --",
            "Admin (admin / admin123)",
            "Lecturer (lec_sunil / password123)",
            "Technical Officer (to_nuwan / password123)",
            "Undergraduate - Dilshan (TG/2024/2109 / password123)",
            "Undergraduate - Tashiru (TG/2024/2110 / password123)",
            "Undergraduate - Sithum (TG/2024/2146 / password123)",
            "Undergraduate - Tharuka (TG/2024/2079 / password123)",
            "Repeat Student (TG/2023/1850 / password123)",
            "Batch-Missed Student (TG/2022/1520 / password123)"
        });
        cmbQuickFill.setFont(UIConstants.FONT_SMALL);
        cmbQuickFill.addActionListener(e -> applyQuickFill());
        quickPanel.add(cmbQuickFill, BorderLayout.CENTER);
        formCard.add(quickPanel, gbc);

        gbc.gridwidth = 1;


        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblUser = new JLabel("Username / Reg No:");
        lblUser.setFont(UIConstants.FONT_BOLD);
        formCard.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = UIHelper.createStyledTextField(18);
        formCard.add(txtUsername, gbc);


        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(UIConstants.FONT_BOLD);
        formCard.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = UIHelper.createStyledPasswordField(18);
        formCard.add(txtPassword, gbc);


        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setOpaque(false);

        btnClear = UIHelper.createSecondaryButton("Clear");
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            cmbQuickFill.setSelectedIndex(0);
        });

        btnLogin = UIHelper.createPrimaryButton("Sign In");
        btnLogin.setPreferredSize(new Dimension(110, 36));
        btnLogin.addActionListener(e -> performLogin());

        btnPanel.add(btnClear);
        btnPanel.add(btnLogin);
        formCard.add(btnPanel, gbc);


        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterListener);
        txtPassword.addKeyListener(enterListener);

        rightPanel.add(formCard, BorderLayout.CENTER);

        container.add(leftPanel);
        container.add(rightPanel);
        setContentPane(container);
    }

    private void applyQuickFill() {
        int idx = cmbQuickFill.getSelectedIndex();
        switch (idx) {
            case 1:
                txtUsername.setText("admin");
                txtPassword.setText("admin123");
                break;
            case 2:
                txtUsername.setText("lec_sunil");
                txtPassword.setText("password123");
                break;
            case 3:
                txtUsername.setText("to_nuwan");
                txtPassword.setText("password123");
                break;
            case 4:
                txtUsername.setText("TG/2024/2109");
                txtPassword.setText("password123");
                break;
            case 5:
                txtUsername.setText("TG/2024/2110");
                txtPassword.setText("password123");
                break;
            case 6:
                txtUsername.setText("TG/2024/2146");
                txtPassword.setText("password123");
                break;
            case 7:
                txtUsername.setText("TG/2024/2079");
                txtPassword.setText("password123");
                break;
            case 8:
                txtUsername.setText("TG/2023/1850");
                txtPassword.setText("password123");
                break;
            case 9:
                txtUsername.setText("TG/2022/1520");
                txtPassword.setText("password123");
                break;
            default:
                break;
        }
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            UIHelper.showWarning(this, "Please enter both username and password.", "Login Required");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Verifying...");

        SwingUtilities.invokeLater(() -> {
            try {
                User user = AuthService.getInstance().login(username, password);
                if (user != null) {
                    openDashboardForUser(user);
                    dispose();
                } else {
                    UIHelper.showError(LoginFrame.this,
                            "Invalid credentials or inactive account.\nPlease verify your username and password.",
                            "Authentication Failed");
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Sign In");
                }
            } catch (Exception ex) {
                UIHelper.showError(LoginFrame.this, "System error during authentication: " + ex.getMessage(), "Error");
                btnLogin.setEnabled(true);
                btnLogin.setText("Sign In");
            }
        });
    }

    private void openDashboardForUser(User user) {
        String role = user.getRole();
        if ("Admin".equalsIgnoreCase(role)) {
            new AdminDashboardFrame().setVisible(true);
        } else if ("Lecturer".equalsIgnoreCase(role)) {
            new LecturerDashboardFrame().setVisible(true);
        } else if ("TO".equalsIgnoreCase(role) || "Technical Officer".equalsIgnoreCase(role)) {
            new TODashboardFrame().setVisible(true);
        } else if ("Undergraduate".equalsIgnoreCase(role) || "Student".equalsIgnoreCase(role)) {
            new UndergraduateDashboardFrame().setVisible(true);
        } else {
            UIHelper.showError(this, "Unknown user role: " + role, "Access Error");
        }
    }
}
