package com.jscheduler.data;

import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CourseRepository {
    private static CourseRepository instance;
    private final ObservableList<Course> courses;
    private final DatabaseConnection dbConnection;

    private CourseRepository() {
        courses = FXCollections.observableArrayList();
        dbConnection = DatabaseConnection.getInstance();
        ensureCourseSchema();
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
        String sql = "SELECT id, name, description, professor, semester, credit_hours, current_grade, letter_grade " +
                     "FROM courses ORDER BY semester, name";
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
                        rs.getString("semester"),
                        rs.getObject("credit_hours") != null ? rs.getDouble("credit_hours") : 3.0,
                        rs.getObject("current_grade") != null ? rs.getDouble("current_grade") : null,
                        rs.getString("letter_grade")
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
     * Ensure required columns exist for courses table.
     */
    private void ensureCourseSchema() {
        String checkSql = "SELECT 1 FROM information_schema.columns " +
                          "WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?";
        String addCreditHours = "ALTER TABLE courses ADD COLUMN credit_hours DECIMAL(3,1) DEFAULT 3.0";
        String addCurrentGrade = "ALTER TABLE courses ADD COLUMN current_grade DECIMAL(5,2)";
        String addLetterGrade = "ALTER TABLE courses ADD COLUMN letter_grade VARCHAR(3)";

        try (Connection conn = dbConnection.getConnection()) {
            if (!columnExists(conn, checkSql, "courses", "credit_hours")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(addCreditHours);
                }
            }
            if (!columnExists(conn, checkSql, "courses", "current_grade")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(addCurrentGrade);
                }
            }
            if (!columnExists(conn, checkSql, "courses", "letter_grade")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(addLetterGrade);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring courses schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean columnExists(Connection conn, String checkSql, String tableName, String columnName)
            throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, tableName);
            pstmt.setString(2, columnName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Add a new course to both the database and observable list.
     * @param course Course to add
     * @return true if successful, false otherwise
     */
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses (id, name, description, professor, semester, credit_hours, current_grade, letter_grade) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getId());
            pstmt.setString(2, course.getName());
            pstmt.setString(3, course.getDescription());
            pstmt.setString(4, course.getProfessor());
            pstmt.setString(5, course.getSemester());
            pstmt.setDouble(6, course.getCreditHours() != null ? course.getCreditHours() : 3.0);

            if (course.getCurrentGrade() != null) {
                pstmt.setDouble(7, course.getCurrentGrade());
            } else {
                pstmt.setNull(7, java.sql.Types.DECIMAL);
            }
            pstmt.setString(8, course.getLetterGrade());

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
        String sql = "UPDATE courses SET name = ?, description = ?, professor = ?, semester = ?, " +
                     "credit_hours = ?, current_grade = ?, letter_grade = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newCourse.getName());
            pstmt.setString(2, newCourse.getDescription());
            pstmt.setString(3, newCourse.getProfessor());
            pstmt.setString(4, newCourse.getSemester());
            pstmt.setDouble(5, newCourse.getCreditHours() != null ? newCourse.getCreditHours() : 3.0);

            if (newCourse.getCurrentGrade() != null) {
                pstmt.setDouble(6, newCourse.getCurrentGrade());
            } else {
                pstmt.setNull(6, java.sql.Types.DECIMAL);
            }
            pstmt.setString(7, newCourse.getLetterGrade());
            pstmt.setString(8, oldCourse.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                int index = courses.indexOf(oldCourse);
                if (index >= 0) {
                    // Update the old course object's properties instead of replacing
                    oldCourse.setName(newCourse.getName());
                    oldCourse.setDescription(newCourse.getDescription());
                    oldCourse.setProfessor(newCourse.getProfessor());
                    oldCourse.setSemester(newCourse.getSemester());
                    oldCourse.setCreditHours(newCourse.getCreditHours());
                    oldCourse.setCurrentGrade(newCourse.getCurrentGrade());
                    oldCourse.setLetterGrade(newCourse.getLetterGrade());
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
