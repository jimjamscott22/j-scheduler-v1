# MariaDB Migration Plan for J-Scheduler

## Overview
This document outlines the plan to migrate j-scheduler from JSON file storage to MariaDB database running on a Raspberry Pi on the local network.

---

## Phase 1: Raspberry Pi MariaDB Setup

### 1.1 Install MariaDB on Raspberry Pi
```bash
sudo apt update
sudo apt install mariadb-server
sudo systemctl enable mariadb
sudo systemctl start mariadb
```

### 1.2 Secure MariaDB Installation
```bash
sudo mysql_secure_installation
```
- Set root password
- Remove anonymous users
- Disallow root login remotely
- Remove test database

### 1.3 Create Database and User
```sql
CREATE DATABASE jscheduler;
CREATE USER 'jscheduler_user'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON jscheduler.* TO 'jscheduler_user'@'%';
FLUSH PRIVILEGES;
```

### 1.4 Configure MariaDB for Network Access
Edit `/etc/mysql/mariadb.conf.d/50-server.cnf`:
```ini
bind-address = 0.0.0.0
```
Restart MariaDB:
```bash
sudo systemctl restart mariadb
```

### 1.5 Configure Firewall (if enabled)
```bash
sudo ufw allow 3306/tcp
```

---

## Phase 2: Database Schema Design

### 2.1 Create Tables

```sql
-- Semesters table
CREATE TABLE semesters (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_current BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE courses (
    id VARCHAR(36) PRIMARY KEY,
    semester_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    professor VARCHAR(255),
    semester VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE,
    INDEX idx_semester_id (semester_id)
);

-- Assignments table
CREATE TABLE assignments (
    id VARCHAR(36) PRIMARY KEY,
    semester_id VARCHAR(36) NOT NULL,
    course_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    submission_deadline DATE,
    status VARCHAR(50) DEFAULT 'Not Started',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_semester_id (semester_id),
    INDEX idx_course_id (course_id),
    INDEX idx_due_date (due_date),
    INDEX idx_status (status)
);
```

---

## Phase 3: Java Application Changes

### 3.1 Add Dependencies to `build.gradle`
```gradle
dependencies {
    // Existing dependencies...

    // MariaDB JDBC Driver
    implementation 'org.mariadb.jdbc:mariadb-java-client:3.3.2'

    // Connection pooling (recommended)
    implementation 'com.zaxxer:HikariCP:5.1.0'
}
```

### 3.2 Create Database Configuration Class
**File**: `src/main/java/com/jscheduler/config/DatabaseConfig.java`
- Load database connection properties
- Support for connection pooling
- Handle connection parameters (host, port, database name, credentials)

### 3.3 Create MariaDB Persistence Service
**File**: `src/main/java/com/jscheduler/service/MariaDbPersistenceService.java`

**Responsibilities:**
- Implement same interface/methods as `JsonPersistenceService`
- CRUD operations for Semester, Course, Assignment
- Use prepared statements to prevent SQL injection
- Proper connection management and resource cleanup
- Transaction support for multi-table operations

**Key Methods:**
- `load()` - Load current semester with courses and assignments
- `save(Semester)` - Save/update semester data
- `saveCourse(Course)` - Insert/update course
- `deleteCourse(String id)` - Delete course and cascade
- `saveAssignment(Assignment)` - Insert/update assignment
- `deleteAssignment(String id)` - Delete assignment

### 3.4 Create Configuration File
**File**: `config.properties` or use environment variables
```properties
db.host=192.168.1.XXX  # Your Raspberry Pi IP
db.port=3306
db.name=jscheduler
db.user=jscheduler_user
db.password=your_secure_password
db.pool.size=10
```

### 3.5 Update DataService
**File**: `src/main/java/com/jscheduler/service/DataService.java`
- Replace `JsonPersistenceService` with `MariaDbPersistenceService`
- Or add configuration to choose between JSON/MariaDB
- Update initialization logic

### 3.6 Add Persistence Interface (Optional but Recommended)
**File**: `src/main/java/com/jscheduler/service/PersistenceService.java`
```java
public interface PersistenceService {
    Semester load();
    void save(Semester semester);
    // Additional methods as needed
}
```
- Both `JsonPersistenceService` and `MariaDbPersistenceService` implement this
- Allows easy switching between storage methods

---

## Phase 4: Data Migration Strategy

### 4.1 Create Migration Tool
**File**: `src/main/java/com/jscheduler/migration/JsonToMariaDbMigrator.java`

**Features:**
- Read existing `semester.json` file
- Insert data into MariaDB tables
- Validate migration success
- Create backup before migration
- Rollback capability if migration fails

### 4.2 Migration Steps
1. Backup existing `semester.json`
2. Load JSON data using `JsonPersistenceService`
3. Connect to MariaDB
4. Insert semester, courses, and assignments
5. Verify data integrity
6. Test application with MariaDB
7. Keep JSON backup for safety

---

## Phase 5: Testing

### 5.1 Unit Tests
- Test database connection
- Test CRUD operations for each entity
- Test transaction rollback scenarios
- Test connection pool behavior

### 5.2 Integration Tests
- Test full application flow with MariaDB
- Test concurrent access scenarios
- Test network failure handling
- Test data consistency

### 5.3 Manual Testing Checklist
- [ ] Add new course
- [ ] Edit existing course
- [ ] Delete course (verify cascade to assignments)
- [ ] Add new assignment
- [ ] Edit assignment
- [ ] Delete assignment
- [ ] View assignments by date
- [ ] View next due assignment
- [ ] Application restart (data persists)

---

## Phase 6: Deployment & Rollback

### 6.1 Deployment Steps
1. Ensure Raspberry Pi MariaDB is running and accessible
2. Update application with new dependencies
3. Add configuration file with database credentials
4. Run migration tool to import existing data
5. Update `DataService` to use `MariaDbPersistenceService`
6. Test thoroughly
7. Deploy to production

### 6.2 Rollback Plan
If issues occur:
1. Stop application
2. Revert `DataService` to use `JsonPersistenceService`
3. Restore `semester.json` from backup
4. Restart application
5. Investigate and fix issues before retry

---

## Phase 7: Future Enhancements

### 7.1 Connection Management
- Implement connection pooling (HikariCP)
- Add connection retry logic
- Handle network timeouts gracefully

### 7.2 Performance Optimization
- Add database indexes for frequently queried fields
- Implement caching layer if needed
- Optimize queries with JOIN operations

### 7.3 Multi-User Support (Future)
- Add user authentication
- Add user table
- Associate data with users
- Implement proper access control

### 7.4 Backup Strategy
- Automated database backups on Raspberry Pi
- Export to JSON periodically as backup
- Consider replication for redundancy

---

## Network Configuration Notes

### Finding Your Raspberry Pi IP Address
```bash
hostname -I
```

### Testing Database Connection from Development Machine
```bash
# Test connection
mysql -h <raspberry_pi_ip> -u jscheduler_user -p jscheduler

# Or using telnet to test port
telnet <raspberry_pi_ip> 3306
```

### Static IP Recommendation
Configure static IP for Raspberry Pi to avoid connection issues when IP changes via DHCP.

---

## Security Considerations

1. **Use Strong Passwords** - Don't use default or weak passwords
2. **Network Security** - MariaDB exposed only on local network
3. **Encrypted Connections** - Consider SSL/TLS for database connections
4. **Credentials Storage** - Don't hardcode credentials; use config files (not committed to git)
5. **User Permissions** - Grant only necessary privileges to application user
6. **Regular Updates** - Keep MariaDB updated on Raspberry Pi

---

## Estimated Timeline

- **Phase 1** (Raspberry Pi Setup): 1-2 hours
- **Phase 2** (Schema Design): 1 hour
- **Phase 3** (Java Implementation): 4-6 hours
- **Phase 4** (Migration Tool): 2-3 hours
- **Phase 5** (Testing): 2-3 hours
- **Phase 6** (Deployment): 1 hour

**Total Estimated Time**: 11-16 hours

---

## Dependencies to Add

```gradle
dependencies {
    // Existing
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'org.openjfx:javafx-controls:17'
    implementation 'org.openjfx:javafx-fxml:17'

    // New for MariaDB
    implementation 'org.mariadb.jdbc:mariadb-java-client:3.3.2'
    implementation 'com.zaxxer:HikariCP:5.1.0'

    // Testing
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.3'
    testImplementation 'org.mockito:mockito-core:5.3.1'
}
```

---

## Questions to Answer Before Migration

1. What is your Raspberry Pi's IP address on the local network?
2. Will the Raspberry Pi have a static IP or use DHCP?
3. Do you want to keep JSON as a fallback option?
4. Should the app support switching between JSON and MariaDB?
5. Do you need the migration to preserve all existing data?

---

## Notes

- Keep `JsonPersistenceService` as backup option
- Consider making storage method configurable
- Document connection string format for future reference
- Test network connectivity issues and fallback behavior
