package com.jscheduler.ui.dialog;

import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML
    private TextField creditHoursField;
    @FXML
    private TextField currentGradeField;
    @FXML
    private ComboBox<String> letterGradeCombo;

    private Course result;
    private boolean editMode = false;
    private String courseId;

    @FXML
    private void initialize() {
        letterGradeCombo.setItems(FXCollections.observableArrayList(
                "A+", "A", "A-",
                "B+", "B", "B-",
                "C+", "C", "C-",
                "D+", "D", "D-",
                "F"
        ));

        creditHoursField.setText("3.0");

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

            if (course.getCreditHours() != null) {
                creditHoursField.setText(String.valueOf(course.getCreditHours()));
            }

            if (course.getCurrentGrade() != null) {
                currentGradeField.setText(String.valueOf(course.getCurrentGrade()));
            }

            if (course.getLetterGrade() != null) {
                letterGradeCombo.setValue(course.getLetterGrade());
            }
        }
    }

    public Course getResult() {
        if (result == null) {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String professor = professorField.getText().trim();
            String semester = semesterField.getText().trim();

            Double creditHours = 3.0;
            if (creditHoursField.getText() != null && !creditHoursField.getText().trim().isEmpty()) {
                try {
                    creditHours = Double.parseDouble(creditHoursField.getText().trim());
                } catch (NumberFormatException e) {
                    creditHours = 3.0;
                }
            }

            Double currentGrade = null;
            if (currentGradeField.getText() != null && !currentGradeField.getText().trim().isEmpty()) {
                try {
                    currentGrade = Double.parseDouble(currentGradeField.getText().trim());
                } catch (NumberFormatException e) {
                    // Invalid grade, leave as null
                }
            }

            String letterGrade = letterGradeCombo.getValue();

            if (editMode) {
                result = new Course(courseId, name, description, professor, semester, creditHours, currentGrade, letterGrade);
            } else {
                result = new Course(name, description, professor, semester);
                result.setCreditHours(creditHours);
                result.setCurrentGrade(currentGrade);
                result.setLetterGrade(letterGrade);
            }
        }
        return result;
    }
}
