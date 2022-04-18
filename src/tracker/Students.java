package tracker;

import java.util.*;
import java.util.stream.Stream;

public class Students {
    Map<String, Student> registry = new HashMap<>();

    public Student addStudent(String input) {
        String[] tokens = input.split("\\s+");
        if (tokens.length < 3) {
            System.out.println("Incorrect credentials");
            return null;
        }
        String firstName = tokens[0];
        if (wrongName(firstName)) {
            System.out.println("Incorrect first name");
            return null;
        }
        String lastName = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length - 1));
        if (wrongName(lastName)) {
            System.out.println("Incorrect last name");
            return null;
        }
        String email = tokens[tokens.length - 1];
        if (wrongEmail(email)) {
            System.out.println("Incorrect email");
            return null;
        }
        if (emailExists(email)) {
            System.out.println("This email is already taken.");
            return null;
        }

        Student student = new Student(registry.size() + 1, firstName, lastName, email);
        registry.put(student.getEmail(), student);

        return student;
    }

    public List<String> processInputAddPoints(String input) {
        List<String> result = new ArrayList<>();

        if (!input.matches("\\w+ \\d+ \\d+ \\d+ \\d+")) {
            System.out.println("Incorrect points format");
            return result;
        }
        String[] tokens = input.split("\\s+");
        String stringId = tokens[0];
        if (!stringId.matches("\\d+")) {
                    /* that's just for the tests, otherwise I'd cut if off earlier,
                    as "Incorrect points format", since I use int IDs */
            System.out.printf("No student is found for id=%s.%n", stringId);
            return result;
        }

        result.add(stringId);
        Stream.of(tokens)
                .skip(1)
                .forEach(result::add);

        return result;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(registry.values());
    }

    public Student getById(int id) {
        return registry.values().stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElse(null);
    }

    private boolean wrongName(String name) {
        if (!name.matches("[A-Za-z]+[ A-Za-z-']*[A-Za-z]+")) {
            return true;
        }
        return name.matches(".*[-']{2}.*");
    }

    private boolean wrongEmail(String email) {
        return !email.matches("[\\w.-]+@[\\w.-]+\\.[\\w]+");
    }

    private boolean emailExists(String email) {
        return registry.containsKey(email);
    }
}
