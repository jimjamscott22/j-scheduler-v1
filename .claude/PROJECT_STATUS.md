# J-Scheduler Project Status

**Last Updated**: 2026-01-30

---

## Current Version
- **Version**: 1.0.0
- **Status**: Active Development
- **Platform**: JavaFX Desktop Application
- **Java Version**: 17

---

## Current Features

### Core Functionality
- ✅ Course management (add, edit, delete)
- ✅ Assignment tracking with status (Not Started, In Progress, Submitted, Graded)
- ✅ Due date and deadline management
- ✅ Next assignment due indicator
- ✅ Semester organization

### Storage
- **Current**: JSON file storage (`~/.jscheduler/semester.json`)
- **Library**: Gson for serialization
- **Backup**: Automatic backup on corrupted file detection

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
│   │   │   ├── Assignment.java
│   │   │   ├── AssignmentStatus.java
│   │   │   ├── Course.java
│   │   │   └── Semester.java
│   │   ├── service/                           # Business logic
│   │   │   ├── DataService.java              # Main data management (singleton)
│   │   │   └── JsonPersistenceService.java   # JSON file I/O
│   │   └── ui/                                # UI controllers
│   │       ├── MainController.java
│   │       └── dialog/
│   │           ├── AssignmentDialogController.java
│   │           └── CourseDialogController.java
│   └── resources/
│       └── ui/                                # FXML layouts
│           ├── AssignmentDialog.fxml
│           ├── CourseDialog.fxml
│           └── main.fxml
├── build.gradle                               # Gradle build configuration
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

### 1.0.0 (2026-01-30)
- Initial release
- Core course and assignment management
- JSON-based storage
- JavaFX desktop UI
