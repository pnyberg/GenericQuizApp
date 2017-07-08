import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuizAppAnswerView extends JPanel {
	private JPanel answerPanel;
	private JLabel answerLabel;
	private JButton answerButton;
	
	public QuizAppAnswerView() {
		setLayout(new GridLayout(2, 1));

		answerLabel = new JLabel("default", JLabel.CENTER);
		answerButton = new JButton("Return to questions");
		
		add(answerLabel);
		add(answerButton);
	}
	
	public void setActionListener(ActionListener app) {
		answerButton.addActionListener(app);
	}
	
	public void setAnswerLabelText(String text) {
		answerLabel.setText(text);
	}
	
	public void setAnswerButtonText(String text) {
		answerButton.setText(text);
	}
	
	protected JButton getAnswerButton() {
		return answerButton;
	}
}
