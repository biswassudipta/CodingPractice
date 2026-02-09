package dsa;

public class BinaryAddition {
    char carry = '0';

    public String addBinary(String a, String b) {
        StringBuilder res = new StringBuilder();
        if (b.length() > a.length()) {
            String temp = a;
            a = b;
            b = temp;

        }
        int j = b.length() - 1;
        for (int i = a.length() - 1; i >= 0; i--) {
            if (j >= 0) {
                res.insert(0, addDigits(a.charAt(i), b.charAt(j)));
            } else {
                res.insert(0, addDigits(a.charAt(i), '0'));
            }
            j--;
        }
        if (carry == '1') {
            res.insert(0, carry);

        }
        return res.toString();

    }

    private char addDigits(char c1, char c2) {
        if (this.carry == '0') {
            if (c1 == '1' && c2 == '1') {
                this.carry = '1';
                return '0';

            } else if (c1 == '1' || c2 == '1') {
                return '1';
            } else {
                return '0';
            }
        } else {
            if (c1 == '1' && c2 == '1') {
                return '1';

            } else if (c1 == '1' || c2 == '1') {
                return '0';
            } else {
                this.carry = '0';
                return '1';
            }
        }
    }
}
