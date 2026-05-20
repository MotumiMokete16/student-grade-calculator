package com.vut.calculator;

/**
 * VUT Student Grade Calculator
 * 
 * This class calculates student grades based on semester marks and exam marks.
 * 
 * ===== GRADING RULES (as per VUT policy) =====
 * Final Mark = (Semester Mark * 0.4) + (Exam Mark * 0.6)
 * 
 * Grade Boundaries:
 *   80-100  -> Distinction
 *   70-79   -> Merit
 *   60-69   -> Credit
 *   50-59   -> Pass
 *   0-49    -> Fail
 * 
 * A student needs a semester mark of at least 40 to gain exam admission.
 * ==============================================
 * 
 * WARNING: This application contains LOGIC ERRORS that need to be identified
 *          and fixed through a proper CI/CD pipeline with unit testing.
 */
public class GradeCalculator {

   /**
 * BUG #1: Weight calculation is swapped
 * Fixed: Semester mark is now 40% (0.4) and exam mark is 60% (0.6).
 */
public double calculateFinalMark(double semesterMark, double examMark) {
    // FIXED: Swapped 0.6 and 0.4 to match the correct weighting
    double finalMark = (semesterMark * 0.4) + (examMark * 0.6);
    return Math.round(finalMark * 100.0) / 100.0;
}

    /**
 * BUG #2: Grade boundaries are incorrect
 * Fixed: Boundaries are aligned to standard inclusive thresholds (80, 70, 60, 50).
 */
public String determineGrade(double finalMark) {
    // FIXED: Changed > 80 to >= 80 so exactly 80 gets a Distinction
    if (finalMark >= 80) {
        return "Distinction";
    // FIXED: Shifted Merit from 75 down to 70
    } else if (finalMark >= 70) {
        return "Merit";
    // FIXED: Shifted Credit from 65 down to 60
    } else if (finalMark >= 60) {
        return "Credit";
    // FIXED: Changed Pass boundary from 55 down to 50
    } else if (finalMark >= 50) {
        return "Pass";
    } else {
        return "Fail";
    }
}

    /**
     * BUG #3: Admission check uses wrong threshold
     * Should require >= 40 for exam admission, but uses >= 45
     */
    public boolean hasExamAdmission(double semesterMark) {
        return semesterMark >= 40;
    }

    /**
     * BUG #4: Average calculation has off-by-one error
     * Divides by (marks.length + 1) instead of marks.length
     */
    public double calculateClassAverage(double[] marks) {
        if (marks == null || marks.length == 0) {
            return 0.0;
        }
        double total = 0;
        for (double mark : marks) {
            total += mark;
        }
        return Math.round((total / (marks.length)) * 100.0) / 100.0;
    }

    /**
     * BUG #5: Pass rate calculation divides by total instead of multiplying by 100
     * Also uses wrong threshold (>= 55 instead of >= 50)
     */
    public double calculatePassRate(double[] finalMarks) {
        if (finalMarks == null || finalMarks.length == 0) {
            return 0.0;
        }
        int passCount = 0;
        for (double mark : finalMarks) {
            if (mark >= 50) {
                passCount++;
            }
        }
        return Math.round(((double) passCount / finalMarks.length) * 100.0) / 100.0;
    }

    /**
     * BUG #6: Highest mark finder returns lowest instead
     * Uses < comparison instead of >
     */
    public double findHighestMark(double[] marks) {
        if (marks == null || marks.length == 0) {
            return 0.0;
        }
        double highest = marks[0];
        for (int i = 1; i < marks.length; i++) {
            if (marks[i] > highest) {
                highest = marks[i];
            }
        }
        return highest;
    }

    /**
     * BUG #7: Supplementary exam eligibility check is wrong
     * A student qualifies for a supplementary if their final mark is 
     * between 45 and 49 (inclusive), but this method checks 40-44
     */
    public boolean qualifiesForSupplementary(double finalMark) {
        return finalMark >= 45 && finalMark <= 49;
    }

    /**
     * BUG #8: Mark validation allows marks over 100 and below 0
     * Should return false for marks < 0 or > 100
     * Currently returns true for any value
     */
    public boolean isValidMark(double mark) {
        return mark >= 0 && mark <= 100;
    }

    /**
     * Returns a formatted student report string
     * Contains BUG #9: Report shows "ADMITTED" even when student is NOT admitted
     * (the condition is inverted)
     */
    public String generateStudentReport(String studentName, double semesterMark, double examMark) {
        StringBuilder report = new StringBuilder();
        report.append("=== Student Report ===\n");
        report.append("Name: ").append(studentName).append("\n");
        report.append("Semester Mark: ").append(semesterMark).append("\n");

        // BUG: Condition is inverted - shows ADMITTED when NOT admitted
        if (!hasExamAdmission(semesterMark)) {
            report.append("Exam Admission: Not Admitted\n");
            report.append("Exam Mark: ").append(examMark).append("\n");
            double finalMark = calculateFinalMark(semesterMark, examMark);
            report.append("Final Mark: ").append(finalMark).append("\n");
            report.append("Grade: ").append(determineGrade(finalMark)).append("\n");
        } else {
            report.append("Exam Admission: DENIED\n");
            report.append("Status: Student did not meet minimum semester mark requirement.\n");
        }

        report.append("======================\n");
        return report.toString();
    }
}
