package com.jscheduler.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty professor;
    private final StringProperty semester;

    public Course(String id, String name, String description, String professor, String semester) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.professor = new SimpleStringProperty(professor);
        this.semester = new SimpleStringProperty(semester);
    }

    // Property getters
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty professorProperty() {
        return professor;
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    // Value getters
    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getProfessor() {
        return professor.get();
    }

    public String getSemester() {
        return semester.get();
    }

    // Setters
    public void setId(String value) {
        id.set(value);
    }

    public void setName(String value) {
        name.set(value);
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public void setProfessor(String value) {
        professor.set(value);
    }

    public void setSemester(String value) {
        semester.set(value);
    }

    @Override
    public String toString() {
        String displayName = name.get();
        String displaySemester = semester.get();
        if (displaySemester != null && !displaySemester.isEmpty()) {
            return displayName + " (" + displaySemester + ")";
        }
        return displayName;
    }
}
