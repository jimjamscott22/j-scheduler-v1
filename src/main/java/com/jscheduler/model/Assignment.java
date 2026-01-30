package com.jscheduler.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    public Assignment(String courseId, String title, String description,
                     LocalDate dueDate, LocalDate submissionDeadline,
                     AssignmentStatus status, String notes) {
        this(generateId(), courseId, title, description, dueDate, submissionDeadline, status, notes);
    }

    public Assignment(String id, String courseId, String title, String description,
                     LocalDate dueDate, LocalDate submissionDeadline,
                     AssignmentStatus status, String notes) {
        this.id = new SimpleStringProperty(id);
        this.courseId = new SimpleStringProperty(courseId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.submissionDeadline = new SimpleObjectProperty<>(submissionDeadline);
        this.status = new SimpleObjectProperty<>(status != null ? status : AssignmentStatus.NOT_STARTED);
        this.notes = new SimpleStringProperty(notes);
        this.courseName = new SimpleStringProperty("");
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
}
