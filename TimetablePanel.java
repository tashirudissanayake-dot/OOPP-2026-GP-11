package com.fot.tms.ui.common;

import com.fot.tms.model.Timetable;
import com.fot.tms.model.User;
import com.fot.tms.service.AuthService;
import com.fot.tms.service.TimetableService;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Reusable Timetable View Panel with department and day filtering (FR-10).
 */
public class TimetablePanel extends JPanel {
    private final TimetableService timetableService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private JComboBox<String> cmbDayFilter;
    private JComboBox<String> cmbTypeFilter;

    public TimetablePanel() {
        this.timetableService = new TimetableService();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIConstants.BG_MAIN);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header & Controls
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Academic Timetable & Schedule");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

        User user = AuthService.getInstance().getCurrentUser();
        String subtitle = "View weekly lectures and practical lab sessions.";
        if (user != null && "TO".equalsIgnoreCase(user.getRole())) {
            subtitle = "Department-specific timetable schedule for Technical Officers.";
        }
        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(UIConstants.FONT_REGULAR);
        lblSub.setForeground(UIConstants.TEXT_MUTED);

        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSub, BorderLayout.SOUTH);
        header.add(titlePanel, BorderLayout.WEST);

        // Filter Bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        filterPanel.add(new JLabel("Day:"));
        cmbDayFilter = new JComboBox<>(new String[]{"All Days", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        cmbDayFilter.addActionListener(e -> applyFilter());
        filterPanel.add(cmbDayFilter);

        filterPanel.add(new JLabel("Type:"));
        cmbTypeFilter = new JComboBox<>(new String[]{"All Types", "Theory", "Practical"});
        cmbTypeFilter.addActionListener(e -> applyFilter());
        filterPanel.add(cmbTypeFilter);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh");
        btnRefresh.addActionListener(e -> loadTimetableData());
        filterPanel.add(btnRefresh);

        header.add(filterPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Course Code", "Course Name", "Session Type", "Day of Week", "Time Slot", "Venue", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UIHelper.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        loadTimetableData();
    }

    public void loadTimetableData() {
        tableModel.setRowCount(0);
        User user = AuthService.getInstance().getCurrentUser();
        List<Timetable> list;

        // Technical Officers view department-specific timetables (FR-10)
        if (user != null && "TO".equalsIgnoreCase(user.getRole())) {
            list = timetableService.getTimetablesByDepartment("Information and Communication Technology");
        } else {
            list = timetableService.getAllTimetables();
        }

        String selDay = cmbDayFilter != null ? (String) cmbDayFilter.getSelectedItem() : "All Days";
        String selType = cmbTypeFilter != null ? (String) cmbTypeFilter.getSelectedItem() : "All Types";

        for (Timetable t : list) {
            if (!"All Days".equals(selDay) && !t.getDayOfWeek().equalsIgnoreCase(selDay)) {
                continue;
            }
            if (!"All Types".equals(selType) && !t.getSessionType().equalsIgnoreCase(selType)) {
                continue;
            }

            tableModel.addRow(new Object[]{
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

    private void applyFilter() {
        loadTimetableData();
    }
}
