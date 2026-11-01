
CREATE DATABASE IF NOT EXISTS fot_tms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fot_tms_db;




CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active' 
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    contact_no VARCHAR(20) DEFAULT '',
    profile_picture_path VARCHAR(255) DEFAULT '',
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;





INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin', 'admin@fot.ruh.ac.lk', 'Active')
ON DUPLICATE KEY UPDATE username=VALUES(username);

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(1, 1, 'System', 'Administrator', '+94771122334', '')
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name);





INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(2, 'lec_sunil', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'sunil@fot.ruh.ac.lk', 'Active'),
(3, 'lec_anura', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'anura@fot.ruh.ac.lk', 'Active'),
(4, 'lec_chaminda', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'chaminda@fot.ruh.ac.lk', 'Active'),
(5, 'lec_niluka', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'niluka@fot.ruh.ac.lk', 'Active'),
(6, 'lec_kusal', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Lecturer', 'kusal@fot.ruh.ac.lk', 'Active')
ON DUPLICATE KEY UPDATE username=VALUES(username);

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(2, 2, 'Dr. Sunil', 'Perera', '+94712345671', ''),
(3, 3, 'Prof. Anura', 'Silva', '+94712345672', ''),
(4, 4, 'Dr. Chaminda', 'Jayawardena', '+94712345673', ''),
(5, 5, 'Ms. Niluka', 'Fernando', '+94712345674', ''),
(6, 6, 'Mr. Kusal', 'Mendis', '+94712345675', '')
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name);

INSERT INTO users (user_id, username, password_hash, role, email, status) VALUES
(7, 'to_nuwan', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'nuwan@fot.ruh.ac.lk', 'Active'),
(8, 'to_sandya', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'sandya@fot.ruh.ac.lk', 'Active'),
(9, 'to_pradeep', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'pradeep@fot.ruh.ac.lk', 'Active'),
(10, 'to_asanka', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TO', 'asanka@fot.ruh.ac.lk', 'Active')
ON DUPLICATE KEY UPDATE username=VALUES(username);

INSERT INTO profiles (profile_id, user_id, first_name, last_name, contact_no, profile_picture_path) VALUES
(7, 7, 'Nuwan', 'Gamage', '+94781234561', ''),
(8, 8, 'Sandya', 'Kumari', '+94781234562', ''),
(9, 9, 'Pradeep', 'Alwis', '+94781234563', ''),
(10, 10, 'Asanka', 'Wickrama', '+94781234564', '')
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name);

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
(29, 'TG/2023/1850', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg1850@fot.ruh.ac.lk', 'Active'),
(30, 'TG/2022/1520', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Undergraduate', 'tg1520@fot.ruh.ac.lk', 'Active')
ON DUPLICATE KEY UPDATE username=VALUES(username);

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
(30, 30, 'Dinuka', 'Attanayake (Batch-Missed)', '+94778899020', '')
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name);
