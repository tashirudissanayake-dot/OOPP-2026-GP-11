package com.fot.tms.ui.common;

import com.fot.tms.model.Notice;
import com.fot.tms.model.User;
import com.fot.tms.service.AuthService;
import com.fot.tms.service.NoticeService;
import com.fot.tms.util.UIConstants;
import com.fot.tms.util.UIHelper;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoticesPanel extends JPanel {
    private final NoticeService noticeService;
    private final JPanel noticeListContainer;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    public NoticesPanel() {
        this.noticeService = new NoticeService();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIConstants.BG_MAIN);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblTitle = new JLabel("Faculty & Department Notices");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel lblSub = new JLabel("Latest announcements and official circulars.");
        lblSub.setFont(UIConstants.FONT_REGULAR);
        lblSub.setForeground(UIConstants.TEXT_MUTED);

        JButton btnRefresh = UIHelper.createSecondaryButton("Refresh Notices");
        btnRefresh.addActionListener(e -> refreshNotices());

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        header.add(lblSub, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // List Container
        noticeListContainer = new JPanel();
        noticeListContainer.setLayout(new BoxLayout(noticeListContainer, BoxLayout.Y_AXIS));
        noticeListContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(noticeListContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        refreshNotices();
    }

    public void refreshNotices() {
        noticeListContainer.removeAll();
        User currentUser = AuthService.getInstance().getCurrentUser();
        String role = currentUser != null ? currentUser.getRole() : "All";

        List<Notice> notices;
        if ("Admin".equalsIgnoreCase(role)) {
            notices = noticeService.getAllNotices();
        } else {
            notices = noticeService.getNoticesForRole(role);
        }

        if (notices.isEmpty()) {
            JPanel emptyPanel = UIHelper.createCardPanel();
            emptyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            JLabel lblEmpty = new JLabel("No notices published at this moment.");
            lblEmpty.setFont(UIConstants.FONT_HEADER);
            lblEmpty.setForeground(UIConstants.TEXT_MUTED);
            emptyPanel.add(lblEmpty);
            noticeListContainer.add(emptyPanel);
        } else {
            for (Notice n : notices) {
                JPanel card = createNoticeCard(n);
                noticeListContainer.add(card);
                noticeListContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        noticeListContainer.revalidate();
        noticeListContainer.repaint();
    }

    private JPanel createNoticeCard(Notice notice) {
        JPanel card = new JPanel(new BorderLayout(10, 8));
        card.setBackground(UIConstants.BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, true),
            new EmptyBorder(16, 18, 16, 18)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Top Row: Title + Role Badge + Date
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(notice.getTitle());
        lblTitle.setFont(UIConstants.FONT_HEADER);
        lblTitle.setForeground(UIConstants.PRIMARY_COLOR);

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        metaPanel.setOpaque(false);

        JLabel lblBadge = new JLabel(" " + notice.getTargetRole() + " ");
        lblBadge.setFont(UIConstants.FONT_SMALL);
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(224, 231, 255));
        lblBadge.setForeground(UIConstants.PRIMARY_LIGHT);
        lblBadge.setBorder(new LineBorder(new Color(199, 210, 254), 1, true));

        String dateStr = notice.getPostedDate() != null ? dateFormat.format(notice.getPostedDate()) : "";
        JLabel lblDate = new JLabel(dateStr);
        lblDate.setFont(UIConstants.FONT_SMALL);
        lblDate.setForeground(UIConstants.TEXT_MUTED);

        metaPanel.add(lblBadge);
        metaPanel.add(lblDate);

        topPanel.add(lblTitle, BorderLayout.CENTER);
        topPanel.add(metaPanel, BorderLayout.EAST);

        // Content
        JTextArea txtContent = new JTextArea(notice.getContent());
        txtContent.setFont(UIConstants.FONT_REGULAR);
        txtContent.setForeground(UIConstants.TEXT_PRIMARY);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setEditable(false);
        txtContent.setOpaque(false);
        txtContent.setBorder(null);

        // Author footer
        JLabel lblAuthor = new JLabel("Posted by: " + notice.getAuthorName());
        lblAuthor.setFont(UIConstants.FONT_SMALL);
        lblAuthor.setForeground(UIConstants.TEXT_MUTED);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(txtContent, BorderLayout.CENTER);
        card.add(lblAuthor, BorderLayout.SOUTH);

        return card;
    }
}
