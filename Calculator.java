import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private StringBuilder currentInput;

    public Calculator() {
        // Frame setup
        setTitle("Calculator");
        setSize(360, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        currentInput = new StringBuilder();

        // Display field
        display = new JTextField();
        display.setBounds(20, 20, 310, 60);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setForeground(Color.BLACK);
        display.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(display);

        // Button labels
        String[] buttons = {
            "C", "/", "*", "←",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", "", ""
        };

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setBounds(20, 100, 310, 360);
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(Color.WHITE);
        add(panel);

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 20));
            button.setFocusPainted(false);

            // Color coding
            if (text.equals("C")) {
                button.setBackground(new Color(230, 57, 70)); // Red
                button.setForeground(Color.WHITE);
            } else if (text.equals("=")) {
                button.setBackground(new Color(0, 168, 132)); // Green
                button.setForeground(Color.WHITE);
            } else if (text.matches("[/*\\-+]")) {
                button.setBackground(new Color(255, 206, 84)); // Yellow
                button.setForeground(Color.BLACK);
            } else {
                button.setBackground(new Color(240, 240, 240)); // Light gray
                button.setForeground(Color.BLACK);
            }

            button.addActionListener(this);
            panel.add(button);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {
            currentInput.append(command);
            display.setText(currentInput.toString());
        } else if (command.equals("C")) {
            currentInput.setLength(0);
            display.setText("");
        } else if (command.equals("←")) {
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1);
                display.setText(currentInput.toString());
            }
        } else if (command.equals("=")) {
            try {
                double result = evaluateExpression(currentInput.toString());
                display.setText(String.valueOf(result));
                currentInput.setLength(0);
                currentInput.append(result);
            } catch (Exception ex) {
                display.setText("Error");
                currentInput.setLength(0);
            }
        } else { // Operator
            // Prevent two consecutive operators
            if (currentInput.length() > 0 && !"+-*/".contains("" + currentInput.charAt(currentInput.length() - 1))) {
                currentInput.append(command);
                display.setText(currentInput.toString());
            }
        }
    }

    // Evaluate expression using simple algorithm (left to right, no precedence)
    private double evaluateExpression(String expr) throws Exception {
        expr = expr.replaceAll("--", "+");

        java.util.List<Double> numbers = new java.util.ArrayList<>();
        java.util.List<Character> operators = new java.util.ArrayList<>();

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (ch >= '0' && ch <= '9' || ch == '.') {
                temp.append(ch);
            } else if ("+-*/".indexOf(ch) != -1) {
                numbers.add(Double.parseDouble(temp.toString()));
                operators.add(ch);
                temp.setLength(0);
            }
        }
        numbers.add(Double.parseDouble(temp.toString()));

        // Compute * and / first
        for (int i = 0; i < operators.size(); ) {
            char op = operators.get(i);
            if (op == '*' || op == '/') {
                double a = numbers.get(i);
                double b = numbers.get(i + 1);
                double r = (op == '*') ? a * b : a / b;
                numbers.set(i, r);
                numbers.remove(i + 1);
                operators.remove(i);
            } else {
                i++;
            }
        }

        // Compute + and -
        double result = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            double b = numbers.get(i + 1);
            if (op == '+') result += b;
            else result -= b;
        }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
