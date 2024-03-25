package org.example.dao;

import org.example.model.Assessment;
import org.example.model.Course;
import org.example.model.enums.AssessmentType;
import org.example.util.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;

public class AssessmentDAOImpl implements AssessmentDAO {
    private final java.sql.Connection connection;

    public AssessmentDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Assessment createAssessment(Assessment assessment) {
        try {
            String sql = "INSERT INTO assessments (course_id, name, type) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, assessment.getCourse().getId());
            ps.setString(2, assessment.getName());
            ps.setString(3, assessment.getType().toString());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                assessment.setId(rs.getInt(1));
                return getAssessmentById(assessment.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAssessment(int id) {
        try {
            String sql = "DELETE FROM assessments WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) {
        try {
            String sql = "UPDATE assessments SET course_id = ?, name = ?, type = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, assessment.getCourse().getId());
            ps.setString(2, assessment.getName());
            ps.setString(3, assessment.getType().toString());
            ps.setInt(4, assessment.getId());
            ps.executeUpdate();

            return assessment;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Assessment getAssessmentById(int id) {
        try {
            String sql = "SELECT assessments.id, assessments.name as assessment_name, assessments.type, courses.id as course_id, courses.name as course_name FROM assessments inner join courses on assessments.course_id = courses.id WHERE assessments.id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Assessment(rs.getInt("id"), new Course(rs.getInt("course_id"), rs.getString("course_name")), rs.getString("assessment_name"), AssessmentType.valueOf(rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        try {
            String sql = "SELECT assessments.id, assessments.name as assessment_name, assessments.type, courses.id as course_id, courses.name as course_name FROM assessments inner join courses on assessments.course_id = courses.id";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assessments.add(new Assessment(rs.getInt("id"), new Course(rs.getInt("course_id"), rs.getString("course_name")), rs.getString("assessment_name"), AssessmentType.valueOf(rs.getString("type"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assessments;
    }

    @Override
    public List<Assessment> getAllAssessmentsByCourse(int courseId) {
        List<Assessment> assessments = new ArrayList<>();
        try {
            String sql = "SELECT assessments.id, assessments.name as assessment_name, assessments.type, courses.id as course_id, courses.name as course_name FROM assessments inner join courses on assessments.course_id = courses.id WHERE courses.id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                assessments.add(new Assessment(rs.getInt("id"), new Course(rs.getInt("course_id"), rs.getString("course_name")), rs.getString("assessment_name"), AssessmentType.valueOf(rs.getString("type"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assessments;
    }
}
