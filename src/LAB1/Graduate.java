package LAB1;
    public class Graduate extends Student {

        private double courseworkGPA;
        private double researchGPA;

        public Graduate(String studentName, int studentId,
                               String email, String level,
                               double courseworkGPA,
                               double researchGPA) {
            super(studentName, studentId, email, level);
            setCourseworkGPA(courseworkGPA);
            setResearchGPA(researchGPA);
        }

        public double getCourseworkGPA() {
            return courseworkGPA;
        }

        public void setCourseworkGPA(double courseworkGPA) {
            validateGPA(courseworkGPA);
            this.courseworkGPA = courseworkGPA;
        }

        public double getResearchGPA() {
            return researchGPA;
        }

        public void setResearchGPA(double researchGPA) {
            validateGPA(researchGPA);
            this.researchGPA = researchGPA;
        }

        private void validateGPA(double gpa) {
            if (gpa < 0 || gpa > 4.0) {
                throw new IllegalArgumentException(
                        "GPA must be between 0 and 4.0");
            }
        }

        @Override
        public double CalculateGPA() {
            return courseworkGPA * 0.7 + researchGPA * 0.3;
        }

        @Override
        public String toString() {
            return super.toString()
                    + ", Type=Graduate"
                    + ", Coursework GPA=" + courseworkGPA
                    + ", Research GPA=" + researchGPA
                    + ", Calculated GPA="
                    + String.format("%.2f", CalculateGPA());
        }
    }

