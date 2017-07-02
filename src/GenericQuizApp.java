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

	private int numberOfQuestions = 5;
	private ArrayList<QuizItem> quizItemList;
	
	private JPanel questionPanel;
	private JLabel questionLabel;
	private JRadioButton[] radiobutton;
	private ButtonGroup radiobuttonGroup;
	private JButton enterButton;
	private int questionIndex;

	private JPanel resultPanel;
	private JLabel resultLabel;
	private JButton resultButton;

	public GenericQuizApp() {
		quizFile = new File(path);
		quizItemList = new ArrayList<QuizItem>();

		initPanels();
		
		try {
			extractData();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		createQuestion();
		
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
	
	private void initPanels() {
		// Question-panel
		questionPanel = new JPanel();
		questionLabel = new JLabel("default", JLabel.CENTER);
		radiobutton = new JRadioButton[4];
		radiobuttonGroup = new ButtonGroup();
		enterButton = new JButton("Enter answer");
		enterButton.addActionListener(this);
		questionPanel.setLayout(new GridLayout(6, 1));
		
		for (int i = 0 ; i < radiobutton.length ; i++) {
			radiobutton[i] = new JRadioButton("" + i + "");
			radiobutton[i].setHorizontalAlignment(JRadioButton.CENTER);
			
			radiobuttonGroup.add(radiobutton[i]);
		}
		
		radiobutton[0].setSelected(true);

		questionPanel.add(questionLabel);
		for (int i = 0 ; i < radiobutton.length ; i++) {
			questionPanel.add(radiobutton[i], JPanel.CENTER_ALIGNMENT);
		}
		questionPanel.add(enterButton);
		add(questionPanel);
		
		// Answer-panel
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(2, 1));
		resultLabel = new JLabel("default", JLabel.CENTER);
		resultButton = new JButton("Return to questions");
		resultButton.addActionListener(this);
		
		resultPanel.add(resultLabel);
		resultPanel.add(resultButton);
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
	
	private void createQuestion() {
		questionIndex = ThreadLocalRandom.current().nextInt(0, quizItemList.size());
		QuizItem item = quizItemList.get(questionIndex);
		
		boolean[] answerSet = new boolean[quizItemList.size()];
		questionLabel.setText(item.getQuestion());
		
		int answerIndex = ThreadLocalRandom.current().nextInt(0, 4);
		answerSet[questionIndex] = true;
		
		for (int index = 0 ; index < radiobutton.length ; index++) {
			if (index == answerIndex) {
				radiobutton[answerIndex].setText(item.getAnswer());
				continue;
			}
			while(true) {
				int additionalAnswerIndex = ThreadLocalRandom.current().nextInt(0, quizItemList.size());	
				if (!answerSet[additionalAnswerIndex]) {
					QuizItem answerItem = quizItemList.get(additionalAnswerIndex);
					radiobutton[index].setText(answerItem.getAnswer());
					answerSet[additionalAnswerIndex] = true;
					break;
				}
			}
		}
	}

	private void checkAnswer() {
		QuizItem item = quizItemList.get(questionIndex);
		resultLabel.setText("<html>" + "Question: " + item.getQuestion() + "<br>" +
							"Your answer: " + getSelectedButtonText(radiobuttonGroup) + "<br>" + 
							"Right answer: " + item.getAnswer() + "</html>");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == enterButton) {
			checkAnswer();
			
			remove(questionPanel);
			add(resultPanel);
		} else if (e.getSource() == resultButton) {
			createQuestion();
			
			remove(resultPanel);
			add(questionPanel);
		}
		
		repaint();
		revalidate();
	}

	/**
	 * Source: https://stackoverflow.com/questions/201287/how-do-i-get-which-jradiobutton-is-selected-from-a-buttongroup
	 */
	private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

	public static void main(String[] args) {
		new GenericQuizApp();
	}
}