-- J-Scheduler Database Schema for MariaDB/MySQL
-- Author: Generated for j-scheduler-v1
-- Description: Schema for tracking courses and assignments

-- Create database (uncomment if you want to create a new database)
-- CREATE DATABASE IF NOT EXISTS jscheduler CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE jscheduler;

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    professor VARCHAR(255),
    semester VARCHAR(100) NOT NULL,
    credit_hours DECIMAL(3,1) DEFAULT 3.0,
    current_grade DECIMAL(5,2),
    letter_grade VARCHAR(3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_semester (semester),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Assignments table
CREATE TABLE IF NOT EXISTS assignments (
    id VARCHAR(50) PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    deadline TIME,
    status VARCHAR(50) NOT NULL DEFAULT 'Not Started',
    notes TEXT,
    grade DECIMAL(5,2),
    max_grade DECIMAL(5,2) DEFAULT 100.0,
    priority VARCHAR(20) DEFAULT 'Medium',
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(50),
    attachment_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_due_date (due_date),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Optional: Create a view for assignments with course information
CREATE OR REPLACE VIEW assignment_details AS
SELECT
    a.id AS assignment_id,
    a.title AS assignment_title,
    a.description AS assignment_description,
    a.due_date,
    a.deadline,
    a.status,
    a.notes,
    c.id AS course_id,
    c.name AS course_name,
    c.professor,
    c.semester
FROM assignments a
JOIN courses c ON a.course_id = c.id
ORDER BY a.due_date ASC;
