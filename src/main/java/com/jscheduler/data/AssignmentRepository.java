package com.jscheduler.data;

import com.jscheduler.model.Assignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.UUID;

public class AssignmentRepository {
    private static AssignmentRepository instance;
    private final ObservableList<Assignment> assignments;
    private final DatabaseConnection dbConnection;

    private AssignmentRepository() {
        assignments = FXCollections.observableArrayList();
        dbConnection = DatabaseConnection.getInstance();
        loadAssignmentsFromDatabase();
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

    /**
     * Load all assignments from the database into the observable list.
     */
    private void loadAssignmentsFromDatabase() {
        String sql = "SELECT id, course_id, title, description, due_date, deadline, status, notes " +
                     "FROM assignments ORDER BY due_date ASC";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            assignments.clear();
            while (rs.next()) {
                Assignment assignment = new Assignment(
                        rs.getString("id"),
                        rs.getString("course_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                        rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("notes")
                );
                assignments.add(assignment);
            }
            System.out.println("Loaded " + assignments.size() + " assignments from database");
        } catch (SQLException e) {
            System.err.println("Error loading assignments from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a new assignment to both the database and observable list.
     * @param assignment Assignment to add
     * @return true if successful, false otherwise
     */
    public boolean addAssignment(Assignment assignment) {
        // Generate ID if not set
        if (assignment.getId() == null || assignment.getId().isEmpty()) {
            assignment.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO assignments (id, course_id, title, description, due_date, deadline, status, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, assignment.getId());
            pstmt.setString(2, assignment.getCourseId());
            pstmt.setString(3, assignment.getTitle());
            pstmt.setString(4, assignment.getDescription());
            pstmt.setDate(5, assignment.getDueDate() != null ? Date.valueOf(assignment.getDueDate()) : null);
            pstmt.setDate(6, assignment.getDeadline() != null ? Date.valueOf(assignment.getDeadline()) : null);
            pstmt.setString(7, assignment.getStatus());
            pstmt.setString(8, assignment.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                assignments.add(assignment);
                System.out.println("Assignment added: " + assignment.getTitle());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding assignment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Remove an assignment from both the database and observable list.
     * @param assignment Assignment to remove
     * @return true if successful, false otherwise
     */
    public boolean removeAssignment(Assignment assignment) {
        String sql = "DELETE FROM assignments WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, assignment.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                assignments.remove(assignment);
                System.out.println("Assignment removed: " + assignment.getTitle());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error removing assignment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update an existing assignment in both the database and observable list.
     * @param assignment The assignment to update
     * @return true if successful, false otherwise
     */
    public boolean updateAssignment(Assignment assignment) {
        String sql = "UPDATE assignments SET course_id = ?, title = ?, description = ?, " +
                     "due_date = ?, deadline = ?, status = ?, notes = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, assignment.getCourseId());
            pstmt.setString(2, assignment.getTitle());
            pstmt.setString(3, assignment.getDescription());
            pstmt.setDate(4, assignment.getDueDate() != null ? Date.valueOf(assignment.getDueDate()) : null);
            pstmt.setDate(5, assignment.getDeadline() != null ? Date.valueOf(assignment.getDeadline()) : null);
            pstmt.setString(6, assignment.getStatus());
            pstmt.setString(7, assignment.getNotes());
            pstmt.setString(8, assignment.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Assignment updated: " + assignment.getTitle());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating assignment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get assignments for a specific course.
     * @param courseId The course ID
     * @return ObservableList of assignments for the course
     */
    public ObservableList<Assignment> getAssignmentsByCourse(String courseId) {
        ObservableList<Assignment> courseAssignments = FXCollections.observableArrayList();
        for (Assignment assignment : assignments) {
            if (assignment.getCourseId().equals(courseId)) {
                courseAssignments.add(assignment);
            }
        }
        return courseAssignments;
    }

    /**
     * Reload all assignments from the database.
     */
    public void refresh() {
        loadAssignmentsFromDatabase();
    }
}
