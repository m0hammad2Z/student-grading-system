package org.example.dao;

import org.example.model.Course;
import org.example.model.Grade;
import org.example.model.User;
import org.example.model.enums.AssessmentType;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.example.model.Assessment;


public class GradeDAOImpl implements GradeDAO {

    private final Connection connection;

    public GradeDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Grade createGrade(Grade grade) {
        try {
            String sql = "INSERT INTO grades (student_id, assessment_id, grade) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, grade.getStudent().getId());
            ps.setInt(2, grade.getAssessment().getId());
            ps.setInt(3, grade.getGrade());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                grade.setId(rs.getInt(1));
                return getGradeById(grade.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteGrade(int id) {
        try {
            String sql = "DELETE FROM grades WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Grade updateGrade(Grade grade) {
        try {
            String sql = "UPDATE grades SET student_id = ?, assessment_id = ?, grade = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, grade.getStudent().getId());
            ps.setInt(2, grade.getAssessment().getId());
            ps.setInt(3, grade.getGrade());
            ps.setInt(4, grade.getId());
            ps.executeUpdate();

            return grade;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Grade getGradeById(int id) {

        try {
            String sql = "SELECT g.id as grade_id, g.grade as grade_value, s.id as student_id, s.first_name, s.last_name, a.id as assessment_id, a.name as assessment_name, a.type as assessment_type, a.course_id as course_id, c.name as course_name FROM grades g JOIN users s ON g.student_id = s.id JOIN assessments a ON g.assessment_id = a.id JOIN courses c ON a.course_id = c.id WHERE g.id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));

                Assessment assessment = new Assessment();
                assessment.setId(rs.getInt("assessment_id"));
                assessment.setName(rs.getString("assessment_name"));
                assessment.setType(AssessmentType.valueOf(rs.getString("assessment_type")));
                assessment.setCourse(new Course(rs.getInt("course_id"), rs.getString("course_name")));

                Grade grade = new Grade();
                grade.setId(rs.getInt("grade_id"));
                grade.setStudent(student);
                grade.setAssessment(assessment);
                grade.setGrade(rs.getInt("grade_value"));
                return grade;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        try {
            String sql = "SELECT g.id as grade_id, g.grade as grade_value, s.id as student_id, s.first_name, s.last_name, a.id as assessment_id, a.name as assessment_name, a.type as assessment_type, a.course_id as course_id, c.name as course_name FROM grades g JOIN users s ON g.student_id = s.id JOIN assessments a ON g.assessment_id = a.id JOIN courses c ON a.course_id = c.id";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));

                Assessment assessment = new Assessment();
                assessment.setId(rs.getInt("assessment_id"));
                assessment.setName(rs.getString("assessment_name"));
                assessment.setType(AssessmentType.valueOf(rs.getString("assessment_type")));
                assessment.setCourse(new Course(rs.getInt("course_id"), rs.getString("course_name")));

                Grade grade = new Grade();
                grade.setId(rs.getInt("grade_id"));
                grade.setStudent(student);
                grade.setAssessment(assessment);
                grade.setGrade(rs.getInt("grade_value"));
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getAllGradesByStudent(int userId) {

        List<Grade> grades = new ArrayList<>();

        try {
            String sql = "SELECT courses.id AS course_id, " +
                    "courses.name AS course_name, " +
                    "users.id AS student_id," +
                    "users.first_name AS student_first_name, " +
                    "users.last_name AS student_last_name, " +
                    "users.email AS student_email, " +
                    "assessments.id AS assessment_id, " +
                    "assessments.name AS assessment_name, " +
                    "assessments.type AS assessment_type, " +
                    "grades.id AS grade_id, " +
                    "grades.grade AS grade_value " +
                    "FROM courses " +
                    "JOIN enrollments ON courses.id = enrollments.course_id " +
                    "JOIN users ON users.id = enrollments.student_id " +
                    "LEFT JOIN assessments ON assessments.course_id = courses.id " +
                    "LEFT JOIN grades ON grades.assessment_id = assessments.id AND grades.student_id = users.id " +
                    "WHERE users.id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("student_first_name"));
                student.setLastName(rs.getString("student_last_name"));
                student.setEmail(rs.getString("student_email"));

                Course course = new Course();
                course.setId(rs.getInt("course_id"));
                course.setName(rs.getString("course_name"));

                Assessment assessment = new Assessment();
                assessment.setId(rs.getInt("assessment_id"));
                assessment.setName(rs.getString("assessment_name"));
                assessment.setType(AssessmentType.valueOf(rs.getString("assessment_type")));
                assessment.setCourse(course);

                Grade grade = new Grade();
                grade.setId(rs.getInt("grade_id"));
                grade.setStudent(student);
                grade.setAssessment(assessment);
                grade.setGrade(rs.getInt("grade_value"));

                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getAssessmentGradesForAllStudents(int assessmentId) {

        List<Grade> grades = new ArrayList<>();

        try {
            String sql = "SELECT courses.id AS course_id, " +
                    "courses.name AS course_name, " +
                    "users.id AS student_id," +
                    "users.first_name AS student_first_name, " +
                    "users.last_name AS student_last_name, " +
                    "users.email AS student_email, " +
                    "assessments.id AS assessment_id, " +
                    "assessments.name AS assessment_name, " +
                    "assessments.type AS assessment_type, " +
                    "grades.id AS grade_id, " +
                    "grades.grade AS grade_value " +
                    "FROM courses " +
                    "JOIN enrollments ON courses.id = enrollments.course_id " +
                    "JOIN users ON users.id = enrollments.student_id " +
                    "LEFT JOIN assessments ON assessments.course_id = courses.id " +
                    "LEFT JOIN grades ON grades.assessment_id = assessments.id AND grades.student_id = users.id " +
                    "WHERE assessments.id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, assessmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("student_first_name"));
                student.setLastName(rs.getString("student_last_name"));
                student.setEmail(rs.getString("student_email"));

                Course course = new Course();
                course.setId(rs.getInt("course_id"));
                course.setName(rs.getString("course_name"));

                Assessment assessment = new Assessment();
                assessment.setId(rs.getInt("assessment_id"));
                assessment.setName(rs.getString("assessment_name"));
                assessment.setType(AssessmentType.valueOf(rs.getString("assessment_type")));
                assessment.setCourse(course);

                Grade grade = new Grade();
                grade.setId(rs.getInt("grade_id"));
                grade.setStudent(student);
                grade.setAssessment(assessment);
                grade.setGrade(rs.getInt("grade_value"));

                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public Grade getGradeByStudentAndAssessment(int studentId, int assessmentId) {
        try {
            String sql = "SELECT g.id as grade_id, g.grade as grade_value, s.id as student_id, s.first_name, s.last_name, a.id as assessment_id, a.name as assessment_name, a.type as assessment_type, a.course_id as course_id, c.name as course_name FROM grades g JOIN users s ON g.student_id = s.id JOIN assessments a ON g.assessment_id = a.id JOIN courses c ON a.course_id = c.id WHERE g.student_id = ? AND g.assessment_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, assessmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));

                Assessment assessment = new Assessment();
                assessment.setId(rs.getInt("assessment_id"));
                assessment.setName(rs.getString("assessment_name"));
                assessment.setType(AssessmentType.valueOf(rs.getString("assessment_type")));
                assessment.setCourse(new Course(rs.getInt("course_id"), rs.getString("course_name")));

                Grade grade = new Grade();
                grade.setId(rs.getInt("grade_id"));
                grade.setStudent(student);
                grade.setAssessment(assessment);
                grade.setGrade(rs.getInt("grade_value"));
                return grade;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
