-- ====================================================================
-- Faculty of Technology Management System (FoT-TMS)
-- Database Initialization & Seeding Script
-- Compliant with ICT2132 Mini Project Specifications & ER Diagram
-- ====================================================================

CREATE DATABASE IF NOT EXISTS fot_tms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fot_tms_db;

-- --------------------------------------------------------------------
-- 1. Table: users
-- --------------------------------------------------------------------
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS course_materials;
DROP TABLE IF EXISTS timetables;
DROP TABLE IF EXISTS notices;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'Admin', 'Lecturer', 'TO', 'Undergraduate'
    email VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active' -- 'Active', 'Inactive'
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 2. Table: profiles
-- --------------------------------------------------------------------
CREATE TABLE profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    contact_no VARCHAR(20) DEFAULT '',
    profile_picture_path VARCHAR(255) DEFAULT '',
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 3. Table: courses
-- --------------------------------------------------------------------
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    coordinator_id INT DEFAULT NULL,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL DEFAULT 3,
    semester INT NOT NULL DEFAULT 1,
    ca_weight DECIMAL(5,2) NOT NULL DEFAULT 40.00,
    final_weight DECIMAL(5,2) NOT NULL DEFAULT 60.00,
    CONSTRAINT fk_course_coordinator FOREIGN KEY (coordinator_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 4. Table: course_materials
-- --------------------------------------------------------------------
CREATE TABLE course_materials (
    material_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_material_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 5. Table: notices
-- --------------------------------------------------------------------
CREATE TABLE notices (
    notice_id INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    target_role VARCHAR(50) NOT NULL DEFAULT 'All', -- 'All', 'Lecturer', 'TO', 'Undergraduate'
    posted_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notice_author FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 6. Table: timetables
-- --------------------------------------------------------------------
CREATE TABLE timetables (
    timetable_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    session_type VARCHAR(20) NOT NULL, -- 'Theory', 'Practical'
    day_of_week VARCHAR(20) NOT NULL, -- 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'
    time_slot VARCHAR(50) NOT NULL,   -- e.g. '08:00 - 10:00'
    venue VARCHAR(50) NOT NULL,       -- e.g. 'LT-01', 'Lab-02'
    department VARCHAR(50) NOT NULL DEFAULT 'Information and Communication Technology',
    CONSTRAINT fk_timetable_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 7. Table: attendance
-- --------------------------------------------------------------------
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    timetable_id INT NOT NULL,
    session_date DATE NOT NULL,
    session_number INT NOT NULL DEFAULT 1, -- 1 to 15
    status VARCHAR(20) NOT NULL DEFAULT 'Present', -- 'Present', 'Absent'
    medical_status VARCHAR(20) NOT NULL DEFAULT 'None', -- 'None', 'Pending', 'Approved', 'Rejected'
    reason VARCHAR(255) DEFAULT '',
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_timetable FOREIGN KEY (timetable_id) REFERENCES timetables(timetable_id) ON DELETE CASCADE,
    UNIQUE KEY uk_attendance_session (student_id, timetable_id, session_number)
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 8. Table: marks
-- --------------------------------------------------------------------
CREATE TABLE marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    ca_quiz DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ca_assessment DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ca_mid DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ca_total DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    final_exam_mark DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    overall_mark DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    letter_grade VARCHAR(5) NOT NULL DEFAULT 'E',
    grade_point DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY uk_student_course_mark (student_id, course_id)
) ENGINE=InnoDB;

-- ====================================================================
-- SEED DATA
-- Baseline: 1 Admin, 5 Lecturers, 4 TOs, 20 Undergraduates
-- Default Passwords: All user accounts hashed with SHA-256 for 'password123'
-- SHA-256 for 'password123' = ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- For admin: 'admin123' = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- ====================================================================

-- 1. Admin
INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin', 'admin@fot.ruh.ac.lk', 'Active');

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(1, 1, 'System', 'Administrator', '+94771122334', '');

-- 2. 5 Lecturers
INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(2, 'lec_sunil', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'sunil@fot.ruh.ac.lk', 'Active'),
(3, 'lec_anura', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'anura@fot.ruh.ac.lk', 'Active'),
(4, 'lec_chaminda', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'chaminda@fot.ruh.ac.lk', 'Active'),
(5, 'lec_niluka', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'niluka@fot.ruh.ac.lk', 'Active'),
(6, 'lec_kusal', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'kusal@fot.ruh.ac.lk', 'Active');

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(2, 2, 'Dr. Sunil', 'Perera', '+94712345671', ''),
(3, 3, 'Prof. Anura', 'Silva', '+94712345672', ''),
(4, 4, 'Dr. Chaminda', 'Jayawardena', '+94712345673', ''),
(5, 5, 'Ms. Niluka', 'Fernando', '+94712345674', ''),
(6, 6, 'Mr. Kusal', 'Mendis', '+94712345675', '');

-- 3. 4 Technical Officers (TOs)
INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(7, 'to_nuwan', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'nuwan@fot.ruh.ac.lk', 'Active'),
(8, 'to_sandya', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'sandya@fot.ruh.ac.lk', 'Active'),
(9, 'to_pradeep', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'pradeep@fot.ruh.ac.lk', 'Active'),
(10, 'to_asanka', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'asanka@fot.ruh.ac.lk', 'Active');

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(7, 7, 'Nuwan', 'Gamage', '+94781234561', ''),
(8, 8, 'Sandya', 'Kumari', '+94781234562', ''),
(9, 9, 'Pradeep', 'Alwis', '+94781234563', ''),
(10, 10, 'Asanka', 'Wickrama', '+94781234564', '');

-- 4. 20 Undergraduates (including project team members & repeat/batch missed)
INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(11, 'TG/2024/2109', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2109@fot.ruh.ac.lk', 'Active'),
(12, 'TG/2024/2110', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2110@fot.ruh.ac.lk', 'Active'),
(13, 'TG/2024/2146', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2146@fot.ruh.ac.lk', 'Active'),
(14, 'TG/2024/2079', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2079@fot.ruh.ac.lk', 'Active'),
(15, 'TG/2024/2001', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2001@fot.ruh.ac.lk', 'Active'),
(16, 'TG/2024/2002', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2002@fot.ruh.ac.lk', 'Active'),
(17, 'TG/2024/2003', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2003@fot.ruh.ac.lk', 'Active'),
(18, 'TG/2024/2004', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2004@fot.ruh.ac.lk', 'Active'),
(19, 'TG/2024/2005', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2005@fot.ruh.ac.lk', 'Active'),
(20, 'TG/2024/2006', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2006@fot.ruh.ac.lk', 'Active'),
(21, 'TG/2024/2007', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2007@fot.ruh.ac.lk', 'Active'),
(22, 'TG/2024/2008', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2008@fot.ruh.ac.lk', 'Active'),
(23, 'TG/2024/2009', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2009@fot.ruh.ac.lk', 'Active'),
(24, 'TG/2024/2010', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2010@fot.ruh.ac.lk', 'Active'),
(25, 'TG/2024/2011', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2011@fot.ruh.ac.lk', 'Active'),
(26, 'TG/2024/2012', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2012@fot.ruh.ac.lk', 'Active'),
(27, 'TG/2024/2013', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2013@fot.ruh.ac.lk', 'Active'),
(28, 'TG/2024/2014', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg2014@fot.ruh.ac.lk', 'Active'),
(29, 'TG/2023/1850', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg1850@fot.ruh.ac.lk', 'Active'), -- Repeat Student
(30, 'TG/2022/1520', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg1520@fot.ruh.ac.lk', 'Active'); -- Batch Missed Student

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(11, 11, 'Dilshan', 'Madushanka', '+94778899001', ''),
(12, 12, 'Tashiru', 'Dissanayake', '+94778899002', ''),
(13, 13, 'Sithum', 'Dilshan', '+94778899003', ''),
(14, 14, 'Tharuka', 'Perera', '+94778899004', ''),
(15, 15, 'Kasun', 'Bandara', '+94778899005', ''),
(16, 16, 'Nimali', 'Jayasinghe', '+94778899006', ''),
(17, 17, 'Raveen', 'Gunasekara', '+94778899007', ''),
(18, 18, 'Kavindu', 'Ranasinghe', '+94778899008', ''),
(19, 19, 'Hansani', 'Weerasinghe', '+94778899009', ''),
(20, 20, 'Oshada', 'Kulasekara', '+94778899010', ''),
(21, 21, 'Sachini', 'Devindi', '+94778899011', ''),
(22, 22, 'Supun', 'Herath', '+94778899012', ''),
(23, 23, 'Lakshitha', 'Abeykoon', '+94778899013', ''),
(24, 24, 'Chathurika', 'Senanayake', '+94778899014', ''),
(25, 25, 'Dhanushka', 'Rajapaksha', '+94778899015', ''),
(26, 26, 'Ruwan', 'Pathirana', '+94778899016', ''),
(27, 27, 'Buddhika', 'Karunaratne', '+94778899017', ''),
(28, 28, 'Maneesha', 'Liyanage', '+94778899018', ''),
(29, 29, 'Vimukthi', 'Wickramasinghe (Repeat)', '+94778899019', ''),
(30, 30, 'Dinuka', 'Attanayake (Batch-Missed)', '+94778899020', '');

-- 5. Courses (Covered Academic Subjects per SRS)
INSERT INTO courses (course_id, coordinator_id, course_code, course_name, credits, semester, ca_weight, final_weight) VALUES
(1, 2, 'ICT2113', 'Business Economics', 3, 1, 40.00, 60.00),
(2, 3, 'ICT2123', 'Object Oriented Programming', 3, 1, 40.00, 60.00),
(3, 4, 'ICT2133', 'E-Business Systems', 3, 1, 40.00, 60.00),
(4, 5, 'ICT2143', 'Data Structures and Algorithms', 3, 1, 40.00, 60.00),
(5, 3, 'ICT2132', 'Object Oriented Programming Practicum', 2, 1, 50.00, 50.00),
(6, 6, 'ICT2153', 'Management Information Systems', 3, 1, 40.00, 60.00),
(7, 2, 'ICT2163', 'Object Oriented Analysis and Design', 3, 1, 40.00, 60.00),
(8, 4, 'ICT2172', 'Soft Skills', 2, 1, 40.00, 60.00),
(9, 5, 'ICT2182', 'English', 2, 1, 40.00, 60.00);

-- 6. Course Materials
INSERT INTO course_materials (material_id, course_id, title, file_path, uploaded_date) VALUES
(1, 1, 'Business Economics - Lesson Plan', 'materials/ICT2113_Business_Economics_Lesson_Plan.pdf', NOW() - INTERVAL 15 DAY),
(2, 1, 'Business Economics - Tutorial 01', 'materials/ICT2113_Econ_Tutorial_01.pdf', NOW() - INTERVAL 12 DAY),
(3, 2, 'OOP - Lesson 05: Exceptions Handling', 'materials/ICT2123_Lesson_05_-_Exceptions.pdf', NOW() - INTERVAL 10 DAY),
(4, 2, 'OOP - Lesson 06: File Handling in Java', 'materials/ICT2123_Lesson_06_-_Introduction_to_File_Handling.pdf', NOW() - INTERVAL 8 DAY),
(5, 3, 'E-Business Systems - Lecture 01 Overview', 'materials/ICT2133_ICT2142_Lecture_01.pdf', NOW() - INTERVAL 14 DAY),
(6, 3, 'E-Business Systems - Practical 01 Lab Sheet', 'materials/ICT2133_ICT2142_E-Business_Syatems_Practical_01.pdf', NOW() - INTERVAL 11 DAY),
(7, 4, 'DSA - Introduction & Course Outline', 'materials/ICT2143_Introduction_2026.pdf', NOW() - INTERVAL 14 DAY),
(8, 4, 'DSA - Lab 01 Introduction Exercise', 'materials/ICT2143_Lab1_DSA_Intro.pdf', NOW() - INTERVAL 10 DAY),
(9, 5, 'OOP Practicum - Lab Sheet 05 & 06', 'materials/ICT2132_ICT2132_-_OOPP_-_Practical_05,06.pdf', NOW() - INTERVAL 9 DAY),
(10, 5, 'OOP Practicum - Lab Sheet 07 & 08', 'materials/ICT2132_ICT2132_-_OOPP_-_Practical_07,08.pdf', NOW() - INTERVAL 6 DAY),
(11, 6, 'MIS - Lecture 01 Foundation', 'materials/ICT2153_Lecture_01.pdf', NOW() - INTERVAL 13 DAY),
(12, 6, 'MIS - Lecture 02 Information Systems', 'materials/ICT2153_Lecture_02.pdf', NOW() - INTERVAL 7 DAY),
(13, 7, 'OOAD - Activity 01 & Lecture 01', 'materials/ICT2163_Activity1-_lec1.pdf', NOW() - INTERVAL 12 DAY),
(14, 7, 'OOAD - Week 02: Object Oriented Languages', 'materials/ICT2163_Wee_2_-_Object_Oriened_Languages.pdf', NOW() - INTERVAL 5 DAY);

-- 7. Notices
INSERT INTO notices (notice_id, author_id, title, content, target_role, posted_date) VALUES
(1, 1, 'Welcome to Academic Year 2026/2027', 'All academic sessions and practicum assessments will strictly adhere to UGC Commission Circular No. 12-2024. Please check your timetables.', 'All', NOW() - INTERVAL 15 DAY),
(2, 1, 'Attendance Policy & Eligibility Criteria Notice', 'Undergraduates must maintain at least 80% effective attendance (including approved medicals) and score >= 40% in CA to sit for final semester examinations.', 'Undergraduate', NOW() - INTERVAL 10 DAY),
(3, 2, 'ICT2123 OOP Practicum Evaluation Reminder', 'Continuous Assessment practical submission deadline is set for Week 14. Ensure all project code and reports are submitted on time.', 'Undergraduate', NOW() - INTERVAL 4 DAY),
(4, 1, 'Technical Officers Meeting - Lab Maintenance', 'All Technical Officers are requested to attend the monthly IT lab maintenance review on Friday at 2:00 PM in Conference Room A.', 'TO', NOW() - INTERVAL 2 DAY),
(5, 3, 'Lecturers Faculty Board Meeting', 'Discussion on semester evaluation matrices and CA weightings will be held in the Dean Office boardroom.', 'Lecturer', NOW() - INTERVAL 1 DAY);

-- 8. Timetables (Department & Faculty Schedule)
INSERT INTO timetables (timetable_id, course_id, session_type, day_of_week, time_slot, venue, department) VALUES
(1, 2, 'Theory', 'Monday', '08:00 - 10:00', 'LT-01', 'Information and Communication Technology'),
(2, 5, 'Practical', 'Monday', '10:00 - 12:00', 'IT-Lab-01', 'Information and Communication Technology'),
(3, 4, 'Theory', 'Tuesday', '08:00 - 10:00', 'LT-02', 'Information and Communication Technology'),
(4, 4, 'Practical', 'Tuesday', '10:00 - 12:00', 'IT-Lab-02', 'Information and Communication Technology'),
(5, 1, 'Theory', 'Wednesday', '08:00 - 10:00', 'LT-01', 'Information and Communication Technology'),
(6, 3, 'Theory', 'Wednesday', '10:00 - 12:00', 'LT-01', 'Information and Communication Technology'),
(7, 7, 'Theory', 'Thursday', '08:00 - 10:00', 'LT-02', 'Information and Communication Technology'),
(8, 6, 'Theory', 'Thursday', '10:00 - 12:00', 'LT-03', 'Information and Communication Technology'),
(9, 8, 'Theory', 'Friday', '08:00 - 10:00', 'LT-01', 'Information and Communication Technology'),
(10, 9, 'Theory', 'Friday', '10:00 - 12:00', 'LT-01', 'Information and Communication Technology'),
(11, 2, 'Practical', 'Friday', '13:00 - 15:00', 'IT-Lab-01', 'Information and Communication Technology');

-- 9. Attendance Seeding (15 Sessions for Theory & Practical for 20 Students)
-- We seed 15 theory sessions (timetable_id=1, ICT2123 Theory) & 15 practical sessions (timetable_id=11, ICT2123 Practical)
-- and timetable_id=2 (ICT2132 Practical) to cover all 5 required scenarios for Theory-Only, Practical-Only & Combined:
-- Scenario 1 (>80% attendance): TG/2024/2109, TG/2024/2110, TG/2024/2001..2005 (14/15 or 15/15 = 93%-100%)
-- Scenario 2 (Exactly 80% attendance): TG/2024/2146 (12/15 sessions = exactly 80.00%)
-- Scenario 3 (<80% attendance without medicals): TG/2024/2079 (9/15 = 60%, medical_status='None' -> Ineligible)
-- Scenario 4 (>80% attendance with medicals): TG/2024/2006 (10 present + 3 approved medicals = 13/15 = 86.67% effective -> Eligible)
-- Scenario 5 (<80% attendance with medicals): TG/2024/2007 (8 present + 2 approved medicals = 10/15 = 66.67% effective -> Ineligible)

-- Insert 15 sessions for all 20 students for Timetables 1 (Course 2 Theory), 11 (Course 2 Practical), and 2 (Course 5 Practical)
DELIMITER //
CREATE PROCEDURE SeedAttendance()
BEGIN
    DECLARE s_id INT;
    DECLARE sess INT;
    DECLARE t_id INT;
    DECLARE actual_tt_id INT;

    SET s_id = 11;
    WHILE s_id <= 30 DO
        -- Seed Timetable 1 (Theory), Timetable 11 (Practical for Course 2), and Timetable 2 (Practical for Course 5)
        SET t_id = 1;
        WHILE t_id <= 3 DO
            IF t_id = 1 THEN SET actual_tt_id = 1;
            ELSEIF t_id = 2 THEN SET actual_tt_id = 11;
            ELSE SET actual_tt_id = 2;
            END IF;

            SET sess = 1;
            WHILE sess <= 15 DO
                -- Case 1: Student 11 (Dilshan) & 12 (Tashiru) & 15-19: >80%
                IF s_id IN (11, 12, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29, 30) THEN
                    IF sess <= 14 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Personal errand');
                    END IF;

                -- Case 2: Student 13 (Sithum) -> Exactly 80% (12/15 present)
                ELSEIF s_id = 13 THEN
                    IF sess <= 12 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Family function');
                    END IF;

                -- Case 3: Student 14 (Tharuka) -> <80% without medicals (9/15 = 60%)
                ELSEIF s_id = 14 THEN
                    IF sess <= 9 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Unexcused');
                    END IF;

                -- Case 4: Student 20 (Oshada) -> >80% with medicals (10 present + 3 approved medicals = 13/15 = 86.7%)
                ELSEIF s_id = 20 THEN
                    IF sess <= 10 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSEIF sess IN (11, 12, 13) THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Approved', 'Hospitalized - Dengue fever (MC verified)');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Personal');
                    END IF;

                -- Case 5: Student 21 (Sachini) -> <80% with medicals (8 present + 2 approved medicals = 10/15 = 66.7%)
                ELSEIF s_id = 21 THEN
                    IF sess <= 8 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSEIF sess IN (9, 10) THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Approved', 'Viral flu with medical certificate');
                    ELSEIF sess = 11 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Pending', 'Medical submission pending doctor signature');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Unexcused absence');
                    END IF;

                -- Default fallback for any other
                ELSE
                    IF sess <= 13 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '');
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, actual_tt_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', '');
                    END IF;
                END IF;

                SET sess = sess + 1;
            END WHILE;
            SET t_id = t_id + 1;
        END WHILE;
        SET s_id = s_id + 1;
    END WHILE;
END //
DELIMITER ;

CALL SeedAttendance();
DROP PROCEDURE SeedAttendance;

-- 10. Marks Seeding (All 20 Undergraduates for 9 Courses)
-- All marks out of 100
-- UGC Circular 12-2024 Grading Scale Applied
INSERT INTO marks (student_id, course_id, ca_quiz, ca_assessment, ca_mid, ca_total, final_exam_mark, overall_mark, letter_grade, grade_point) VALUES
-- Student 11: Dilshan Madushanka (High achiever: A+ / A)
(11, 1, 90.00, 88.00, 85.00, 87.67, 88.00, 87.87, 'A+', 4.00),
(11, 2, 95.00, 92.00, 90.00, 92.33, 90.00, 90.93, 'A+', 4.00),
(11, 3, 85.00, 80.00, 82.00, 82.33, 84.00, 83.33, 'A',  4.00),
(11, 4, 88.00, 85.00, 86.00, 86.33, 86.00, 86.13, 'A+', 4.00),
(11, 5, 92.00, 90.00, 88.00, 90.00, 92.00, 91.00, 'A+', 4.00),
(11, 6, 80.00, 82.00, 84.00, 82.00, 80.00, 80.80, 'A',  4.00),
(11, 7, 88.00, 86.00, 84.00, 86.00, 88.00, 87.20, 'A+', 4.00),
(11, 8, 85.00, 85.00, 85.00, 85.00, 85.00, 85.00, 'A+', 4.00),
(11, 9, 82.00, 80.00, 84.00, 82.00, 80.00, 80.80, 'A',  4.00),

-- Student 12: Tashiru Dissanayake (Strong student: A / A-)
(12, 1, 80.00, 78.00, 75.00, 77.67, 78.00, 77.87, 'A',  4.00),
(12, 2, 85.00, 82.00, 80.00, 82.33, 84.00, 83.33, 'A',  4.00),
(12, 3, 75.00, 72.00, 74.00, 73.67, 72.00, 72.67, 'A-', 3.70),
(12, 4, 80.00, 78.00, 76.00, 78.00, 80.00, 79.20, 'A',  4.00),
(12, 5, 84.00, 82.00, 80.00, 82.00, 84.00, 83.00, 'A',  4.00),
(12, 6, 76.00, 74.00, 72.00, 74.00, 75.00, 74.60, 'A-', 3.70),
(12, 7, 78.00, 75.00, 72.00, 75.00, 76.00, 75.60, 'A',  4.00),
(12, 8, 80.00, 80.00, 80.00, 80.00, 80.00, 80.00, 'A',  4.00),
(12, 9, 75.00, 78.00, 76.00, 76.33, 75.00, 75.53, 'A',  4.00),

-- Student 13: Sithum Dilshan (B+ / B)
(13, 1, 68.00, 65.00, 66.00, 66.33, 67.00, 66.73, 'B+', 3.30),
(13, 2, 70.00, 68.00, 65.00, 67.67, 70.00, 69.07, 'B+', 3.30),
(13, 3, 64.00, 62.00, 60.00, 62.00, 62.00, 62.00, 'B',  3.00),
(13, 4, 66.00, 68.00, 65.00, 66.33, 68.00, 67.33, 'B+', 3.30),
(13, 5, 72.00, 70.00, 68.00, 70.00, 72.00, 71.00, 'A-', 3.70),
(13, 6, 60.00, 62.00, 64.00, 62.00, 60.00, 60.80, 'B',  3.00),
(13, 7, 65.00, 64.00, 62.00, 63.67, 65.00, 64.47, 'B',  3.00),
(13, 8, 70.00, 70.00, 70.00, 70.00, 70.00, 70.00, 'A-', 3.70),
(13, 9, 68.00, 65.00, 67.00, 66.67, 68.00, 67.47, 'B+', 3.30),

-- Student 14: Tharuka Perera (C+ / C) - Eligible in CA (>=40%), but low in Attendance
(14, 1, 52.00, 50.00, 48.00, 50.00, 52.00, 51.20, 'C+', 2.30),
(14, 2, 55.00, 52.00, 50.00, 52.33, 54.00, 53.33, 'C+', 2.30),
(14, 3, 48.00, 46.00, 45.00, 46.33, 48.00, 47.33, 'C',  2.00),
(14, 4, 50.00, 48.00, 47.00, 48.33, 50.00, 49.33, 'C',  2.00),
(14, 5, 54.00, 52.00, 50.00, 52.00, 54.00, 53.00, 'C+', 2.30),
(14, 6, 46.00, 45.00, 44.00, 45.00, 46.00, 45.60, 'C',  2.00),
(14, 7, 50.00, 48.00, 46.00, 48.00, 50.00, 49.20, 'C',  2.00),
(14, 8, 55.00, 55.00, 55.00, 55.00, 55.00, 55.00, 'B-', 2.70),
(14, 9, 52.00, 50.00, 48.00, 50.00, 52.00, 51.20, 'C+', 2.30),

-- Student 15: Kasun Bandara (Ineligible in CA < 40%)
(15, 1, 35.00, 32.00, 30.00, 32.33, 35.00, 33.93, 'D',  1.00),
(15, 2, 38.00, 34.00, 32.00, 34.67, 36.00, 35.47, 'D+', 1.30),
(15, 3, 30.00, 28.00, 25.00, 27.67, 30.00, 29.07, 'E',  0.00),
(15, 4, 34.00, 32.00, 30.00, 32.00, 34.00, 33.20, 'D',  1.00),
(15, 5, 36.00, 35.00, 32.00, 34.33, 35.00, 34.67, 'D',  1.00),
(15, 6, 32.00, 30.00, 28.00, 30.00, 32.00, 31.20, 'D',  1.00),
(15, 7, 35.00, 34.00, 32.00, 33.67, 35.00, 34.47, 'D',  1.00),
(15, 8, 45.00, 45.00, 45.00, 45.00, 45.00, 45.00, 'C',  2.00),
(15, 9, 38.00, 35.00, 34.00, 35.67, 38.00, 37.07, 'D+', 1.30);

-- Populate marks for remaining students 16 to 30
INSERT INTO marks (student_id, course_id, ca_quiz, ca_assessment, ca_mid, ca_total, final_exam_mark, overall_mark, letter_grade, grade_point)
SELECT u.user_id, c.course_id,
       65.00 + (u.user_id % 20),
       62.00 + (u.user_id % 22),
       60.00 + (u.user_id % 25),
       (65.00 + (u.user_id % 20) + 62.00 + (u.user_id % 22) + 60.00 + (u.user_id % 25))/3.0,
       65.00 + (u.user_id % 20),
       ((65.00 + (u.user_id % 20) + 62.00 + (u.user_id % 22) + 60.00 + (u.user_id % 25))/3.0 * c.ca_weight / 100.0) + ((65.00 + (u.user_id % 20)) * c.final_weight / 100.0),
       'B+', 3.30
FROM users u
CROSS JOIN courses c
WHERE u.role = 'Undergraduate' AND u.user_id NOT IN (11, 12, 13, 14, 15);
