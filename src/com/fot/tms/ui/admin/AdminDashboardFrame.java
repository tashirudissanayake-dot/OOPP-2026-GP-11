package com.fot.tms.ui.admin;

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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;




public class AdminDashboardFrame extends JFrame {
 private final UserService userService;
 private final CourseService courseService;
 private final NoticeService noticeService;
 private final TimetableService timetableService;
 private final AttendanceService attendanceService;
 private final MarkService markService;
 private final GPACalculatorService gpaService;

 private JPanel mainContentPanel;
 private CardLayout cardLayout;


 private DefaultTableModel userTableModel;
 private DefaultTableModel courseTableModel;
 private DefaultTableModel timetableTableModel;
 private DefaultTableModel noticeTableModel;
 private DefaultTableModel reportsTableModel;

 private JTable userTable;
 private JTable courseTable;
 private JTable timetableTable;
 private JTable noticeTable;
 private JTable reportsTable;

 public AdminDashboardFrame() {
 this.userService = new UserService();
 this.courseService = new CourseService();
 this.noticeService = new NoticeService();
 this.timetableService = new TimetableService();
 this.attendanceService = new AttendanceService();
 this.markService = new MarkService();
 this.gpaService = new GPACalculatorService();

 setTitle("Faculty of Technology Management System - Administrator Dashboard");
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 setSize(1200, 750);
 setMinimumSize(new Dimension(1050, 650));
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
 mainContentPanel.add(createUserManagementPanel(), "USERS");
 mainContentPanel.add(createCourseManagementPanel(), "COURSES");
 mainContentPanel.add(createTimetableManagementPanel(), "TIMETABLES");
 mainContentPanel.add(createNoticeManagementPanel(), "NOTICES");
 mainContentPanel.add(createBatchReportsPanel(), "REPORTS");
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



 JLabel lblBrand = new JLabel("FoT-TMS Admin");
 lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
 lblBrand.setForeground(Color.WHITE);
 lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

 JLabel lblSub = new JLabel("System Overseer Portal");
 lblSub.setFont(UIConstants.FONT_SMALL);
 lblSub.setForeground(UIConstants.ACCENT_COLOR);
 lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

 sidebar.add(lblBrand);
 sidebar.add(lblSub);
 sidebar.add(Box.createRigidArea(new Dimension(0, 30)));



 sidebar.add(createNavButton(" Dashboard Overview", "OVERVIEW"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Manage Users", "USERS"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Course Catalog", "COURSES"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Faculty Timetables", "TIMETABLES"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Manage Notices", "NOTICES"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Batch Reports & GPA", "REPORTS"));
 sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
 sidebar.add(createNavButton(" Admin Profile", "PROFILE"));

 sidebar.add(Box.createVerticalGlue());



 JButton btnLogout = UIHelper.createDangerButton(" Sign Out");
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
 if ("USERS".equals(cardName)) loadUserData();
 if ("COURSES".equals(cardName)) loadCourseData();
 if ("TIMETABLES".equals(cardName)) loadTimetableData();
 if ("NOTICES".equals(cardName)) loadNoticeData();
 if ("REPORTS".equals(cardName)) loadReportsData();
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

 User current = AuthService.getInstance().getCurrentUser();
 String name = current != null ? current.getFullName() : "Administrator";

 JLabel lblWelcome = new JLabel("Welcome, " + name + " (Role: Administrator)");
 lblWelcome.setFont(UIConstants.FONT_HEADER);
 lblWelcome.setForeground(UIConstants.TEXT_PRIMARY);

 JLabel lblDate = new JLabel("Faculty of Technology • University of Ruhuna");
 lblDate.setFont(UIConstants.FONT_SMALL);
 lblDate.setForeground(UIConstants.TEXT_MUTED);

 header.add(lblWelcome, BorderLayout.WEST);
 header.add(lblDate, BorderLayout.EAST);
 return header;
 }

 
 
 private JPanel createOverviewPanel() {
 JPanel panel = new JPanel(new BorderLayout(20, 20));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 JPanel statsGrid = new JPanel(new GridLayout(2, 4, 15, 15));
 statsGrid.setOpaque(false);

 List<User> allUsers = userService.getAllUsers();
 long adminCount = allUsers.stream().filter(u -> "Admin".equalsIgnoreCase(u.getRole())).count();
 long lecCount = allUsers.stream().filter(u -> "Lecturer".equalsIgnoreCase(u.getRole())).count();
 long toCount = allUsers.stream().filter(u -> "TO".equalsIgnoreCase(u.getRole())).count();
 long ugCount = allUsers.stream().filter(u -> "Undergraduate".equalsIgnoreCase(u.getRole())).count();

 statsGrid.add(UIHelper.createStatCard("Total Undergraduates", String.valueOf(ugCount), UIConstants.PRIMARY_LIGHT));
 statsGrid.add(UIHelper.createStatCard("Lecturers Assigned", String.valueOf(lecCount), UIConstants.SUCCESS_COLOR));
 statsGrid.add(UIHelper.createStatCard("Technical Officers", String.valueOf(toCount), UIConstants.ACCENT_COLOR));
 statsGrid.add(UIHelper.createStatCard("System Admins", String.valueOf(adminCount), UIConstants.WARNING_COLOR));

 statsGrid.add(UIHelper.createStatCard("Active Courses", String.valueOf(courseService.getAllCourses().size()), UIConstants.PRIMARY_COLOR));
 statsGrid.add(UIHelper.createStatCard("Timetable Sessions", String.valueOf(timetableService.getAllTimetables().size()), UIConstants.INFO_COLOR));
 statsGrid.add(UIHelper.createStatCard("Published Notices", String.valueOf(noticeService.getAllNotices().size()), UIConstants.ACCENT_HOVER));
 statsGrid.add(UIHelper.createStatCard("UGC Circular", "12-2024", UIConstants.SUCCESS_COLOR));

 panel.add(statsGrid, BorderLayout.NORTH);



 JPanel infoCard = UIHelper.createCardPanel();
 infoCard.setLayout(new BorderLayout(15, 15));

 JLabel lblSys = new JLabel("System Operational Status & Compliance Matrix");
 lblSys.setFont(UIConstants.FONT_HEADER);
 lblSys.setForeground(UIConstants.TEXT_PRIMARY);

 JTextArea txtSysInfo = new JTextArea(
 " Multi-tier desktop application: Presentation Layer (Swing), Business Logic Layer, DAO Layer (MySQL).\n" +
 " Database Baseline Supported: 1 Admin, 5 Lecturers, 4 Technical Officers, 20 Undergraduates (FR-06).\n" +
 " Session Engine: 15 Theory & 15 Practical sessions tracking per module (FR-11).\n" +
 " Attendance Criteria: Raw vs Effective percentage (>80%, 80%, <80% with/without medicals) (FR-15, FR-16).\n" +
 " Evaluation Matrix: CA Total >= 40% (FR-19) & Combined Eligibility check >= 80% attendance + CA (FR-20).\n" +
 " Grading Standard: Full UGC Commission Circular No. 12-2024 Scale with SGPA/CGPA computation (FR-22, FR-23).\n" +
 " Security: SHA-256 cryptographic password hashing (NFR-04) & Parameterized SQL queries (NFR-05)."
 );
 txtSysInfo.setFont(UIConstants.FONT_REGULAR);
 txtSysInfo.setEditable(false);
 txtSysInfo.setOpaque(false);
 txtSysInfo.setLineWrap(true);
 txtSysInfo.setWrapStyleWord(true);

 infoCard.add(lblSys, BorderLayout.NORTH);
 infoCard.add(txtSysInfo, BorderLayout.CENTER);

 panel.add(infoCard, BorderLayout.CENTER);
 return panel;
 }


 private JPanel createUserManagementPanel() {
 JPanel panel = new JPanel(new BorderLayout(15, 15));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 
 
 JPanel topBar = new JPanel(new BorderLayout(10, 10));
 topBar.setOpaque(false);

 JLabel lblTitle = new JLabel("User Account Management");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
 topBar.add(lblTitle, BorderLayout.WEST);

 JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
 actionPanel.setOpaque(false);

 JComboBox<String> cmbRoleFilter = new JComboBox<>(new String[]{"All Roles", "Admin", "Lecturer", "TO", "Undergraduate"});
 cmbRoleFilter.addActionListener(e -> filterUsers((String) cmbRoleFilter.getSelectedItem()));
 actionPanel.add(new JLabel("Filter Role:"));
 actionPanel.add(cmbRoleFilter);

 JButton btnAddUser = UIHelper.createPrimaryButton("+ Add New User");
 btnAddUser.addActionListener(e -> openAddUserDialog());
 actionPanel.add(btnAddUser);

 JButton btnResetPass = UIHelper.createSecondaryButton("Reset Password");
 btnResetPass.addActionListener(e -> resetSelectedUserPassword());
 actionPanel.add(btnResetPass);

 JButton btnToggleStatus = UIHelper.createSecondaryButton("Activate/Deactivate");
 btnToggleStatus.addActionListener(e -> toggleSelectedUserStatus());
 actionPanel.add(btnToggleStatus);

 JButton btnDelete = UIHelper.createDangerButton("Delete User");
 btnDelete.addActionListener(e -> deleteSelectedUser());
 actionPanel.add(btnDelete);

 topBar.add(actionPanel, BorderLayout.EAST);
 panel.add(topBar, BorderLayout.NORTH);

 
 
 String[] cols = {"User ID", "Username / Reg No", "Full Name", "Role", "Email", "Contact No", "Status"};
 userTableModel = new DefaultTableModel(cols, 0) {
 @Override
 public boolean isCellEditable(int r, int c) { return false; }
 };
 userTable = new JTable(userTableModel);
 userTable.setAutoCreateRowSorter(true);
 UIHelper.styleTable(userTable);

 JScrollPane scroll = new JScrollPane(userTable);
 scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
 panel.add(scroll, BorderLayout.CENTER);

 loadUserData();
 return panel;
 }

 private void loadUserData() {
 userTableModel.setRowCount(0);
 List<User> list = userService.getAllUsers();
 for (User u : list) {
 Profile p = u.getProfile();
 userTableModel.addRow(new Object[]{
 u.getUserId(),
 u.getUsername(),
 p != null ? p.getFullName() : "",
 u.getRole(),
 u.getEmail(),
 p != null ? p.getContactNo() : "",
 u.getStatus()
 });
 }
 }

 private void filterUsers(String role) {
 userTableModel.setRowCount(0);
 List<User> list = "All Roles".equals(role) ? userService.getAllUsers() : userService.getUsersByRole(role);
 for (User u : list) {
 Profile p = u.getProfile();
 userTableModel.addRow(new Object[]{
 u.getUserId(),
 u.getUsername(),
 p != null ? p.getFullName() : "",
 u.getRole(),
 u.getEmail(),
 p != null ? p.getContactNo() : "",
 u.getStatus()
 });
 }
 }

 private void openAddUserDialog() {
 JDialog dialog = new JDialog(this, "Create New User Account", true);
 dialog.setSize(480, 480);
 dialog.setLocationRelativeTo(this);
 dialog.setLayout(new BorderLayout(15, 15));

 JPanel form = new JPanel(new GridBagLayout());
 form.setBackground(Color.WHITE);
 form.setBorder(new EmptyBorder(20, 25, 20, 25));
 GridBagConstraints gbc = new GridBagConstraints();
 gbc.insets = new Insets(6, 8, 6, 8);
 gbc.fill = GridBagConstraints.HORIZONTAL;

 JTextField txtUn = UIHelper.createStyledTextField(18);
 JPasswordField txtPw = UIHelper.createStyledPasswordField(18);
 JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Undergraduate", "Lecturer", "TO", "Admin"});
 JTextField txtEm = UIHelper.createStyledTextField(18);
 JTextField txtFn = UIHelper.createStyledTextField(18);
 JTextField txtLn = UIHelper.createStyledTextField(18);
 JTextField txtPhone = UIHelper.createStyledTextField(18);

 gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Role:"), gbc);
 gbc.gridx = 1; form.add(cmbRole, gbc);

 gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Username (Reg No):"), gbc);
 gbc.gridx = 1; form.add(txtUn, gbc);

 gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Password:"), gbc);
 gbc.gridx = 1; form.add(txtPw, gbc);

 gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Email Address:"), gbc);
 gbc.gridx = 1; form.add(txtEm, gbc);

 gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("First Name:"), gbc);
 gbc.gridx = 1; form.add(txtFn, gbc);

 gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Last Name:"), gbc);
 gbc.gridx = 1; form.add(txtLn, gbc);

 gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Contact Number:"), gbc);
 gbc.gridx = 1; form.add(txtPhone, gbc);

 dialog.add(form, BorderLayout.CENTER);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
 JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
 btnCancel.addActionListener(e -> dialog.dispose());

 JButton btnSave = UIHelper.createPrimaryButton("Create Account");
 btnSave.addActionListener(e -> {
 String un = txtUn.getText().trim();
 String pw = new String(txtPw.getPassword()).trim();
 String role = (String) cmbRole.getSelectedItem();
 String em = txtEm.getText().trim();
 String fn = txtFn.getText().trim();
 String ln = txtLn.getText().trim();
 String ph = txtPhone.getText().trim();

 if (!ValidationUtil.isNotEmpty(un) || !ValidationUtil.isNotEmpty(pw) || !ValidationUtil.isNotEmpty(em)) {
 UIHelper.showWarning(dialog, "Username, Password, and Email are mandatory fields.", "Validation Warning");
 return;
 }
 if (!ValidationUtil.isValidEmail(em)) {
 UIHelper.showWarning(dialog, "Please enter a valid email address.", "Invalid Email");
 return;
 }

 User user = new User();
 user.setUsername(un);
 user.setRole(role);
 user.setEmail(em);
 user.setStatus("Active");

 Profile prof = new Profile();
 prof.setFirstName(fn);
 prof.setLastName(ln);
 prof.setContactNo(ph);

 boolean ok = userService.createUser(user, prof, pw);
 if (ok) {
 UIHelper.showInfo(dialog, "User account created successfully!", "Success");
 loadUserData();
 dialog.dispose();
 } else {
 UIHelper.showError(dialog, "Failed to create user. Username may already exist.", "Error");
 }
 });

 btnBar.add(btnCancel);
 btnBar.add(btnSave);
 dialog.add(btnBar, BorderLayout.SOUTH);
 dialog.setVisible(true);
 }

 private void resetSelectedUserPassword() {
 int row = userTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a user from the table first.", "Selection Required");
 return;
 }
 int modelRow = userTable.convertRowIndexToModel(row);
 int userId = (int) userTableModel.getValueAt(modelRow, 0);
 String username = (String) userTableModel.getValueAt(modelRow, 1);

 String newPass = JOptionPane.showInputDialog(this, "Enter new password for user '" + username + "':", "Reset Password (UC-A5)", JOptionPane.QUESTION_MESSAGE);
 if (newPass != null && !newPass.trim().isEmpty()) {
 boolean ok = userService.resetPassword(userId, newPass.trim());
 if (ok) {
 UIHelper.showInfo(this, "Password for '" + username + "' has been reset successfully.", "Success");
 } else {
 UIHelper.showError(this, "Failed to reset password.", "Error");
 }
 }
 }

 private void toggleSelectedUserStatus() {
 int row = userTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a user from the table first.", "Selection Required");
 return;
 }
 int modelRow = userTable.convertRowIndexToModel(row);
 int userId = (int) userTableModel.getValueAt(modelRow, 0);
 String role = (String) userTableModel.getValueAt(modelRow, 3);
 User current = AuthService.getInstance().getCurrentUser();

 if (current != null && current.getUserId() == userId) {
 UIHelper.showWarning(this, "Security Violation: You cannot deactivate your own administrative account!", "Action Denied");
 return;
 }
 if ("Admin".equalsIgnoreCase(role)) {
 UIHelper.showWarning(this, "Administrator accounts cannot be deactivated.", "Action Denied");
 return;
 }

 String curStatus = (String) userTableModel.getValueAt(row, 6);
 String newStatus = "Active".equalsIgnoreCase(curStatus) ? "Inactive" : "Active";

 boolean ok = userService.updateStatus(userId, newStatus);
 if (ok) {
 loadUserData();
 UIHelper.showInfo(this, "User status updated to: " + newStatus, "Status Updated");
 } else {
 UIHelper.showError(this, "Failed to update user status.", "Error");
 }
 }

 private void deleteSelectedUser() {
 int row = userTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a user to delete.", "Selection Required");
 return;
 }
 int modelRow = userTable.convertRowIndexToModel(row);
 int userId = (int) userTableModel.getValueAt(modelRow, 0);
 String username = (String) userTableModel.getValueAt(modelRow, 1);
 String role = (String) userTableModel.getValueAt(modelRow, 3);
 User current = AuthService.getInstance().getCurrentUser();

 if (current != null && current.getUserId() == userId) {
 UIHelper.showWarning(this, "Security Violation: You cannot delete your own administrative account!", "Action Denied");
 return;
 }
 if ("Admin".equalsIgnoreCase(role)) {
 UIHelper.showWarning(this, "Administrator accounts cannot be deleted to prevent system lockout.", "Action Denied");
 return;
 }

 if (UIHelper.showConfirm(this, "Are you sure you want to permanently delete user '" + username + "'?\nThis will remove related profile, marks, and attendance data.", "Confirm Delete")) {
 boolean ok = userService.deleteUser(userId);
 if (ok) {
 loadUserData();
 UIHelper.showInfo(this, "User deleted successfully.", "Success");
 } else {
 UIHelper.showError(this, "Failed to delete user.", "Error");
 }
 }
 }



 private JPanel createCourseManagementPanel() {
 JPanel panel = new JPanel(new BorderLayout(15, 15));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 JPanel topBar = new JPanel(new BorderLayout(10, 10));
 topBar.setOpaque(false);

 JLabel lblTitle = new JLabel("Course Catalog & Evaluation Criteria");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
 topBar.add(lblTitle, BorderLayout.WEST);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
 btnBar.setOpaque(false);

 JButton btnAddCourse = UIHelper.createPrimaryButton("+ Add New Course");
 btnAddCourse.addActionListener(e -> openCourseDialog(null));
 btnBar.add(btnAddCourse);

 JButton btnEditCourse = UIHelper.createSecondaryButton("Edit Course");
 btnEditCourse.addActionListener(e -> {
 int row = courseTable.getSelectedRow();
 if (row >= 0) {
 int modelRow = courseTable.convertRowIndexToModel(row);
 int id = (int) courseTableModel.getValueAt(modelRow, 0);
 Course c = courseService.getCourseById(id);
 openCourseDialog(c);
 } else {
 UIHelper.showWarning(this, "Please select a course to edit.", "Selection Required");
 }
 });
 btnBar.add(btnEditCourse);

 JButton btnDeleteCourse = UIHelper.createDangerButton("Delete Course");
 btnDeleteCourse.addActionListener(e -> deleteSelectedCourse());
 btnBar.add(btnDeleteCourse);

 topBar.add(btnBar, BorderLayout.EAST);
 panel.add(topBar, BorderLayout.NORTH);

 String[] cols = {"ID", "Course Code", "Course Name", "Credits", "Semester", "CA Weight (%)", "Final Weight (%)", "Coordinator"};
 courseTableModel = new DefaultTableModel(cols, 0) {
 @Override public boolean isCellEditable(int r, int c) { return false; }
 };
 courseTable = new JTable(courseTableModel);
 courseTable.setAutoCreateRowSorter(true);
 UIHelper.styleTable(courseTable);

 JScrollPane scroll = new JScrollPane(courseTable);
 scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
 panel.add(scroll, BorderLayout.CENTER);

 loadCourseData();
 return panel;
 }

 private void loadCourseData() {
 courseTableModel.setRowCount(0);
 List<Course> list = courseService.getAllCourses();
 for (Course c : list) {
 courseTableModel.addRow(new Object[]{
 c.getCourseId(),
 c.getCourseCode(),
 c.getCourseName(),
 c.getCredits(),
 c.getSemester(),
 c.getCaWeight() + "%",
 c.getFinalWeight() + "%",
 c.getCoordinatorName()
 });
 }
 }

 private void openCourseDialog(Course courseToEdit) {
 boolean isEdit = courseToEdit != null;
 JDialog dialog = new JDialog(this, isEdit ? "Edit Course Details" : "Create New Course", true);
 dialog.setSize(480, 420);
 dialog.setLocationRelativeTo(this);
 dialog.setLayout(new BorderLayout(15, 15));

 JPanel form = new JPanel(new GridBagLayout());
 form.setBackground(Color.WHITE);
 form.setBorder(new EmptyBorder(20, 25, 20, 25));
 GridBagConstraints gbc = new GridBagConstraints();
 gbc.insets = new Insets(6, 8, 6, 8);
 gbc.fill = GridBagConstraints.HORIZONTAL;

 JTextField txtCode = UIHelper.createStyledTextField(18);
 JTextField txtName = UIHelper.createStyledTextField(18);
 JSpinner spinCredits = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
 JSpinner spinSem = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
 JSpinner spinCA = new JSpinner(new SpinnerNumberModel(40.0, 0.0, 100.0, 5.0));
 JSpinner spinFinal = new JSpinner(new SpinnerNumberModel(60.0, 0.0, 100.0, 5.0));

 List<User> lecturers = userService.getUsersByRole("Lecturer");
 JComboBox<User> cmbLecturers = new JComboBox<>();
 cmbLecturers.addItem(null);
 
 for (User u : lecturers) {
 cmbLecturers.addItem(u);
 }
 cmbLecturers.setRenderer(new DefaultListCellRenderer() {
 @Override
 public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
 super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
 if (value instanceof User) {
 User u = (User) value;
 setText(u.getFullName() != null && !u.getFullName().isEmpty() ? u.getFullName() + " (" + u.getUsername() + ")" : u.getUsername());
 } else {
 setText("-- None (Unassigned) --");
 }
 return this;
 }
 });

 if (isEdit) {
 txtCode.setText(courseToEdit.getCourseCode());
 txtName.setText(courseToEdit.getCourseName());
 spinCredits.setValue(courseToEdit.getCredits());
 spinSem.setValue(courseToEdit.getSemester());
 spinCA.setValue(courseToEdit.getCaWeight());
 spinFinal.setValue(courseToEdit.getFinalWeight());
 if (courseToEdit.getCoordinatorId() != null) {
 for (int i = 0; i < cmbLecturers.getItemCount(); i++) {
 User u = cmbLecturers.getItemAt(i);
 if (u != null && u.getUserId() == courseToEdit.getCoordinatorId()) {
 cmbLecturers.setSelectedIndex(i);
 break;
 }
 }
 }
 }

 gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Course Code (e.g. ICT2123):"), gbc);
 gbc.gridx = 1; form.add(txtCode, gbc);

 gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Course Name:"), gbc);
 gbc.gridx = 1; form.add(txtName, gbc);

 gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Credits:"), gbc);
 gbc.gridx = 1; form.add(spinCredits, gbc);

 gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Semester:"), gbc);
 gbc.gridx = 1; form.add(spinSem, gbc);

 gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("CA Weight (%):"), gbc);
 gbc.gridx = 1; form.add(spinCA, gbc);

 gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Final Exam Weight (%):"), gbc);
 gbc.gridx = 1; form.add(spinFinal, gbc);

 gbc.gridx = 0; gbc.gridy = 6; form.add(new JLabel("Course Coordinator:"), gbc);
 gbc.gridx = 1; form.add(cmbLecturers, gbc);

 dialog.add(form, BorderLayout.CENTER);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
 JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
 btnCancel.addActionListener(e -> dialog.dispose());

 JButton btnSave = UIHelper.createPrimaryButton(isEdit ? "Update Course" : "Save Course");
 btnSave.addActionListener(e -> {
 String code = txtCode.getText().trim();
 String name = txtName.getText().trim();
 int cr = (int) spinCredits.getValue();
 int sem = (int) spinSem.getValue();
 double ca = (double) spinCA.getValue();
 double fin = (double) spinFinal.getValue();
 User selLec = (User) cmbLecturers.getSelectedItem();

 if (!ValidationUtil.isNotEmpty(code) || !ValidationUtil.isNotEmpty(name)) {
 UIHelper.showWarning(dialog, "Course Code and Name are mandatory.", "Validation Warning");
 return;
 }

 if (Math.abs((ca + fin) - 100.0) > 0.01) {
 UIHelper.showWarning(dialog, "CA Weight and Final Weight must sum up to 100%. (Current sum: " + (ca + fin) + "%)", "Weighting Error");
 return;
 }

 Course course = isEdit ? courseToEdit : new Course();
 course.setCourseCode(code);
 course.setCourseName(name);
 course.setCredits(cr);
 course.setSemester(sem);
 course.setCaWeight(ca);
 course.setFinalWeight(fin);
 course.setCoordinatorId(selLec != null ? selLec.getUserId() : null);

 boolean ok = isEdit ? courseService.updateCourse(course) : courseService.createCourse(course);
 if (ok) {
 UIHelper.showInfo(dialog, "Course saved successfully!", "Success");
 loadCourseData();
 dialog.dispose();
 } else {
 UIHelper.showError(dialog, "Failed to save course. Ensure course code is unique.", "Error");
 }
 });

 btnBar.add(btnCancel);
 btnBar.add(btnSave);
 dialog.add(btnBar, BorderLayout.SOUTH);
 dialog.setVisible(true);
 }

 private void deleteSelectedCourse() {
 int row = courseTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a course to delete.", "Selection Required");
 return;
 }
 int modelRow = courseTable.convertRowIndexToModel(row);
 int id = (int) courseTableModel.getValueAt(modelRow, 0);
 String code = (String) courseTableModel.getValueAt(modelRow, 1);

 if (UIHelper.showConfirm(this, "Delete course " + code + "? This will remove associated timetables and marks.", "Confirm Delete")) {
 boolean ok = courseService.deleteCourse(id);
 if (ok) {
 loadCourseData();
 UIHelper.showInfo(this, "Course deleted successfully.", "Success");
 } else {
 UIHelper.showError(this, "Failed to delete course.", "Error");
 }
 }
 }


 private JPanel createTimetableManagementPanel() {
 JPanel panel = new JPanel(new BorderLayout(15, 15));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 JPanel topBar = new JPanel(new BorderLayout(10, 10));
 topBar.setOpaque(false);

 JLabel lblTitle = new JLabel("Faculty & Department Timetable Scheduling");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
 topBar.add(lblTitle, BorderLayout.WEST);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
 btnBar.setOpaque(false);

 JButton btnAdd = UIHelper.createPrimaryButton("+ Add Timetable Entry");
 btnAdd.addActionListener(e -> openTimetableDialog(null));
 btnBar.add(btnAdd);

 JButton btnEdit = UIHelper.createSecondaryButton("Edit Entry");
 btnEdit.addActionListener(e -> {
 int row = timetableTable.getSelectedRow();
 if (row >= 0) {
 int modelRow = timetableTable.convertRowIndexToModel(row);
 int id = (int) timetableTableModel.getValueAt(modelRow, 0);
 Timetable tt = timetableService.getTimetableById(id);
 openTimetableDialog(tt);
 } else {
 UIHelper.showWarning(this, "Please select a timetable row to edit.", "Selection Required");
 }
 });
 btnBar.add(btnEdit);

 JButton btnDelete = UIHelper.createDangerButton("Delete Entry");
 btnDelete.addActionListener(e -> deleteSelectedTimetable());
 btnBar.add(btnDelete);

 topBar.add(btnBar, BorderLayout.EAST);
 panel.add(topBar, BorderLayout.NORTH);

 String[] cols = {"ID", "Course Code", "Course Name", "Session Type", "Day of Week", "Time Slot", "Venue", "Department"};
 timetableTableModel = new DefaultTableModel(cols, 0) {
 @Override public boolean isCellEditable(int r, int c) { return false; }
 };
 timetableTable = new JTable(timetableTableModel);
 timetableTable.setAutoCreateRowSorter(true);
 UIHelper.styleTable(timetableTable);

 JScrollPane scroll = new JScrollPane(timetableTable);
 scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
 panel.add(scroll, BorderLayout.CENTER);

 loadTimetableData();
 return panel;
 }

 private void loadTimetableData() {
 timetableTableModel.setRowCount(0);
 List<Timetable> list = timetableService.getAllTimetables();
 for (Timetable t : list) {
 timetableTableModel.addRow(new Object[]{
 t.getTimetableId(),
 t.getCourseCode(),
 t.getCourseName(),
 t.getSessionType(),
 t.getDayOfWeek(),
 t.getTimeSlot(),
 t.getVenue(),
 t.getDepartment()
 });
 }
 }

 private void openTimetableDialog(Timetable timetableToEdit) {
 boolean isEdit = timetableToEdit != null;
 JDialog dialog = new JDialog(this, isEdit ? "Edit Timetable Entry" : "Add Timetable Entry", true);
 dialog.setSize(480, 420);
 dialog.setLocationRelativeTo(this);
 dialog.setLayout(new BorderLayout(15, 15));

 JPanel form = new JPanel(new GridBagLayout());
 form.setBackground(Color.WHITE);
 form.setBorder(new EmptyBorder(20, 25, 20, 25));
 GridBagConstraints gbc = new GridBagConstraints();
 gbc.insets = new Insets(6, 8, 6, 8);
 gbc.fill = GridBagConstraints.HORIZONTAL;

 List<Course> courses = courseService.getAllCourses();
 JComboBox<Course> cmbCourses = new JComboBox<>(courses.toArray(new Course[0]));
 JComboBox<String> cmbType = new JComboBox<>(new String[]{"Theory", "Practical"});
 JComboBox<String> cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
 JTextField txtTime = UIHelper.createStyledTextField(18);
 txtTime.setText("08:00 - 10:00");
 JTextField txtVenue = UIHelper.createStyledTextField(18);
 txtVenue.setText("LT-01");
 JTextField txtDept = UIHelper.createStyledTextField(18);
 txtDept.setText("Information and Communication Technology");

 if (isEdit) {
 for (int i = 0; i < cmbCourses.getItemCount(); i++) {
 Course c = cmbCourses.getItemAt(i);
 if (c != null && c.getCourseId() == timetableToEdit.getCourseId()) {
 cmbCourses.setSelectedIndex(i);
 break;
 }
 }
 cmbType.setSelectedItem(timetableToEdit.getSessionType());
 cmbDay.setSelectedItem(timetableToEdit.getDayOfWeek());
 txtTime.setText(timetableToEdit.getTimeSlot());
 txtVenue.setText(timetableToEdit.getVenue());
 txtDept.setText(timetableToEdit.getDepartment());
 }

 gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Course:"), gbc);
 gbc.gridx = 1; form.add(cmbCourses, gbc);

 gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Session Type:"), gbc);
 gbc.gridx = 1; form.add(cmbType, gbc);

 gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Day of Week:"), gbc);
 gbc.gridx = 1; form.add(cmbDay, gbc);

 gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Time Slot:"), gbc);
 gbc.gridx = 1; form.add(txtTime, gbc);

 gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Venue:"), gbc);
 gbc.gridx = 1; form.add(txtVenue, gbc);

 gbc.gridx = 0; gbc.gridy = 5; form.add(new JLabel("Department:"), gbc);
 gbc.gridx = 1; form.add(txtDept, gbc);

 dialog.add(form, BorderLayout.CENTER);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
 JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
 btnCancel.addActionListener(e -> dialog.dispose());

 JButton btnSave = UIHelper.createPrimaryButton(isEdit ? "Update Timetable" : "Save Timetable Entry");
 btnSave.addActionListener(e -> {
 Course c = (Course) cmbCourses.getSelectedItem();
 if (c == null) return;
 Timetable tt = isEdit ? timetableToEdit : new Timetable();
 tt.setCourseId(c.getCourseId());
 tt.setSessionType((String) cmbType.getSelectedItem());
 tt.setDayOfWeek((String) cmbDay.getSelectedItem());
 tt.setTimeSlot(txtTime.getText().trim());
 tt.setVenue(txtVenue.getText().trim());
 tt.setDepartment(txtDept.getText().trim());

 boolean ok = isEdit ? timetableService.updateTimetable(tt) : timetableService.createTimetable(tt);
 if (ok) {
 UIHelper.showInfo(dialog, isEdit ? "Timetable entry updated successfully!" : "Timetable entry added successfully!", "Success");
 loadTimetableData();
 dialog.dispose();
 } else {
 UIHelper.showError(dialog, isEdit ? "Failed to update timetable entry." : "Failed to create timetable entry.", "Error");
 }
 });

 btnBar.add(btnCancel);
 btnBar.add(btnSave);
 dialog.add(btnBar, BorderLayout.SOUTH);
 dialog.setVisible(true);
 }

 private void deleteSelectedTimetable() {
 int row = timetableTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a timetable row to delete.", "Selection Required");
 return;
 }
 int modelRow = timetableTable.convertRowIndexToModel(row);
 int id = (int) timetableTableModel.getValueAt(modelRow, 0);
 if (UIHelper.showConfirm(this, "Delete selected timetable session?", "Confirm Delete")) {
 boolean ok = timetableService.deleteTimetable(id);
 if (ok) {
 loadTimetableData();
 UIHelper.showInfo(this, "Timetable entry deleted successfully.", "Success");
 } else {
 UIHelper.showError(this, "Failed to delete timetable entry.", "Error");
 }
 }
 }



 private JPanel createNoticeManagementPanel() {
 JPanel panel = new JPanel(new BorderLayout(15, 15));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 JPanel topBar = new JPanel(new BorderLayout(10, 10));
 topBar.setOpaque(false);

 JLabel lblTitle = new JLabel("Notice & Announcement Broadcasting");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
 topBar.add(lblTitle, BorderLayout.WEST);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
 btnBar.setOpaque(false);

 JButton btnAdd = UIHelper.createPrimaryButton("+ Publish Notice");
 btnAdd.addActionListener(e -> openPublishNoticeDialog(null));
 btnBar.add(btnAdd);

 JButton btnEdit = UIHelper.createSecondaryButton("Edit Notice");
 btnEdit.addActionListener(e -> {
 int row = noticeTable.getSelectedRow();
 if (row >= 0) {
 int modelRow = noticeTable.convertRowIndexToModel(row);
 int id = (int) noticeTableModel.getValueAt(modelRow, 0);
 Notice n = noticeService.getNoticeById(id);
 openPublishNoticeDialog(n);
 } else {
 UIHelper.showWarning(this, "Please select a notice to edit.", "Selection Required");
 }
 });
 btnBar.add(btnEdit);

 JButton btnDelete = UIHelper.createDangerButton("Delete Notice");
 btnDelete.addActionListener(e -> deleteSelectedNotice());
 btnBar.add(btnDelete);

 topBar.add(btnBar, BorderLayout.EAST);
 panel.add(topBar, BorderLayout.NORTH);

 String[] cols = {"ID", "Title", "Target Audience", "Author", "Posted Date"};
 noticeTableModel = new DefaultTableModel(cols, 0) {
 @Override public boolean isCellEditable(int r, int c) { return false; }
 };
 noticeTable = new JTable(noticeTableModel);
 noticeTable.setAutoCreateRowSorter(true);
 UIHelper.styleTable(noticeTable);

 JScrollPane scroll = new JScrollPane(noticeTable);
 scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
 panel.add(scroll, BorderLayout.CENTER);

 loadNoticeData();
 return panel;
 }

 private void loadNoticeData() {
 noticeTableModel.setRowCount(0);
 List<Notice> list = noticeService.getAllNotices();
 for (Notice n : list) {
 noticeTableModel.addRow(new Object[]{
 n.getNoticeId(),
 n.getTitle(),
 n.getTargetRole(),
 n.getAuthorName(),
 n.getPostedDate()
 });
 }
 }

 private void openPublishNoticeDialog(Notice noticeToEdit) {
 boolean isEdit = noticeToEdit != null;
 JDialog dialog = new JDialog(this, isEdit ? "Edit Official Notice" : "Publish New Official Notice", true);
 dialog.setSize(520, 420);
 dialog.setLocationRelativeTo(this);
 dialog.setLayout(new BorderLayout(15, 15));

 JPanel form = new JPanel(new BorderLayout(10, 10));
 form.setBackground(Color.WHITE);
 form.setBorder(new EmptyBorder(20, 20, 20, 20));

 JPanel topFields = new JPanel(new GridLayout(2, 2, 10, 10));
 topFields.setOpaque(false);

 JTextField txtTitle = UIHelper.createStyledTextField(20);
 JComboBox<String> cmbTarget = new JComboBox<>(new String[]{"All", "Undergraduate", "Lecturer", "TO"});

 topFields.add(new JLabel("Notice Title:"));
 topFields.add(txtTitle);
 topFields.add(new JLabel("Target Audience:"));
 topFields.add(cmbTarget);

 form.add(topFields, BorderLayout.NORTH);

 JTextArea txtContent = new JTextArea();
 txtContent.setFont(UIConstants.FONT_REGULAR);
 txtContent.setLineWrap(true);
 txtContent.setWrapStyleWord(true);
 txtContent.setBorder(new LineBorder(UIConstants.BORDER_COLOR, 1));

 if (isEdit) {
 txtTitle.setText(noticeToEdit.getTitle());
 cmbTarget.setSelectedItem(noticeToEdit.getTargetRole());
 txtContent.setText(noticeToEdit.getContent());
 }

 JScrollPane contentScroll = new JScrollPane(txtContent);
 contentScroll.setBorder(BorderFactory.createTitledBorder("Notice Content / Announcement:"));
 form.add(contentScroll, BorderLayout.CENTER);

 dialog.add(form, BorderLayout.CENTER);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
 JButton btnCancel = UIHelper.createSecondaryButton("Cancel");
 btnCancel.addActionListener(e -> dialog.dispose());

 JButton btnPub = UIHelper.createPrimaryButton(isEdit ? "Update Notice" : "Publish Now");
 btnPub.addActionListener(e -> {
 String t = txtTitle.getText().trim();
 String c = txtContent.getText().trim();
 String target = (String) cmbTarget.getSelectedItem();

 if (!ValidationUtil.isNotEmpty(t) || !ValidationUtil.isNotEmpty(c)) {
 UIHelper.showWarning(dialog, "Title and Content cannot be empty.", "Validation Warning");
 return;
 }

 User user = AuthService.getInstance().getCurrentUser();
 Notice n = isEdit ? noticeToEdit : new Notice();
 if (!isEdit) {
 n.setAuthorId(user != null ? user.getUserId() : 1);
 }
 n.setTitle(t);
 n.setContent(c);
 n.setTargetRole(target);

 boolean ok = isEdit ? noticeService.updateNotice(n) : noticeService.createNotice(n);
 if (ok) {
 UIHelper.showInfo(dialog, isEdit ? "Notice updated successfully!" : "Notice published successfully!", "Success");
 loadNoticeData();
 dialog.dispose();
 } else {
 UIHelper.showError(dialog, isEdit ? "Failed to update notice." : "Failed to publish notice.", "Error");
 }
 });

 btnBar.add(btnCancel);
 btnBar.add(btnPub);
 dialog.add(btnBar, BorderLayout.SOUTH);
 dialog.setVisible(true);
 }

 private void deleteSelectedNotice() {
 int row = noticeTable.getSelectedRow();
 if (row < 0) {
 UIHelper.showWarning(this, "Please select a notice to delete.", "Selection Required");
 return;
 }
 int modelRow = noticeTable.convertRowIndexToModel(row);
 int id = (int) noticeTableModel.getValueAt(modelRow, 0);
 if (UIHelper.showConfirm(this, "Delete selected notice?", "Confirm Delete")) {
 boolean ok = noticeService.deleteNotice(id);
 if (ok) {
 loadNoticeData();
 UIHelper.showInfo(this, "Notice deleted successfully.", "Success");
 } else {
 UIHelper.showError(this, "Failed to delete notice.", "Error");
 }
 }
 }


 private JPanel createBatchReportsPanel() {
 JPanel panel = new JPanel(new BorderLayout(15, 15));
 panel.setBackground(UIConstants.BG_MAIN);
 panel.setBorder(new EmptyBorder(25, 25, 25, 25));

 JPanel topBar = new JPanel(new BorderLayout(10, 10));
 topBar.setOpaque(false);

 JLabel lblTitle = new JLabel("Batch Academic Performance & GPA Report");
 lblTitle.setFont(UIConstants.FONT_TITLE);
 lblTitle.setForeground(UIConstants.TEXT_PRIMARY);
 topBar.add(lblTitle, BorderLayout.WEST);

 JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
 btnBar.setOpaque(false);

 JButton btnExport = UIHelper.createSuccessButton(" Export to CSV");
 btnExport.addActionListener(e -> ReportExportHelper.exportTableToCSV(this, reportsTable, "Batch_GPA_Report.csv"));
 btnBar.add(btnExport);

 JButton btnRefresh = UIHelper.createSecondaryButton("Refresh Report");
 btnRefresh.addActionListener(e -> loadReportsData());
 btnBar.add(btnRefresh);

 topBar.add(btnBar, BorderLayout.EAST);
 panel.add(topBar, BorderLayout.NORTH);

 String[] cols = {"Reg No", "Student Name", "Semester", "Total Credits", "Quality Points", "SGPA", "CGPA (Cumulative)", "Academic Standing"};
 reportsTableModel = new DefaultTableModel(cols, 0) {
 @Override public boolean isCellEditable(int r, int c) { return false; }
 };
 reportsTable = new JTable(reportsTableModel);
 UIHelper.styleTable(reportsTable);

 JScrollPane scroll = new JScrollPane(reportsTable);
 scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
 panel.add(scroll, BorderLayout.CENTER);

 loadReportsData();
 return panel;
 }

 private void loadReportsData() {
 reportsTableModel.setRowCount(0);
 List<GPACalculatorService.StudentGPASummary> list = gpaService.getBatchGPASummaries(1);
 for (GPACalculatorService.StudentGPASummary s : list) {
 reportsTableModel.addRow(new Object[]{
 s.getStudentRegNo(),
 s.getStudentName(),
 "Semester " + s.getSemester(),
 s.getTotalCredits(),
 String.format("%.2f", s.getTotalQualityPoints()),
 String.format("%.2f", s.getSgpa()),
 String.format("%.2f", s.getCgpa()),
 s.getAcademicStanding()
 });
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
