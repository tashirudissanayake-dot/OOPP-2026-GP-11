package com.fot.tms.ui.to;

import com.fot.tms.model.Attendance;
import com.fot.tms.model.Timetable;
import com.fot.tms.model.User;
import com.fot.tms.service.AttendanceService;
import com.fot.tms.service.AuthService;
import com.fot.tms.service.TimetableService;
import com.fot.tms.service.UserService;
import com.fot.tms.ui.LoginFrame;
import com.fot.tms.ui.common.NoticesPanel;
import com.fot.tms.ui.common.ProfilePanel;
import com.fot.tms.ui.common.TimetablePanel;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;
import com.fot.tms.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Technical Officer (TO) Dashboard Frame for FoT-TMS.
 * Pure Java Swing interface for Support Personnel (FR-04, FR-10, FR-11, FR-12, FR-13).
 */
public class TODashboardFrame extends JFrame {
    private final AttendanceService attendanceService;
    private final TimetableService timetableService;
    private final UserService userService;
    private final User currentUser;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Attendance Log components
    private JComboBox<Timetable> cmbTimetable;
    private JComboBox<Integer> cmbSessionNumber;
    private JSpinner spinSessionDate;
    private DefaultTableModel attendanceTableModel;
    private JTable attendanceTable;

    // Medical Log components
    private DefaultTableModel medicalsTableModel;
    private JTable medicalsTable;

    public TODashboardFrame() {
        this.attendanceService = new AttendanceService();
        this.timetableService = new TimetableService();
        this.userService = new UserService();
        this.currentUser = AuthService.getInstance().getCurrentUser();

        setTitle("Faculty of Technology Management System - Technical Officer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // Center Container
        JPanel centerContainer = new JPanel(new BorderLayout());
        JPanel topHeader = createTopHeader();
        centerContainer.add(topHeader, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIConstants.BG_MAIN);

        // Add views
        mainContentPanel.add(createOverviewPanel(), "OVERVIEW");
        mainContentPanel.add(createDailyAttendancePanel(), "ATTENDANCE");
        mainContentPanel.add(createMedicalSubmissionPanel(), "MEDICALS");
        mainContentPanel.add(new TimetablePanel(), "TIMETABLE");
        mainContentPanel.add(new NoticesPanel(), "NOTICES");
        mainContentPanel.add(new ProfilePanel(), "PROFILE");

        centerContainer.add(mainContentPanel, BorderLayout.CENTER);
        root.add(centerContainer, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIConstants.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        JLabel lblBrand = new JLabel("FoT-TMS Support");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Technical Officer Portal");
        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(UIConstants.ACCENT_COLOR);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createNavButton("📊 Dashboard Overview", "OVERVIEW"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("📝 Log Session Attendance", "ATTENDANCE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("🏥 Submit Medical Records", "MEDICALS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("📅 Department Timetable", "TIMETABLE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("📢 Notices", "NOTICES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("👤 TO Profile", "PROFILE"));

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = UIHelper.createDangerButton("🚪 Sign Out");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(210, 38));
        btnLogout.addActionListener(e -> logout());
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BOLD);
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(UIConstants.BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, cardName);
            if ("ATTENDANCE".equals(cardName)) loadAttendanceData();
            if ("MEDICALS".equals(cardName)) loadMedicalsData();
        });
        return btn;
    }

    private JPanel createTopHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(12, 25, 12, 25)
        ));

        String name = currentUser != null ? currentUser.getFullName() : "Technical Officer";
        JLabel lblWelcome = new JLabel("Welcome, " + name + " (Role: Technical Officer)");
        lblWelcome.setFont(UIConstants.FONT_HEADER);
        lblWelcome.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel lblInfo = new JLabel("Department of Information and Communication Technology");
        lblInfo.setFont(UIConstants.FONT_SMALL);
        lblInfo.setForeground(UIConstants.TEXT_MUTED);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(lblInfo, BorderLayout.EAST);
        return header;
    }

    // ==========================================
    // 1. OVERVIEW PANEL
    // ==========================================
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 15, 15));
        statsGrid.setOpaque(false);

        List<Timetable> deptTt = timetableService.getTimetablesByDepartment("Information and Communication Technology");
        List<Attendance> allMeds = attendanceService.getAllMedicals();
        List<User> students = userService.getUsersByRole("Undergraduate");

        statsGrid.add(UIHelper.createStatCard("Registered Undergraduates", String.valueOf(students.size()), UIConstants.PRIMARY_LIGHT));
        statsGrid.add(UIHelper.createStatCard("Department Lab Sessions", String.valueOf(deptTt.size()), UIConstants.INFO_COLOR));
        statsGrid.add(UIHelper.createStatCard("Total Medical Submissions", String.valueOf(allMeds.size()), UIConstants.WARNING_COLOR));

        panel.add(statsGrid, BorderLayout.NORTH);

        JPanel infoCard = UIHelper.createCardPanel();
        infoCard.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Technical Officer Operational Responsibilities (UC-T1 to UC-T4)");
        lblTitle.setFont(UIConstants.FONT_HEADER);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

        JTextArea txt = new JTextArea(
            "1. Attendance Logging (FR-11, FR-12):\n" +
            "   - Log student attendance for exactly 15 Theory and 15 Practical sessions per course module.\n" +
            "   - Mark 'Present' or 'Absent' with exact session timestamps.\n\n" +
            "2. Medical Record Submission (FR-13):\n" +
            "   - Log medical applications submitted by undergraduates with supporting medical reasons.\n" +
            "   - The initial approval status is automatically defaulted to 'Pending' for Lecturer review.\n\n" +
            "3. Timetables & Notices (FR-10, FR-09):\n" +
            "   - View department-specific lab and lecture schedules, venues, and administrative circulars."
        );
        txt.setFont(UIConstants.FONT_REGULAR);
        txt.setEditable(false);
        txt.setOpaque(false);

        infoCard.add(lblTitle, BorderLayout.NORTH);
        infoCard.add(txt, BorderLayout.CENTER);
        panel.add(infoCard, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // 2. DAILY ATTENDANCE LOGGING (FR-11, FR-12, UC-T2)
    // ==========================================
    private JPanel createDailyAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Daily Session Attendance Logging (15 Sessions)");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        // Selector controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        controls.add(new JLabel("Timetable Session:"));
        List<Timetable> timetables = timetableService.getAllTimetables();
        cmbTimetable = new JComboBox<>(timetables.toArray(new Timetable[0]));
        cmbTimetable.addActionListener(e -> loadAttendanceData());
        controls.add(cmbTimetable);

        controls.add(new JLabel("Session No:"));
        Integer[] sessions = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        cmbSessionNumber = new JComboBox<>(sessions);
        cmbSessionNumber.addActionListener(e -> loadAttendanceData());
        controls.add(cmbSessionNumber);

        JButton btnMarkAllPresent = UIHelper.createSecondaryButton("Mark All Present");
        btnMarkAllPresent.addActionListener(e -> setAllStatus("Present"));
        controls.add(btnMarkAllPresent);

        JButton btnSaveAttendance = UIHelper.createPrimaryButton("💾 Save Attendance");
        btnSaveAttendance.addActionListener(e -> saveAttendanceSession());
        controls.add(btnSaveAttendance);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Table with editable Present/Absent checkboxes or combo
        String[] cols = {"User ID", "Reg No", "Student Name", "Attendance Status (Present / Absent)", "Medical Status", "Remarks / Reason"};
        attendanceTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3 || col == 5; // Status and remarks editable
            }
        };
        attendanceTable = new JTable(attendanceTableModel);
        UIHelper.styleTable(attendanceTable);

        // Setup dropdown editor for Attendance Status column
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Present", "Absent"});
        attendanceTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cmbStatus));

        JScrollPane scroll = new JScrollPane(attendanceTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadAttendanceData();
        return panel;
    }

    private void loadAttendanceData() {
        attendanceTableModel.setRowCount(0);
        Timetable tt = (Timetable) cmbTimetable.getSelectedItem();
        Integer sess = (Integer) cmbSessionNumber.getSelectedItem();
        if (tt == null || sess == null) return;

        List<User> students = userService.getUsersByRole("Undergraduate");
        List<Attendance> existing = attendanceService.getAttendanceForSession(tt.getTimetableId(), sess);

        for (User u : students) {
            String status = "Present";
            String medStatus = "None";
            String reason = "";

            for (Attendance a : existing) {
                if (a.getStudentId() == u.getUserId()) {
                    status = a.getStatus();
                    medStatus = a.getMedicalStatus();
                    reason = a.getReason();
                    break;
                }
            }

            attendanceTableModel.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getFullName(),
                status,
                medStatus,
                reason
            });
        }
    }

    private void setAllStatus(String status) {
        for (int i = 0; i < attendanceTableModel.getRowCount(); i++) {
            attendanceTableModel.setValueAt(status, i, 3);
        }
    }

    private void saveAttendanceSession() {
        Timetable tt = (Timetable) cmbTimetable.getSelectedItem();
        Integer sess = (Integer) cmbSessionNumber.getSelectedItem();
        if (tt == null || sess == null) return;

        int rowCount = attendanceTableModel.getRowCount();
        Date today = Date.valueOf(LocalDate.now());

        int successCount = 0;
        for (int i = 0; i < rowCount; i++) {
            int studentId = (int) attendanceTableModel.getValueAt(i, 0);
            String status = (String) attendanceTableModel.getValueAt(i, 3);

            boolean ok = attendanceService.recordSessionAttendance(studentId, tt.getTimetableId(), today, sess, status);
            if (ok) successCount++;
        }

        if (successCount == rowCount) {
            UIHelper.showInfo(this, "Session " + sess + " attendance saved successfully for " + successCount + " undergraduates!", "Success");
            loadAttendanceData();
        } else {
            UIHelper.showWarning(this, "Saved " + successCount + " out of " + rowCount + " student records.", "Partial Save");
        }
    }

    // ==========================================
    // 3. SUBMIT MEDICAL RECORDS (FR-13, UC-T3)
    // ==========================================
    private JPanel createMedicalSubmissionPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Log Undergraduate Medical Certificates (Default: Pending)");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnAdd = UIHelper.createPrimaryButton("+ Submit New Medical Record");
        btnAdd.addActionListener(e -> openSubmitMedicalDialog());
        btnBar.add(btnAdd);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadMedicalsData());
        btnBar.add(btnRefresh);

        topBar.add(btnBar, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Student Reg No", "Student Name", "Course", "Session Type", "Session No", "Date", "Status", "Medical Status", "Supporting Reason"};
        medicalsTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        medicalsTable = new JTable(medicalsTableModel);
        UIHelper.styleTable(medicalsTable);

        JScrollPane scroll = new JScrollPane(medicalsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadMedicalsData();
        return panel;
    }

    private void loadMedicalsData() {
        medicalsTableModel.setRowCount(0);
        List<Attendance> list = attendanceService.getAllMedicals();
        for (Attendance a : list) {
            medicalsTableModel.addRow(new Object[]{
                a.getAttendanceId(),
                a.getStudentRegNo(),
                a.getStudentName(),
                a.getCourseCode(),
                a.getSessionType(),
                "Session " + a.getSessionNumber(),
                a.getSessionDate(),
                a.getStatus(),
                a.getMedicalStatus(),
                a.getReason()
            });
        }
    }

    private void openSubmitMedicalDialog() {
        JDialog dialog = new JDialog(this, "Log Medical Certificate Submission", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<User> students = userService.getUsersByRole("Undergraduate");
        JComboBox<User> cmbStudent = new JComboBox<>(students.toArray(new User[0]));

        List<Timetable> timetables = timetableService.getAllTimetables();
        JComboBox<Timetable> cmbTt = new JComboBox<>(timetables.toArray(new Timetable[0]));

        JComboBox<Integer> cmbSess = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        JTextField txtReason = UIHelper.createStyledTextField(18);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Undergraduate:"), gbc);
        gbc.gridx = 1; form.add(cmbStudent, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Timetable Session:"), gbc);
        gbc.gridx = 1; form.add(cmbTt, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Session Number (1-15):"), gbc);
        gbc.gridx = 1; form.add(cmbSess, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Medical Reason / Certificate:"), gbc);
        gbc.gridx = 1; form.add(txtReason, gbc);

        JLabel lblNotice = new JLabel("<html><i>Note: Status defaults to 'Pending' awaiting Lecturer approval (FR-13).</i></html>");
        lblNotice.setFont(UIConstants.FONT_SMALL);
        lblNotice.setForeground(UIConstants.TEXT_MUTED);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; form.add(lblNotice, gbc);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = UIHelper.createPrimaryButton("Submit Medical Record");
        btnSave.addActionListener(e -> {
            User student = (User) cmbStudent.getSelectedItem();
            Timetable tt = (Timetable) cmbTt.getSelectedItem();
            Integer sess = (Integer) cmbSess.getSelectedItem();
            String reason = txtReason.getText().trim();

            if (student == null || tt == null || sess == null || !ValidationUtil.isNotEmpty(reason)) {
                UIHelper.showWarning(dialog, "Please specify all medical submission details.", "Missing Information");
                return;
            }

            boolean ok = attendanceService.submitMedical(student.getUserId(), tt.getTimetableId(), sess, reason);
            if (ok) {
                UIHelper.showInfo(dialog, "Medical record logged with status 'Pending' successfully!", "Success");
                loadMedicalsData();
                dialog.dispose();
            } else {
                UIHelper.showError(dialog, "Failed to submit medical record.", "Error");
            }
        });

        btnBar.add(btnCancel);
        btnBar.add(btnSave);
        dialog.add(btnBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void logout() {
        if (UIHelper.showConfirm(this, "Are you sure you want to sign out?", "Confirm Sign Out")) {
            AuthService.getInstance().logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
