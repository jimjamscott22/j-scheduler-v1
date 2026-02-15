package com.jscheduler.service;

import com.jscheduler.model.Course;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class GPACalculator {

    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();

    static {
        GRADE_POINTS.put("A+", 4.0);
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("A-", 3.7);
        GRADE_POINTS.put("B+", 3.3);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("B-", 2.7);
        GRADE_POINTS.put("C+", 2.3);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("C-", 1.7);
        GRADE_POINTS.put("D+", 1.3);
        GRADE_POINTS.put("D", 1.0);
        GRADE_POINTS.put("D-", 0.7);
        GRADE_POINTS.put("F", 0.0);
    }

    /**
     * Calculate GPA from a list of courses
     */
    public static double calculateGPA(ObservableList<Course> courses) {
        double totalPoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : courses) {
            String letterGrade = course.getLetterGrade();
            Double creditHours = course.getCreditHours();

            if (letterGrade != null && !letterGrade.trim().isEmpty() && creditHours != null) {
                Double gradePoint = GRADE_POINTS.get(letterGrade.toUpperCase().trim());
                if (gradePoint != null) {
                    totalPoints += gradePoint * creditHours;
                    totalCredits += creditHours;
                }
            }
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    /**
     * Convert numeric grade to letter grade
     */
    public static String getLetterGrade(double numericGrade) {
        if (numericGrade >= 97) return "A+";
        if (numericGrade >= 93) return "A";
        if (numericGrade >= 90) return "A-";
        if (numericGrade >= 87) return "B+";
        if (numericGrade >= 83) return "B";
        if (numericGrade >= 80) return "B-";
        if (numericGrade >= 77) return "C+";
        if (numericGrade >= 73) return "C";
        if (numericGrade >= 70) return "C-";
        if (numericGrade >= 67) return "D+";
        if (numericGrade >= 63) return "D";
        if (numericGrade >= 60) return "D-";
        return "F";
    }

    /**
     * Get grade point for a letter grade
     */
    public static Double getGradePoint(String letterGrade) {
        if (letterGrade == null) return null;
        return GRADE_POINTS.get(letterGrade.toUpperCase().trim());
    }

    /**
     * Calculate semester GPA for courses in a specific semester
     */
    public static double calculateSemesterGPA(ObservableList<Course> courses, String semester) {
        double totalPoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : courses) {
            if (semester.equals(course.getSemester())) {
                String letterGrade = course.getLetterGrade();
                Double creditHours = course.getCreditHours();

                if (letterGrade != null && !letterGrade.trim().isEmpty() && creditHours != null) {
                    Double gradePoint = GRADE_POINTS.get(letterGrade.toUpperCase().trim());
                    if (gradePoint != null) {
                        totalPoints += gradePoint * creditHours;
                        totalCredits += creditHours;
                    }
                }
            }
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
}

