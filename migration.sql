-- Migration script to add new features to existing j-scheduler database
-- Run this if you already have the database created

-- Add new columns to courses table
ALTER TABLE courses
ADD COLUMN IF NOT EXISTS credit_hours DECIMAL(3,1) DEFAULT 3.0,
ADD COLUMN IF NOT EXISTS current_grade DECIMAL(5,2),
ADD COLUMN IF NOT EXISTS letter_grade VARCHAR(3);

-- Add new columns to assignments table
ALTER TABLE assignments
ADD COLUMN IF NOT EXISTS grade DECIMAL(5,2),
ADD COLUMN IF NOT EXISTS max_grade DECIMAL(5,2) DEFAULT 100.0,
ADD COLUMN IF NOT EXISTS priority VARCHAR(20) DEFAULT 'Medium',
ADD COLUMN IF NOT EXISTS is_recurring BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS recurrence_pattern VARCHAR(50),
ADD COLUMN IF NOT EXISTS attachment_path VARCHAR(500);

-- Add index for new priority column
ALTER TABLE assignments
ADD INDEX IF NOT EXISTS idx_priority (priority);

-- Update existing records to have default priority if NULL
UPDATE assignments SET priority = 'Medium' WHERE priority IS NULL;

