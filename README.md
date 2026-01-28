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

### Prerequisites
- **JDK 17, 19, or 21 (LTS recommended)** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/temurin/releases/)
- **Git** (optional, for cloning the repository)

**Important:** This project uses Gradle 8.12, which supports Java 17-21. If you have a newer Java version (22+), you'll need to install Java 21 alongside it.

Note: JavaFX dependencies are automatically managed by the Gradle JavaFX plugin - no manual setup required.

### Setup Instructions

1. **Clone or download the repository**
   ```bash
   git clone https://github.com/yourusername/j-scheduler-v1.git
   cd j-scheduler-v1
   ```

2. **Verify Java installation**
   ```bash
   java -version
   ```
   Ensure version 17 or higher is installed.

3. **Build the project**
   
   On Windows (PowerShell):
   ```powershell
   .\gradlew.bat build
   ```
   
   On Windows (Command Prompt):
   ```cmd
   gradlew.bat build
   ```
   
   On Linux/Mac:
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   
   On Windows (PowerShell):
   ```powershell
   .\gradlew.bat run
   ```
   
   On Windows (Command Prompt):
   ```cmd
   gradlew.bat run
   ```
   
   On Linux/Mac:
   ```bash
   ./gradlew run
   ```

### Troubleshooting

- **"Unsupported class file major version"**: Your Java version is too new for Gradle 8.12. Install Java 21 LTS from [Adoptium](https://adoptium.net/temurin/releases/?version=21) and set JAVA_HOME:
  ```powershell
  # PowerShell (temporary, for current session)
  $env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot"
  
  # To set permanently (Windows):
  # 1. Search "Environment Variables" in Windows
  # 2. Click "Environment Variables" button
  # 3. Add/Edit JAVA_HOME under System Variables
  # 4. Restart your terminal/IDE
  ```

- **JAVA_HOME not set**: Ensure the `JAVA_HOME` environment variable points to your JDK 17-21 installation
- **Permission denied (Linux/Mac)**: Run `chmod +x gradlew` to make the Gradle wrapper executable
- **Build fails**: Try cleaning first with `./gradlew clean build`

### Development

To open the project in an IDE:
- **IntelliJ IDEA**: File → Open → Select the project folder (build.gradle will be auto-detected)
- **Eclipse**: Import → Gradle → Existing Gradle Project
- **VS Code**: Open folder with Java Extension Pack installed

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
