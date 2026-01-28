package com.jscheduler.ui.dialog;

import com.jscheduler.model.Assignment;
import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.UUID;

public class AssignmentDialogController {

    @FXML
    private ComboBox<Course> courseCombo;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextArea notesArea;

    private Assignment assignment;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList(
                "Not Started",
                "In Progress",
                "Submitted",
                "Late"
        ));
        statusCombo.getSelectionModel().selectFirst();
    }

    public void setCourseOptions(ObservableList<Course> courses, Course selectedCourse) {
        courseCombo.setItems(courses);
        if (selectedCourse != null) {
            courseCombo.getSelectionModel().select(selectedCourse);
        } else if (!courses.isEmpty()) {
            courseCombo.getSelectionModel().selectFirst();
        }
    }

    public Assignment getAssignment() {
        if (okClicked) {
            Course selectedCourse = courseCombo.getSelectionModel().getSelectedItem();
            String courseId = selectedCourse == null ? null : selectedCourse.getId();
            if (assignment == null) {
                assignment = new Assignment(
                        UUID.randomUUID().toString(),
                        courseId,
                        titleField.getText(),
                        descriptionArea.getText(),
                        dueDatePicker.getValue(),
                        deadlineDatePicker.getValue(),
                        statusCombo.getValue(),
                        notesArea.getText()
                );
            } else {
                assignment.setCourseId(courseId);
                assignment.setTitle(titleField.getText());
                assignment.setDescription(descriptionArea.getText());
                assignment.setDueDate(dueDatePicker.getValue());
                assignment.setDeadline(deadlineDatePicker.getValue());
                assignment.setStatus(statusCombo.getValue());
                assignment.setNotes(notesArea.getText());
            }
        }
        return assignment;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
}
