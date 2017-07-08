import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class QuizAppQuestionView extends JPanel {
	private JLabel questionLabel;
	private JRadioButton[] radiobutton;
	private ButtonGroup radiobuttonGroup;
	private JButton enterButton;
	
	private int numberOfAnswerOptions;
	
	public QuizAppQuestionView() {
		numberOfAnswerOptions = 4;
		
		questionLabel = new JLabel("default", JLabel.CENTER);
		radiobutton = new JRadioButton[numberOfAnswerOptions];
		radiobuttonGroup = new ButtonGroup();
		enterButton = new JButton("Enter answer");
		setLayout(new GridLayout(6, 1));
		
		for (int i = 0 ; i < radiobutton.length ; i++) {
			radiobutton[i] = new JRadioButton("" + i + "");
			radiobutton[i].setHorizontalAlignment(JRadioButton.CENTER);
			
			radiobuttonGroup.add(radiobutton[i]);
		}
		
		radiobutton[0].setSelected(true);

		add(questionLabel);
		for (int i = 0 ; i < radiobutton.length ; i++) {
			add(radiobutton[i], JPanel.CENTER_ALIGNMENT);
		}
		add(enterButton);
	}
	
	public void setAnswerOption(int index, String text){
		 radiobutton[index].setText(text);
	}
	
	public void setSelectedAnswerOption(int index) {
		radiobutton[index].setSelected(true);
	}

	/**
	 * Source: https://stackoverflow.com/questions/201287/how-do-i-get-which-jradiobutton-is-selected-from-a-buttongroup
	 */
	public String getSelectedButtonText() {
        for (Enumeration<AbstractButton> buttons = radiobuttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

	public int getNumberOfAnswerOptions() {
		return numberOfAnswerOptions;
	}

	public void setNumberOfAnswerOptions(int numberOfAnswerOptions) {
		this.numberOfAnswerOptions = numberOfAnswerOptions;
	}

	public void setActionListener(ActionListener app) {
		enterButton.addActionListener(app);
	}

	public void setQuestionLabelText(String text) {
		questionLabel.setText(text);
	}
	
	public JButton getEnterButton() {
		return enterButton;
	}
}
