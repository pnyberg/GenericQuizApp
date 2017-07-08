import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuizAppStartView extends JPanel {
	private JLabel startLabel;
	private JButton startButton;
	
	public QuizAppStartView() {
		startLabel = new JLabel("Welcome to the QuizApp!");
		startButton = new JButton();
	}
	
	public void setActionListener(ActionListener app) {
		startButton.addActionListener(app);
	}
}
