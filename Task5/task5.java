import java.util.*;
interface CourseManager {
    void addCourse(Course course);
    void removeCourse(String courseCode);
    void displayCourses();
}
interface StudentManager {
    void registerCourse(String studentId, String courseCode);
    void dropCourse(String studentId, String courseCode);
    void displayStudentCourses(String studentId);
}
class Course {
    private final String courseCode;
    private final String title;
    private final int capacity;
    private final String schedule;
    private int enrolledStudents;
    public Course(String courseCode, String title, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolledStudents = 0;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public String getTitle() {
        return title;
    }
    public boolean enrollStudent() {
        if (enrolledStudents < capacity) {
            enrolledStudents++;
            return true;
        }
        return false;
    }
    public void dropStudent() {
        if (enrolledStudents > 0) {
            enrolledStudents--;
        }
    }
    @Override
    public String toString() {
        return courseCode + " - " + title + " (" + (capacity - enrolledStudents) + " slots available, " + schedule + ")";
    }
}
class Student {
    private final String studentId;
    private final String name;
    private final List<Course> registeredCourses;
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }
    public String getStudentId() {
        return studentId;
    }
    public String getName() {
        return name;
    }
    public void registerCourse(Course course) {
        registeredCourses.add(course);
    }
    public void dropCourse(Course course) {
        registeredCourses.remove(course);
    }
    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }
    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}
class CourseDatabase implements CourseManager {
    private final Map<String, Course> courses = new HashMap<>();
    @Override
    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }
    @Override
    public void removeCourse(String courseCode) {
        courses.remove(courseCode);
    }
    @Override
    public void displayCourses() {
        System.out.println("\n📚 Available Courses:");
        courses.values().forEach(System.out::println);
    }
    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }
}
class StudentDatabase implements StudentManager {
    private final Map<String, Student> students = new HashMap<>();
    private int studentCounter = 1; // Auto-generate Student IDs
    public String addStudent(String name) {
        String studentId = "S" + (100 + studentCounter++);
        Student student = new Student(studentId, name);
        students.put(studentId, student);
        System.out.println("✅ Welcome, " + name + "! Your Student ID: " + studentId);
        return studentId;
    }
    @Override
    public void registerCourse(String studentId, String courseCode) {
        Student student = students.get(studentId);
        Course course = task5.courseDatabase.getCourse(courseCode);

        if (student != null && course != null) {
            if (course.enrollStudent()) {
                student.registerCourse(course);
                System.out.println("✅ Course " + courseCode + " registered successfully for " + student.getName());
            } else {
                System.out.println("❌ Course " + courseCode + " is full.");
            }
        } else {
            System.out.println("❌ Invalid student ID or course code.");
        }
    }
    @Override
    public void dropCourse(String studentId, String courseCode) {
        Student student = students.get(studentId);
        Course course = task5.courseDatabase.getCourse(courseCode);
        if (student != null && course != null) {
            student.dropCourse(course);
            course.dropStudent();
            System.out.println("✅ Course " + courseCode + " dropped successfully for " + student.getName());
        } else {
            System.out.println("❌ Invalid student ID or course code.");
        }
    }
    @Override
    public void displayStudentCourses(String studentId) {
        Student student = students.get(studentId);
        if (student != null) {
            System.out.println("\n📜 Registered Courses for " + student.getName() + ":");
            student.getRegisteredCourses().forEach(System.out::println);
        } else {
            System.out.println("❌ Invalid student ID.");
        }
    }
}
class CourseRegistrationUI {
    private final Scanner scanner;
    private final CourseManager courseManager;
    private final StudentDatabase studentManager;
    private String currentStudentId;
    public CourseRegistrationUI(CourseManager courseManager, StudentDatabase studentManager) {
        this.scanner = new Scanner(System.in);
        this.courseManager = courseManager;
        this.studentManager = studentManager;
    }
    public void run() {
        System.out.print("Enter your name to register: ");
        String name = scanner.nextLine();
        currentStudentId = studentManager.addStudent(name);
        while (true) {
            System.out.println("\n🔹 Student Course Registration System");
            System.out.println("1️⃣ View Available Courses");
            System.out.println("2️⃣ Register for a Course");
            System.out.println("3️⃣ Drop a Course");
            System.out.println("4️⃣ View Registered Courses");
            System.out.println("5️⃣ Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> courseManager.displayCourses();
                case 2 -> registerCourse();
                case 3 -> dropCourse();
                case 4 -> studentManager.displayStudentCourses(currentStudentId);
                case 5 -> {
                    System.out.println("✅ Exiting the system. Thank you!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
    private void registerCourse() {
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.next();
        studentManager.registerCourse(currentStudentId, courseCode);
    }
    private void dropCourse() {
        System.out.print("Enter Course Code to drop: ");
        String courseCode = scanner.next();
        studentManager.dropCourse(currentStudentId, courseCode);
    }
}
public class task5 {
    public static final CourseDatabase courseDatabase = new CourseDatabase();
    public static final StudentDatabase studentDatabase = new StudentDatabase();
    public static void main(String[] args) {
        loadSampleData();
        new CourseRegistrationUI(courseDatabase, studentDatabase).run();
    }
    private static void loadSampleData() {
        courseDatabase.addCourse(new Course("UCSH601", "Discrete Mathematics", 2, "Mon-Wed 10:00 AM"));
        courseDatabase.addCourse(new Course("UCSH602", "Information Retrieval", 2, "Tue-Thu 2:00 PM"));
        courseDatabase.addCourse(new Course("UCSH603", "High Performance Computing", 1, "Wed-Fri 11:00 AM"));
    }
}
