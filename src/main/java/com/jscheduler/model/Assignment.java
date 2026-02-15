package com.jscheduler.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.UUID;

public class Assignment {
    private final StringProperty id;
    private final StringProperty courseId;
    private final StringProperty title;
    private final StringProperty description;
    private final ObjectProperty<LocalDate> dueDate;
    private final ObjectProperty<LocalDate> submissionDeadline;
    private final ObjectProperty<AssignmentStatus> status;
    private final StringProperty notes;

    private final StringProperty courseName;

    // New fields for enhanced features
    private final ObjectProperty<Double> grade;
    private final ObjectProperty<Double> maxGrade;
    private final ObjectProperty<AssignmentPriority> priority;
    private final BooleanProperty isRecurring;
    private final StringProperty recurrencePattern; // e.g., "WEEKLY", "BIWEEKLY", "MONTHLY"
    private final StringProperty attachmentPath;

    public Assignment(String courseId, String title, String description,
                     LocalDate dueDate, LocalDate submissionDeadline,
                     AssignmentStatus status, String notes) {
        this(generateId(), courseId, title, description, dueDate, submissionDeadline, status, notes,
             null, null, AssignmentPriority.MEDIUM, false, null, null);
    }

    public Assignment(String id, String courseId, String title, String description,
                     LocalDate dueDate, LocalDate submissionDeadline,
                     AssignmentStatus status, String notes) {
        this(id, courseId, title, description, dueDate, submissionDeadline, status, notes,
             null, null, AssignmentPriority.MEDIUM, false, null, null);
    }

    public Assignment(String id, String courseId, String title, String description,
                     LocalDate dueDate, LocalDate submissionDeadline,
                     AssignmentStatus status, String notes,
                     Double grade, Double maxGrade, AssignmentPriority priority,
                     boolean isRecurring, String recurrencePattern, String attachmentPath) {
        this.id = new SimpleStringProperty(id);
        this.courseId = new SimpleStringProperty(courseId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.submissionDeadline = new SimpleObjectProperty<>(submissionDeadline);
        this.status = new SimpleObjectProperty<>(status != null ? status : AssignmentStatus.NOT_STARTED);
        this.notes = new SimpleStringProperty(notes);
        this.courseName = new SimpleStringProperty("");

        // Initialize new fields
        this.grade = new SimpleObjectProperty<>(grade);
        this.maxGrade = new SimpleObjectProperty<>(maxGrade != null ? maxGrade : 100.0);
        this.priority = new SimpleObjectProperty<>(priority != null ? priority : AssignmentPriority.MEDIUM);
        this.isRecurring = new SimpleBooleanProperty(isRecurring);
        this.recurrencePattern = new SimpleStringProperty(recurrencePattern);
        this.attachmentPath = new SimpleStringProperty(attachmentPath);
    }

    private static String generateId() {
        return "a_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty courseIdProperty() {
        return courseId;
    }

    public String getCourseId() {
        return courseId.get();
    }

    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> submissionDeadlineProperty() {
        return submissionDeadline;
    }

    public LocalDate getSubmissionDeadline() {
        return submissionDeadline.get();
    }

    public void setSubmissionDeadline(LocalDate submissionDeadline) {
        this.submissionDeadline.set(submissionDeadline);
    }

    public ObjectProperty<AssignmentStatus> statusProperty() {
        return status;
    }

    public AssignmentStatus getStatus() {
        return status.get();
    }

    public void setStatus(AssignmentStatus status) {
        this.status.set(status);
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    // Grade tracking
    public ObjectProperty<Double> gradeProperty() {
        return grade;
    }

    public Double getGrade() {
        return grade.get();
    }

    public void setGrade(Double grade) {
        this.grade.set(grade);
    }

    public ObjectProperty<Double> maxGradeProperty() {
        return maxGrade;
    }

    public Double getMaxGrade() {
        return maxGrade.get();
    }

    public void setMaxGrade(Double maxGrade) {
        this.maxGrade.set(maxGrade);
    }

    // Priority
    public ObjectProperty<AssignmentPriority> priorityProperty() {
        return priority;
    }

    public AssignmentPriority getPriority() {
        return priority.get();
    }

    public void setPriority(AssignmentPriority priority) {
        this.priority.set(priority);
    }

    // Recurring assignments
    public BooleanProperty isRecurringProperty() {
        return isRecurring;
    }

    public boolean isRecurring() {
        return isRecurring.get();
    }

    public void setRecurring(boolean recurring) {
        this.isRecurring.set(recurring);
    }

    public StringProperty recurrencePatternProperty() {
        return recurrencePattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern.get();
    }

    public void setRecurrencePattern(String pattern) {
        this.recurrencePattern.set(pattern);
    }

    // File attachments
    public StringProperty attachmentPathProperty() {
        return attachmentPath;
    }

    public String getAttachmentPath() {
        return attachmentPath.get();
    }

    public void setAttachmentPath(String path) {
        this.attachmentPath.set(path);
    }
}
