package dsa;

import java.util.ArrayList;
import java.util.List;

public class PalindromePartitioning {
    List<List<String>> result = new ArrayList<>();

    public List<List<String>> partition(String s) {
        List<String> list = new ArrayList<>();
        partition(s, 0, list);
        return result;

    }

    private void partition(String s, int index, List<String> list) {
        if (index == s.length()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < s.length(); i++) {
            if (isPalindrome(s, index, i)) {
                list.add(s.substring(index, i + 1));
                partition(s, i + 1, list);
                list.removeLast();
            }

        }

    }

    boolean isPalindrome(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
}
