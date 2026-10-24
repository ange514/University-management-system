
import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);

    // Create tables
    public static void createTables() throws SQLException {

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        Statement statement = connection.createStatement();

        statement.execute("""
            CREATE TABLE instructors (
                instructor_id SERIAL PRIMARY KEY,
                instructor_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL
            )
        """);

        statement.execute("""
            CREATE TABLE students (
                student_id SERIAL PRIMARY KEY,
                student_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                level VARCHAR(50),
                student_type VARCHAR(20) NOT NULL
                    CHECK (student_type IN ('UNDERGRADUATE', 'GRADUATE')),
                gpa DOUBLE PRECISION,
                coursework_gpa DOUBLE PRECISION,
                research_gpa DOUBLE PRECISION
            )
        """);

        statement.execute("""
            CREATE TABLE courses (
                course_id SERIAL PRIMARY KEY,
                course_name VARCHAR(100) NOT NULL,
                credits INTEGER NOT NULL CHECK (credits > 0),
                instructor_id INTEGER REFERENCES instructors(instructor_id)
            )
        """);

        statement.execute("""
            CREATE TABLE enrollments (
                student_id INTEGER REFERENCES students(student_id)
                    ON DELETE CASCADE,
                course_id INTEGER REFERENCES courses(course_id)
                    ON DELETE CASCADE,
                enrollment_date DATE DEFAULT CURRENT_DATE,
                PRIMARY KEY (student_id, course_id)
            )
        """);

        statement.close();
        connection.close();

        System.out.println("Tables created or already exist.");
    }

    // Add student
    public static void addStudent() throws SQLException {

        System.out.print("Student name: ");
        String name = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Level: ");
        String level = input.nextLine();

        System.out.print("Student type (UNDERGRADUATE/GRADUATE): ");
        String type = input.nextLine().trim().toUpperCase();

        if (!type.equals("UNDERGRADUATE") && !type.equals("GRADUATE")) {
            System.out.println("Invalid student type.");
            return;
        }

        Double gpa = null;
        Double courseworkGpa = null;
        Double researchGpa = null;

        if (type.equals("UNDERGRADUATE")) {

            System.out.print("GPA (0-4): ");
            gpa = Double.parseDouble(input.nextLine());

            if (gpa < 0 || gpa > 4) {
                System.out.println("GPA must be between 0 and 4.");
                return;
            }

        } else {

            System.out.print("Coursework GPA (0-4): ");
            courseworkGpa = Double.parseDouble(input.nextLine());

            System.out.print("Research GPA (0-4): ");
            researchGpa = Double.parseDouble(input.nextLine());

            if (courseworkGpa < 0 || courseworkGpa > 4
                    || researchGpa < 0 || researchGpa > 4) {
                System.out.println("Both GPAs must be between 0 and 4.");
                return;
            }
        }

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = """
            INSERT INTO students
            (student_name, email, level, student_type, gpa,
             coursework_gpa, research_gpa)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, email);
        statement.setString(3, level);
        statement.setString(4, type);

        if (gpa == null) {
            statement.setNull(5, Types.DOUBLE);
        } else {
            statement.setDouble(5, gpa);
        }

        if (courseworkGpa == null) {
            statement.setNull(6, Types.DOUBLE);
        } else {
            statement.setDouble(6, courseworkGpa);
        }

        if (researchGpa == null) {
            statement.setNull(7, Types.DOUBLE);
        } else {
            statement.setDouble(7, researchGpa);
        }

        statement.executeUpdate();

        System.out.println("Student added successfully.");

        statement.close();
        connection.close();
    }

    // View students
    public static void viewStudents() throws SQLException {

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        Statement statement = connection.createStatement();

        ResultSet result = statement.executeQuery(
                "SELECT * FROM students ORDER BY student_id"
        );

        while (result.next()) {
            System.out.println(
                    "ID: " + result.getInt("student_id")
                            + " | Name: " + result.getString("student_name")
                            + " | Email: " + result.getString("email")
                            + " | Level: " + result.getString("level")
                            + " | Type: " + result.getString("student_type")
                            + " | GPA: " + result.getObject("gpa")
                            + " | Coursework GPA: " + result.getObject("coursework_gpa")
                            + " | Research GPA: " + result.getObject("research_gpa")
            );
        }

        result.close();
        statement.close();
        connection.close();
    }

    // Update student email
    public static void updateStudentEmail() throws SQLException {

        System.out.print("Student ID: ");
        int id = Integer.parseInt(input.nextLine());

        System.out.print("New email: ");
        String email = input.nextLine();

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = "UPDATE students SET email = ? WHERE student_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, email);
        statement.setInt(2, id);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Student email updated successfully.");
        } else {
            System.out.println("Student not found.");
        }

        statement.close();
        connection.close();
    }

    // Delete student
    public static void deleteStudent() throws SQLException {

        System.out.print("Student ID: ");
        int id = Integer.parseInt(input.nextLine());

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = "DELETE FROM students WHERE student_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }

        statement.close();
        connection.close();
    }

    // Add instructor
    public static void addInstructor() throws SQLException {

        System.out.print("Instructor name: ");
        String name = input.nextLine();

        System.out.print("Instructor email: ");
        String email = input.nextLine();

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = """
            INSERT INTO instructors (instructor_name, email)
            VALUES (?, ?)
        """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, email);

        statement.executeUpdate();

        System.out.println("Instructor added successfully.");

        statement.close();
        connection.close();
    }

    // Add course
    public static void addCourse() throws SQLException {

        System.out.print("Course name: ");
        String name = input.nextLine();

        System.out.print("Course credits: ");
        int credits = Integer.parseInt(input.nextLine());

        if (credits <= 0) {
            System.out.println("Credits must be greater than zero.");
            return;
        }

        System.out.print("Instructor ID: ");
        int instructorId = Integer.parseInt(input.nextLine());

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = """
            INSERT INTO courses (course_name, credits, instructor_id)
            VALUES (?, ?, ?)
        """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setInt(2, credits);
        statement.setInt(3, instructorId);

        statement.executeUpdate();

        System.out.println("Course added successfully.");

        statement.close();
        connection.close();
    }

    // Enroll student
    public static void enrollStudent() throws SQLException {

        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(input.nextLine());

        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(input.nextLine());

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = """
            INSERT INTO enrollments (student_id, course_id)
            VALUES (?, ?)
        """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, studentId);
        statement.setInt(2, courseId);

        statement.executeUpdate();

        System.out.println("Student enrolled successfully.");

        statement.close();
        connection.close();
    }

    // View enrollments using JOIN
    public static void viewEnrollments() throws SQLException {

        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/university",
                "postgres",
                "admin"
        );

        String sql = """
            SELECT s.student_id, s.student_name,
                   c.course_id, c.course_name, e.enrollment_date
            FROM enrollments e
            JOIN students s ON e.student_id = s.student_id
            JOIN courses c ON e.course_id = c.course_id
            ORDER BY s.student_id
        """;

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            System.out.println(
                    "Student ID: " + result.getInt("student_id")
                            + " | Student: " + result.getString("student_name")
                            + " | Course ID: " + result.getInt("course_id")
                            + " | Course: " + result.getString("course_name")
                            + " | Date: " + result.getDate("enrollment_date")
            );
        }

        result.close();
        statement.close();
        connection.close();
    }

    // Main menu
    public static void main(String[] args) {

        try {
            createTables();

            while (true) {
                System.out.println("\n===== UNIVERSITY MANAGEMENT =====");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student Email");
                System.out.println("4. Delete Student");
                System.out.println("5. Add Instructor");
                System.out.println("6. Add Course");
                System.out.println("7. Enroll Student");
                System.out.println("8. View Enrollments");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                String choice = input.nextLine();

                try {
                    switch (choice) {
                        case "1" -> addStudent();
                        case "2" -> viewStudents();
                        case "3" -> updateStudentEmail();
                        case "4" -> deleteStudent();
                        case "5" -> addInstructor();
                        case "6" -> addCourse();
                        case "7" -> enrollStudent();
                        case "8" -> viewEnrollments();
                        case "0" -> {
                            System.out.println("Goodbye!");
                            input.close();
                            return;
                        }
                        default -> System.out.println("Invalid option.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection or table error: " + e.getMessage());
        }
    }
}
