package com.fot.tms.service;

import com.fot.tms.dao.CourseDAO;
import com.fot.tms.dao.CourseMaterialDAO;
import com.fot.tms.model.Course;
import com.fot.tms.model.CourseMaterial;
import com.fot.tms.util.ValidationUtil;

import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;
    private final CourseMaterialDAO materialDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
        this.materialDAO = new CourseMaterialDAO();
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public Course getCourseById(int courseId) {
        return courseDAO.findById(courseId);
    }

    public List<Course> getCoursesByCoordinator(int lecturerId) {
        return courseDAO.findByCoordinatorId(lecturerId);
    }

    public boolean createCourse(Course course) {
        if (course == null || !ValidationUtil.isNotEmpty(course.getCourseCode()) || !ValidationUtil.isNotEmpty(course.getCourseName())) {
            return false;
        }
        return courseDAO.create(course);
    }

    public boolean updateCourse(Course course) {
        if (course == null || course.getCourseId() <= 0) return false;
        return courseDAO.update(course);
    }

    public boolean deleteCourse(int courseId) {
        return courseDAO.delete(courseId);
    }

    public List<CourseMaterial> getMaterialsByCourse(int courseId) {
        return materialDAO.findByCourseId(courseId);
    }

    public List<CourseMaterial> getAllMaterials() {
        return materialDAO.findAll();
    }

    public CourseMaterial getMaterialById(int materialId) {
        return materialDAO.findById(materialId);
    }

    public boolean addCourseMaterial(CourseMaterial material) {
        if (material == null || !ValidationUtil.isNotEmpty(material.getTitle())) {
            return false;
        }
        return materialDAO.create(material);
    }

    public boolean updateCourseMaterial(CourseMaterial material) {
        if (material == null || material.getMaterialId() <= 0) return false;
        return materialDAO.update(material);
    }

    public boolean deleteCourseMaterial(int materialId) {
        return materialDAO.delete(materialId);
    }
}
