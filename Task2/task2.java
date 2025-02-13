import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

// Interface for input handling
interface IMarkProvider {
    LinkedHashMap<String, Integer> getMarks();
}

// Interface for calculation logic
interface ICalculator {
    int calculateTotal(LinkedHashMap<String, Integer> marks);
    double calculateAverage(int total, int subjects);
    boolean hasFailingSubject(LinkedHashMap<String, Integer> marks);
    String determineGrade(double percentage, boolean hasFailingSubject);
    String getGradeComment(String grade);
    String getGradeEmoji(String grade);
}

// Interface for result display
interface IResultDisplay {
    void showResults(int total, double average, String grade, String comment, String emoji, LinkedHashMap<String, Integer> marks);
}

// Implementation for user input with subject selection
class ConsoleMarkProvider implements IMarkProvider {
    private final Scanner scanner = new Scanner(System.in);
    private static final String[] SUBJECTS = {
        "English", "Additional Language", "C Programming", "Digital Logic", "Data Mining",
        "Discrete Mathematics", "ODE", "Calculus", "Computer Organization", "Networking"
    };

    @Override
    public LinkedHashMap<String, Integer> getMarks() {
        LinkedHashMap<String, Integer> marks = new LinkedHashMap<>();
        int subjects = getValidInteger("Enter number of subjects: ", 1, SUBJECTS.length);

        // Display subject options
        System.out.println("\nSelect subjects from the following:");
        for (int i = 0; i < SUBJECTS.length; i++) {
            System.out.println((i + 1) + ". " + SUBJECTS[i]);
        }

        for (int i = 1; i <= subjects; i++) {
            int choice = getValidInteger("Select subject " + i + " (Enter number 1-10): ", 1, SUBJECTS.length);
            String subjectName = SUBJECTS[choice - 1];

            if (marks.containsKey(subjectName)) {
                System.out.println("Error: Subject already selected. Choose a different one.");
                i--; // Retry this iteration
                continue;
            }

            int mark = getValidInteger("Enter marks for " + subjectName + " (0-100): ", 0, 100);
            marks.put(subjectName, mark);
        }
        return marks;
    }

    // Method to get valid integer input with range
    private int getValidInteger(String prompt, int min, int max) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Error: Value must be between " + min + " and " + max + ".");
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}

// Implementation for grade calculations
class GradeCalculator implements ICalculator {
    // Grade thresholds
    private static final int GRADE_O_PLUS = 85;
    private static final int GRADE_O = 75;
    private static final int GRADE_A_PLUS = 65;
    private static final int GRADE_A = 55;
    private static final int GRADE_B = 40;

    @Override
    public int calculateTotal(LinkedHashMap<String, Integer> marks) {
        return marks.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public double calculateAverage(int total, int subjects) {
        return (subjects > 0) ? (double) total / subjects : 0;
    }

    @Override
    public boolean hasFailingSubject(LinkedHashMap<String, Integer> marks) {
        return marks.values().stream().anyMatch(mark -> mark < 40);
    }

    @Override
    public String determineGrade(double percentage, boolean hasFailingSubject) {
        if (hasFailingSubject) return "F";
        if (percentage >= GRADE_O_PLUS) return "O+";
        if (percentage >= GRADE_O) return "O";
        if (percentage >= GRADE_A_PLUS) return "A+";
        if (percentage >= GRADE_A) return "A";
        if (percentage >= GRADE_B) return "B";
        return "F";
    }

    @Override
    public String getGradeComment(String grade) {
        return switch (grade) {
            case "O+" -> "Phenomenal Performance!";
            case "O" -> "Excellent Work!";
            case "A+" -> "Very Good, Keep it Up!";
            case "A" -> "Nice Effort, Improve!";
            case "B" -> "Need More Hard Work!";
            default -> "Failed due to low marks in one or more subjects.";
        };
    }

    @Override
    public String getGradeEmoji(String grade) {
        return switch (grade) {
            case "O+" -> "🏆";
            case "O" -> "🌟";
            case "A+" -> "🎯";
            case "A" -> "👍";
            case "B" -> "✅";
            default -> "❌";
        };
    }
}

// Implementation for displaying results
class ConsoleResultDisplay implements IResultDisplay {
    @Override
    public void showResults(int total, double average, String grade, String comment, String emoji, LinkedHashMap<String, Integer> marks) {
        System.out.println("\n===============================");
        System.out.println("       STUDENT GRADE REPORT     ");
        System.out.println("===============================");
        System.out.println("Subject-wise Marks:");
        marks.forEach((subject, mark) -> System.out.printf(" - %s: %d%n", subject, mark));
        System.out.println("-------------------------------");
        System.out.printf("Total Marks: %d%n", total);
        System.out.printf("Average Percentage: %.2f%%%n", average);
        System.out.printf("Grade: %s %s%n", grade, emoji);
        System.out.printf("Comment: %s%n", comment);
        System.out.println("===============================");
    }
}

// Controller Class (Handles Main Logic)
class GradeController {
    private final IMarkProvider markProvider;
    private final ICalculator calculator;
    private final IResultDisplay resultDisplay;

    public GradeController(IMarkProvider markProvider, ICalculator calculator, IResultDisplay resultDisplay) {
        this.markProvider = markProvider;
        this.calculator = calculator;
        this.resultDisplay = resultDisplay;
    }

    public void processGrades() {
        LinkedHashMap<String, Integer> marks = markProvider.getMarks();
        int total = calculator.calculateTotal(marks);
        double average = calculator.calculateAverage(total, marks.size());
        boolean hasFailingSubject = calculator.hasFailingSubject(marks);
        String grade = calculator.determineGrade(average, hasFailingSubject);
        String comment = calculator.getGradeComment(grade);
        String emoji = calculator.getGradeEmoji(grade);

        resultDisplay.showResults(total, average, grade, comment, emoji, marks);
    }
}

// Main application class (Minimal Load)
public class task2 {
    public static void main(String[] args) {
        try {
            GradeController controller = new GradeController(new ConsoleMarkProvider(), new GradeCalculator(), new ConsoleResultDisplay());
            controller.processGrades();
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}
