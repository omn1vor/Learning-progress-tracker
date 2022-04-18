package tracker;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);
    private final Students students = new Students();
    private final Program program = new Program();

    public CLI() {
        mainMenu();
    }

    private void mainMenu() {
        System.out.println("Learning Progress Tracker");
        while (true) {
            String input = scanner.nextLine().strip().toLowerCase();
            if ("exit".equals(input)) {
                System.out.println("Bye!");
                break;
            } else if ("add students".equals(input)) {
                addStudents();
            } else if ("list".equals(input)) {
                listStudents();
            } else if ("add points".equals(input)) {
                addPoints();
            } else if ("find".equals(input)) {
                findStudent();
            } else if ("statistics".equals(input)) {
                statistics();
            } else if ("notify".equals(input)) {
                showNotifications();
            } else if ("back".equals(input)) {
                System.out.println("Enter 'exit' to exit the program.");
            } else if (input.isEmpty()) {
                System.out.println("No input.");
            } else {
                System.out.println("Error: unknown command!");
            }
        }
    }

    private void addStudents() {
        int count = 0;
        System.out.println("Enter student credentials or 'back' to return");
        while (true) {
            String input = scanner.nextLine().strip().toLowerCase();
            if ("back".equals(input)) {
                System.out.printf("Total %d students have been added.%n", count);
                break;
            } else {
                Student newStudent = students.addStudent(input);
                if (newStudent != null) {
                    count++;
                    System.out.println("The student has been added.");
                }
            }
        }
    }

    private void listStudents() {
        List<Student> list = students.getStudents();
        if (list.isEmpty()) {
            System.out.println("No students found");
            return;
        }

        System.out.println("Students:");
        list.stream()
                .map(Student::getId)
                .forEach(System.out::println);
    }

    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return");

        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                break;
            } else {
                List<String> result = students.processInputAddPoints(input);
                if (result.isEmpty()) {
                    continue;
                }
                int id = Integer.parseInt(result.get(0));
                Student student = students.getById(id);
                if (student == null) {
                    System.out.printf("No student is found for id=%d.%n", id);
                    continue;
                }
                program.addPoints(student, result.subList(1, result.size()));
                System.out.println("Points updated.");
            }
        }
    }

    private void findStudent() {
        System.out.println("Enter an id or 'back' to return");

        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                break;
            } else {
                if (!input.matches("\\d+")) {
                    System.out.printf("No student is found for id=%s%n", input);
                    continue;
                }
                Student student = students.getById(Integer.parseInt(input));
                if (student == null) {
                    System.out.printf("No student is found for id=%s%n", input);
                    continue;
                }
                System.out.println(program.getStudentInfo(student));
            }
        }
    }

    private void statistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        System.out.print(program.getStatistics());

        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                break;
            } else {
                Course course = program.getCourse(input);
                if (course == null) {
                    System.out.println("Unknown course");
                    continue;
                }
                System.out.print(course.getTopLearnersInfo());
            }
        }
    }

    private void showNotifications() {
        Notifications manager = Notifications.manager();
        List<Notification> notifications = manager.getNotifications();
        int studentsCount = manager.getStudentsCount(notifications);
        notifications.forEach(System.out::println);
        System.out.printf("Total %d students have been notified.", studentsCount);
    }
}
