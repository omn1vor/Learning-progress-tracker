package tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public class Program {
    private final List<Course> courses = new ArrayList<>();

    public Program() {
        courses.add(new Course("Java", 600));
        courses.add(new Course("DSA", 400));
        courses.add(new Course("Databases", 480));
        courses.add(new Course("Spring", 550));
    }

    public void addPoints(Student student, List<String> points) {
        for (int i = 0; i < courses.size(); i++) {
            int intPoints = Integer.parseInt(points.get(i));
            if (intPoints > 0) {
                courses.get(i).addTask(new Task(student, intPoints));
            }
        }
    }

    public Course getCourse(String name) {
        return courses.stream()
                .filter(i -> name.strip().equalsIgnoreCase(i.getName()))
                .findAny().orElse(null);
    }

    public Course getCourse(int index) {
        if (index < 0 || index >= courses.size()) {
            return null;
        } else {
            return courses.get(index);
        }

    }

    public String getStudentInfo(Student student) {
        return courses.stream()
                .map(i -> i.getName() + "=" + i.getStudentPoints(student))
                .collect(Collectors.joining("; ", student.getId() + " points: ", ""));
    }

    public String getStatistics() {
        StringBuilder sb = new StringBuilder();

        List<Course> mostPopular = getMostPopular();
        sb.append(String.format("Most popular: %s%n", Course.listToString(mostPopular)));
        sb.append(String.format("Least popular: %s%n", Course.listToString(getLeastPopular(mostPopular))));

        List<Course> mostActive = getMostActive();
        sb.append(String.format("Highest activity: %s%n", Course.listToString(mostActive)));
        sb.append(String.format("Lowest activity: %s%n", Course.listToString(getLeastActive(mostActive))));

        List<Course> easiest = getEasiest();
        sb.append(String.format("Easiest course: %s%n", Course.listToString(easiest)));
        sb.append(String.format("Hardest course: %s%n", Course.listToString(getHardest(easiest))));

        return sb.toString();
    }

    public List<Course> getMostPopular() {
        int maxStudents = courses.stream().mapToInt(Course::getStudentCount)
                .max().orElse(0);
        if (maxStudents == 0) {
            return List.of();
        }
        return filtered(Course::getStudentCount, maxStudents);
    }

    public List<Course> getLeastPopular(List<Course> opposite) {
        if (opposite.isEmpty()) {
            return List.of();
        }
        int minStudents = courses.stream().mapToInt(Course::getStudentCount)
                .min().orElse(0);
        return filtered(Course::getStudentCount, minStudents, opposite);
    }

    public List<Course> getMostActive() {
        int maxTasks = courses.stream().mapToInt(Course::getTasksCount)
                .max().orElse(0);
        if (maxTasks == 0) {
            return List.of();
        }
        return filtered(Course::getTasksCount, maxTasks);
    }

    public List<Course> getLeastActive(List<Course> opposite) {
        if (opposite.isEmpty()) {
            return List.of();
        }
        int minTasks = courses.stream().mapToInt(Course::getTasksCount)
                .min().orElse(0);
        return filtered(Course::getTasksCount, minTasks, opposite);
    }

    public List<Course> getEasiest() {
        double avg = courses.stream().mapToDouble(Course::getAverageGrade)
                .max().orElse(0);
        if (avg == 0) {
            return List.of();
        }
        return filtered(Course::getAverageGrade, avg);
    }

    public List<Course> getHardest(List<Course> opposite) {
        if (opposite.isEmpty()) {
            return List.of();
        }
        double avg = courses.stream().mapToDouble(Course::getAverageGrade)
                .min().orElse(0);
        return filtered(Course::getAverageGrade, avg, opposite);
    }

    private <T> List<Course> filtered(Function<Course, T> f, T value) {
        return courses.stream()
                .filter(course -> f.apply(course).equals(value))
                .collect(Collectors.toList());
    }

    private <T> List<Course> filtered(Function<Course, T> f, T value, List<Course> opposite) {
        return filtered(f, value).stream()
                .filter(i -> !opposite.contains(i))
                .collect(Collectors.toList());

    }
}
