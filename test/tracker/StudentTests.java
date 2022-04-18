package tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTests {
    Students students;

    @BeforeEach
    public void init() {
        students = new Students();
    }

    @Test
    public void addingStudents() {
        assertNull(students.addStudent("help"));
        assertNull(students.addStudent("John Doe email"));
        assertNull(students.addStudent("J. Doe name@domain.com"));
        assertNull(students.addStudent("John D. name@domain.com"));
        assertNotNull(students.addStudent("John Doe jdoe@mail.net"));
        assertNull(students.addStudent("Marylin Manson jdoe@mail.net"));
    }

    @Test
    public void addingPoints() {
        students.addStudent("John Doe jdoe@mail.net");
        List<String> result = students.processInputAddPoints("1000 10 10 5 8");
        assertNull(students.getById(Integer.parseInt(result.get(0))));

        result = students.processInputAddPoints("1 10 0 5 8");
        Student student = students.getById(Integer.parseInt(result.get(0)));
        assertNotNull(student);

        Program program = new Program();
        program.addPoints(student, result.subList(1, result.size()));
        for (int i = 1; i < result.size(); i++) {
            int intPoints = Integer.parseInt(result.get(i));
            assertEquals(intPoints > 0 ? 1 : 0, program.getCourse(i - 1).getTasksCount());
        }
    }

    @Test
    public void gettingListOfStudents() {
        students.addStudent("John Doe jdoe@mail.net");
        assertEquals(1, students.getStudents().size());
        assertEquals("jdoe@mail.net", students.getStudents().get(0).getEmail());
    }

    @Test
    public void gettingStudentInfo() {
        students.addStudent("John Doe jdoe@mail.net");

        List<String> result = students.processInputAddPoints("1 1 1 1 1");
        Student student = students.getById(Integer.parseInt(result.get(0)));
        assertNotNull(student);

        Program program = new Program();
        program.addPoints(student, result.subList(1, result.size()));
        assertEquals("1 points: Java=1; DSA=1; Databases=1; Spring=1", program.getStudentInfo(student));
    }

    @Test
    public void gettingStatistics() {
        students.addStudent("John First first@mail.net");
        students.addStudent("John Second second@mail.net");

        Program program = new Program();
        List<String> result;
        Student student;
        List<String> inputs;

        inputs = List.of(
                "1 8 7 7 5",
                "1 7 6 9 7",
                "1 6 5 5 0");
        for (String input : inputs) {
            result = students.processInputAddPoints(input);
            student = students.getById(Integer.parseInt(result.get(0)));
            assertNotNull(student);
            program.addPoints(student, result.subList(1, result.size()));
        }

        inputs = List.of(
                "2 8 0 8 6",
                "2 7 0 0 0",
                "2 9 0 0 5");
        for (String input : inputs) {
            result = students.processInputAddPoints(input);
            student = students.getById(Integer.parseInt(result.get(0)));
            assertNotNull(student);
            program.addPoints(student, result.subList(1, result.size()));
        }

        String stats = """
                Most popular: Java, Databases, Spring
                Least popular: DSA
                Highest activity: Java
                Lowest activity: DSA
                Easiest course: Java
                Hardest course: Spring
                """;
        assertEquals(stats, program.getStatistics());

        Course course = program.getCourse("java");
        assertNotNull(course);
        stats = """
                Java
                id    points    completed
                2     24        4.0%
                1     21        3.5%
                """;
        assertEquals(stats, course.getTopLearnersInfo());
    }

    @Test
    public void gettingNotifications() {
        students.addStudent("John Doe johnd@email.net");
        students.addStudent("John Second second@mail.net");

        Program program = new Program();
        List<String> result;
        Student student;
        List<String> inputs;

        inputs = List.of("1 600 400 0 0");
        for (String input : inputs) {
            result = students.processInputAddPoints(input);
            student = students.getById(Integer.parseInt(result.get(0)));
            assertNotNull(student);
            program.addPoints(student, result.subList(1, result.size()));
        }

        Notifications manager = Notifications.manager();
        List<Notification> notifications = manager.getNotifications();
        int studentCount = manager.getStudentsCount(notifications);
        String expected =
                """
                To: johnd@email.net
                Re: Your Learning Progress
                Hello, John Doe! You have accomplished our Java course!
                To: johnd@email.net
                Re: Your Learning Progress
                Hello, John Doe! You have accomplished our DSA course!
                Total 1 students have been notified.
                """;
        String actual = notifications.stream()
                .map(Notification::toString)
                .collect(Collectors.joining(System.lineSeparator(),
                        "",
                        String.format("%nTotal %d students have been notified.%n", studentCount)));
        assertEquals(expected, actual);

        notifications = manager.getNotifications();
        studentCount = manager.getStudentsCount(notifications);
        expected = "Total 0 students have been notified.";
        actual = String.format("Total %d students have been notified.", studentCount);
        assertEquals(expected, actual);
    }
}
