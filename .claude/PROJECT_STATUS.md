# J-Scheduler Project Status

**Last Updated**: 2026-02-15

---

## Current Version
- **Version**: 1.1.0
- **Status**: Active Development
- **Platform**: JavaFX Desktop Application
- **Java Version**: 17

---

## Current Features

### Core Functionality
- ✅ Course management (add, edit, delete)
- ✅ Assignment tracking with status (Not Started, In Progress, Submitted, Late)
- ✅ Due date and deadline management
- ✅ Next assignment due indicator
- ✅ Semester organization
- ✅ Course filtering (click course to see its assignments)
- ✅ "All Assignments" button to view all assignments

### NEW FEATURES (v1.1.0)

#### Dashboard with Statistics
- ✅ Statistics cards showing key metrics
- ✅ Pie chart for assignment status distribution
- ✅ Bar chart for pending assignments by course
- ✅ Bar chart for assignments by priority
- ✅ Real-time GPA calculation
- ✅ Average grade display

#### Grade Tracking
- ✅ Grade field for each assignment (0-100)
- ✅ Max grade field (default: 100)
- ✅ Percentage calculation
- ✅ Average grade across all assignments

#### GPA Calculator
- ✅ Credit hours per course
- ✅ Numeric grade per course (0-100)
- ✅ Letter grade selection (A+ to F)
- ✅ 4.0 scale GPA calculation
- ✅ Semester and overall GPA

#### Assignment Priority Levels
- ✅ Four priority levels: Urgent, High, Medium, Low
- ✅ Color-coded display
- ✅ Priority-based filtering
- ✅ Priority distribution charts

#### Recurring Assignments
- ✅ Recurring checkbox
- ✅ Recurrence patterns: Daily, Weekly, Biweekly, Monthly
- ✅ Pattern tracking

#### File Attachments
- ✅ File browser for selecting attachments
- ✅ Full path storage
- ✅ Clear attachment button
- ✅ Support for any file type

### Storage
- **Current**: MariaDB database
- **Connection**: JDBC with connection pooling
- **Backup**: Database-level backup recommended

### User Interface
- JavaFX-based desktop application
- Dialog-based course and assignment creation/editing
- Main controller with observable lists for real-time UI updates

---

## Recent Changes (2026-01-30)

### Bug Fixes
- Changed button type from `OK_DONE` to `OK` in dialog FXML files
  - `src/main/resources/ui/CourseDialog.fxml`
  - `src/main/resources/ui/AssignmentDialog.fxml`

---

## Project Structure

```
j-scheduler-v1/
├── src/main/
│   ├── java/com/jscheduler/
│   │   ├── App.java                           # Application entry point
│   │   ├── model/                             # Data models
│   │   │   ├── Assignment.java                # Assignment model with new fields
│   │   │   ├── AssignmentStatus.java          # Assignment status enum
│   │   │   ├── AssignmentPriority.java        # NEW: Priority levels
│   │   │   └── Course.java                    # Course model with GPA fields
│   │   ├── data/                              # Data access layer
│   │   │   ├── AssignmentRepository.java      # Assignment CRUD operations
│   │   │   ├── CourseRepository.java          # Course CRUD operations
│   │   │   └── DatabaseConnection.java        # Database connection management
│   │   ├── service/                           # Business logic
│   │   │   └── GPACalculator.java             # NEW: GPA calculation service
│   │   └── ui/                                # UI controllers
│   │       ├── MainController.java            # Main window controller
│   │       ├── DashboardController.java       # NEW: Dashboard controller
│   │       └── dialog/
│   │           ├── AssignmentDialogController.java  # Assignment dialog
│   │           └── CourseDialogController.java      # Course dialog
│   └── resources/
│       ├── database.properties                # Database configuration
│       └── ui/                                # FXML layouts
│           ├── AssignmentDialog.fxml          # Assignment dialog with new fields
│           ├── CourseDialog.fxml              # Course dialog with GPA fields
│           ├── Dashboard.fxml                 # NEW: Dashboard layout
│           ├── MainView.fxml                  # Main window layout
│           └── styles.css                     # Application styles
├── build.gradle                               # Gradle build configuration
├── schema.sql                                 # Database schema
├── migration.sql                              # NEW: Migration script for existing DBs
├── NEW_FEATURES.md                           # NEW: Feature documentation
└── .claude/                                   # Project documentation
    ├── PROJECT_STATUS.md                      # This file
    └── MARIADB_MIGRATION_PLAN.md             # Database migration plan
```

---

## Key Design Patterns

### Singleton Pattern
- `DataService` - Single instance manages all data operations

### Observer Pattern
- `ObservableList` for courses and assignments
- Automatic UI updates when data changes

### Service Layer
- `DataService` - Coordinates between persistence and UI
- `JsonPersistenceService` - Handles file I/O operations

---

## Dependencies

### Core
- JavaFX 17 (Controls, FXML)
- Gson 2.10.1 (JSON serialization)

### Build
- Gradle 8.10.2
- JavaFX Gradle Plugin

---

## Planned Improvements

### High Priority
1. **Database Migration** - See `MARIADB_MIGRATION_PLAN.md`
   - Migrate from JSON to MariaDB on Raspberry Pi
   - Network-based storage for multi-device access
   - Improved data integrity and querying

### Medium Priority
2. **Enhanced UI**
   - Assignment filtering (by course, status, date range)
   - Calendar view for assignments
   - Dashboard with statistics

3. **Additional Features**
   - Grade tracking
   - GPA calculator
   - Assignment priority levels
   - Recurring assignments
   - File attachments for assignments

### Low Priority
4. **Multi-User Support**
   - User authentication
   - User-specific data isolation
   - Shared course data

5. **Export/Import**
   - Export to CSV/PDF
   - Import from other systems
   - Sync with calendar applications

6. **Notifications**
   - Desktop notifications for upcoming due dates
   - Reminder system
   - Email integration (optional)

---

## Known Issues

### Current
- None reported

### Technical Debt
- No automated tests currently
- Hard-coded default semester name ("Fall 2026")
- Limited error handling in UI layer
- No logging framework

---

## Testing Status
- **Unit Tests**: Not implemented
- **Integration Tests**: Not implemented
- **Manual Testing**: Ongoing

---

## Build Instructions

### Prerequisites
- JDK 17 or higher
- Gradle 8.x

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew run
```

### Package
```bash
./gradlew jpackage
```

---

## Development Notes

### Data Flow
1. UI controllers trigger actions
2. `DataService` coordinates operations
3. `JsonPersistenceService` handles persistence
4. ObservableLists update UI automatically

### Adding New Features
1. Update model classes if needed
2. Modify `DataService` for business logic
3. Update persistence service for storage
4. Create/modify UI controllers and FXML

---

## Configuration

### Data Location
- **Windows**: `%USERPROFILE%\.jscheduler\semester.json`
- **Linux/Mac**: `~/.jscheduler/semester.json`

### Backup Files
- Corrupted files backed up as `semester.json.backup`

---

## Contact & Collaboration
- Project maintained by: Jamie
- Platform: Local development on Windows
- IDE: IntelliJ IDEA

---

## Version History

### 1.1.0 (2026-02-15)
- ✨ NEW: Dashboard with comprehensive statistics and charts
- ✨ NEW: Grade tracking for assignments (grade/max grade)
- ✨ NEW: GPA calculator with letter grades and credit hours
- ✨ NEW: Assignment priority levels (Urgent, High, Medium, Low)
- ✨ NEW: Recurring assignments with multiple patterns
- ✨ NEW: File attachments for assignments
- 🔧 Enhanced UI with color-coded priorities and statuses
- 🔧 Updated database schema with new fields
- 📝 Added migration.sql for existing databases
- 📝 Comprehensive NEW_FEATURES.md documentation

### 1.0.0 (2026-01-30)
- Initial release
- Core course and assignment management
- MariaDB database storage
- JavaFX desktop UI
- Status tracking (Not Started, In Progress, Submitted, Late)
- Course filtering and "All Assignments" view
