package com.fot.tms.ui.admin;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class AdminDashboardFrame extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(24, 43, 73);
    private static final Color PRIMARY_LIGHT = new Color(37, 99, 235);
    private static final Color ACCENT_COLOR = new Color(14, 165, 233);
    private static final Color ACCENT_HOVER = new Color(2, 132, 199);
    private static final Color SUCCESS_COLOR = new Color(16, 185, 129);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color INFO_COLOR = new Color(59, 130, 246);
    private static final Color BG_MAIN = new Color(248, 250, 252);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color BG_SIDEBAR = new Color(15, 23, 42);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);

    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 28);



public AdminDashboardFrame() {
        setTitle("Faculty of Technology Management System - Administrator Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1664, 960);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        initComponents();
}




    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.add(createSidebar(), BorderLayout.WEST);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(createTopHeader(), BorderLayout.NORTH);
        centerContainer.add(createOverviewPanel(), BorderLayout.CENTER);
        root.add(centerContainer, BorderLayout.CENTER);
        setContentPane(root);
    }



    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(263, 900));
        sidebar.setBorder(new EmptyBorder(18, 18, 18, 18));


        JLabel lblBrand = new JLabel("FoT-TMS Admin");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("System Overseer Portal");
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(ACCENT_COLOR);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));





        sidebar.add(createNavButton(" Dashboard Overview"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Manage Users"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Course Catalog"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Faculty Timetables"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Manage Notices"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Batch Reports & GPA"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton(" Admin Profile"));

        sidebar.add(Box.createVerticalGlue());
        JButton btnLogout = new JButton(" Sign Out");
        btnLogout.setFont(FONT_BOLD);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(239, 68, 68));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new EmptyBorder(10, 15, 10, 15));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> dispose());
        sidebar.add(btnLogout);
        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 1, 10, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {

        });
        return btn;
    }

    private JPanel createTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 27, 12, 25)
        ));

        JLabel lblWelcome = new JLabel("Welcome, System Administrator (Role: Administrator)");
        lblWelcome.setFont(FONT_HEADER);
        lblWelcome.setForeground(TEXT_PRIMARY);

        JLabel lblDate = new JLabel("Faculty of Technology • University of Ruhuna");
        lblDate.setFont(FONT_SMALL);
        lblDate.setForeground(TEXT_MUTED);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(lblDate, BorderLayout.EAST);
        return header;
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(27, 27, 15, 17));

        JPanel statsGrid = new JPanel(new GridLayout(2, 4, 16, 15));
        statsGrid.setOpaque(false);

        statsGrid.add(createStatCard("Total Undergraduates", "20", PRIMARY_LIGHT));//values are in as example .need to display using calculation
        statsGrid.add(createStatCard("Lecturers Assigned", "5", SUCCESS_COLOR));
        statsGrid.add(createStatCard("Technical Officers", "4", ACCENT_COLOR));
        statsGrid.add(createStatCard("System Admins", "1", WARNING_COLOR));
        statsGrid.add(createStatCard("Active Courses", "9", PRIMARY_COLOR));
        statsGrid.add(createStatCard("Timetable Sessions", "10", INFO_COLOR));
        statsGrid.add(createStatCard("Published Notices", "5", ACCENT_HOVER));
        statsGrid.add(createStatCard("UGC Circular", "12-2024", SUCCESS_COLOR));
        panel.add(statsGrid, BorderLayout.NORTH);



        JPanel infoCard = new JPanel(new BorderLayout(15, 15));
        infoCard.setBackground(BG_CARD);
        infoCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 17, 16, 17)
        ));

        JLabel lblSys = new JLabel("System Operational Status & Compliance Matrix");
        lblSys.setFont(FONT_HEADER);
        lblSys.setForeground(TEXT_PRIMARY);

        JTextArea txtSysInfo = new JTextArea(
                " Multi-tier desktop application: Presentation Layer (Swing), Business Logic Layer, DAO Layer (MySQL).\n" +
                " Database Baseline Supported: 1 Admin, 5 Lecturers, 4 Technical Officers, 20 Undergraduates (FR-06).\n" +
                " Session Engine: 15 Theory & 15 Practical sessions tracking per module (FR-11).\n" +
                " Attendance Criteria: Raw vs Effective percentage (>80%, 80%, <80% with/without medicals) (FR-15, FR-16).\n" +
                " Evaluation Matrix: CA Total >= 40% (FR-19) & Combined Eligibility check >= 80% attendance + CA (FR-20).\n" +
                " Grading Standard: Full UGC Commission Circular No. 12-2024 Scale with SGPA/CGPA computation (FR-22, FR-23).\n" +
                " Security: SHA-256 cryptographic password hashing (NFR-04) & Parameterized SQL queries (NFR-05)."
        );
        txtSysInfo.setFont(FONT_REGULAR);
        txtSysInfo.setEditable(false);
        txtSysInfo.setOpaque(false);
        txtSysInfo.setLineWrap(true);
        txtSysInfo.setWrapStyleWord(true);

        infoCard.add(lblSys, BorderLayout.NORTH);
        infoCard.add(txtSysInfo, BorderLayout.CENTER);
        panel.add(infoCard, BorderLayout.CENTER);
        return panel;
    }




    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(14, 20, 14, 18)
        ));



        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SMALL);
        lblTitle.setForeground(TEXT_MUTED);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_CARD_VALUE);
        lblValue.setForeground(accentColor);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }







    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardFrame().setVisible(true));
    }














}
