import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuizAppResultView extends JPanel {
	private JLabel resultLabel;
	private JButton resultButton;

	public QuizAppResultView() {
		setLayout(new GridLayout(2, 1));

		resultLabel = new JLabel("default", JLabel.CENTER);
		resultButton = new JButton("What to do now?");
		
		add(resultLabel);
		add(resultButton);
	}
	
	public void setActionListener(ActionListener app) {
		resultButton.addActionListener(app);
	}
	
	public void setResult(int correctAnswers, int totalQuestions) {
		resultLabel.setText("Result: " + correctAnswers + "/" + totalQuestions);
	}
}
