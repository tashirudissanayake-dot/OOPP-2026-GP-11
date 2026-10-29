package com.fot.tms.ui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;



public class ProfilePanel extends JPanel {
    private final JTextField txtUsername = createField();
    private final JTextField txtRole = createField();
    private final JTextField txtFirstName = createField();
    private final JTextField txtLastName = createField();
    private final JTextField txtEmail = createField();
    private final JTextField txtContactNo = createField();
    private final JTextField txtPhotoPath = createField();


    public ProfilePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 251));
        setBorder(new EmptyBorder(25, 25, 25, 25));


        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setOpaque(false);
        JLabel title = new JLabel("My Profile & Account Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(new Color(31, 41, 55));
        JLabel subtitle = new JLabel("Manage your contact information and profile settings.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(107, 114, 128));
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(22, 28, 22, 28)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel avatar = new JLabel("U", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(100, 100));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(224, 231, 255));
        avatar.setForeground(new Color(79, 70, 229));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 32));
        avatar.setBorder(new LineBorder(new Color(199, 210, 254), 2, true));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        card.add(avatar, gbc);


        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        addFieldRow(card, gbc, 1, "Username (Registration No):", txtUsername);
        addFieldRow(card, gbc, 2, "Role:", txtRole);
        addFieldRow(card, gbc, 3, "First Name:", txtFirstName);
        addFieldRow(card, gbc, 4, "Last Name:", txtLastName);
        addFieldRow(card, gbc, 5, "Email Address:", txtEmail);
        addFieldRow(card, gbc, 6, "Contact Number:", txtContactNo);

        JPanel photoRow = new JPanel(new BorderLayout(8, 0));
        photoRow.setOpaque(false);
        photoRow.add(txtPhotoPath, BorderLayout.CENTER);
        JButton browseButton = createSecondaryButton("Browse...");
        photoRow.add(browseButton, BorderLayout.EAST);
        addFieldRow(card, gbc, 7, "Profile Picture Path:", photoRow);


        txtUsername.setEditable(false);
        txtRole.setEditable(false);
        txtUsername.setBackground(new Color(241, 245, 249));
        txtRole.setBackground(new Color(241, 245, 249));


        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setOpaque(false);
        buttons.add(createSecondaryButton("Change Password"));
        buttons.add(createPrimaryButton("Save Profile Changes"));
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.weightx = 1;
        card.add(buttons, gbc);


        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setOpaque(false);
        center.add(card);
        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }




    private static void addFieldRow(JPanel panel, GridBagConstraints gbc, int row,
                                    String labelText, Component field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }


    private static JTextField createField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(new Color(31, 41, 55));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        field.setPreferredSize(new Dimension(280, 36));
        return field;
    }


    private static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(79, 70, 229));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }


    private static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(new Color(55, 65, 81));
        button.setBackground(new Color(249, 250, 251));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(9, 14, 9, 14)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Profile UI Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ProfilePanel());
            frame.setSize(900, 760);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


}
