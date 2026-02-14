package dsa;

import java.util.ArrayDeque;
import java.util.Deque;

public class PolishNotation {
    public int evalRPN(String[] tokens) {
        if (tokens.length == 0) return 0;
        Deque<Integer> stack = new ArrayDeque<>();
        int i = 0;
        while (i < tokens.length) {
            if (tokens[i].equals("+") || tokens[i].equals("-") || tokens[i].equals("*") || tokens[i].equals("/")) {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                if (tokens[i].equals("+")) {
                    stack.push(operand1 + operand2);
                }
                if (tokens[i].equals("-")) {
                    stack.push(operand1 - operand2);
                }
                if (tokens[i].equals("*")) {
                    stack.push(operand1 * operand2);
                }
                if (tokens[i].equals("/")) {
                    stack.push(operand1 / operand2);
                }
            } else {
                stack.push(Integer.parseInt(tokens[i]));
            }
            i++;
        }

        return stack.pop();
    }
}
