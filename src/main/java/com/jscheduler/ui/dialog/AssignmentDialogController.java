package com.jscheduler.ui.dialog;

import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentDialogController {

    @FXML
    private DialogPane dialogPane;
    @FXML
    private ComboBox<String> courseCombo;
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

    private Assignment result;
    private boolean editMode = false;
    private String assignmentId;
    private Map<String, String> courseMap = new HashMap<>();

    @FXML
    private void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList(
                "Not Started",
                "In Progress",
                "Submitted",
                "Late"
        ));
        statusCombo.getSelectionModel().selectFirst();

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.disableProperty().bind(
                titleField.textProperty().isEmpty()
                .or(courseCombo.valueProperty().isNull())
                .or(dueDatePicker.valueProperty().isNull())
            );
        }
    }

    public void setCourses(List<Course> courses) {
        courseMap.clear();
        for (Course course : courses) {
            courseMap.put(course.getName(), course.getId());
        }
        courseCombo.setItems(FXCollections.observableArrayList(courseMap.keySet()));
    }

    public void setData(Assignment assignment) {
        if (assignment != null) {
            this.editMode = true;
            this.assignmentId = assignment.getId();
            titleField.setText(assignment.getTitle());
            descriptionArea.setText(assignment.getDescription());
            dueDatePicker.setValue(assignment.getDueDate());
            deadlineDatePicker.setValue(assignment.getSubmissionDeadline());
            notesArea.setText(assignment.getNotes());

            if (assignment.getStatus() != null) {
                statusCombo.setValue(assignment.getStatus().getDisplayName());
            }

            for (Map.Entry<String, String> entry : courseMap.entrySet()) {
                if (entry.getValue().equals(assignment.getCourseId())) {
                    courseCombo.setValue(entry.getKey());
                    break;
                }
            }
        }
    }

    public Assignment getResult() {
        if (result == null) {
            String selectedCourseName = courseCombo.getValue();
            String courseId = courseMap.get(selectedCourseName);
            String title = titleField.getText().trim();
            String description = descriptionArea.getText() != null ? descriptionArea.getText().trim() : "";
            String notes = notesArea.getText() != null ? notesArea.getText().trim() : "";
            AssignmentStatus status = AssignmentStatus.fromString(statusCombo.getValue());

            if (editMode) {
                result = new Assignment(
                    assignmentId,
                    courseId,
                    title,
                    description,
                    dueDatePicker.getValue(),
                    deadlineDatePicker.getValue(),
                    status,
                    notes
                );
            } else {
                result = new Assignment(
                    courseId,
                    title,
                    description,
                    dueDatePicker.getValue(),
                    deadlineDatePicker.getValue(),
                    status,
                    notes
                );
            }
        }
        return result;
    }
}
