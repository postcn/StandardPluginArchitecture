package builtinPlugins;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import export.Message;
import export.MessageHandler;
import export.Plugin;

public class CalculatorPlugin implements Plugin {
	public static final String NAME = "Calculator Plugin";
	public static final String CALCULATION_CLOSE_MESSAGE = "%d calculations were performed by the " + NAME;
	public static final String CLEAR = "0";
	public static final String EQUAL = "=";
	public static final String[] entries = {"0","1","2","3","4","5","6","7","8","9"};
	public enum CalcFunction {ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, NONE};
	private static HashMap<CalcFunction, String> convertFunc;
	
	private JPanel contentPanel;
	private MessageHandler messageHandler;
	private int storedValue = 0;
	private int calculationsPerformed = 0;
	private CalcFunction func = CalcFunction.NONE;
	
	private JPanel calculatorPanel;
	private JTextField entryBox;
	
	static {
		convertFunc = new HashMap<>();
		convertFunc.put(CalcFunction.ADDITION, "+");
		convertFunc.put(CalcFunction.SUBTRACTION, "-");
		convertFunc.put(CalcFunction.MULTIPLICATION, "*");
		convertFunc.put(CalcFunction.DIVISION, "/");
		convertFunc.put(CalcFunction.NONE, "");
	}

	@Override
	public void Load(JPanel renderPanel, MessageHandler messageHandler) {
		this.contentPanel = renderPanel;
		this.messageHandler = messageHandler;
		
		setupCalculator();
	}

	@Override
	public void Run() {
		this.contentPanel.add(calculatorPanel);
	}

	@Override
	public void Stop() {
		this.contentPanel.remove(calculatorPanel);
		messageHandler.sendMessage(new Message(NAME, String.format(CALCULATION_CLOSE_MESSAGE, calculationsPerformed)));
		this.calculationsPerformed = 0;
	}

	@Override
	public String getIdentifier() {
		return NAME;
	}
	
	private void setupCalculator() {
		this.calculatorPanel = new JPanel();
		calculatorPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		this.entryBox = new JTextField();
		entryBox.setEditable(false);
		entryBox.setText(CLEAR);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 4;
		
		calculatorPanel.add(entryBox, c);
		
		JButton[] buttons = new JButton[10];
		for (int i=0; i<10; i++) {
			JButton button = new JButton(entries[i]);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					addDigit(button.getText());
				}
				
			});
			buttons[i] = button;
		}
		
		//zero button.
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		calculatorPanel.add(buttons[0], c);
		
		//one button.
		c.gridwidth = 1;
		c.gridy = 3;
		calculatorPanel.add(buttons[1], c);
		
		//4 button
		c.gridy = 2;
		calculatorPanel.add(buttons[4], c);
		
		//7 button
		c.gridy = 1;
		calculatorPanel.add(buttons[7], c);
		
		//8 button
		c.gridx = 1;
		calculatorPanel.add(buttons[8], c);
		
		//5 button
		c.gridy= 2;
		calculatorPanel.add(buttons[5], c);
		
		//2 button
		c.gridy = 3;
		calculatorPanel.add(buttons[2], c);
		
		//3 button
		c.gridx = 2;
		calculatorPanel.add(buttons[3], c);
		
		//6 button
		c.gridy = 2;
		calculatorPanel.add(buttons[6], c);
		
		//9 button
		c.gridy = 1;
		calculatorPanel.add(buttons[9], c);
		
		//*
		c.gridx = 3;
		calculatorPanel.add(createOperatorButton(CalcFunction.MULTIPLICATION), c);
		
		///
		c.gridy = 2;
		calculatorPanel.add(createOperatorButton(CalcFunction.DIVISION), c);
		
		//-
		c.gridy = 3;
		calculatorPanel.add(createOperatorButton(CalcFunction.SUBTRACTION), c);
		
		//+
		c.gridy = 4;
		calculatorPanel.add(createOperatorButton(CalcFunction.ADDITION), c);
		
		//=
		
		JButton equals = new JButton(EQUAL);
		equals.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculate();
			}
			
		});
		c.gridx = 2;
		calculatorPanel.add(equals, c);
		
		
		calculatorPanel.repaint();
		this.contentPanel.add(calculatorPanel);
		this.contentPanel.revalidate();
		this.contentPanel.repaint();
	}
	
	private JButton createOperatorButton(CalcFunction functionToExecute) {
		JButton newButton = new JButton(convertFunc.get(functionToExecute));
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				storedValue = Integer.parseInt(entryBox.getText());
				func = functionToExecute;
				entryBox.setText(CLEAR);
			}
			
		});
		
		return newButton;
	}
	
	private void addDigit(String digit) {
		if (this.entryBox.getText().equals(CLEAR)) {
			this.entryBox.setText(digit);
		} else {
			this.entryBox.setText(this.entryBox.getText() + digit);
		}
	}
	
	private void calculate() {
		int currentValue = Integer.parseInt(entryBox.getText());
		int newValue;
		switch(func) {
		case ADDITION:
			newValue = storedValue + currentValue;
			break;
		case DIVISION:
			newValue = storedValue / currentValue;
			break;
		case MULTIPLICATION:
			newValue = storedValue * currentValue;
			break;
		case SUBTRACTION:
			newValue = storedValue - currentValue;
			break;
		case NONE:
		default:
			newValue = currentValue;
			break;
		}
		if (newValue != currentValue) {
			calculationsPerformed++;
		}
		entryBox.setText(newValue + "");
	}

}
