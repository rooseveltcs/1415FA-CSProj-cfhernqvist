package calculator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * @author Fredrik Hernqvist
 * */

public class Calculator extends JFrame {

	JPanel panel;
	JButton[] btn_digit, btn_operator;
	JLabel lbl_digits, lbl_operators, lbl_info;
	JButton btn_equals, btn_comma, btn_clear;
	JTextField tfd_field;
	String operators = "+-*/^%";
	
	
	void addInput (char c)
	{
		String current = tfd_field.getText();
		current += c;
		tfd_field.setText(current);
	}
	
	void calculate ()
	{
		String s = tfd_field.getText();
		s = removeBadStuff(s);//remove irrelevant characters
		s = removeExtras(s);//remove irrelevant relevant characters
		lbl_info.setText(s+"=");//show what we ended up with to minimize confusion
		s = applyOps(s, "^%"); //i hope this is the right priority order
		s = applyOps(s, "*/");
		s = applyOps(s, "+-");
		tfd_field.setText(s);//show result
		if (Double.valueOf(s) == Double.valueOf(s).intValue())
			tfd_field.setText(""+Double.valueOf(s).intValue());//get rid of .0
	}
	
	void validateInput ()//removes any wierd stuff from the text field
	{
		String s = tfd_field.getText();
		String s2 = removeBadStuff(s);
		if (!s.equals(s2))
			tfd_field.setText(s2);
		
	}
	
	void clear ()
	{
		tfd_field.setText("");
	}
	
	String removeChar (String s, int i)
	{
		return s.substring(0, i)+s.substring(i+1, s.length());
	}
	
	String removeBadStuff (String s) //removes irrelevant characters
	{
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (operators.indexOf(c) == -1 && !Character.isDigit(c) && c != '.')
			{
				s = removeChar(s, i);
				i --;
			}
		}
		return s;
	}
	
	String removeExtras (String s) //remove out of place operators and commas
	{
		for (int i = 1; i < s.length(); i++)
		{
			if (!Character.isDigit(s.charAt(i-1)) && !Character.isDigit(s.charAt(i)))
			{
				s = removeChar(s, i);
				i --;
			}
		}
		if (s.length() > 0 && !Character.isDigit(s.charAt(0)))
			s = removeChar(s, 0);
		if (s.length() > 0 && !Character.isDigit(s.charAt(s.length()-1)))
			s = removeChar(s, s.length()-1);
		return s;
	}
	
	int leftBound (String s, int i) //find the index where the number to the left of an operator at i starts
	{
		i --;
		for (; i >= 0; i--)
			if (!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.')
				break;
		return i+1;
	}
	int rightBound (String s, int i)//find the index where the number to the right of an operator at i ends
	{
		i ++;
		for (; i < s.length(); i++)
			if (!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.')
				break;
		return i;
	}
	
	String applyOps (String s, String ops) //applies all operators in the string ops
	{
		for (int i = 1; i < s.length(); i++)
			if (ops.indexOf(s.charAt(i)) != -1)
			{
				int left = leftBound(s, i), right = rightBound(s, i);
				double a = Double.valueOf(s.substring(left, i))
					, b = Double.valueOf(s.substring(i+1, right));
				//System.out.println("l: "+left+", r: "+right+", a: "+a+", b: "+b);
				String result = s.substring(0, left)+applyOperation(a, b, s.charAt(i))+s.substring(right, s.length());
				System.out.println(result);
				return applyOps (result, ops); //keep applying operators recursively
			}
		return s; //if we got here there are no more of that kind of operator
	}
	
	double applyOperation (double a, double b, char op) //returns a op b
	{
		switch(op)
		{
		case '+':
			return a+b;
		case '-':
			return a-b;
		case '*':
			return a*b;
		case '/':
			return a/b;
		case '%':
			return a%b;
		case '^':
			return Math.pow(a, b);
		}
		return 0;
	}
	
	void init (){ //initializes the window, setting up the layout and actionlisteners
		setResizable(false);
		setTitle("Calculator");
		
		btn_equals = new JButton("="); //initialize buttons
		btn_equals.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				calculate();
			}}); 
		btn_comma = new JButton(".");
		btn_comma.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addInput('.');
			}});
		btn_clear = new JButton("C");
		btn_clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clear();
			}}); 
		lbl_digits = new JLabel("Digits"); //labels
		lbl_operators = new JLabel("Operators");
		lbl_info = new JLabel("by Fredrik Hernqvist");
		tfd_field = new JTextField();
		tfd_field.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				calculate();
			}}); 
		tfd_field.addKeyListener(new KeyAdapter(){
				public void keyReleased (KeyEvent e){
					validateInput();
				}
				public void keyTyped (KeyEvent e){
					validateInput();
				}
			});
		btn_digit = new JButton[10];
		for (int i = 0; i < 10; i++) //set up digit buttons
		{
			final char j = (char)(i+'0');
			btn_digit[i] = new JButton(""+i);
			btn_digit[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addInput(j);
				}});
		}
		
		btn_operator = new JButton[operators.length()];
		for (int i = 0; i < operators.length(); i++) //set up operator buttons
		{
			final char j = operators.charAt(i);
			btn_operator[i] = new JButton(""+operators.charAt(i));
			btn_operator[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addInput(j); //add input from buttons
				}});
		}
		panel = (JPanel)getContentPane();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints(); //textfield
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0.5;
		panel.add(tfd_field, c);
		
		c = new GridBagConstraints(); 
		c.gridy = 0;
		c.gridwidth = 6;
		panel.add(lbl_info, c); //info label
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(lbl_digits, c);//digits label
		c.gridx = 4;
		panel.add(lbl_operators, c); //operators label
		
		int dx = 0, dy = 3; //starting positions of digits
		c = new GridBagConstraints();
		c.gridx = dx; //position of zero
		c.gridy = dy+3;
		c.fill = GridBagConstraints.HORIZONTAL;
		for (int n = 0; n < 10; n++) //loop through the digit buttons, adding them
		{
			panel.add(btn_digit[n], c);
			c.gridx = dx+n%3; //position of next digit
			c.gridy = dy+n/3;
		}
		c.gridx ++;
		panel.add(btn_comma, c);
		c.gridx ++;
		c.weightx = 0;
		panel.add(btn_clear, c);
		
		JPanel dividerPanel = new JPanel(); //just a small panel that fills a gap between digits and operators
		dividerPanel.setSize(10, 10);
		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 3;
		c.weightx = 1;
		panel.add(dividerPanel, c);
		
		dx = 4;//starting x of operators
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		for (int n = 0; n < operators.length(); n++) //loop through the operator buttons, adding them
		{
			c.gridx = dx+n%2;
			c.gridy = dy+n/2;
			panel.add(btn_operator[n], c);
		}
		c = new GridBagConstraints();
		c.gridx = dx;
		c.gridy = dy+3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btn_equals, c);
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main (String[] Args)
	{
		Calculator calculator = new Calculator();
		calculator.setVisible(true);
		calculator.init();
	}
}
