package dsa;

public class ATOI {
    private boolean isNegative = false;
    private boolean isSignOperatorAssigned = false;
    private static final int SPACE =32;
    private static final int PLUS_OPERATOR =43;
    private static final int MINUS_OPERATOR =45;
    private static final int ZER0 =48;
    private static final int NINE =57;

    public int myAtoi(String s) {
        char[] chars = s.toCharArray();
        String digit = "";
        for (int i = 0; i < s.length(); i++) {
            if (isDigit(chars[i])) {
                digit += String.valueOf(chars[i]);
            } else if (!isSignOperator(chars[i], digit) && !isLeadingSpace(chars[i], digit)) {
                break;
            }
        }
        if (digit.isEmpty()) {
            return 0;
        } else {
            int result;
            try {
                result = Integer.parseInt(digit);
                if (isNegative) {
                    return -1 * result;
                } else {
                    return result;
                }
            } catch (NumberFormatException e) {
                if (isNegative) {
                    return Integer.MIN_VALUE;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        }
    }

    private boolean isDigit(char i) {
        return i >= ZER0 && i <= NINE;
    }

    private boolean isSignOperator(char i, String digit) {
        if (i == MINUS_OPERATOR && digit.isEmpty() && !isSignOperatorAssigned) {
            isNegative = true;
            isSignOperatorAssigned = true;
            return true;
        }
        if (i == PLUS_OPERATOR && digit.isEmpty() && !isSignOperatorAssigned) {
            isSignOperatorAssigned = true;
            return true;
        }
        return false;
    }

    private boolean isLeadingSpace(char i, String digit) {
        return i == SPACE && digit.isEmpty() && !isSignOperatorAssigned;
    }
}
