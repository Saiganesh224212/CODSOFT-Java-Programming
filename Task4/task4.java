import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class Question {
    protected String questionText;
    protected String[] options;
    protected int correctAnswerIndex;
    protected int questionNumber;

    public Question(String questionText, String[] options, int correctAnswerIndex, int questionNumber) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.questionNumber = questionNumber;
    }

    public abstract void displayQuestion(JLabel questionLabel, JRadioButton[] optionButtons);
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
class CricketQuestion extends Question {
    public CricketQuestion(String questionText, String[] options, int correctAnswerIndex, int questionNumber) {
        super(questionText, options, correctAnswerIndex, questionNumber);
    }

    @Override
    public void displayQuestion(JLabel questionLabel, JRadioButton[] optionButtons) {
        questionLabel.setText("<html><h3>Question " + questionNumber + ": " + questionText + "</h3></html>");
        for (int i = 0; i < options.length; i++) {
            optionButtons[i].setText(options[i]);
        }
    }
}
class QuizTimer extends SwingWorker<Void, Integer> {
    private int timeRemaining;
    private JLabel timerLabel;
    private Runnable onTimeOut;

    public QuizTimer(int timeRemaining, JLabel timerLabel, Runnable onTimeOut) {
        this.timeRemaining = timeRemaining;
        this.timerLabel = timerLabel;
        this.onTimeOut = onTimeOut;
    }

    @Override
    protected Void doInBackground() {
        for (int i = timeRemaining; i >= 0; i--) {
            try {
                Thread.sleep(1000); // Wait for 1 second
                publish(i); // Publish the time left to the UI
            } catch (InterruptedException e) {
                // Handle the case where the thread is interrupted (e.g. when switching questions)
                return null; 
            }
        }
        onTimeOut.run(); // After the timer ends, execute the onTimeOut action
        return null;
    }

    @Override
    protected void process(java.util.List<Integer> chunks) {
        int timeLeft = chunks.get(chunks.size() - 1);
        timerLabel.setText("Time: " + timeLeft);
    }

    public void stopTimer() {
        this.cancel(true); // Cancel the current task and stop the timer
    }
}

public class task4 {
    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton submitButton;
    private JLabel timerLabel;

    private int score = 0;
    private int currentQuestionIndex = 0;
    private Question[] questions;
    private QuizTimer currentTimer; // Store the current running timer

    public task4(Question[] questions) {
        this.questions = questions;
    }

    public void createAndShowGUI() {
        frame = new JFrame("Quiz Application");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
    
        questionLabel = new JLabel();
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(questionLabel);
    
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            optionButtons[i].setBackground(new Color(245, 245, 245));
            buttonGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        panel.add(optionsPanel);
    
        timerLabel = new JLabel("Time: 10");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setForeground(Color.RED);
        panel.add(timerLabel);
        submitButton = new JButton("SUBMIT");  
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(0, 123, 255)); 
        submitButton.setForeground(Color.BLACK); 
        submitButton.setPreferredSize(new Dimension(150, 40));  
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);  
        panel.add(submitButton);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    startNextQuestion();
    
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    startNextQuestion();
                } else {
                    displayFinalResults();
                    System.exit(0); 
                }
            }
        });
    }
    
    

    public void startNextQuestion() {
        // Stop the previous timer if any
        if (currentTimer != null && !currentTimer.isDone()) {
            currentTimer.stopTimer();
        }

        if (currentQuestionIndex < questions.length) {
            Question currentQuestion = questions[currentQuestionIndex];
            currentQuestion.displayQuestion(questionLabel, optionButtons);
            buttonGroup.clearSelection();
            // Start the timer for this question, reset to 10 seconds each time
            currentTimer = new QuizTimer(10, timerLabel, this::onTimeOut);
            currentTimer.execute();
        }
    }

    public void onTimeOut() {
        // Handle the action when time is up (auto skip)
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            startNextQuestion();
        } else {
            displayFinalResults();
            System.exit(0); 
        }
    }

    public void checkAnswer() {
        Question currentQuestion = questions[currentQuestionIndex];
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected() && (i + 1) == currentQuestion.getCorrectAnswerIndex()) {
                score++;
                break;
            }
        }
    }

    public void displayFinalResults() {
        JOptionPane.showMessageDialog(frame, "Quiz Over! Your final score is: " + score + "/" + questions.length);
    }

    public static void main(String[] args) {
        String[] options1 = {"Sachin Tendulkar", "Virat Kohli", "Ricky Ponting", "Brian Lara"};
        Question q1 = new CricketQuestion("Who has scored the most runs in ODI cricket?", options1, 1, 1);

        String[] options2 = {"Australia", "India", "South Africa", "Pakistan"};
        Question q2 = new CricketQuestion("Which country has won the most ICC Cricket World Cups?", options2, 1, 2);

        String[] options3 = {"MS Dhoni", "Kumar Sangakkara", "Adam Gilchrist", "Mark Boucher"};
        Question q3 = new CricketQuestion("Who holds the record for the most dismissals by a wicketkeeper in ODIs?", options3, 2, 3);

        String[] options4 = {"Donald Bradman", "Sachin Tendulkar", "Virat Kohli", "Brian Lara"};
        Question q4 = new CricketQuestion("Who has the highest batting average in Test cricket?", options4, 1, 4);

        // Create the quiz app
        Question[] questions = {q1, q2, q3, q4};
        task4 quizApp = new task4(questions);
        quizApp.createAndShowGUI();
    }
}
