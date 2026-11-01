USE fot_tms_db;

CREATE TABLE IF NOT EXISTS courses (
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

CREATE TABLE IF NOT EXISTS course_materials (
    material_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_material_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS notices (
    notice_id INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    target_role VARCHAR(50) NOT NULL DEFAULT 'All',
    posted_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notice_author FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS timetables (
    timetable_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    session_type VARCHAR(20) NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    venue VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL DEFAULT 'Information and Communication Technology',
    CONSTRAINT fk_timetable_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO courses (course_id, coordinator_id, course_code, course_name, credits, semester, ca_weight, final_weight) VALUES
(1, 2, 'ICT2113', 'Business Economics', 3, 1, 40.00, 60.00),
(2, 3, 'ICT2123', 'Object Oriented Programming', 3, 1, 40.00, 60.00),
(3, 4, 'ICT2133', 'E-Business Systems', 3, 1, 40.00, 60.00),
(4, 5, 'ICT2143', 'Data Structures and Algorithms', 3, 1, 40.00, 60.00),
(5, 3, 'ICT2132', 'Object Oriented Programming Practicum', 2, 1, 50.00, 50.00),
(6, 6, 'ICT2153', 'Management Information Systems', 3, 1, 40.00, 60.00),
(7, 2, 'ICT2163', 'Object Oriented Analysis and Design', 3, 1, 40.00, 60.00),
(8, 4, 'ICT2172', 'Soft Skills', 2, 1, 40.00, 60.00),
(9, 5, 'ICT2182', 'English', 2, 1, 40.00, 60.00)
ON DUPLICATE KEY UPDATE course_code=VALUES(course_code);

INSERT INTO course_materials (material_id, course_id, title, file_path, uploaded_date) VALUES
(1, 2, 'Lecture 01 - Intro to OOP Concepts', 'materials/ict2123_lec01.pdf', NOW() - INTERVAL 10 DAY),
(2, 2, 'Lecture 02 - Classes and Objects in Java', 'materials/ict2123_lec02.pdf', NOW() - INTERVAL 8 DAY),
(3, 2, 'Lecture 03 - Inheritance and Polymorphism', 'materials/ict2123_lec03.pdf', NOW() - INTERVAL 5 DAY),
(4, 5, 'Practicum Lab Sheet 01 - GUI Design with Swing', 'materials/ict2132_lab01.pdf', NOW() - INTERVAL 12 DAY),
(5, 5, 'Practicum Lab Sheet 02 - JDBC & Database Connection', 'materials/ict2132_lab02.pdf', NOW() - INTERVAL 6 DAY),
(6, 4, 'DSA Chapter 01 - Asymptotic Analysis & Arrays', 'materials/ict2143_ch01.pdf', NOW() - INTERVAL 14 DAY),
(7, 3, 'E-Business Systems Lecture Notes 01', 'materials/ict2133_unit01.pdf', NOW() - INTERVAL 7 DAY)
ON DUPLICATE KEY UPDATE title=VALUES(title);

INSERT INTO notices (notice_id, author_id, title, content, target_role, posted_date) VALUES
(1, 1, 'Welcome to Academic Year 2026/2027', 'All academic sessions and practicum assessments will strictly adhere to UGC Commission Circular No. 12-2024. Please check your timetables.', 'All', NOW() - INTERVAL 15 DAY),
(2, 1, 'Attendance Policy & Eligibility Criteria Notice', 'Undergraduates must maintain at least 80% effective attendance (including approved medicals) and score >= 40% in CA to sit for final semester examinations.', 'Undergraduate', NOW() - INTERVAL 10 DAY),
(3, 2, 'ICT2123 OOP Practicum Evaluation Reminder', 'Continuous Assessment practical submission deadline is set for Week 14. Ensure all project code and reports are submitted on time.', 'Undergraduate', NOW() - INTERVAL 4 DAY),
(4, 1, 'Technical Officers Meeting - Lab Maintenance', 'All Technical Officers are requested to attend the monthly IT lab maintenance review on Friday at 2:00 PM in Conference Room A.', 'TO', NOW() - INTERVAL 2 DAY),
(5, 3, 'Lecturers Faculty Board Meeting', 'Discussion on semester evaluation matrices and CA weightings will be held in the Dean Office boardroom.', 'Lecturer', NOW() - INTERVAL 1 DAY)
ON DUPLICATE KEY UPDATE title=VALUES(title);

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
(10, 9, 'Theory', 'Friday', '10:00 - 12:00', 'LT-01', 'Information and Communication Technology')
ON DUPLICATE KEY UPDATE time_slot=VALUES(time_slot);
