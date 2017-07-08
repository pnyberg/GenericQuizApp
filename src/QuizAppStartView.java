import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuizAppStartView extends JPanel {
	private JLabel startLabel;
	private JButton startButton, filePathButton;
	private JPanel filePathPanel;
	private JTextField filePathTextField;

	public QuizAppStartView(String filePathText) {
		setLayout(new GridLayout(3, 1));

		startLabel = new JLabel("Welcome to the QuizApp!", JLabel.CENTER);
		startButton = new JButton("Get started!");
		filePathButton = new JButton("Change file!");
		filePathPanel = new JPanel();
		filePathTextField = new JTextField(filePathText);
		
		filePathTextField.setPreferredSize( new Dimension( 230, 20 ));
		filePathTextField.setCaretPosition(filePathTextField.getText().length());
		
		filePathPanel.add(filePathTextField);
		filePathPanel.add(filePathButton);
		
		add(startLabel);
		add(filePathPanel);
		add(startButton);
	}
	
	public void setActionListener(ActionListener app) {
		startButton.addActionListener(app);
		filePathButton.addActionListener(app);
	}
	
	public void setTextFieldText(String pathText) {
		filePathTextField.setText(pathText);
	}
	
	protected JButton getStartButton() {
		return startButton;
	}

	protected JButton getFilePathButton() {
		return filePathButton;
	}
}
