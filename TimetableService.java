package com.fot.tms.service;

import com.fot.tms.dao.TimetableDAO;
import com.fot.tms.model.Timetable;
import com.fot.tms.util.ValidationUtil;

import java.util.List;

public class TimetableService {
    private final TimetableDAO timetableDAO;

    public TimetableService() {
        this.timetableDAO = new TimetableDAO();
    }

    public List<Timetable> getAllTimetables() {
        return timetableDAO.findAll();
    }

    public List<Timetable> getTimetablesByDepartment(String department) {
        return timetableDAO.findByDepartment(department);
    }

    public Timetable getTimetableById(int timetableId) {
        return timetableDAO.findById(timetableId);
    }

    public boolean createTimetable(Timetable tt) {
        if (tt == null || tt.getCourseId() <= 0 || !ValidationUtil.isNotEmpty(tt.getDayOfWeek()) || !ValidationUtil.isNotEmpty(tt.getTimeSlot())) {
            return false;
        }
        return timetableDAO.create(tt);
    }

    public boolean updateTimetable(Timetable tt) {
        if (tt == null || tt.getTimetableId() <= 0) return false;
        return timetableDAO.update(tt);
    }

    public boolean deleteTimetable(int timetableId) {
        return timetableDAO.delete(timetableId);
    }
}
