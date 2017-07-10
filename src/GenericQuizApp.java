import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
	private int[] questionAmountOptions = {1, 5, 10, 15, 20, 25};
	
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
		setSize(320, 270);
		setVisible(true);
	}
	
	private void initViews() {
		// Start-view
		startView = new QuizAppStartView(path, questionAmountOptions);
		startView.setActionListener(this);
		startView.getFilePathTextField().getDocument().addDocumentListener(dl);
		updateQuestionAmountBox();
		
		// Question-view
		questionView = new QuizAppQuestionView();
		questionView.setActionListener(this);
		
		// Answer-view
		answerView = new QuizAppAnswerView();
		answerView.setActionListener(this);

		// Result-view
		resultView = new QuizAppResultView();
		resultView.setActionListener(this);

		// Set the first view 
		add(startView);
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
	
	private void updateQuestionAmountBox() {
		System.out.println("Update question amount");
		// TODO: fix so that only updates when necessary (not for every letter-change?)
	}
	
	private void updateResultLabel() {
		resultView.setResult(numberOfRightAnswers, numberOfQuestions);
	}
	
	DocumentListener dl = new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {
			// Nothing
		}

		public void removeUpdate(DocumentEvent e) {
			updateQuestionAmountBox();
			System.out.println("Effective");
		}

		public void insertUpdate(DocumentEvent e) {
			updateQuestionAmountBox();
			System.out.println("PEnis");
		}
	};

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startView.getStartButton()) {
			try {
				extractData();
				numberOfQuestions = Math.min(quizItemList.size(), startView.getChosenAmountOfQuestions()); 
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.exit(0);
			}
			
			createQuestions();
			
			remove(startView);
			add(questionView);
		} else if (e.getSource() == startView.getFilePathButton()) {
	        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filePathText = fileChooser.getSelectedFile().getPath(); 
				quizFile = new File(filePathText);
				startView.setTextFieldText(quizFile.getName());
	        } else {
	        	// Do nothing, file not changed
	        }
			 
		} else if (e.getSource() == questionView.getEnterButton()) {
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
		
		if (e.getSource() == startView.getFilePathTextField()) {
			System.out.println("trying");
		}

		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new GenericQuizApp();
	}
}