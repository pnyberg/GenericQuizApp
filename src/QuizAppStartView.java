import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuizAppStartView extends JPanel {
	private JLabel startLabel;
	private JButton startButton;
	private JTextField filePathTextField;

	public QuizAppStartView(String filePathText) {
		setLayout(new GridLayout(3, 1));

		startLabel = new JLabel("Welcome to the QuizApp!", JLabel.CENTER);
		startButton = new JButton("Get started!");
		filePathTextField = new JTextField(filePathText);	
		
		add(startLabel);
		add(filePathTextField);
		add(startButton);
	}
	
	public void setActionListener(ActionListener app) {
		startButton.addActionListener(app);
	}
	
	protected JButton getStartButton() {
		return startButton;
	}
}
