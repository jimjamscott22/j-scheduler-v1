package com.jscheduler.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Assignment {
    private final StringProperty id;
    private final StringProperty courseId;
    private final StringProperty title;
    private final StringProperty description;
    private final ObjectProperty<LocalDate> dueDate;
    private final ObjectProperty<LocalDate> deadline;
    private final StringProperty status;
    private final StringProperty notes;

    public Assignment(String id, String courseId, String title, String description,
                      LocalDate dueDate, LocalDate deadline, String status, String notes) {
        this.id = new SimpleStringProperty(id);
        this.courseId = new SimpleStringProperty(courseId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.status = new SimpleStringProperty(status);
        this.notes = new SimpleStringProperty(notes);
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty courseIdProperty() { return courseId; }
    public StringProperty titleProperty() { return title; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public ObjectProperty<LocalDate> deadlineProperty() { return deadline; }
    public StringProperty statusProperty() { return status; }
    public StringProperty notesProperty() { return notes; }

    // Value getters
    public String getId() { return id.get(); }
    public String getCourseId() { return courseId.get(); }
    public String getTitle() { return title.get(); }
    public String getDescription() { return description.get(); }
    public LocalDate getDueDate() { return dueDate.get(); }
    public LocalDate getDeadline() { return deadline.get(); }
    public String getStatus() { return status.get(); }
    public String getNotes() { return notes.get(); }

    // Setters
    public void setId(String value) { id.set(value); }
    public void setCourseId(String value) { courseId.set(value); }
    public void setTitle(String value) { title.set(value); }
    public void setDescription(String value) { description.set(value); }
    public void setDueDate(LocalDate value) { dueDate.set(value); }
    public void setDeadline(LocalDate value) { deadline.set(value); }
    public void setStatus(String value) { status.set(value); }
    public void setNotes(String value) { notes.set(value); }

    @Override
    public String toString() {
        return title.get();
    }
}
