package com.jscheduler.model;

import javafx.beans.property.*;
import java.util.UUID;

public class Course {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty professor;
    private final StringProperty semester;

    // New fields for GPA calculation
    private final ObjectProperty<Double> creditHours;
    private final ObjectProperty<Double> currentGrade;
    private final StringProperty letterGrade;

    public Course(String name, String description, String professor, String semester) {
        this(generateId(), name, description, professor, semester, 3.0, null, null);
    }

    public Course(String id, String name, String description, String professor, String semester) {
        this(id, name, description, professor, semester, 3.0, null, null);
    }

    public Course(String id, String name, String description, String professor, String semester,
                  Double creditHours, Double currentGrade, String letterGrade) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.professor = new SimpleStringProperty(professor);
        this.semester = new SimpleStringProperty(semester);
        this.creditHours = new SimpleObjectProperty<>(creditHours != null ? creditHours : 3.0);
        this.currentGrade = new SimpleObjectProperty<>(currentGrade);
        this.letterGrade = new SimpleStringProperty(letterGrade);
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

    // Credit hours
    public ObjectProperty<Double> creditHoursProperty() {
        return creditHours;
    }

    public Double getCreditHours() {
        return creditHours.get();
    }

    public void setCreditHours(Double creditHours) {
        this.creditHours.set(creditHours);
    }

    // Current grade
    public ObjectProperty<Double> currentGradeProperty() {
        return currentGrade;
    }

    public Double getCurrentGrade() {
        return currentGrade.get();
    }

    public void setCurrentGrade(Double currentGrade) {
        this.currentGrade.set(currentGrade);
    }

    // Letter grade
    public StringProperty letterGradeProperty() {
        return letterGrade;
    }

    public String getLetterGrade() {
        return letterGrade.get();
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade.set(letterGrade);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
