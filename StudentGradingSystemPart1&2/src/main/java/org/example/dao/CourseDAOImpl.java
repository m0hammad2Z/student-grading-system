package org.example.dao;

import org.example.model.Course;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class CourseDAOImpl implements CourseDAO {
    private final Connection connection;

    public CourseDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Course createCourse(Course course) {
        String sql = "INSERT INTO courses (name) VALUES (?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, course.getName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                course.setId(resultSet.getInt(1));
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course getCourseById(int id) {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> getAllInstructorCourses(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.id, c.name FROM courses c JOIN course_instructors ci ON c.id = ci.course_id WHERE ci.instructor_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> getAllUnassignedCourses(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.id, c.name FROM courses c WHERE c.id NOT IN (SELECT ci.course_id FROM course_instructors ci WHERE ci.instructor_id = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }


    @Override
    public List<Course> getAllStudentCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.id, c.name FROM courses c JOIN enrollments e ON c.id = e.course_id WHERE e.student_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Map<Course, Double> getCoursesWithAverage(List<Integer> courseIds) {
        StringBuilder sql = new StringBuilder("SELECT c.id as course_id, c.name as course_name, AVG(grades.grade) as average FROM courses c" +
                " LEFT JOIN assessments a ON c.id = a.course_id" +
                " LEFT JOIN grades ON a.id = grades.assessment_id" +
                " WHERE c.id IN (");
        for (int i = 0; i < courseIds.size(); i++) {
            sql.append("?");
            if (i < courseIds.size() - 1) {
                sql.append(", ");
            } else {
                sql.append(")");
            }
        }
        sql.append(" GROUP BY c.id");

        Map<Course, Double> coursesWithAverage = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < courseIds.size(); i++) {
                preparedStatement.setInt(i + 1, courseIds.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("course_id"));
                course.setName(resultSet.getString("course_name"));
                coursesWithAverage.put(course, resultSet.getDouble("average"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursesWithAverage;
    }

    @Override
    public Course updateCourse(Course course) {
        String sql = "UPDATE courses SET name = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, course.getName());
            preparedStatement.setInt(2, course.getId());
            preparedStatement.executeUpdate();

            return course;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean assignInstructorToCourse(List<Integer> instructorIds, int courseId) {
        String sql = "INSERT INTO course_instructors (course_id, instructor_id) VALUES";

        for (int i = 0; i < instructorIds.size(); i++) {
            sql += " (?, ?)";
            if (i < instructorIds.size() - 1) {
                sql += ",";
            }
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < instructorIds.size(); i++) {
                preparedStatement.setInt(i * 2 + 1, courseId);
                preparedStatement.setInt(i * 2 + 2, instructorIds.get(i));
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeInstructorFromCourse(List<Integer> instructorIds, int courseId) {
        String sql = "DELETE FROM course_instructors WHERE course_id = ? AND instructor_id IN (";
        for (int i = 0; i < instructorIds.size(); i++) {
            sql += "?";
            if (i < instructorIds.size() - 1) {
                sql += ", ";
            } else {
                sql += ")";
            }
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            for (int i = 0; i < instructorIds.size(); i++) {
                preparedStatement.setInt(i + 2, instructorIds.get(i));
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean assignStudentsToCourse(List<Integer> studentIds, int courseId, int instructorId) {
        String sql = "INSERT INTO enrollments (course_id, student_id, instructor_id) VALUES";

        for (int i = 0; i < studentIds.size(); i++) {
            sql += " (?, ?, ?)";
            if (i < studentIds.size() - 1) {
                sql += ",";
            }
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < studentIds.size(); i++) {
                preparedStatement.setInt(i * 3 + 1, courseId);
                preparedStatement.setInt(i * 3 + 2, studentIds.get(i));
                preparedStatement.setInt(i * 3 + 3, instructorId);
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeStudentsFromCourse(List<Integer> studentIds, int courseId) {
        String sql = "DELETE FROM enrollments WHERE course_id = ? AND student_id IN (";
        for (int i = 0; i < studentIds.size(); i++) {
            sql += "?";
            if (i < studentIds.size() - 1) {
                sql += ", ";
            } else {
                sql += ")";
            }
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            for (int i = 0; i < studentIds.size(); i++) {
                preparedStatement.setInt(i + 2, studentIds.get(i));
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStudentEnrolled(int studentId, int courseId, int instructorId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ? AND instructor_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.setInt(3, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStudentEnrolled(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCourseAssigned(int courseId, int instructorId) {
        String sql = "SELECT * FROM course_instructors WHERE course_id = ? AND instructor_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

