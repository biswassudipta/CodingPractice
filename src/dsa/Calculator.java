package dsa;

import java.util.ArrayDeque;
import java.util.Deque;

public class Calculator {
    private static final Character OPENING_PARENTHESES = '(';
    private static final Character CLOSING_PARENTHESES = ')';
    private static final Character PLUS = '+';
    private static final Character MINUS = '-';

    public int calculate(String s) {

        Deque<Integer> stack = new ArrayDeque<>();
        Deque<Character> operatorStack = new ArrayDeque<>();
        int result = 0;
        char lastOperator = '\u0000';
        char lastOperatorInStack;
        int i = 0;

        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == PLUS || c == MINUS) {
                lastOperator = c;
                i++;

            } else if (c == OPENING_PARENTHESES) {
                stack.push(result);
                operatorStack.push(lastOperator);
                lastOperator = '\u0000';
                result = 0;
                i++;


            } else if (c == CLOSING_PARENTHESES && !stack.isEmpty()) {
                lastOperatorInStack=operatorStack.pop();
                if (lastOperatorInStack == MINUS) {
                    result =stack.pop()-result;
                } else {
                    result = stack.pop() + result;
                }

                i++;
            } else if (Character.isDigit(c)) {
                if (lastOperator == '\u0000') {
                    while (i < s.length() && Character.isDigit(s.charAt(i))) {
                        result = result * 10 + (s.charAt(i++) - '0');
                    }
                } else {
                    if (lastOperator == MINUS) {
                        int number = 0;
                        while (i < s.length() && Character.isDigit(s.charAt(i))) {
                            number = number * 10 + (s.charAt(i++) - '0');

                        }
                        result = result - number;
                        lastOperator = '\u0000';
                    } else {
                        int number = 0;
                        while (i < s.length() && Character.isDigit(s.charAt(i))) {
                            number = number * 10 + (s.charAt(i++) - '0');
                        }
                        result = result + number;
                        lastOperator = '\u0000';
                    }
                }

            } else {
                i++;

            }

        }
        return result;
    }
}
