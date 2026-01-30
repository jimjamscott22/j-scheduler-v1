package com.jscheduler.ui.dialog;

import com.jscheduler.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CourseDialogController {

    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField professorField;
    @FXML
    private TextField semesterField;

    private Course result;
    private boolean editMode = false;
    private String courseId;

    @FXML
    private void initialize() {
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.disableProperty().bind(
                nameField.textProperty().isEmpty()
                .or(professorField.textProperty().isEmpty())
                .or(semesterField.textProperty().isEmpty())
            );
        }
    }

    public void setData(Course course) {
        if (course != null) {
            this.editMode = true;
            this.courseId = course.getId();
            nameField.setText(course.getName());
            descriptionArea.setText(course.getDescription());
            professorField.setText(course.getProfessor());
            semesterField.setText(course.getSemester());
        }
    }

    public Course getResult() {
        if (result == null) {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String professor = professorField.getText().trim();
            String semester = semesterField.getText().trim();

            if (editMode) {
                result = new Course(courseId, name, description, professor, semester);
            } else {
                result = new Course(name, description, professor, semester);
            }
        }
        return result;
    }
}
