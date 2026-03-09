package dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAnagrams {
    static final int MAX_CHAR = 256;

    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            map.computeIfAbsent(GroupAnagrams.sortString(s), _ -> new ArrayList<>()).add(s);

        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    private static String sortString(String s) {
        StringBuilder builder = new StringBuilder();
        int[] charCount = new int[MAX_CHAR];

        for (int i = 0; i < s.length(); i++) {
            charCount[s.charAt(i)]++;
        }

        for (int i = 0; i < MAX_CHAR; i++) {
            builder.append(String.valueOf((char) i).repeat(Math.max(0, charCount[i])));
        }
        return builder.toString();
    }
}
