-- ====================================================================
-- Member 3: Session Attendance & Medical Tracking Module
-- Database Script: attendance
-- ====================================================================

USE fot_tms_db;

-- 1. Table: attendance (Managed by Member 3)
CREATE TABLE IF NOT EXISTS attendance (
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

-- Attendance Seeding: 15 Sessions covering all 5 SRS scenarios
-- Scenario 1 (>80% attendance raw): TG/2024/2109, TG/2024/2110, etc.
-- Scenario 2 (Exactly 80% attendance): TG/2024/2146 (12/15 sessions = 80%)
-- Scenario 3 (<80% attendance without medicals): TG/2024/2079 (9/15 = 60%)
-- Scenario 4 (>80% attendance with medicals): TG/2024/2006 (10 present + 3 approved MC = 13/15 = 86.67%)
-- Scenario 5 (<80% attendance with medicals): TG/2024/2007 (8 present + 2 approved MC = 10/15 = 66.67%)

DELIMITER //
CREATE PROCEDURE IF NOT EXISTS SeedMember3Attendance()
BEGIN
    DECLARE s_id INT;
    DECLARE sess INT;
    DECLARE t_id INT;

    SET s_id = 11;
    WHILE s_id <= 30 DO
        SET t_id = 1; -- Theory & Practical
        WHILE t_id <= 2 DO
            SET sess = 1;
            WHILE sess <= 15 DO
                IF s_id IN (11, 12, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29, 30) THEN
                    IF sess <= 14 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Personal errand')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    END IF;

                ELSEIF s_id = 13 THEN
                    IF sess <= 12 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Family function')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    END IF;

                ELSEIF s_id = 14 THEN
                    IF sess <= 9 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Unexcused')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    END IF;

                ELSEIF s_id = 20 THEN
                    IF sess <= 10 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    ELSEIF sess IN (11, 12, 13) THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Approved', 'Hospitalized - Dengue fever (MC verified)')
                        ON DUPLICATE KEY UPDATE medical_status=VALUES(medical_status);
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Personal')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    END IF;

                ELSEIF s_id = 21 THEN
                    IF sess <= 8 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Present', 'None', '')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
                    ELSEIF sess IN (9, 10) THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Approved', 'Viral flu with medical certificate')
                        ON DUPLICATE KEY UPDATE medical_status=VALUES(medical_status);
                    ELSEIF sess = 11 THEN
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'Pending', 'Medical submission pending doctor signature')
                        ON DUPLICATE KEY UPDATE medical_status=VALUES(medical_status);
                    ELSE
                        INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason)
                        VALUES (s_id, t_id, DATE_ADD('2026-06-01', INTERVAL sess*7 DAY), sess, 'Absent', 'None', 'Unexcused absence')
                        ON DUPLICATE KEY UPDATE status=VALUES(status);
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

CALL SeedMember3Attendance();
DROP PROCEDURE SeedMember3Attendance;
