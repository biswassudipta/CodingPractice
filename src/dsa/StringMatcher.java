package dsa;

import java.util.ArrayList;
import java.util.List;

public class StringMatcher {
    private static final int BASE = 5631;
    private static final int DATASET_SIZE = 256;


    public List<Integer> findMatchesKnuthMorris(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (text == null || pattern == null) return result;
        if (pattern.isEmpty() || pattern.length() > text.length()) return result;
        int[] lps = lps(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    result.add(i - j);
                    j = lps[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return result;
    }

    private int[] lps(String pattern) {
        int n = pattern.length();
        int[] lps = new int[n];
        int j = 0;
        int i = 1;
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
                lps[i] = j;
                i++;
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public List<Integer> findMatchesRabinKarp(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (text == null || pattern == null) return result;
        int textLen = text.length();
        int patternLen = pattern.length();
        if (textLen < patternLen) return result;

        int p = 0;
        int t = 0;
        int h = 1;

        for (int i = 0; i < patternLen - 1; i++)
            h = (h * DATASET_SIZE) % BASE;

        for (int i = 0; i < patternLen; i++) {
            p = Math.floorMod(DATASET_SIZE * p + pattern.charAt(i), BASE);
            t = Math.floorMod(DATASET_SIZE * t + text.charAt(i), BASE);
        }

        for (int i = 0; i <= textLen - patternLen; i++) {
            if (p == t) {
                int j;
                for (j = 0; j < patternLen; j++) {
                    if (text.charAt(j + i) != pattern.charAt(j)) break;
                }
                if (j == patternLen) {
                    result.add(i);
                }
            }
            if (i < textLen - patternLen) {
                t = Math.floorMod(DATASET_SIZE * (t - (text.charAt(i) * h)) + text.charAt(i + patternLen), BASE);
            }
        }
        return result;
    }
}
