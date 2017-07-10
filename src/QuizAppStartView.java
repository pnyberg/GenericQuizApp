import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuizAppStartView extends JPanel {
	private JLabel startLabel;
	private JLabel questionAmountLabel;
	private JButton startButton, filePathButton;
	private JComboBox questionAmountBox;
	private JPanel filePathPanel;
	private JPanel questionAmountPanel;
	private JTextField filePathTextField;

	public QuizAppStartView(String filePathText, int[] questionAmountOptions) {
		setLayout(new GridLayout(4, 1));

		startLabel = new JLabel("Welcome to the QuizApp!", JLabel.CENTER);
		startButton = new JButton("Get started!");
		filePathButton = new JButton("Change file!");
		questionAmountPanel = new JPanel();
		questionAmountLabel = new JLabel("Choose the number of questions: ");
		questionAmountBox = new JComboBox<Integer>();
		filePathPanel = new JPanel();
		filePathTextField = new JTextField(filePathText);
		
		filePathTextField.setPreferredSize( new Dimension( 230, 20 ));
		filePathTextField.setCaretPosition(filePathTextField.getText().length());
		
		for (int item : questionAmountOptions) {
			questionAmountBox.addItem(item);
		}
				
		filePathPanel.add(filePathTextField);
		filePathPanel.add(filePathButton);
		
		questionAmountPanel.add(questionAmountLabel);
		questionAmountPanel.add(questionAmountBox);
		
		add(startLabel);
		add(filePathPanel);
		add(questionAmountPanel);
		add(startButton);
	}
	
	public void setActionListener(ActionListener app) {
		startButton.addActionListener(app);
		filePathButton.addActionListener(app);
		questionAmountBox.addActionListener(app);
		filePathTextField.addActionListener(app);
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
	
	protected JTextField getFilePathTextField() {
		return filePathTextField;
	}
	
	protected int getChosenAmountOfQuestions() {
		return (int)questionAmountBox.getSelectedItem();
	}
}
