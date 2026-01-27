package com.jscheduler.ui.dialog;

import com.jscheduler.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.UUID;

public class CourseDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField professorField;
    @FXML
    private TextField semesterField;

    private Course course;
    private boolean okClicked = false;

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            nameField.setText(course.getName());
            descriptionArea.setText(course.getDescription());
            professorField.setText(course.getProfessor());
            semesterField.setText(course.getSemester());
        }
    }

    public Course getCourse() {
        if (okClicked) {
            if (course == null) {
                course = new Course(
                    UUID.randomUUID().toString(),
                    nameField.getText(),
                    descriptionArea.getText(),
                    professorField.getText(),
                    semesterField.getText()
                );
            } else {
                course.setName(nameField.getText());
                course.setDescription(descriptionArea.getText());
                course.setProfessor(professorField.getText());
                course.setSemester(semesterField.getText());
            }
        }
        return course;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
}
