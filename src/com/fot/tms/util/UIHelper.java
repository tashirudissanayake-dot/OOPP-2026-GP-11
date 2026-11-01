package com.fot.tms.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * UI Component Helper to build consistent, clean, modern Swing controls.
 */
public class UIHelper {

    private UIHelper() {}

    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, UIConstants.PRIMARY_LIGHT, UIConstants.TEXT_ON_PRIMARY);
    }

    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, UIConstants.SUCCESS_COLOR, UIConstants.TEXT_ON_PRIMARY);
    }

    public static JButton createDangerButton(String text) {
        return createStyledButton(text, UIConstants.DANGER_COLOR, UIConstants.TEXT_ON_PRIMARY);
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BOLD);
        btn.setBackground(Color.WHITE);
        btn.setForeground(UIConstants.TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(new LineBorder(UIConstants.BORDER_COLOR, 1, true), new EmptyBorder(7, 15, 7, 15)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createStyledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(UIConstants.FONT_REGULAR);
        tf.setBorder(new CompoundBorder(new LineBorder(UIConstants.BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(UIConstants.FONT_REGULAR);
        pf.setBorder(new CompoundBorder(new LineBorder(UIConstants.BORDER_COLOR, 1, true), new EmptyBorder(6, 10, 6, 10)));
        return pf;
    }

    public static <E> JComboBox<E> createStyledComboBox(E[] items) {
        JComboBox<E> cb = new JComboBox<>(items);
        cb.setFont(UIConstants.FONT_REGULAR);
        cb.setBackground(Color.WHITE);
        return cb;
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    public static JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, true),
            new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_SMALL);
        lblTitle.setForeground(UIConstants.TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UIConstants.FONT_CARD_VALUE);
        lblValue.setForeground(accentColor);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);
        return panel;
    }

    public static void styleTable(JTable table) {
        table.setFont(UIConstants.FONT_REGULAR);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(UIConstants.TEXT_PRIMARY);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER_COLOR);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(UIConstants.FONT_BOLD);
        header.setBackground(UIConstants.BG_TABLE_HEADER);
        header.setForeground(UIConstants.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setDefaultRenderer(Number.class, centerRenderer);
    }

    public static void showInfo(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static boolean showConfirm(Component parent, String message, String title) {
        int res = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return res == JOptionPane.YES_OPTION;
    }
}
