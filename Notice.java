package com.fot.tms.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {
    private int noticeId;
    private int authorId;
    private String authorName;
    private String title;
    private String content;
    private String targetRole; // 'All', 'Lecturer', 'TO', 'Undergraduate'
    private Timestamp postedDate;

    public Notice() {}

    public Notice(int noticeId, int authorId, String title, String content, String targetRole, Timestamp postedDate) {
        this.noticeId = noticeId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.targetRole = targetRole;
        this.postedDate = postedDate;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName != null ? authorName : "Administration";
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return title + " [" + targetRole + "]";
    }
}
