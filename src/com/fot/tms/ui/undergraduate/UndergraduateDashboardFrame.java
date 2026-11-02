package com.fot.tms.ui.undergraduate;

import com.fot.tms.model.*;
import com.fot.tms.service.*;
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
import java.io.File;
import java.util.List;



public class UndergraduateDashboardFrame extends JFrame {
    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final TimetableService timetableService;
    private final MarkService markService;
    private final GPACalculatorService gpaService;
    private final User currentUser;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Attendance View components
    private JComboBox<Course> cmbAttendanceCourse;
    private JComboBox<String> cmbAttendanceFormat;
    private DefaultTableModel attendanceHistoryTableModel;
    private JTable attendanceHistoryTable;
    private JLabel lblAttStatsSummary;

    // Marks and GPA components
    private DefaultTableModel marksTableModel;
    private JTable marksTable;
    private JLabel lblSgpaVal;
    private JLabel lblCgpaVal;
    private JLabel lblStandingVal;
    private JLabel lblCreditsVal;

    // Medicals components
    private DefaultTableModel medicalsTableModel;
    private JTable medicalsTable;

    // Materials components
    private DefaultTableModel coursesTableModel;
    private JTable coursesTable;
    private DefaultTableModel materialsTableModel;
    private JTable materialsTable;

    public UndergraduateDashboardFrame() {
        this.courseService = new CourseService();
        this.attendanceService = new AttendanceService();
        this.timetableService = new TimetableService();
        this.markService = new MarkService();
        this.gpaService = new GPACalculatorService();
        this.currentUser = AuthService.getInstance().getCurrentUser();

        setTitle("Faculty of Technology Management System - Undergraduate Portal");
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
        mainContentPanel.add(createAttendanceViewPanel(), "ATTENDANCE");
        mainContentPanel.add(createMarksAndGpaPanel(), "MARKS_GPA");
        mainContentPanel.add(createMedicalStatusPanel(), "MEDICALS");
        mainContentPanel.add(createCoursesAndMaterialsPanel(), "COURSES");
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

        JLabel lblBrand = new JLabel("FoT-TMS Student");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Undergraduate Portal");
        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(UIConstants.ACCENT_COLOR);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createNavButton("Student Overview", "OVERVIEW"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("My Attendance", "ATTENDANCE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("Marks, Grades & GPA", "MARKS_GPA"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("My Medicals", "MEDICALS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("Courses & Materials", "COURSES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("Faculty Timetable", "TIMETABLE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("Notices", "NOTICES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(createNavButton("Student Profile", "PROFILE"));

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = UIHelper.createDangerButton("Sign Out");
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
            if ("ATTENDANCE".equals(cardName)) loadAttendanceDetails();
            if ("MARKS_GPA".equals(cardName)) loadMarksAndGPA();
            if ("MEDICALS".equals(cardName)) loadMedicals();
            if ("COURSES".equals(cardName)) loadCoursesAndMaterials();
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

        String name = currentUser != null ? currentUser.getFullName() : "Student";
        String reg = currentUser != null ? currentUser.getUsername() : "";
        JLabel lblWelcome = new JLabel("Welcome, " + name + " (" + reg + ")");
        lblWelcome.setFont(UIConstants.FONT_HEADER);
        lblWelcome.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel lblInfo = new JLabel("Faculty of Technology - University of Ruhuna");
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

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 15, 15));
        statsGrid.setOpaque(false);

        int studentId = currentUser != null ? currentUser.getUserId() : 0;
        GPACalculatorService.StudentGPASummary gpaSum = gpaService.calculateGPAForStudent(studentId);
        List<Course> courses = courseService.getAllCourses();
        List<Attendance> atts = attendanceService.getStudentAttendanceHistory(studentId, null);
        long presentCount = atts.stream().filter(Attendance::isPresentOrApprovedMedical).count();
        double overallAttPct = atts.size() > 0 ? ((double) presentCount / atts.size()) * 100.0 : 0.0;

        statsGrid.add(UIHelper.createStatCard("Registered Courses", String.valueOf(courses.size()), UIConstants.PRIMARY_LIGHT));
        statsGrid.add(UIHelper.createStatCard("Overall Effective Att %", String.format("%.1f%%", overallAttPct), overallAttPct >= 80.0 ? UIConstants.SUCCESS_COLOR : UIConstants.DANGER_COLOR));
        statsGrid.add(UIHelper.createStatCard("Current GPA", String.format("%.2f", gpaSum.getGpa()), UIConstants.PRIMARY_COLOR));
        statsGrid.add(UIHelper.createStatCard("Total Credits", String.valueOf(gpaSum.getTotalCredits()), UIConstants.INFO_COLOR));

        panel.add(statsGrid, BorderLayout.NORTH);

        JPanel infoCard = UIHelper.createCardPanel();
        infoCard.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Academic Standing & Examination Eligibility Summary");
        lblTitle.setFont(UIConstants.FONT_HEADER);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

        JTextArea txt = new JTextArea(
            "Student Reg No: " + (currentUser != null ? currentUser.getUsername() : "") + "\n" +
            "Academic Standing: " + gpaSum.getAcademicStanding() + "\n" +
            "Grading Compliance: UGC Commission Circular No. 12-2024\n\n" +
            "Mandatory Exam Sitting Eligibility Criteria:\n" +
            "• Attendance Eligibility: Effective Attendance (Present + Approved Medicals) must be >= 80% (FR-20).\n" +
            "• Continuous Assessment (CA) Eligibility: Total CA marks must be >= 40% (FR-19).\n" +
            "• Final Semester Marks: Computed based on Course CA Weight and Final Exam Weight (FR-21)."
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
    // 2. ATTENDANCE VIEW (FR-15, FR-16, UC-U3)
    // ==========================================
    private JPanel createAttendanceViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("My Session Attendance & Medical Tracking");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        controls.add(new JLabel("Course:"));
        List<Course> courses = courseService.getAllCourses();
        cmbAttendanceCourse = new JComboBox<>(courses.toArray(new Course[0]));
        cmbAttendanceCourse.addActionListener(e -> loadAttendanceDetails());
        controls.add(cmbAttendanceCourse);

        controls.add(new JLabel("Format:"));
        cmbAttendanceFormat = new JComboBox<>(new String[]{"Combined (Theory + Practical)", "Theory Only", "Practical Only"});
        cmbAttendanceFormat.addActionListener(e -> loadAttendanceDetails());
        controls.add(cmbAttendanceFormat);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadAttendanceDetails());
        controls.add(btnRefresh);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Attendance stats banner
        JPanel statsBanner = UIHelper.createCardPanel();
        statsBanner.setLayout(new BorderLayout());
        lblAttStatsSummary = new JLabel("Loading attendance metrics...");
        lblAttStatsSummary.setFont(UIConstants.FONT_HEADER);
        lblAttStatsSummary.setForeground(UIConstants.PRIMARY_COLOR);
        statsBanner.add(lblAttStatsSummary, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(statsBanner, BorderLayout.NORTH);

        String[] cols = {"Session No", "Course Code", "Course Name", "Session Type", "Session Date", "Attendance Status", "Medical Status", "Remarks"};
        attendanceHistoryTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        attendanceHistoryTable = new JTable(attendanceHistoryTableModel);
        UIHelper.styleTable(attendanceHistoryTable);

        JScrollPane scroll = new JScrollPane(attendanceHistoryTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        centerPanel.add(scroll, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        loadAttendanceDetails();
        return panel;
    }

    private void loadAttendanceDetails() {
        attendanceHistoryTableModel.setRowCount(0);
        if (currentUser == null) return;
        Course c = (Course) cmbAttendanceCourse.getSelectedItem();
        if (c == null) return;

        int viewIdx = cmbAttendanceFormat.getSelectedIndex(); // 0: Combined, 1: Theory, 2: Practical
        StudentAttendanceSummary summary = attendanceService.calculateStudentAttendanceSummary(currentUser.getUserId(), c.getCourseId());

        double rawPct, effPct;
        int tot, pres, med;
        if (viewIdx == 1) { // Theory
            tot = summary.getTheoryTotalSessions();
            pres = summary.getTheoryPresentCount();
            med = summary.getTheoryApprovedMedicals();
            rawPct = summary.getTheoryRawPercentage();
            effPct = summary.getTheoryEffectivePercentage();
        } else if (viewIdx == 2) { // Practical
            tot = summary.getPracticalTotalSessions();
            pres = summary.getPracticalPresentCount();
            med = summary.getPracticalApprovedMedicals();
            rawPct = summary.getPracticalRawPercentage();
            effPct = summary.getPracticalEffectivePercentage();
        } else { // Combined
            tot = summary.getCombinedTotalSessions();
            pres = summary.getCombinedPresentCount();
            med = summary.getCombinedApprovedMedicals();
            rawPct = summary.getCombinedRawPercentage();
            effPct = summary.getCombinedEffectivePercentage();
        }

        boolean eligible = effPct >= 80.0;
        String statusText = eligible ? "ELIGIBLE (>= 80%)" : "INELIGIBLE (< 80%)";

        lblAttStatsSummary.setText(String.format(
            "<html>%s • Total: %d | Present: %d | Approved MC: %d | Raw: <b>%.2f%%</b> | Effective: <font color='%s'><b>%.2f%%</b></font> | Status: <font color='%s'><b>%s</b></font> | Scenario: <b>%s</b></html>",
            c.getCourseCode(), tot, pres, med, rawPct,
            eligible ? "#10b981" : "#ef4444", effPct,
            eligible ? "#10b981" : "#ef4444", statusText,
            summary.getScenarioCategory()
        ));

        // Load session history rows
        List<Attendance> history = attendanceService.getStudentAttendanceHistory(currentUser.getUserId(), c.getCourseId());
        for (Attendance a : history) {
            if (viewIdx == 1 && !"Theory".equalsIgnoreCase(a.getSessionType())) continue;
            if (viewIdx == 2 && !"Practical".equalsIgnoreCase(a.getSessionType())) continue;

            attendanceHistoryTableModel.addRow(new Object[]{
                "Session " + a.getSessionNumber(),
                a.getCourseCode(),
                a.getCourseName(),
                a.getSessionType(),
                a.getSessionDate(),
                a.getStatus(),
                a.getMedicalStatus(),
                a.getReason()
            });
        }
    }

    // ==========================================
    // 3. MARKS, GRADES & GPA PANEL (FR-17 to FR-23, UC-U5)
    // ==========================================
    private JPanel createMarksAndGpaPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Academic Grades, Continuous Assessment & GPA");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh Results");
        btnRefresh.addActionListener(e -> loadMarksAndGPA());
        topBar.add(btnRefresh, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // GPA Cards Top Strip
        JPanel cardsStrip = new JPanel(new GridLayout(1, 4, 15, 15));
        cardsStrip.setOpaque(false);

        lblSgpaVal = new JLabel("0.00");
        lblCgpaVal = new JLabel("0.00");
        lblStandingVal = new JLabel("Loading...");
        lblCreditsVal = new JLabel("0");

        cardsStrip.add(createDynamicStatCard("Semester 1 GPA (SGPA)", lblSgpaVal, UIConstants.PRIMARY_LIGHT));
        cardsStrip.add(createDynamicStatCard("Cumulative GPA (CGPA)", lblCgpaVal, UIConstants.PRIMARY_COLOR));
        cardsStrip.add(createDynamicStatCard("Academic Standing", lblStandingVal, UIConstants.SUCCESS_COLOR));
        cardsStrip.add(createDynamicStatCard("Total Registered Credits", lblCreditsVal, UIConstants.INFO_COLOR));

        JPanel centerContainer = new JPanel(new BorderLayout(15, 15));
        centerContainer.setOpaque(false);
        centerContainer.add(cardsStrip, BorderLayout.NORTH);

        String[] cols = {"Course Code", "Course Name", "Credits", "Quiz (100)", "Assessment (100)", "Mid Exam (100)", "CA Total (100)", "CA >= 40%", "Final Exam (100)", "Overall Mark", "Letter Grade", "Grade Point"};
        marksTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        marksTable = new JTable(marksTableModel);
        UIHelper.styleTable(marksTable);

        JScrollPane scroll = new JScrollPane(marksTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        centerContainer.add(scroll, BorderLayout.CENTER);

        panel.add(centerContainer, BorderLayout.CENTER);

        loadMarksAndGPA();
        return panel;
    }

    private JPanel createDynamicStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_SMALL);
        lblTitle.setForeground(UIConstants.TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(UIConstants.TEXT_PRIMARY);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadMarksAndGPA() {
        marksTableModel.setRowCount(0);
        if (currentUser == null) return;

        int studentId = currentUser.getUserId();
        GPACalculatorService.StudentGPASummary gpaSum = gpaService.calculateGPAForStudent(studentId);
        if (lblSgpaVal != null) lblSgpaVal.setText(String.format("%.2f", gpaSum.getSgpa()));
        if (lblCgpaVal != null) lblCgpaVal.setText(String.format("%.2f", gpaSum.getCgpa()));
        if (lblStandingVal != null) lblStandingVal.setText(gpaSum.getAcademicStanding());
        if (lblCreditsVal != null) lblCreditsVal.setText(String.valueOf(gpaSum.getTotalCredits()));

        List<Mark> marks = markService.getMarksForStudent(studentId);
        for (Mark m : marks) {
            boolean caEligible = m.getCaTotal() >= 40.0;
            marksTableModel.addRow(new Object[]{
                m.getCourseCode(),
                m.getCourseName(),
                m.getCredits(),
                String.format("%.2f", m.getCaQuiz()),
                String.format("%.2f", m.getCaAssessment()),
                String.format("%.2f", m.getCaMid()),
                String.format("%.2f", m.getCaTotal()),
                caEligible ? "ELIGIBLE" : "INELIGIBLE",
                String.format("%.2f", m.getFinalExamMark()),
                String.format("%.2f", m.getOverallMark()),
                m.getLetterGrade(),
                String.format("%.2f", m.getGradePoint())
            });
        }
    }

    // ==========================================
    // 4. MEDICAL STATUS VIEW (UC-U4)
    // ==========================================
    private JPanel createMedicalStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Submitted Medical Certificates & Approval Status");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnSubmit = UIHelper.createPrimaryButton("+ Submit Medical Certificate");
        btnSubmit.addActionListener(e -> openSubmitMedicalDialog());
        btnBar.add(btnSubmit);

        JButton btnViewDoc = UIHelper.createSecondaryButton("View Certificate Document");
        btnViewDoc.addActionListener(e -> viewSelectedMedicalFile());
        btnBar.add(btnViewDoc);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh Status");
        btnRefresh.addActionListener(e -> loadMedicals());
        btnBar.add(btnRefresh);

        topBar.add(btnBar, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Attendance ID", "Course Code", "Course Name", "Session Type", "Session No", "Date", "Approval Status", "Medical Reason"};
        medicalsTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        medicalsTable = new JTable(medicalsTableModel);
        medicalsTable.setAutoCreateRowSorter(true);
        UIHelper.styleTable(medicalsTable);
        medicalsTable.getColumnModel().getColumn(0).setMinWidth(0);
        medicalsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        medicalsTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(medicalsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadMedicals();
        return panel;
    }

    private void loadMedicals() {
        medicalsTableModel.setRowCount(0);
        if (currentUser == null) return;

        List<Attendance> list = attendanceService.getMedicalsForStudent(currentUser.getUserId());
        for (Attendance a : list) {
            if (!"None".equalsIgnoreCase(a.getMedicalStatus())) {
                String fullReason = a.getReason();
                String displayReason = fullReason;
                String certFilePath = "No file";
                if (fullReason != null && fullReason.contains("[File: ") && fullReason.contains("]")) {
                    int fs = fullReason.indexOf("[File: ");
                    int fe = fullReason.lastIndexOf("]");
                    certFilePath = fullReason.substring(fs + 7, fe).trim();
                    displayReason = fullReason.substring(0, fs).trim();
                    if (displayReason.isEmpty()) displayReason = "(See attached file)";
                }
                medicalsTableModel.addRow(new Object[]{
                    a.getAttendanceId(),
                    a.getCourseCode(),
                    a.getCourseName(),
                    a.getSessionType(),
                    "Session " + a.getSessionNumber(),
                    a.getSessionDate(),
                    a.getMedicalStatus(),
                    displayReason
                });
            }
        }
    }

    private void viewSelectedMedicalFile() {
        int row = medicalsTable.getSelectedRow();
        if (row < 0) {
            UIHelper.showWarning(this, "Please select a medical record from the table.", "Selection Required");
            return;
        }
        int modelRow = medicalsTable.convertRowIndexToModel(row);
        int attId = (int) medicalsTableModel.getValueAt(modelRow, 0);
        Attendance att = attendanceService.getAttendanceById(attId);
        String reason = att != null ? att.getReason() : null;
        if (reason != null && reason.contains("[File: ") && reason.contains("]")) {
            int start = reason.indexOf("[File: ") + 7;
            int end = reason.lastIndexOf("]");
            String path = reason.substring(start, end).trim();
            File file = new File(path);
            if (file.exists()) {
                try {
                    Desktop.getDesktop().open(file);
                    return;
                } catch (Exception ex) {
                    UIHelper.showError(this, "Could not open document: " + ex.getMessage(), "Error");
                    return;
                }
            } else {
                UIHelper.showInfo(this, "Certificate Document Path: " + path + "\n(File recorded on system).", "Medical Document Info");
                return;
            }
        }
        String cleanR = (String) medicalsTableModel.getValueAt(modelRow, 7);
        UIHelper.showInfo(this, "No external document attached for this medical record.\nReason: " + (cleanR != null ? cleanR : "N/A"), "Medical Info");
    }
    private void openSubmitMedicalDialog() {
        JDialog dialog = new JDialog(this, "Submit Medical Certificate", true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Timetable> timetables = timetableService.getAllTimetables();
        JComboBox<Timetable> cmbTt = new JComboBox<>(timetables.toArray(new Timetable[0]));

        JComboBox<Integer> cmbSess = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        JTextField txtReason = UIHelper.createStyledTextField(18);
        JTextField txtCertPath = UIHelper.createStyledTextField(14);
        JButton btnBrowse = UIHelper.createSecondaryButton("Browse File...");
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Medical Certificate Document / Image");
            int res = chooser.showOpenDialog(dialog);
            if (res == JFileChooser.APPROVE_OPTION) {
                txtCertPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.setOpaque(false);
        filePanel.add(txtCertPath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Subject & Timetable:"), gbc);
        gbc.gridx = 1; form.add(cmbTt, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Session Number:"), gbc);
        gbc.gridx = 1; form.add(cmbSess, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Medical Reason:"), gbc);
        gbc.gridx = 1; form.add(txtReason, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Attach Certificate (*):"), gbc);
        gbc.gridx = 1; form.add(filePanel, gbc);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSubmit = UIHelper.createPrimaryButton("Upload & Submit Medical");
        btnSubmit.addActionListener(e -> {
            Timetable tt = (Timetable) cmbTt.getSelectedItem();
            Integer sess = (Integer) cmbSess.getSelectedItem();
            String reason = txtReason.getText().trim();
            String filePath = txtCertPath.getText().trim();

            if (tt == null || sess == null || !ValidationUtil.isNotEmpty(reason)) {
                UIHelper.showWarning(dialog, "Please specify timetable session and medical reason.", "Validation Warning");
                return;
            }

            if (!ValidationUtil.isNotEmpty(filePath)) {
                UIHelper.showWarning(dialog, "Please attach a medical certificate report or image document (Required).", "Certificate Document Required");
                return;
            }
            String fullReason = reason + " [File: " + filePath + "]";




            boolean ok = attendanceService.submitMedical(currentUser.getUserId(), tt.getTimetableId(), sess, fullReason);
            if (ok) {
                UIHelper.showInfo(dialog, "Medical certificate submitted successfully (Status: Pending)!\nTechnical Officer & Lecturer will review your certificate.", "Success");
                loadMedicals();
                dialog.dispose();
            } else {
                UIHelper.showError(dialog, "Failed to submit medical certificate.", "Error");
            }
        });

        btnBar.add(btnCancel);
        btnBar.add(btnSubmit);
        dialog.add(btnBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ==========================================
    // 5. COURSES & MATERIALS (FR-08, UC-U2)
    // ==========================================
    private JPanel createCoursesAndMaterialsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Curriculum Courses & Learning Materials");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnOpen = UIHelper.createPrimaryButton("Open / View Material File");
        btnOpen.addActionListener(e -> openSelectedMaterialFile());
        btnBar.add(btnOpen);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadCoursesAndMaterials());
        btnBar.add(btnRefresh);

        topBar.add(btnBar, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Tabbed Pane for: 1. Course Curriculum Details (FR-07, Assignment Page 2), 2. Learning Materials (FR-08)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.FONT_BOLD);

        // --- TAB 1: Registered Course Curriculum Details ---
        JPanel coursesTab = new JPanel(new BorderLayout(10, 10));
        coursesTab.setBackground(Color.WHITE);
        coursesTab.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] courseCols = {"Course Code", "Course Name", "Credits", "Semester", "CA Weight (%)", "Final Weight (%)", "Course Coordinator"};
        coursesTableModel = new DefaultTableModel(courseCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        coursesTable = new JTable(coursesTableModel);
        coursesTable.setAutoCreateRowSorter(true);
        UIHelper.styleTable(coursesTable);
        JScrollPane courseScroll = new JScrollPane(coursesTable);
        courseScroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        coursesTab.add(courseScroll, BorderLayout.CENTER);

        // --- TAB 2: Course Learning Materials ---
        JPanel materialsTab = new JPanel(new BorderLayout(10, 10));
        materialsTab.setBackground(Color.WHITE);
        materialsTab.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] matCols = {"Course Code", "Course Name", "Material Title", "Resource / File Path", "Date Uploaded"};
        materialsTableModel = new DefaultTableModel(matCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        materialsTable = new JTable(materialsTableModel);
        materialsTable.setAutoCreateRowSorter(true);
        UIHelper.styleTable(materialsTable);
        JScrollPane matScroll = new JScrollPane(materialsTable);
        matScroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        materialsTab.add(matScroll, BorderLayout.CENTER);

        tabbedPane.addTab("Registered Course Modules", coursesTab);
        tabbedPane.addTab("Learning Materials & Resources", materialsTab);

        panel.add(tabbedPane, BorderLayout.CENTER);

        loadCoursesAndMaterials();
        return panel;
    }

    private void openSelectedMaterialFile() {
        int row = materialsTable.getSelectedRow();
        if (row < 0) {
            UIHelper.showWarning(this, "Please select a course material row to open.", "Selection Required");
            return;
        }
        int modelRow = materialsTable.convertRowIndexToModel(row);
        String path = (String) materialsTableModel.getValueAt(modelRow, 3);
        File file = new File(path);
        if (!file.exists()) {
            file = new File("../" + path);
        }
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), path);
        }
        if (!file.exists() && path.contains("/")) {
            String fname = path.substring(path.lastIndexOf("/") + 1);
            file = new File(new File(System.getProperty("user.dir"), "materials"), fname);
        }
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception ex) {
                UIHelper.showError(this, "Could not open file: " + ex.getMessage(), "Error");
            }
        } else {
            UIHelper.showInfo(this, "Material file path: " + path + "\n(File record is active in catalog).", "Material Info");
        }
    }

    private void loadCoursesAndMaterials() {
        // Load Courses Details (FR-07, Page 2: "Can see their course details")
        if (coursesTableModel != null) {
            coursesTableModel.setRowCount(0);
            List<Course> courses = courseService.getAllCourses();
            for (Course c : courses) {
                coursesTableModel.addRow(new Object[]{
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getCredits() + " Credits",
                    "Semester " + c.getSemester(),
                    String.format("%.1f%%", c.getCaWeight()),
                    String.format("%.1f%%", c.getFinalWeight()),
                    c.getCoordinatorName() != null ? c.getCoordinatorName() : "Department Academic Staff"
                });
            }
        }

        // Load Course Materials (FR-08)
        if (materialsTableModel != null) {
            materialsTableModel.setRowCount(0);
            List<CourseMaterial> list = courseService.getAllMaterials();
            for (CourseMaterial m : list) {
                materialsTableModel.addRow(new Object[]{
                    m.getCourseCode(),
                    m.getCourseName(),
                    m.getTitle(),
                    m.getFilePath(),
                    m.getUploadedDate()
                });
            }
        }
    }

    private void logout() {
        if (UIHelper.showConfirm(this, "Are you sure you want to sign out?", "Confirm Sign Out")) {
            AuthService.getInstance().logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
