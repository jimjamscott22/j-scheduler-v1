package com.jscheduler.ui.dialog;

import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentPriority;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
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
    private ComboBox<String> priorityCombo;
    @FXML
    private TextField gradeField;
    @FXML
    private TextField maxGradeField;
    @FXML
    private CheckBox recurringCheckBox;
    @FXML
    private ComboBox<String> recurrenceCombo;
    @FXML
    private TextField attachmentField;
    @FXML
    private Button browseButton;
    @FXML
    private Button clearAttachmentButton;
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

        priorityCombo.setItems(FXCollections.observableArrayList(
                "Low",
                "Medium",
                "High",
                "Urgent"
        ));
        priorityCombo.getSelectionModel().select("Medium");

        recurrenceCombo.setItems(FXCollections.observableArrayList(
                "Daily",
                "Weekly",
                "Biweekly",
                "Monthly"
        ));

        maxGradeField.setText("100.0");

        // Enable/disable recurrence pattern based on checkbox
        recurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            recurrenceCombo.setDisable(!newVal);
            if (!newVal) {
                recurrenceCombo.getSelectionModel().clearSelection();
            }
        });

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.disableProperty().bind(
                titleField.textProperty().isEmpty()
                .or(courseCombo.valueProperty().isNull())
                .or(dueDatePicker.valueProperty().isNull())
            );
        }
    }

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Attachment");
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        if (file != null) {
            attachmentField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleClearAttachment() {
        attachmentField.clear();
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

            if (assignment.getPriority() != null) {
                priorityCombo.setValue(assignment.getPriority().getDisplayName());
            }

            if (assignment.getGrade() != null) {
                gradeField.setText(String.valueOf(assignment.getGrade()));
            }

            if (assignment.getMaxGrade() != null) {
                maxGradeField.setText(String.valueOf(assignment.getMaxGrade()));
            }

            recurringCheckBox.setSelected(assignment.isRecurring());
            if (assignment.isRecurring() && assignment.getRecurrencePattern() != null) {
                recurrenceCombo.setValue(assignment.getRecurrencePattern());
            }

            if (assignment.getAttachmentPath() != null) {
                attachmentField.setText(assignment.getAttachmentPath());
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
            AssignmentPriority priority = AssignmentPriority.fromString(priorityCombo.getValue());

            Double grade = null;
            if (gradeField.getText() != null && !gradeField.getText().trim().isEmpty()) {
                try {
                    grade = Double.parseDouble(gradeField.getText().trim());
                } catch (NumberFormatException e) {
                    // Invalid grade, leave as null
                }
            }

            Double maxGrade = 100.0;
            if (maxGradeField.getText() != null && !maxGradeField.getText().trim().isEmpty()) {
                try {
                    maxGrade = Double.parseDouble(maxGradeField.getText().trim());
                } catch (NumberFormatException e) {
                    maxGrade = 100.0;
                }
            }

            boolean isRecurring = recurringCheckBox.isSelected();
            String recurrencePattern = isRecurring ? recurrenceCombo.getValue() : null;
            String attachmentPath = attachmentField.getText() != null && !attachmentField.getText().trim().isEmpty()
                    ? attachmentField.getText().trim()
                    : null;

            if (editMode) {
                result = new Assignment(
                    assignmentId,
                    courseId,
                    title,
                    description,
                    dueDatePicker.getValue(),
                    deadlineDatePicker.getValue(),
                    status,
                    notes,
                    grade,
                    maxGrade,
                    priority,
                    isRecurring,
                    recurrencePattern,
                    attachmentPath
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
                result.setGrade(grade);
                result.setMaxGrade(maxGrade);
                result.setPriority(priority);
                result.setRecurring(isRecurring);
                result.setRecurrencePattern(recurrencePattern);
                result.setAttachmentPath(attachmentPath);
            }
        }
        return result;
    }
}

