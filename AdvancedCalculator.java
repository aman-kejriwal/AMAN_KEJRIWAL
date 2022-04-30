//advanced calculator
import javax.swing.JButton;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.lang.Math;
import java.util.Stack;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.*;

class CreateButton {
    JButton name;

    public CreateButton(String symbol, ActionListener listener, JPanel pane) {
        name = new JButton(symbol);
        name.addActionListener(listener);
        name.setActionCommand(symbol);
        pane.add(name);
    }
}

class Calculator {
    private String token = "";
    private String exception = "";

    public String getException() {
        return exception;
    }

    public boolean checkInput(String in) {
        int open = 0;
        int closed = 0;
        int opCount = 0;
        int numCount = 0;
        int sqrtCount = 0;
        boolean valid = true;

        Scanner reader = new Scanner(in);

        while (reader.hasNext()) {
            token = reader.next();
            if (token.equals("("))
                open++;
            if (token.equals(")"))
                closed++;
            if (isNumber(token))
                numCount++;
            if (isOperator(token))
                opCount++;
            if (token.equals("sqrt"))
                sqrtCount++;
        }

        if (open != closed) {
            valid = false;
            exception = "Error: unbalanced parenthesis";
        }

        if (numCount <= opCount) {
            valid = false;
            exception = "Error: too many operators";
        }

        if (opCount == 0 && sqrtCount == 0) {
            valid = false;
            exception = "Error: no operator";
        }
        return valid;
    }

    public double readInput(String exp) {
        Scanner input = new Scanner(exp);
        Stack<Double> num = new Stack<Double>();
        Stack<String> op = new Stack<String>();

        while (input.hasNext()) {
            token = input.next();
            if (isNumber(token)) {
                num.push(Double.parseDouble(token));
            } else if (token.equals("(")) {
                op.push(token);
            } else if (token.equals(")")) {
                while (!op.peek().equals("(")) {
                    if (op.peek().equals("sqrt")) {
                        num.push(Math.sqrt(num.pop()));
                        op.pop();
                    } else
                        num.push(evaluate(op.pop(), num.pop(), num.pop()));
                }
                op.pop();
            } else {
                while (!op.empty() && hasPrecedence(token, op.peek())) {
                    if (op.peek().equals("sqrt")) {
                        num.push(Math.sqrt(num.pop()));
                        op.pop();
                    } else
                        num.push(evaluate(op.pop(), num.pop(), num.pop()));
                }
                op.push(token);
            }
        }

        while (!op.empty()) {
            if (op.peek().equals("sqrt")) {
                num.push(Math.sqrt(num.pop()));
                op.pop();
            } else
                num.push(evaluate(op.pop(), num.pop(), num.pop()));
        }
        return num.pop();
    }

    private static boolean isNumber(String exp) {
        if (exp.startsWith("-") && (exp.length() > 1))
            return true;
        else if (exp.startsWith("0"))
            return true;
        else if (exp.startsWith("1"))
            return true;
        else if (exp.startsWith("2"))
            return true;
        else if (exp.startsWith("3"))
            return true;
        else if (exp.startsWith("4"))
            return true;
        else if (exp.startsWith("5"))
            return true;
        else if (exp.startsWith("6"))
            return true;
        else if (exp.startsWith("7"))
            return true;
        else if (exp.startsWith("8"))
            return true;
        else if (exp.startsWith("9"))
            return true;
        else
            return false;
    }

    private static boolean isOperator(String exp) {
        if (exp.equals("+"))
            return true;
        else if (exp.equals("-"))
            return true;
        else if (exp.equals("*"))
            return true;
        else if (exp.equals("/"))
            return true;
        else if (exp.equals("^"))
            return true;
        else
            return false;
    }

    private static boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")"))
            return false;
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-")))
            return false;
        if (op1.equals("^") && (op2.equals("/") || op2.equals("*")))
            return false;
        if (op1.equals("^") && (op2.equals("-") || op2.equals("+")))
            return false;
        if (op1.equals("sqrt") && (op2.equals("/") || op2.equals("*")))
            return false;
        if (op1.equals("sqrt") && (op2.equals("-") || op2.equals("+")))
            return false;
        else
            return true;
    }

    private static double evaluate(String operation, double num2, double num1) {
        if (operation.equals("+")) {
            num1 = num1 + num2;
        } else if (operation.equals("-")) {
            num1 = num1 - num2;
        } else if (operation.equals("*")) {
            num1 = num1 * num2;
        } else if (operation.equals("/")) {
            num1 = num1 / num2;
        } else if (operation.equals("^")) {
            num1 = Math.pow(num1, num2);
        }
        return num1;
    }
}

public class CalculatorGUI {

    // CreateButton one, two, three, four, five, six, seven, eight, nine, dot, zero,
    // addition, subtract, multiply, divide,
    // equals, clear, sqrt, close, open, exp, ans, neg;

    JTextField displayBox;
    private String button = "";
    // The "input" variable holds user input that is readable to the "Calculator"
    // class
    private String input = "";
    // The variable "newEntry" resets the screen when the user clicks any button
    // after evaluating the expression
    // It removes the dependency on the "clear" button
    private boolean newEntry = false;
    private double result;
    DecimalFormat formater = new DecimalFormat("0.##########");

    public static void main(String[] args) {
        CalculatorGUI gui = new CalculatorGUI();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame("Calculator");
        JPanel centerPane = new JPanel();
        JPanel bottomPane = new JPanel();

        centerPane.setLayout(new GridLayout(5, 4));
        bottomPane.setLayout(new GridLayout(1, 2, 2, 2));

        displayBox = new JTextField(20);

        CreateButton sqrt = new CreateButton("sqrt", new OperationButton(), centerPane);
        CreateButton exp = new CreateButton(" ^ ", new OperationButton(), centerPane);
        CreateButton open = new CreateButton("( ", new OperationButton(), centerPane);
        CreateButton close = new CreateButton(" )", new OperationButton(), centerPane);
        CreateButton seven = new CreateButton("7", new NumberButton(), centerPane);
        CreateButton eight = new CreateButton("8", new NumberButton(), centerPane);
        CreateButton nine = new CreateButton("9", new NumberButton(), centerPane);
        CreateButton addition = new CreateButton(" + ", new OperationButton(), centerPane);
        CreateButton four = new CreateButton("4", new NumberButton(), centerPane);
        CreateButton five = new CreateButton("5", new NumberButton(), centerPane);
        CreateButton six = new CreateButton("6", new NumberButton(), centerPane);
        CreateButton subtract = new CreateButton(" - ", new OperationButton(), centerPane);
        CreateButton one = new CreateButton("1", new NumberButton(), centerPane);
        CreateButton two = new CreateButton("2", new NumberButton(), centerPane);
        CreateButton three = new CreateButton("3", new NumberButton(), centerPane);
        CreateButton multiply = new CreateButton(" * ", new OperationButton(), centerPane);
        CreateButton dot = new CreateButton(".", new NumberButton(), centerPane);
        CreateButton zero = new CreateButton("0", new NumberButton(), centerPane);
        CreateButton equals = new CreateButton(" = ", new EqualsButton(), centerPane);
        CreateButton divide = new CreateButton(" / ", new OperationButton(), centerPane);
        CreateButton ans = new CreateButton("ans", new OperationButton(), bottomPane);
        CreateButton neg = new CreateButton("(-)", new OperationButton(), bottomPane);
        CreateButton clear = new CreateButton("clear", new OperationButton(), bottomPane);

        frame.getContentPane().add(BorderLayout.NORTH, displayBox);
        frame.getContentPane().add(BorderLayout.CENTER, centerPane);
        frame.getContentPane().add(BorderLayout.SOUTH, bottomPane);

        frame.setSize(250, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class NumberButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            if (newEntry) {
                displayBox.setText("");
                newEntry = false;
                input = "";
            }

            button = event.getActionCommand();
            displayBox.setText(displayBox.getText() + button);
            input = input + button;
        }
    }

    class OperationButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            double ans = 0.0;
            button = event.getActionCommand();

            if (newEntry) {
                displayBox.setText("");
                newEntry = false;
                input = "";
            }

            if (button == "ans") {
                displayBox.setText(displayBox.getText() + formater.format(result));
                input = input + Double.toString(result);
            } else if (button == "(-)") {
                displayBox.setText(displayBox.getText() + "-");
                input = input + "-";
            } else if (button == "clear") {
                displayBox.setText("");
                input = "";
            } else if (button == "sqrt") {
                displayBox.setText(displayBox.getText() + "sqrt( ");
                input = input + "sqrt ( "; // extra spaces makes string readable to "Calculator" class
            } else {
                displayBox.setText(displayBox.getText() + button);
                input = input + button;
            }
        }
    }

    class EqualsButton implements ActionListener {
        Calculator evaluate = new Calculator();

        public void actionPerformed(ActionEvent event) {
            newEntry = true;

            if (evaluate.checkInput(input)) {
                button = event.getActionCommand();
                result = evaluate.readInput(input);
                displayBox.setText(displayBox.getText() + button + formater.format(result));
            } else
                displayBox.setText(evaluate.getException());
        }
    }
}
