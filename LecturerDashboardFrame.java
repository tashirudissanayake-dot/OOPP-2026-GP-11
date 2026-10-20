package com.fot.tms.ui.lecturer;

import com.fot.tms.model.*;
import com.fot.tms.service.*;
import com.fot.tms.ui.LoginFrame;
import com.fot.tms.ui.common.NoticesPanel;
import com.fot.tms.ui.common.ProfilePanel;
import com.fot.tms.ui.common.ReportExportHelper;
import com.fot.tms.ui.common.TimetablePanel;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;
import com.fot.tms.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;


public class LecturerDashboardFrame extends JFrame {
    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final MarkService markService;
    private final GPACalculatorService gpaService;
    private final User currentUser;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

  
    private DefaultTableModel materialsTableModel;
    private DefaultTableModel medicalsTableModel;
    private DefaultTableModel marksTableModel;
    private DefaultTableModel eligibilityTableModel;
    private DefaultTableModel attendanceSummaryTableModel;

    private JTable materialsTable;
    private JTable medicalsTable;
    private JTable marksTable;
    private JTable eligibilityTable;
    private JTable attendanceSummaryTable;

    
    private JComboBox<Course> cmbMarksCourse;
    private JComboBox<Course> cmbEligibilityCourse;
    private JComboBox<Course> cmbAttendanceCourse;
    private JComboBox<String> cmbAttendanceViewType;

    public LecturerDashboardFrame() {
        this.courseService = new CourseService();
        this.attendanceService = new AttendanceService();
        this.markService = new MarkService();
        this.gpaService = new GPACalculatorService();
        this.currentUser = AuthService.getInstance().getCurrentUser();

        setTitle("Faculty of Technology Management System - Lecturer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 780);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());

        
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

       
        JPanel centerContainer = new JPanel(new BorderLayout());
        JPanel topHeader = createTopHeader();
        centerContainer.add(topHeader, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIConstants.BG_MAIN);

    
        mainContentPanel.add(createOverviewPanel(), "OVERVIEW");
        mainContentPanel.add(createMaterialsPanel(), "MATERIALS");
        mainContentPanel.add(createMedicalsPanel(), "MEDICALS");
        mainContentPanel.add(createMarksEntryPanel(), "MARKS");
        mainContentPanel.add(createAttendanceSummaryPanel(), "ATTENDANCE_SUMMARY");
        mainContentPanel.add(createEligibilityPanel(), "ELIGIBILITY");
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

        JLabel lblBrand = new JLabel("FoT-TMS Lecturer");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Academic Staff Portal");
        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(UIConstants.ACCENT_COLOR);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));

        sidebar.add(createNavButton("📊 Lecturer Dashboard", "OVERVIEW"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("📝 Record Marks & CA", "MARKS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("🏥 Review Medicals", "MEDICALS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("📈 Attendance Scenarios", "ATTENDANCE_SUMMARY"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("🎯 Exam Eligibility & GPA", "ELIGIBILITY"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("📂 Course Materials", "MATERIALS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("📅 Faculty Timetable", "TIMETABLE"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("📢 Notices", "NOTICES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        sidebar.add(createNavButton("👤 My Profile", "PROFILE"));

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
        btn.setBorder(new EmptyBorder(9, 14, 9, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(210, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, cardName);
            if ("MATERIALS".equals(cardName)) loadMaterialsData();
            if ("MEDICALS".equals(cardName)) loadMedicalsData();
            if ("MARKS".equals(cardName)) loadMarksData();
            if ("ATTENDANCE_SUMMARY".equals(cardName)) loadAttendanceSummaryData();
            if ("ELIGIBILITY".equals(cardName)) loadEligibilityData();
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

        String name = currentUser != null ? currentUser.getFullName() : "Lecturer";
        JLabel lblWelcome = new JLabel("Academic Portal: " + name + " (Lecturer)");
        lblWelcome.setFont(UIConstants.FONT_HEADER);
        lblWelcome.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel lblInfo = new JLabel("ICT2132 Object Oriented Programming Practicum");
        lblInfo.setFont(UIConstants.FONT_SMALL);
        lblInfo.setForeground(UIConstants.TEXT_MUTED);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(lblInfo, BorderLayout.EAST);
        return header;
    }

    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 15, 15));
        statsGrid.setOpaque(false);

        List<Course> myCourses = courseService.getCoursesByCoordinator(currentUser != null ? currentUser.getUserId() : 0);
        List<Attendance> pendingMeds = attendanceService.getPendingMedicals();
        List<CourseMaterial> materials = courseService.getAllMaterials();

        statsGrid.add(UIHelper.createStatCard("Assigned Courses", String.valueOf(myCourses.size()), UIConstants.PRIMARY_LIGHT));
        statsGrid.add(UIHelper.createStatCard("Pending Medicals", String.valueOf(pendingMeds.size()), UIConstants.WARNING_COLOR));
        statsGrid.add(UIHelper.createStatCard("Course Materials", String.valueOf(materials.size()), UIConstants.SUCCESS_COLOR));
        statsGrid.add(UIHelper.createStatCard("UGC Grading", "12-2024", UIConstants.ACCENT_COLOR));

        panel.add(statsGrid, BorderLayout.NORTH);

        JPanel detailsCard = UIHelper.createCardPanel();
        detailsCard.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Academic Evaluation & Governance Guidelines");
        lblTitle.setFont(UIConstants.FONT_HEADER);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

        JTextArea txt = new JTextArea(
            "Continuous Assessment (CA) & Exam Eligibility Standard Rules:\n" +
            "1. Continuous Assessment: Total CA marks must be >= 40% (FR-19).\n" +
            "2. Attendance Requirement: Combined Effective Attendance (Present + Approved Medicals) must be >= 80% (FR-20).\n" +
            "3. Final Examination Eligibility: Requires BOTH CA >= 40% AND Attendance >= 80%.\n" +
            "4. Overall Mark Calculation: CA Total weighted at configured % (default 40%) + Final Exam weighted at % (default 60%).\n" +
            "5. UGC Commission Circular No. 12-2024 Grade Scale:\n" +
            "   [A+: >=85 (4.00)] [A: 75-84 (4.00)] [A-: 70-74 (3.70)] [B+: 65-69 (3.30)] [B: 60-64 (3.00)]\n" +
            "   [B-: 55-59 (2.70)] [C+: 50-54 (2.30)] [C: 45-49 (2.00)] [C-: 40-44 (1.70)] [D+: 35-39 (1.30)]\n" +
            "   [D: 30-34 (1.00)] [E: 0-29 (0.00)]"
        );
        txt.setFont(new Font("Consolas", Font.PLAIN, 13));
        txt.setEditable(false);
        txt.setOpaque(false);

        detailsCard.add(lblTitle, BorderLayout.NORTH);
        detailsCard.add(txt, BorderLayout.CENTER);
        panel.add(detailsCard, BorderLayout.CENTER);

        return panel;
    }

    
    private JPanel createMarksEntryPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Continuous Assessment (CA) & Final Marks Entry");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        controls.add(new JLabel("Select Course Module:"));
        List<Course> courses = courseService.getAllCourses();
        cmbMarksCourse = new JComboBox<>(courses.toArray(new Course[0]));
        cmbMarksCourse.addActionListener(e -> loadMarksData());
        controls.add(cmbMarksCourse);

        JButton btnEditMark = UIHelper.createPrimaryButton("✏️ Input / Edit Marks");
        btnEditMark.addActionListener(e -> openEditMarkDialog());
        controls.add(btnEditMark);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadMarksData());
        controls.add(btnRefresh);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Reg No", "Student Name", "Quiz (100)", "Assessment (100)", "Mid Exam (100)", "CA Total (100)", "CA >= 40%", "Final Exam (100)", "Overall Mark (100)", "Letter Grade", "Grade Point"};
        marksTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        marksTable = new JTable(marksTableModel);
        UIHelper.styleTable(marksTable);

        JScrollPane scroll = new JScrollPane(marksTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadMarksData();
        return panel;
    }

    private void loadMarksData() {
        marksTableModel.setRowCount(0);
        Course c = (Course) cmbMarksCourse.getSelectedItem();
        if (c == null) return;

        List<Mark> marks = markService.getMarksForCourse(c.getCourseId());
        for (Mark m : marks) {
            boolean caEligible = m.getCaTotal() >= 40.0;
            marksTableModel.addRow(new Object[]{
                m.getStudentRegNo(),
                m.getStudentName(),
                String.format("%.2f", m.getCaQuiz()),
                String.format("%.2f", m.getCaAssessment()),
                String.format("%.2f", m.getCaMid()),
                String.format("%.2f", m.getCaTotal()),
                caEligible ? "ELIGIBLE" : "NOT ELIGIBLE",
                String.format("%.2f", m.getFinalExamMark()),
                String.format("%.2f", m.getOverallMark()),
                m.getLetterGrade(),
                String.format("%.2f", m.getGradePoint())
            });
        }
    }

    private void openEditMarkDialog() {
        int row = marksTable.getSelectedRow();
        if (row < 0) {
            UIHelper.showWarning(this, "Please select an undergraduate from the table to enter/edit marks.", "Selection Required");
            return;
        }

        Course c = (Course) cmbMarksCourse.getSelectedItem();
        if (c == null) return;

        List<Mark> marks = markService.getMarksForCourse(c.getCourseId());
        if (row >= marks.size()) return;
        Mark selectedMark = marks.get(row);

        JDialog dialog = new JDialog(this, "Record Marks - " + selectedMark.getStudentRegNo(), true);
        dialog.setSize(460, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtQuiz = UIHelper.createStyledTextField(10);
        txtQuiz.setText(String.valueOf(selectedMark.getCaQuiz()));

        JTextField txtAss = UIHelper.createStyledTextField(10);
        txtAss.setText(String.valueOf(selectedMark.getCaAssessment()));

        JTextField txtMid = UIHelper.createStyledTextField(10);
        txtMid.setText(String.valueOf(selectedMark.getCaMid()));

        JTextField txtFinal = UIHelper.createStyledTextField(10);
        txtFinal.setText(String.valueOf(selectedMark.getFinalExamMark()));

        JLabel lblPreview = new JLabel("Preview: Overall: " + selectedMark.getOverallMark() + " | Grade: " + selectedMark.getLetterGrade());
        lblPreview.setFont(UIConstants.FONT_BOLD);
        lblPreview.setForeground(UIConstants.PRIMARY_LIGHT);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1; form.add(new JLabel(selectedMark.getStudentRegNo() + " - " + selectedMark.getStudentName()), gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; form.add(new JLabel(c.getCourseCode() + " (" + c.getCaWeight() + "% CA / " + c.getFinalWeight() + "% Final)"), gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Quiz Mark (0 - 100):"), gbc);
        gbc.gridx = 1; form.add(txtQuiz, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Assessment Mark (0 - 100):"), gbc);
        gbc.gridx = 1; form.add(txtAss, gbc);

        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Mid Exam Mark (0 - 100):"), gbc);
        gbc.gridx = 1; form.add(txtMid, gbc);

        gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Final Exam Mark (0 - 100):"), gbc);
        gbc.gridx = 1; form.add(txtFinal, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; form.add(lblPreview, gbc);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = UIHelper.createPrimaryButton("Save & Calculate Grade");
        btnSave.addActionListener(e -> {
            try {
                double q = Double.parseDouble(txtQuiz.getText().trim());
                double a = Double.parseDouble(txtAss.getText().trim());
                double m = Double.parseDouble(txtMid.getText().trim());
                double f = Double.parseDouble(txtFinal.getText().trim());

                if (!ValidationUtil.isValidMark(q) || !ValidationUtil.isValidMark(a) || !ValidationUtil.isValidMark(m) || !ValidationUtil.isValidMark(f)) {
                    UIHelper.showWarning(dialog, "All marks must be between 0.00 and 100.00.", "Out of Range (NFR-08)");
                    return;
                }

                boolean ok = markService.saveOrUpdateMarks(selectedMark.getStudentId(), c.getCourseId(), q, a, m, f);
                if (ok) {
                    UIHelper.showInfo(dialog, "Marks updated & UGC Circular 12-2024 Grade calculated successfully!", "Success");
                    loadMarksData();
                    dialog.dispose();
                } else {
                    UIHelper.showError(dialog, "Failed to save marks.", "Error");
                }
            } catch (NumberFormatException nfe) {
                UIHelper.showWarning(dialog, "Please enter valid numeric mark values.", "Invalid Number Format");
            }
        });

        btnBar.add(btnCancel);
        btnBar.add(btnSave);
        dialog.add(btnBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    
    private JPanel createMedicalsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Undergraduate Medical Certificate Submissions");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnApprove = UIHelper.createSuccessButton("✓ Approve Medical");
        btnApprove.addActionListener(e -> reviewSelectedMedical(true));
        btnBar.add(btnApprove);

        JButton btnReject = UIHelper.createDangerButton("✗ Reject Medical");
        btnReject.addActionListener(e -> reviewSelectedMedical(false));
        btnBar.add(btnReject);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadMedicalsData());
        btnBar.add(btnRefresh);

        topBar.add(btnBar, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Attendance ID", "Reg No", "Student Name", "Course", "Session Type", "Session No", "Date", "Status", "Medical Status", "Supporting Reason"};
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

    private void reviewSelectedMedical(boolean approve) {
        int row = medicalsTable.getSelectedRow();
        if (row < 0) {
            UIHelper.showWarning(this, "Please select a medical record from the table.", "Selection Required");
            return;
        }
        int attId = (int) medicalsTableModel.getValueAt(row, 0);
        String student = (String) medicalsTableModel.getValueAt(row, 1);
        String action = approve ? "APPROVE" : "REJECT";

        if (UIHelper.showConfirm(this, "Are you sure you want to " + action + " medical certificate for " + student + "?", "Confirm Medical Action")) {
            boolean ok = attendanceService.reviewMedical(attId, approve);
            if (ok) {
                loadMedicalsData();
                UIHelper.showInfo(this, "Medical status updated to: " + (approve ? "Approved" : "Rejected"), "Success");
            } else {
                UIHelper.showError(this, "Failed to update medical status.", "Error");
            }
        }
    }

    
    private JPanel createAttendanceSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Undergraduate Attendance Metrics & 5 Scenario Classifications");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        controls.add(new JLabel("Course:"));
        List<Course> courses = courseService.getAllCourses();
        cmbAttendanceCourse = new JComboBox<>(courses.toArray(new Course[0]));
        cmbAttendanceCourse.addActionListener(e -> loadAttendanceSummaryData());
        controls.add(cmbAttendanceCourse);

        controls.add(new JLabel("Format:"));
        cmbAttendanceViewType = new JComboBox<>(new String[]{"Combined (Theory + Practical)", "Theory Only", "Practical Only"});
        cmbAttendanceViewType.addActionListener(e -> loadAttendanceSummaryData());
        controls.add(cmbAttendanceViewType);

        JButton btnExport = UIHelper.createSuccessButton("📥 Export CSV");
        btnExport.addActionListener(e -> ReportExportHelper.exportTableToCSV(this, attendanceSummaryTable, "Batch_Attendance_Report.csv"));
        controls.add(btnExport);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Reg No", "Student Name", "Total Sessions", "Present", "Approved MC", "Raw %", "Effective %", "Scenario Category (SRS FR-16)", "Eligibility (>=80%)"};
        attendanceSummaryTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        attendanceSummaryTable = new JTable(attendanceSummaryTableModel);
        UIHelper.styleTable(attendanceSummaryTable);

        JScrollPane scroll = new JScrollPane(attendanceSummaryTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadAttendanceSummaryData();
        return panel;
    }

    private void loadAttendanceSummaryData() {
        attendanceSummaryTableModel.setRowCount(0);
        Course c = (Course) cmbAttendanceCourse.getSelectedItem();
        if (c == null) return;

        int viewIdx = cmbAttendanceViewType.getSelectedIndex();
        List<StudentAttendanceSummary> list = attendanceService.getBatchAttendanceSummaries(c.getCourseId());

        for (StudentAttendanceSummary s : list) {
            int tot, pres, med;
            double raw, eff;

            if (viewIdx == 1) { // Theory Only
                tot = s.getTheoryTotalSessions();
                pres = s.getTheoryPresentCount();
                med = s.getTheoryApprovedMedicals();
                raw = s.getTheoryRawPercentage();
                eff = s.getTheoryEffectivePercentage();
            } else if (viewIdx == 2) { // Practical Only
                tot = s.getPracticalTotalSessions();
                pres = s.getPracticalPresentCount();
                med = s.getPracticalApprovedMedicals();
                raw = s.getPracticalRawPercentage();
                eff = s.getPracticalEffectivePercentage();
            } else { // Combined
                tot = s.getCombinedTotalSessions();
                pres = s.getCombinedPresentCount();
                med = s.getCombinedApprovedMedicals();
                raw = s.getCombinedRawPercentage();
                eff = s.getCombinedEffectivePercentage();
            }

            boolean eligible = eff >= 80.0;

            attendanceSummaryTableModel.addRow(new Object[]{
                s.getStudentRegNo(),
                s.getStudentName(),
                tot,
                pres,
                med,
                String.format("%.2f%%", raw),
                String.format("%.2f%%", eff),
                s.getScenarioCategory(),
                eligible ? "ELIGIBLE" : "INELIGIBLE"
            });
        }
    }

    
    private JPanel createEligibilityPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Combined Exam Eligibility & Academic Standings");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        controls.add(new JLabel("Course:"));
        List<Course> courses = courseService.getAllCourses();
        cmbEligibilityCourse = new JComboBox<>(courses.toArray(new Course[0]));
        cmbEligibilityCourse.addActionListener(e -> loadEligibilityData());
        controls.add(cmbEligibilityCourse);

        JButton btnExport = UIHelper.createSuccessButton("📥 Export Eligibility CSV");
        btnExport.addActionListener(e -> ReportExportHelper.exportTableToCSV(this, eligibilityTable, "Batch_Exam_Eligibility.csv"));
        controls.add(btnExport);

        topBar.add(controls, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Reg No", "Student Name", "Effective Att %", "Att >= 80%", "CA Total (100)", "CA >= 40%", "Sit for Final Exam?", "Overall Mark", "UGC Grade", "Grade Point"};
        eligibilityTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        eligibilityTable = new JTable(eligibilityTableModel);
        UIHelper.styleTable(eligibilityTable);

        JScrollPane scroll = new JScrollPane(eligibilityTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadEligibilityData();
        return panel;
    }

    private void loadEligibilityData() {
        eligibilityTableModel.setRowCount(0);
        Course c = (Course) cmbEligibilityCourse.getSelectedItem();
        if (c == null) return;

        List<StudentEligibilitySummary> list = markService.getBatchEligibility(c.getCourseId());
        for (StudentEligibilitySummary s : list) {
            eligibilityTableModel.addRow(new Object[]{
                s.getStudentRegNo(),
                s.getStudentName(),
                String.format("%.2f%%", s.getEffectiveAttendancePercentage()),
                s.isAttendanceEligible() ? "YES" : "NO",
                String.format("%.2f", s.getCaTotal()),
                s.isCaEligible() ? "YES" : "NO",
                s.isFinalExamEligible() ? "ELIGIBLE TO SIT" : "BARRED",
                String.format("%.2f", s.getOverallMark()),
                s.getLetterGrade(),
                String.format("%.2f", s.getGradePoint())
            });
        }
    }

    
    private JPanel createMaterialsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Course Learning Materials & Resources");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
        topBar.add(lblTitle, BorderLayout.WEST);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnUpload = UIHelper.createPrimaryButton("+ Upload New Material");
        btnUpload.addActionListener(e -> openUploadMaterialDialog());
        btnBar.add(btnUpload);

        JButton btnDelete = UIHelper.createDangerButton("Delete Material");
        btnDelete.addActionListener(e -> deleteSelectedMaterial());
        btnBar.add(btnDelete);

        topBar.add(btnBar, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Course Code", "Course Name", "Material Title", "File / Resource Path", "Uploaded Date"};
        materialsTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        materialsTable = new JTable(materialsTableModel);
        UIHelper.styleTable(materialsTable);

        JScrollPane scroll = new JScrollPane(materialsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        loadMaterialsData();
        return panel;
    }

    private void loadMaterialsData() {
        materialsTableModel.setRowCount(0);
        List<CourseMaterial> list = courseService.getAllMaterials();
        for (CourseMaterial m : list) {
            materialsTableModel.addRow(new Object[]{
                m.getMaterialId(),
                m.getCourseCode(),
                m.getCourseName(),
                m.getTitle(),
                m.getFilePath(),
                m.getUploadedDate()
            });
        }
    }

    private void openUploadMaterialDialog() {
        JDialog dialog = new JDialog(this, "Upload Learning Material", true);
        dialog.setSize(480, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Course> courses = courseService.getAllCourses();
        JComboBox<Course> cmbCourse = new JComboBox<>(courses.toArray(new Course[0]));
        JTextField txtTitle = UIHelper.createStyledTextField(18);
        JTextField txtPath = UIHelper.createStyledTextField(14);
        JButton btnBrowse = UIHelper.createSecondaryButton("Browse...");
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Course Material File");
            int res = chooser.showOpenDialog(dialog);
            if (res == JFileChooser.APPROVE_OPTION) {
                txtPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.setOpaque(false);
        filePanel.add(txtPath, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Course Module:"), gbc);
        gbc.gridx = 1; form.add(cmbCourse, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Material Title:"), gbc);
        gbc.gridx = 1; form.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("File Path:"), gbc);
        gbc.gridx = 1; form.add(filePanel, gbc);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = UIHelper.createPrimaryButton("Upload Material");
        btnSave.addActionListener(e -> {
            Course c = (Course) cmbCourse.getSelectedItem();
            String t = txtTitle.getText().trim();
            String p = txtPath.getText().trim();

            if (c == null || !ValidationUtil.isNotEmpty(t) || !ValidationUtil.isNotEmpty(p)) {
                UIHelper.showWarning(dialog, "Please fill in all material details.", "Validation Warning");
                return;
            }

            CourseMaterial cm = new CourseMaterial();
            cm.setCourseId(c.getCourseId());
            cm.setTitle(t);
            cm.setFilePath(p);

            boolean ok = courseService.addCourseMaterial(cm);
            if (ok) {
                UIHelper.showInfo(dialog, "Learning material uploaded successfully!", "Success");
                loadMaterialsData();
                dialog.dispose();
            } else {
                UIHelper.showError(dialog, "Failed to upload material.", "Error");
            }
        });

        btnBar.add(btnCancel);
        btnBar.add(btnSave);
        dialog.add(btnBar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedMaterial() {
        int row = materialsTable.getSelectedRow();
        if (row < 0) {
            UIHelper.showWarning(this, "Please select a material to delete.", "Selection Required");
            return;
        }
        int id = (int) materialsTableModel.getValueAt(row, 0);
        String title = (String) materialsTableModel.getValueAt(row, 3);
        if (UIHelper.showConfirm(this, "Are you sure you want to delete '" + title + "'?", "Confirm Delete")) {
            boolean ok = courseService.deleteCourseMaterial(id);
            if (ok) {
                loadMaterialsData();
                UIHelper.showInfo(this, "Material deleted successfully.", "Success");
            } else {
                UIHelper.showError(this, "Failed to delete material.", "Error");
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
