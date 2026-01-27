# j-scheduler-v1

Cross-platform desktop app for tracking college assignments for a single semester across up to 5 courses. Built in Java 17+ with a JavaFX UI, local JSON persistence, and optional reminders.

## Goals
- Simple, fast workflow for entering and checking assignments.
- Clean, low-friction UI that works on Windows and Linux.
- Local-first data stored in a readable JSON file.

## Core Features
- Course management: add, edit, delete courses (name, description, professor, semester).
- Assignment tracking: due date, submission deadline, status, and notes.
- Search and filter by course, status, date range, or title.
- Optional: calendar view and reminder notifications.

## UI Sketch (Concept)
```
+--------------------------------------------------------------------------------------+
| Toolbar: [Semester v] [Search................] [Status v] [From] [To] [+ Assignment] |
|          [+ Course]                                                                   |
+--------------------------------------------------------------------------------------+
| Tabs: [Assignments] [Calendar]                                                       |
+----------------------+---------------------------------------------------------------+
| Courses              | Assignments                                                   |
| [ListView]           | [TableView: Title | Course | Due | Deadline | Status | Notes] |
| [+ Add] [Edit]       |                                                               |
| [Delete]             |                                                               |
+----------------------+---------------------------------------------------------------+
| Details (selected assignment)                                                        |
| Title [text]  Due [date]  Deadline [date]  Status [combo]                            |
| Notes [textarea..........................]     [Save] [Revert]                       |
+--------------------------------------------------------------------------------------+
| Status: Saved 12:05 PM | Next due: Math202 HW3 (2 days)                               |
+--------------------------------------------------------------------------------------+
```

## Tech Stack
- Language: Java 17+ (LTS)
- UI: JavaFX
- Build: Gradle or Maven (to be added)
- Storage: JSON (Jackson or Gson)
- Packaging: jpackage for Windows and Linux installers

## Planned Project Structure
```
assignment-tracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── ui/         -> JavaFX views and controllers
│   │   │   ├── model/      -> Course, Assignment, Semester
│   │   │   ├── controller/ -> UI and business logic glue
│   │   │   ├── service/    -> JSON persistence and search
│   │   │   └── App.java    -> Entry point
│   │   └── resources/
│   │       └── data/       -> Default data and settings
│   └── test/
│       └── ...
├── build.gradle (or pom.xml)
└── README.md
```

## Data Model (Planned)
Course
- id
- name
- description
- professor
- semester

Assignment
- id
- courseId
- title
- description
- dueDate
- submissionDeadline
- status (Not Started, In Progress, Submitted, Late)
- notes

## Persistence (Planned JSON)
Data will load on startup and save on changes and exit.

Example:
```json
{
  "semester": "Fall 2026",
  "courses": [
    {
      "id": "c1",
      "name": "CS101",
      "description": "Intro to CS",
      "professor": "Dr. Lin"
    }
  ],
  "assignments": [
    {
      "id": "a1",
      "courseId": "c1",
      "title": "Homework 1",
      "description": "Chapters 1-2",
      "dueDate": "2026-09-15",
      "submissionDeadline": "2026-09-16",
      "status": "Not Started",
      "notes": ""
    }
  ]
}
```

## Getting Started
This repo is in early planning. Build files and source code will be added next.

Prerequisites
- JDK 17+
- JavaFX SDK (if not using a build plugin that pulls JavaFX)

Planned run commands (once build files exist)
- Gradle: `./gradlew run`
- Maven: `mvn javafx:run`

## Roadmap
Phase 1: MVP
- Course and assignment models
- CRUD UI for courses and assignments

Phase 2: Persistence
- JSON load and save

Phase 3: Search and Filter
- Search bar and status/date filters

Phase 4 (Optional): Calendar
- Timeline or month view

Phase 5 (Optional): Reminders
- Scheduled alerts before due dates

Phase 6: Packaging
- jpackage installers for Windows and Linux

## Packaging (Planned)
- Windows: `.exe` installer
- Linux: `.deb` and `.rpm`
- Use `jpackage` from JDK 17+

## License
TBD
