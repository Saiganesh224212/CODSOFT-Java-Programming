/* 
NUMBER GAME
1. Generate a random number within a specified range, such as 1 to 100.
2. Prompt the user to enter their guess for the generated number.
3. Compare the user's guess with the generated number and provide feedback on whether the guess
is correct, too high, or too low.
4. Repeat steps 2 and 3 until the user guesses the correct number.
You can incorporate additional details as follows:
5. Limit the number of attempts the user has to guess the number.
6. Add the option for multiple rounds, allowing the user to play again.
7. Display the user's score, which can be based on the number of attempts taken or rounds won.
 */

import java.util.Random;
import java.util.Scanner;
public class task1 {

    // Game class handling the core logic
public static class GameSession {
    private int secretNumber;
    private int maxAttempts;
    private int attemptsUsed;
    private int score;
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public GameSession(int maxAttempts) {
        this.secretNumber = generateRandomNumber();
        this.maxAttempts = maxAttempts;
        this.attemptsUsed = 0;
        this.score = 0;
    }

    private int generateRandomNumber() {
        return random.nextInt(100) + 1;
    }

    public void startGame() {
        System.out.println("\n🎯 A number between 1 and 100 has been chosen. Can you guess it?");
        System.out.println("💡 You have " + maxAttempts + " attempts. Best of luck!");

        while (attemptsUsed < maxAttempts) {
            int userGuess = getUserGuess();
            attemptsUsed++;

            if (userGuess > secretNumber) {
                System.out.println("📈 Too high! Attempts left: " + (maxAttempts - attemptsUsed));
            } else if (userGuess < secretNumber) {
                System.out.println("📉 Too low! Attempts left: " + (maxAttempts - attemptsUsed));
            } else {
                System.out.println("🎉 Correct! The number was " + secretNumber);
                calculateScore();
                return;
            }
        }

        System.out.println("❌ Out of attempts! The correct number was: " + secretNumber);
    }

    private int getUserGuess() {
        int guess;
        while (true) {
            System.out.print("➡️ Enter your guess: ");
            if (scanner.hasNextInt()) {
                guess = scanner.nextInt();
                if (guess >= 1 && guess <= 100) {
                    return guess;
                }
            }
            System.out.println("⚠️ Invalid input! Please enter a number between 1 and 100.");
            scanner.nextLine();
        }
    }
    private void calculateScore() {
        this.score = (maxAttempts - attemptsUsed + 1) * 10;
        System.out.println("🏆 You scored " + score + " points!");
    }
    public int getScore() {
        return score;
    }
}
private static boolean askToPlayAgain(Scanner scanner) {
    System.out.print("\n🔄 Play another round? (yes/no): ");
    String response = scanner.next().trim().toLowerCase();
    return response.equals("yes");
}
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalScore = 0;
        boolean playAgain = true;

        System.out.println("🎮 Welcome to GuessMaster – The Ultimate Number Challenge!");

        while (playAgain) {
            GameSession game = new GameSession(7); // Set max attempts per round
            game.startGame();
            totalScore += game.getScore();

            System.out.println("⭐ Total Score: " + totalScore);
            playAgain = askToPlayAgain(scanner);
        }

        System.out.println("\n🏁 Final Score: " + totalScore);
        System.out.println("Thanks for playing! 🎉");
        scanner.close();
    }
}




