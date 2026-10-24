package LAB1;

public abstract class Student {
    private String studentName;
    private int studentId;
    private String email;
    private String level;

    public Student(String studentName, int studentId, String email, String level) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.email = email;
        this.level = level;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract double CalculateGPA();

    @Override
    public String toString() {
        return "Student{" +
                "studentName='" + studentName + '\'' +
                ", studentId=" + studentId +
                ", email='" + email + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
