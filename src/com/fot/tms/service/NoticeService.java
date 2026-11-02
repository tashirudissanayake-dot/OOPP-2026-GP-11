package com.fot.tms.service;

import com.fot.tms.dao.NoticeDAO;
import com.fot.tms.model.Notice;
import com.fot.tms.util.ValidationUtil;

import java.util.List;

public class NoticeService {
    private final NoticeDAO noticeDAO;

    public NoticeService() {
        this.noticeDAO = new NoticeDAO();
    }

    public List<Notice> getAllNotices() {
        return noticeDAO.findAll();
    }

    public Notice getNoticeById(int noticeId) {
        return noticeDAO.findById(noticeId);
    }

    public List<Notice> getNoticesForRole(String role) {
        return noticeDAO.findByTargetRole(role);
    }

    public boolean createNotice(Notice notice) {
        if (notice == null || !ValidationUtil.isNotEmpty(notice.getTitle()) || !ValidationUtil.isNotEmpty(notice.getContent())) {
            return false;
        }
        return noticeDAO.create(notice);
    }

    public boolean updateNotice(Notice notice) {
        if (notice == null || notice.getNoticeId() <= 0) return false;
        return noticeDAO.update(notice);
    }

    public boolean deleteNotice(int noticeId) {
        return noticeDAO.delete(noticeId);
    }
}
