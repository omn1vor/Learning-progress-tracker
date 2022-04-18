package tracker;

public class Task {
    private final Student student;
    private final int grade;

    public Task(Student student, int grade) {
        this.student = student;
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return "Task{" +
                "student=" + student +
                ", grade=" + grade +
                '}';
    }
}
