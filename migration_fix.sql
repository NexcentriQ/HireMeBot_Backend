-- HireMe Bot Database Schema Migration
-- This script fixes the column type mismatches between existing schema and JPA entities
-- IMPORTANT: Backup your database before running this script!

USE hiremebot_db;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Step 1: Drop all existing foreign key constraints
ALTER TABLE application_documents DROP FOREIGN KEY IF EXISTS application_documents_ibfk_1;
ALTER TABLE application_questions DROP FOREIGN KEY IF EXISTS application_questions_ibfk_1;
ALTER TABLE applications DROP FOREIGN KEY IF EXISTS applications_ibfk_1;
ALTER TABLE applications DROP FOREIGN KEY IF EXISTS FKlj19dymh9m1vb6k2invfebqhd;
ALTER TABLE managers DROP FOREIGN KEY IF EXISTS managers_ibfk_1;
ALTER TABLE oauth_accounts DROP FOREIGN KEY IF EXISTS oauth_accounts_ibfk_1;
ALTER TABLE otp_verifications DROP FOREIGN KEY IF EXISTS otp_verifications_ibfk_1;
ALTER TABLE recruiter_student_assignments DROP FOREIGN KEY IF EXISTS recruiter_student_assignments_ibfk_1;
ALTER TABLE recruiter_student_assignments DROP FOREIGN KEY IF EXISTS recruiter_student_assignments_ibfk_2;
ALTER TABLE recruiter_student_assignments DROP FOREIGN KEY IF EXISTS recruiter_student_assignments_ibfk_3;
ALTER TABLE recruiters DROP FOREIGN KEY IF EXISTS recruiters_ibfk_1;
ALTER TABLE refresh_tokens DROP FOREIGN KEY IF EXISTS refresh_tokens_ibfk_1;
ALTER TABLE students DROP FOREIGN KEY IF EXISTS students_ibfk_1;
ALTER TABLE user_plans DROP FOREIGN KEY IF EXISTS fk_user_plans_user;

-- Step 2: Modify ID columns from VARCHAR(16) to BINARY(16)
-- Users table
ALTER TABLE users MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));

-- Students table
ALTER TABLE students MODIFY COLUMN id BINARY(16) NOT NULL;

-- Recruiters table
ALTER TABLE recruiters MODIFY COLUMN id BINARY(16) NOT NULL;

-- Managers table
ALTER TABLE managers MODIFY COLUMN id BINARY(16) NOT NULL;

-- Applications table
ALTER TABLE applications MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE applications MODIFY COLUMN student_id BINARY(16) NOT NULL;
ALTER TABLE applications MODIFY COLUMN recruiter_student_assignments_id BINARY(16) NOT NULL;

-- Application Documents table
ALTER TABLE application_documents MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE application_documents MODIFY COLUMN application_id BINARY(16) NOT NULL;

-- Application Questions table
ALTER TABLE application_questions MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE application_questions MODIFY COLUMN application_id BINARY(16) NOT NULL;

-- OAuth Accounts table
ALTER TABLE oauth_accounts MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE oauth_accounts MODIFY COLUMN user_id BINARY(16) NOT NULL;

-- OTP Verifications table
ALTER TABLE otp_verifications MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE otp_verifications MODIFY COLUMN user_id BINARY(16);

-- Refresh Tokens table
ALTER TABLE refresh_tokens MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE refresh_tokens MODIFY COLUMN user_id BINARY(16) NOT NULL;

-- Recruiter Student Assignments table
ALTER TABLE recruiter_student_assignments MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE recruiter_student_assignments MODIFY COLUMN recruiter_id BINARY(16) NOT NULL;
ALTER TABLE recruiter_student_assignments MODIFY COLUMN student_id BINARY(16) NOT NULL;
ALTER TABLE recruiter_student_assignments MODIFY COLUMN assigned_by BINARY(16) NOT NULL;

-- User Plans table
ALTER TABLE user_plans MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));
ALTER TABLE user_plans MODIFY COLUMN user_id BINARY(16) NOT NULL;

-- Object Store Entries table
ALTER TABLE object_store_entries MODIFY COLUMN id BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1));

-- Step 3: Fix TEXT/BLOB column issues
-- Application Documents
ALTER TABLE application_documents MODIFY COLUMN resume_url TEXT NOT NULL;
ALTER TABLE application_documents MODIFY COLUMN cover_letter_url TEXT;

-- Application Questions
ALTER TABLE application_questions MODIFY COLUMN question TEXT NOT NULL;
ALTER TABLE application_questions MODIFY COLUMN answer TEXT NOT NULL;

-- Applications
ALTER TABLE applications MODIFY COLUMN job_link TEXT NOT NULL;

-- OAuth Accounts
ALTER TABLE oauth_accounts MODIFY COLUMN provider VARCHAR(50) NOT NULL;
ALTER TABLE oauth_accounts MODIFY COLUMN access_token TEXT;
ALTER TABLE oauth_accounts MODIFY COLUMN refresh_token TEXT;

-- OTP Verifications
ALTER TABLE otp_verifications MODIFY COLUMN type VARCHAR(20) NOT NULL;
ALTER TABLE otp_verifications MODIFY COLUMN purpose VARCHAR(50) NOT NULL;

-- User Plans
ALTER TABLE user_plans MODIFY COLUMN status VARCHAR(20);

-- Step 4: Recreate foreign key constraints
-- Students
ALTER TABLE students 
    ADD CONSTRAINT students_ibfk_1 
    FOREIGN KEY (id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Recruiters
ALTER TABLE recruiters 
    ADD CONSTRAINT recruiters_ibfk_1 
    FOREIGN KEY (id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Managers
ALTER TABLE managers 
    ADD CONSTRAINT managers_ibfk_1 
    FOREIGN KEY (id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Recruiter Student Assignments
ALTER TABLE recruiter_student_assignments 
    ADD CONSTRAINT recruiter_student_assignments_ibfk_1 
    FOREIGN KEY (recruiter_id) REFERENCES recruiters(id) 
    ON DELETE CASCADE;

ALTER TABLE recruiter_student_assignments 
    ADD CONSTRAINT recruiter_student_assignments_ibfk_2 
    FOREIGN KEY (student_id) REFERENCES students(id) 
    ON DELETE CASCADE;

ALTER TABLE recruiter_student_assignments 
    ADD CONSTRAINT recruiter_student_assignments_ibfk_3 
    FOREIGN KEY (assigned_by) REFERENCES managers(id);

-- Applications
ALTER TABLE applications 
    ADD CONSTRAINT applications_ibfk_1 
    FOREIGN KEY (student_id) REFERENCES students(id) 
    ON DELETE CASCADE;

ALTER TABLE applications 
    ADD CONSTRAINT FKlj19dymh9m1vb6k2invfebqhd 
    FOREIGN KEY (recruiter_student_assignments_id) REFERENCES recruiter_student_assignments(id) 
    ON DELETE CASCADE;

-- Application Documents
ALTER TABLE application_documents 
    ADD CONSTRAINT application_documents_ibfk_1 
    FOREIGN KEY (application_id) REFERENCES applications(id) 
    ON DELETE CASCADE;

-- Application Questions
ALTER TABLE application_questions 
    ADD CONSTRAINT application_questions_ibfk_1 
    FOREIGN KEY (application_id) REFERENCES applications(id) 
    ON DELETE CASCADE;

-- OAuth Accounts
ALTER TABLE oauth_accounts 
    ADD CONSTRAINT oauth_accounts_ibfk_1 
    FOREIGN KEY (user_id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Add unique constraint for OAuth provider
ALTER TABLE oauth_accounts 
    ADD UNIQUE INDEX uk_oauth_user_provider (user_id, provider);

-- OTP Verifications
ALTER TABLE otp_verifications 
    ADD CONSTRAINT otp_verifications_ibfk_1 
    FOREIGN KEY (user_id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Refresh Tokens
ALTER TABLE refresh_tokens 
    ADD CONSTRAINT refresh_tokens_ibfk_1 
    FOREIGN KEY (user_id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- User Plans
ALTER TABLE user_plans 
    ADD CONSTRAINT fk_user_plans_user 
    FOREIGN KEY (user_id) REFERENCES users(id) 
    ON DELETE CASCADE;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Verify the schema
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'hiremebot_db'
    AND COLUMN_NAME = 'id'
ORDER BY TABLE_NAME, ORDINAL_POSITION;
