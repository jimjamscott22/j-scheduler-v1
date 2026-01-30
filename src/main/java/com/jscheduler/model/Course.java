package com.jscheduler.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.UUID;

public class Course {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty professor;
    private final StringProperty semester;

    public Course(String name, String description, String professor, String semester) {
        this(generateId(), name, description, professor, semester);
    }

    public Course(String id, String name, String description, String professor, String semester) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.professor = new SimpleStringProperty(professor);
        this.semester = new SimpleStringProperty(semester);
    }

    private static String generateId() {
        return "c_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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

    public StringProperty professorProperty() {
        return professor;
    }

    public String getProfessor() {
        return professor.get();
    }

    public void setProfessor(String professor) {
        this.professor.set(professor);
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
