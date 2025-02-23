import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

class Student {
    String firstName, lastName, address, className;
    Date birthdate;
    Map<String, Double> grades;

    public Student(String firstName, String lastName, Date birthdate, String address, String className) {
        this.firstName = removeAccents(firstName);
        this.lastName = removeAccents(lastName);
        this.birthdate = birthdate;
        this.address = removeAccents(address);
        this.className = removeAccents(className);
        this.grades = new HashMap<>();
    }

    public void addGrade(String subject, double grade) {
        grades.put(removeAccents(subject), grade);
    }

    public double getAverageGrade() {
        return grades.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public String getRank() {
        double avg = getAverageGrade();
        if (avg >= 8.5) return "A";
        if (avg >= 7.0) return "B";
        if (avg >= 5.5) return "C";
        if (avg >= 4.0) return "D";
        return "<D";
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " - Rank: " + getRank();
    }

    public static String removeAccents(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
    }
}

class Class {
    String className;
    List<Student> students;

    public Class(String className) {
        this.className = Student.removeAccents(className);
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public Map<String, Long> getRankDistribution() {
        Map<String, Long> rankCounts = new HashMap<>();
        List<String> ranks = Arrays.asList("A", "B", "C", "D", "<D");
        ranks.forEach(rank -> rankCounts.put(rank, students.stream().filter(s -> s.getRank().equals(rank)).count()));
        return rankCounts;
    }

    public void displayStudents() {
        students.forEach(System.out::println);
        System.out.println("Rank Distribution: " + getRankDistribution());
    }
}

public class SchoolManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Class> classes = new HashMap<>();

        Class class1 = new Class("IT63");
        Student s1 = new Student("Le", "Trung", new Date(), "Hanoi", "IT63");
        s1.addGrade("OOP", 9.0);
        s1.addGrade("Project Management", 8.0);
        s1.addGrade("Machine Learning", 7.5);
        s1.addGrade("Database", 8.5);
        s1.addGrade("Mobile App", 9.0);
        class1.addStudent(s1);

        Student s2 = new Student("Nguyen", "An", new Date(), "HCM", "IT63");
        s2.addGrade("OOP", 6.0);
        s2.addGrade("Project Management", 7.0);
        s2.addGrade("Machine Learning", 5.0);
        s2.addGrade("Database", 6.5);
        s2.addGrade("Mobile App", 6.0);
        class1.addStudent(s2);

        classes.put("IT63", class1);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Hien thi danh sach lop");
            System.out.println("2. Hien thi danh sach sinh vien cua lop");
            System.out.println("3. Thoat");
            System.out.print("Chon mot tuy chon: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Danh sach lop: " + classes.keySet());
                    break;
                case 2:
                    System.out.print("Nhap ma lop de xem danh sach sinh vien: ");
                    String classCode = scanner.nextLine();
                    if (classes.containsKey(classCode)) {
                        classes.get(classCode).displayStudents();
                    } else {
                        System.out.println("Lop khong ton tai.");
                    }
                    break;
                case 3:
                    System.out.println("Thoat chuong trinh.");
                    return;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
            }
        }
    }
}
