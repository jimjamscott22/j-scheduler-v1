# CLAUDE.md - j-scheduler-v1

## Project Overview

A JavaFX desktop application for tracking college assignments across courses within a semester. Built with Java 21 and MariaDB for persistent storage.

## Tech Stack

- **Language:** Java 21 (LTS)
- **UI Framework:** JavaFX 21.0.2
- **Build System:** Gradle 8.x
- **Database:** MariaDB (on Raspberry Pi, local network)
- **Legacy Fallback:** JSON via Gson 2.10.1

## Project Structure

```
src/main/java/com/jscheduler/
├── App.java                    # JavaFX entry point
├── model/                      # Domain models (Assignment, Course, Semester, AssignmentStatus)
├── service/                    # Business logic (DataService, JsonPersistenceService)
├── data/                       # Database layer (DatabaseConnection, *Repository)
└── ui/                         # Controllers (MainController, dialog controllers)

src/main/resources/
├── ui/                         # FXML layouts and CSS
└── database.properties         # MariaDB connection config
```

## Build & Run

```bash
./gradlew build    # Compile
./gradlew run      # Run application
```

## Key Patterns

- **Singleton:** DataService, DatabaseConnection, Repositories
- **Repository Pattern:** CourseRepository, AssignmentRepository
- **MVC:** Controllers, Models, FXML Views
- **Observer:** ObservableList for reactive UI updates

## ID Conventions

- Courses: `c_` + UUID (8 chars) - e.g., `c_a1b2c3d4`
- Assignments: `a_` + UUID (8 chars) - e.g., `a_e5f6g7h8`

## Assignment Statuses

- `NOT_STARTED` - Not Started
- `IN_PROGRESS` - In Progress
- `SUBMITTED` - Submitted
- `LATE` - Late

## Database

- Config: `src/main/resources/database.properties`
- Schema: `schema.sql` (courses, assignments tables with FK cascade)
- Setup guide: `DATABASE_SETUP.md`

## Data Flow

1. User action → MainController
2. Controller → Repository (CRUD)
3. Repository → SQL via DatabaseConnection
4. ObservableList auto-updates UI
5. Changes persist to MariaDB immediately

## Important Files

| File | Purpose |
|------|---------|
| `App.java` | Application entry point |
| `MainController.java` | Main UI logic (502 lines) |
| `CourseRepository.java` | Course database operations |
| `AssignmentRepository.java` | Assignment database operations |
| `MainView.fxml` | Main window layout |
| `database.properties` | DB connection settings |

## Current Limitations

- No automated tests
- Hard-coded default semester ("Fall 2026")
- No multi-user support
- Calendar view not implemented
- No reminder notifications yet
