package tracker;

import java.util.*;
import java.util.stream.Collectors;

public class Course {
    private final String name;
    private final int totalPoints;
    private final List<Task> tasks = new ArrayList<>();
    private final Set<Student> students = new HashSet<>();

    public Course(String name, int totalPoints) {
        this.name = name;
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public int getStudentCount() {
        return students.size();
    }

    public int getTasksCount() {
        return tasks.size();
    }

    public double getAverageGrade() {
        return tasks.stream()
                .mapToInt(Task::getGrade)
                .average().orElse(0.0);
    }

    public void addTask(Task task) {
        tasks.add(task);
        students.add(task.getStudent());
    }

    public static String listToString(List<Course> courses) {
        if (courses.isEmpty()) {
            return "n/a";
        }
        return courses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(", "));
    }

    public String getTopLearnersInfo() {
        StringBuilder sb = new StringBuilder(name + System.lineSeparator());
        sb.append("id    points    completed");
        sb.append(System.lineSeparator());
        tasks.stream()
                .collect(Collectors.groupingBy(Task::getStudent,
                        LinkedHashMap::new,
                        Collectors.summingInt(Task::getGrade)))
                .entrySet().stream()
                .map(entry -> new Task(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(Task::getGrade).reversed()
                        .thenComparingInt(task -> task.getStudent().getId()))
                .forEach(task -> {
                    sb.append(String.format("%-5d %-9d %.1f%%%n",
                            task.getStudent().getId(),
                            task.getGrade(),
                            (double) task.getGrade() * 100 / totalPoints));
                });
        return sb.toString();
    }

    public int getStudentPoints(Student student) {
        return tasks.stream()
                .filter(i -> student.equals(i.getStudent()))
                .mapToInt(Task::getGrade)
                .sum();
    }

    @Override
    public String toString() {
        return name;
    }

}
