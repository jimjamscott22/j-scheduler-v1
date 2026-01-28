package com.jscheduler.data;

import com.jscheduler.model.Assignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssignmentRepository {
    private static AssignmentRepository instance;
    private final ObservableList<Assignment> assignments;

    private AssignmentRepository() {
        assignments = FXCollections.observableArrayList();
    }

    public static AssignmentRepository getInstance() {
        if (instance == null) {
            instance = new AssignmentRepository();
        }
        return instance;
    }

    public ObservableList<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }
}
