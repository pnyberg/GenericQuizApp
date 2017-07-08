import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenericQuizApp extends JFrame implements ActionListener {
	private String path = "Saudiska kungar.txt";
	private File quizFile;

	private int numberOfQuestions;
	private int numberOfRightAnswers;
	private ArrayList<QuizItem> quizItemList;
	private int questionListIndex;
	private ArrayList<Integer> questionList;
	
	private QuizAppStartView startView;
	private QuizAppQuestionView questionView;
	private QuizAppAnswerView answerView;
	private QuizAppResultView resultView;

	public GenericQuizApp() {
		quizFile = new File(path);
		quizItemList = new ArrayList<QuizItem>();
		
		numberOfQuestions = 5;
		numberOfRightAnswers = 0;

		initViews();
		
		try {
			extractData();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		createQuestions();
		
		initFrameAttributes();
	}
	
	private void initFrameAttributes() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (int)((dim.width-w)*0.4);
        int y = (int)((dim.height-h)*0.4);

        // Move the window
        this.setLocation(x, y);
        
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320, 230);
		setVisible(true);
	}
	
	private void initViews() {
		// Start-view
		startView = new QuizAppStartView();
		startView.setActionListener(this);
		add(startView);
		
		// Question-view
		questionView = new QuizAppQuestionView();
		questionView.setActionListener(this);
		
		// Answer-view
		answerView = new QuizAppAnswerView();
		answerView.setActionListener(this);

		// Result-view
		resultView = new QuizAppResultView();
		resultView.setActionListener(this);
	}
	
	/**
	 * Extracts data from the file and putting them in "quizItemList"
	 */
	private void extractData() throws IOException {
		Scanner scanner = new Scanner(quizFile);

		// Makes sure the first word is the tag "QUESTION" (basically format-check)
		if (!scanner.next().equals("QUESTION:")) {
			System.exit(0);
		}
		
		while (scanner.hasNext()) {
			StringBuilder question = new StringBuilder();
			StringBuilder answer = new StringBuilder();

			for (String t = scanner.next() ; !t.equals("ANSWER:") ; t = scanner.next()) {
				question.append(t + " ");
			}
			for (String t = scanner.next() ; !t.equals("QUESTION:") ; t = scanner.next()) {
				answer.append(t + " ");
				
				if (!scanner.hasNext()) {
					break;
				}
			}
			
			quizItemList.add(new QuizItem(question.toString(), answer.toString()));
		}
		
		scanner.close();
	}
	
	private void createQuestions() {
		questionList = new ArrayList<Integer>(); 
		boolean[] questionUsed = new boolean[quizItemList.size()];

		for (int i = 0 ; i < numberOfQuestions ; i++) {
			while(true) {
				int questionIndex = ThreadLocalRandom.current().nextInt(0, quizItemList.size());
				if (!questionUsed[questionIndex]) {
					questionList.add(questionIndex);
					questionUsed[questionIndex] = true;
					break;
				}
			}
		}
		
		setQuestion();
	}
	
	/**
	 * Set swing-components to contain the relevant questions information
	 */
	private void setQuestion() {
		int questionIndex = questionList.get(questionListIndex);
		QuizItem item = quizItemList.get(questionIndex);
		
		boolean[] answerSet = new boolean[quizItemList.size()];
		questionView.setQuestionLabelText(item.getQuestion());
		
		int answerIndex = ThreadLocalRandom.current().nextInt(0, 4);
		answerSet[questionIndex] = true;
		
		for (int index = 0 ; index < questionView.getNumberOfAnswerOptions() ; index++) {
			if (index == answerIndex) {
				questionView.setAnswerOption(answerIndex, item.getAnswer());
				continue;
			}
			while(true) {
				int additionalAnswerIndex = ThreadLocalRandom.current().nextInt(0, quizItemList.size());	
				if (!answerSet[additionalAnswerIndex]) {
					QuizItem answerItem = quizItemList.get(additionalAnswerIndex);
					questionView.setAnswerOption(index, answerItem.getAnswer());
					answerSet[additionalAnswerIndex] = true;
					break;
				}
			}
		}
	}
	
	private void setNextQuestion() {
		questionListIndex++;
		questionView.setSelectedAnswerOption(0);

		setQuestion();
		
		if (questionListIndex == (numberOfQuestions - 1)) { // last question
			answerView.setAnswerButtonText("Done! Show results!");
		}
	}

	/**
	 * Print information about the answer and calculate number of right answers
	 */
	private void checkAnswer() {
		int questionIndex = questionList.get(questionListIndex);
		QuizItem item = quizItemList.get(questionIndex);
		answerView.setAnswerLabelText("<html>" + "Question: " + item.getQuestion() + "<br>" +
				"Your answer: " + questionView.getSelectedButtonText() + "<br>" + 
				"Right answer: " + item.getAnswer() + "</html>");
		
		if (questionView.getSelectedButtonText().equals(item.getAnswer())) {
			numberOfRightAnswers++;
		}
	}
	
	private void updateResultLabel() {
		resultView.setResult(numberOfRightAnswers, numberOfQuestions);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == questionView.getEnterButton()) {
			checkAnswer();
			
			remove(questionView);
			add(answerView);
		} else if (e.getSource() == answerView.getAnswerButton()) {
			if (questionListIndex == (numberOfQuestions - 1)) { // last question
				updateResultLabel();
				
				remove(answerView);
				add(resultView);
			} else {
				setNextQuestion();
				
				remove(answerView);
				add(questionView);
			}
		}
		
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new GenericQuizApp();
	}
}