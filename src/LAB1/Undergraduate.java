
package LAB1;

class UndergraduateStudent extends Student {

    private double gpa;

    public UndergraduateStudent(String studentName, int studentId,
                                String email, String level,
                                double gpa) {
        super(studentName, studentId, email, level);
        setGpa(gpa);
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (gpa < 0 || gpa > 4.0) {
            throw new IllegalArgumentException(
                    "GPA must be between 0 and 4.0");
        }
        this.gpa = gpa;
    }

    @Override
    public double CalculateGPA() {
        return gpa;
    }

    @Override
    public String toString() {
        return super.toString()
                + ", Type=Undergraduate"
                + ", GPA=" + CalculateGPA();
    }
}
