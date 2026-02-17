package dsa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak {
    private boolean result;
    private Set<String> set;

    public boolean wordBreakDP(String s, List<String> wordDict) {
        int len = s.length();
        Set<String> dict = new HashSet<>();
        int maxLen = 0;
        for (String word : wordDict) {
            dict.add(word);
            maxLen = Math.max(maxLen, word.length());
        }
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 1; i <= len; i++) {
            dp[i] = dict.contains(s.substring(0, i));
            int j = i - 1;
            while (!dp[i] && i - j <= maxLen && j > 0) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
                j--;
            }

        }
        return dp[len];

    }

    public boolean wordBreakBT(String s, List<String> wordDict) {
        int len = s.length();
        this.set = new HashSet<>(wordDict);
        generate(s, 0, len, "");
        return result;
    }

    private void generate(String s, int index, int len, String word) {
        if ((index == len) && (set.contains(word))) {
            result = true;
            return;

        }
        if (index >= len) {
            return;
        }
        for (int i = index; i < s.length(); i++) {

            word = (i - index) > 0 ? s.substring(index, i + 1) : String.valueOf(s.charAt(i));
            if (set.contains(word)) {
                generate(s, i + 1, len, word);
            }

        }

    }
}
