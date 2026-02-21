package dsa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MinimumWindowSubString {
    public String minWindowWithoutDuplicates(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        if (s.equals(t)) {
            return t;
        }
        int minWindowLength = Integer.MAX_VALUE;
        int windowStart = 0;
        int windowEnd = 0;
        int subStringStart = 0;
        int subStringEnd = 0;
        Set<Character> patternSet = new HashSet<>();
        Map<Character, Integer> textMap = new HashMap<>();

        for (int i = 0; i < t.length(); i++) {
            patternSet.add(t.charAt(i));
        }
        while (windowEnd < s.length()) {
            while (windowEnd < s.length()) {
                char ch = s.charAt(windowEnd);
                textMap.put(ch, textMap.getOrDefault(ch, 0) + 1);
                if (textMap.keySet().containsAll(patternSet)) {
                    break;
                }

                windowEnd++;
            }
            while (windowStart <= windowEnd && windowStart < s.length()) {
                char ch = s.charAt(windowStart);
                int count = textMap.get(ch);
                count--;
                if (count == 0) {
                    textMap.remove(ch);
                    if (patternSet.contains(ch)) {
                        if (windowStart <= windowEnd && windowEnd < s.length()) {


                            if (windowEnd - windowStart + 1 < minWindowLength) {
                                subStringStart = windowStart;
                                subStringEnd = windowEnd + 1;
                                minWindowLength = subStringEnd - subStringStart;
                            }
                        }
                        break;
                    }
                } else {
                    textMap.put(ch, textMap.getOrDefault(ch, 0) - 1);
                }
                windowStart++;
            }

            windowEnd++;
            windowStart++;
        }
        return s.substring(subStringStart, subStringEnd);
    }

    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        if (s.equals(t)) {
            return t;
        }
        int minWindowLength = Integer.MAX_VALUE;
        int windowStart = 0;
        int windowEnd = 0;
        int subStringStart = 0;
        int subStringEnd = 0;
        Map<Character, Integer> patternSet = new HashMap<>();
        Map<Character, Integer> textMap = new HashMap<>();

        for (int i = 0; i < t.length(); i++) {
            char ch = t.charAt(i);
            patternSet.put(ch, patternSet.getOrDefault(ch, 0) + 1);
        }
        while (windowEnd < s.length()) {
            while (windowEnd < s.length()) {
                char ch = s.charAt(windowEnd);
                textMap.put(ch, textMap.getOrDefault(ch, 0) + 1);
                boolean found = true;
                for (Map.Entry<Character, Integer> entry : patternSet.entrySet()) {
                    found = found && (textMap.getOrDefault(entry.getKey(), -1) >= entry.getValue());
                }
                if (found) {
                    break;
                }

                windowEnd++;
            }
            while (windowStart <= windowEnd && windowStart < s.length()) {
                char ch = s.charAt(windowStart);
                textMap.put(ch, textMap.getOrDefault(ch, 0) - 1);
                boolean match = false;
                for (Map.Entry<Character, Integer> entry : patternSet.entrySet()) {
                    if (textMap.getOrDefault(entry.getKey(), Integer.MIN_VALUE) < entry.getValue()) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    if (windowStart <= windowEnd && windowEnd < s.length()) {


                        if (windowEnd - windowStart + 1 < minWindowLength) {
                            subStringStart = windowStart;
                            subStringEnd = windowEnd + 1;
                            minWindowLength = subStringEnd - subStringStart;
                        }
                    }
                    break;
                }
                windowStart++;
            }

            windowEnd++;
            windowStart++;
        }
        return s.substring(subStringStart, subStringEnd);
    }
}

