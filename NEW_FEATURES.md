# J-Scheduler New Features Guide

## Version 1.1.0 - Feature Update

This document describes the new features added to J-Scheduler for enhanced assignment and course management.

---

## New Features Overview

### 1. **Dashboard with Statistics**

A comprehensive dashboard tab that provides at-a-glance insights into your academic performance:

#### Statistics Cards:
- **Total Courses** - Number of courses you're enrolled in
- **Total Assignments** - Total assignments across all courses
- **Completed Assignments** - Assignments marked as submitted
- **Pending Assignments** - Assignments not yet completed
- **Due This Week** - Assignments due within the next 7 days
- **Overdue** - Past-due assignments not yet submitted
- **Current GPA** - Calculated from letter grades entered for courses
- **Average Grade** - Average grade from graded assignments

#### Charts:
- **Assignment Status Distribution** (Pie Chart) - Visual breakdown of assignment statuses
- **Pending Assignments by Course** (Bar Chart) - Shows workload distribution across courses
- **Pending Assignments by Priority** (Bar Chart) - Displays assignment priority distribution

### 2. **Grade Tracking**

Track your performance on individual assignments:

- **Grade Field** - Enter the grade you received (e.g., 95)
- **Max Grade Field** - Set the maximum possible points (default: 100)
- **Percentage Calculation** - Automatically calculate your percentage
- **Average Grade Display** - See your average across all graded assignments

### 3. **GPA Calculator**

Comprehensive GPA tracking at the course level:

#### Course Settings:
- **Credit Hours** - Specify the credit hours for each course (default: 3.0)
- **Current Grade** - Enter your numeric grade in the course (0-100)
- **Letter Grade** - Select your letter grade (A+, A, A-, B+, B, B-, etc.)

#### GPA Calculation:
- **4.0 Scale** - Standard GPA calculation using 4.0 scale
- **Semester GPA** - Calculate GPA for specific semesters
- **Overall GPA** - View cumulative GPA across all courses
- **Letter Grade Conversion** - Automatic conversion between numeric and letter grades

#### Grade Point Scale:
```
A+, A  = 4.0
A-     = 3.7
B+     = 3.3
B      = 3.0
B-     = 2.7
C+     = 2.3
C      = 2.0
C-     = 1.7
D+     = 1.3
D      = 1.0
D-     = 0.7
F      = 0.0
```

### 4. **Assignment Priority Levels**

Organize and prioritize your workload:

#### Priority Levels:
- **Urgent** - Critical assignments requiring immediate attention (Red)
- **High** - Important assignments with near deadlines (Orange)
- **Medium** - Standard priority assignments (Blue) - Default
- **Low** - Lower priority or long-term assignments (Gray)

#### Features:
- Color-coded display in the UI
- Filter assignments by priority
- Priority-based charts in the dashboard
- Helps you focus on what matters most

### 5. **Recurring Assignments**

Perfect for weekly quizzes, discussion posts, or regular homework:

#### Recurrence Patterns:
- **Daily** - Assignments that repeat every day
- **Weekly** - Assignments that repeat every week
- **Biweekly** - Assignments that repeat every two weeks
- **Monthly** - Assignments that repeat every month

#### How It Works:
1. Check the "Recurring" checkbox when creating/editing an assignment
2. Select the recurrence pattern from the dropdown
3. The system tracks the pattern for your reference
4. Useful for planning and time management

### 6. **File Attachments**

Attach reference files to your assignments:

#### Supported Features:
- **Browse Button** - Select any file from your computer
- **File Path Storage** - Stores the full path to the attached file
- **Quick Access** - Keep important files linked to assignments
- **Clear Button** - Remove attachment if needed

#### Use Cases:
- Link to assignment PDFs
- Attach project requirements documents
- Reference study materials
- Store submission confirmations

---

## Database Schema Updates

The database has been enhanced to support these features. If you have an existing database:

### Run the Migration Script:
```sql
-- Execute migration.sql to add new columns
source migration.sql;
```

### New Course Columns:
- `credit_hours` (DECIMAL 3,1) - Default: 3.0
- `current_grade` (DECIMAL 5,2) - Nullable
- `letter_grade` (VARCHAR 3) - Nullable

### New Assignment Columns:
- `grade` (DECIMAL 5,2) - Nullable
- `max_grade` (DECIMAL 5,2) - Default: 100.0
- `priority` (VARCHAR 20) - Default: 'Medium'
- `is_recurring` (BOOLEAN) - Default: FALSE
- `recurrence_pattern` (VARCHAR 50) - Nullable
- `attachment_path` (VARCHAR 500) - Nullable

---

## How to Use the New Features

### Adding a Course with Grade Tracking:
1. Click "+ Course" button
2. Fill in basic information (Name, Professor, Semester)
3. **NEW:** Set Credit Hours (e.g., 3.0, 4.0)
4. **NEW:** Optionally enter Current Grade (0-100)
5. **NEW:** Select Letter Grade from dropdown
6. Click OK to save

### Creating an Assignment with All Features:
1. Click "+ Assignment" button
2. Select Course and enter Title
3. Set Due Date and Deadline
4. **NEW:** Choose Priority Level (Low/Medium/High/Urgent)
5. **NEW:** Enter Grade if already graded
6. **NEW:** Check "Recurring" and select pattern if applicable
7. **NEW:** Click "Browse" to attach a file
8. Add any notes and click OK

### Viewing the Dashboard:
1. Click the "Dashboard" tab (first tab)
2. View real-time statistics at the top
3. Analyze charts for workload and priority distribution
4. Monitor your GPA and average grade

### Calculating Your GPA:
1. Add letter grades to your courses
2. Ensure credit hours are set correctly
3. View your GPA on the Dashboard
4. GPA updates automatically as you add/update grades

---

## Tips and Best Practices

### For Grade Tracking:
- Enter grades as you receive them for accurate averages
- Use the max grade field if assignments are worth different amounts
- Track your progress throughout the semester

### For Priority Management:
- Mark time-sensitive assignments as "Urgent" or "High"
- Use "Medium" for standard homework
- Mark long-term projects as "Low" until they're closer to due

### For Recurring Assignments:
- Set up weekly assignments at the start of the semester
- Use recurrence patterns to plan ahead
- Great for courses with regular quizzes or discussions

### For GPA Tracking:
- Update letter grades regularly throughout the semester
- Verify credit hours match your course registration
- Use the dashboard to monitor your academic standing

### For File Attachments:
- Attach assignment rubrics or requirements
- Link to research materials or resources
- Store important reference documents

---

## Keyboard Shortcuts & UI Tips

- **Double-click** a course to edit it
- **Double-click** an assignment to edit it
- Use **Tab** to navigate through dialog fields
- The Dashboard **refreshes automatically** when data changes

---

## Color Coding

The UI uses intuitive color coding:

### Assignment Status:
- **Gray** - Not Started
- **Blue** - In Progress
- **Green** - Submitted
- **Red** - Late

### Priority Levels:
- **Red** - Urgent
- **Orange** - High
- **Blue** - Medium
- **Gray** - Low

### Statistics Cards:
- **Green** - Positive metrics (Completed)
- **Orange** - Warning metrics (Pending)
- **Red** - Alert metrics (Overdue)
- **Blue** - Information metrics (Due This Week)
- **Purple** - Academic metrics (GPA, Grades)

---

## Troubleshooting

### Dashboard not showing data:
- Ensure you have courses and assignments created
- Check that letter grades are entered for GPA calculation
- Verify database connection is active

### GPA showing as "N/A":
- Enter letter grades for at least one course
- Verify credit hours are set for courses
- Check that grades follow the A-F scale

### Attachment file not found:
- Ensure the file path is correct
- Verify the file hasn't been moved or deleted
- Use absolute paths for reliability

---

## Future Enhancements

Planned features for future releases:
- Calendar view with recurring assignment visualization
- Export reports (PDF, CSV)
- Email notifications for upcoming deadlines
- Grade predictions and "what-if" scenarios
- Mobile companion app
- Cloud synchronization

---

## Support

For issues or feature requests:
1. Check the database migration was run successfully
2. Verify all dependencies are installed
3. Review error logs in the console
4. Consult the schema.sql for database structure

---

**Last Updated**: February 15, 2026
**Version**: 1.1.0

