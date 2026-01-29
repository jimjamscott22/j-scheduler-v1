package com.jscheduler.data;

import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.UUID;

public class CourseRepository {
    private static CourseRepository instance;
    private final ObservableList<Course> courses;
    private final DatabaseConnection dbConnection;

    private CourseRepository() {
        courses = FXCollections.observableArrayList();
        dbConnection = DatabaseConnection.getInstance();
        loadCoursesFromDatabase();
    }

    public static CourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    /**
     * Load all courses from the database into the observable list.
     */
    private void loadCoursesFromDatabase() {
        String sql = "SELECT id, name, description, professor, semester FROM courses ORDER BY semester, name";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            courses.clear();
            while (rs.next()) {
                Course course = new Course(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("professor"),
                        rs.getString("semester")
                );
                courses.add(course);
            }
            System.out.println("Loaded " + courses.size() + " courses from database");
        } catch (SQLException e) {
            System.err.println("Error loading courses from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a new course to both the database and observable list.
     * @param course Course to add
     * @return true if successful, false otherwise
     */
    public boolean addCourse(Course course) {
        // Generate ID if not set
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO courses (id, name, description, professor, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getId());
            pstmt.setString(2, course.getName());
            pstmt.setString(3, course.getDescription());
            pstmt.setString(4, course.getProfessor());
            pstmt.setString(5, course.getSemester());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                courses.add(course);
                System.out.println("Course added: " + course.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Remove a course from both the database and observable list.
     * @param course Course to remove
     * @return true if successful, false otherwise
     */
    public boolean removeCourse(Course course) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                courses.remove(course);
                System.out.println("Course removed: " + course.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error removing course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update an existing course in both the database and observable list.
     * @param oldCourse The course to update
     * @param newCourse The updated course data
     * @return true if successful, false otherwise
     */
    public boolean updateCourse(Course oldCourse, Course newCourse) {
        String sql = "UPDATE courses SET name = ?, description = ?, professor = ?, semester = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newCourse.getName());
            pstmt.setString(2, newCourse.getDescription());
            pstmt.setString(3, newCourse.getProfessor());
            pstmt.setString(4, newCourse.getSemester());
            pstmt.setString(5, oldCourse.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                int index = courses.indexOf(oldCourse);
                if (index >= 0) {
                    // Update the old course object's properties instead of replacing
                    oldCourse.setName(newCourse.getName());
                    oldCourse.setDescription(newCourse.getDescription());
                    oldCourse.setProfessor(newCourse.getProfessor());
                    oldCourse.setSemester(newCourse.getSemester());
                }
                System.out.println("Course updated: " + newCourse.getName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reload all courses from the database.
     */
    public void refresh() {
        loadCoursesFromDatabase();
    }
}
