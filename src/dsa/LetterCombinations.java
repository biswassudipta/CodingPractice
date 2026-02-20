package dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinations {
    public List<String> letterCombinations(String digits) {
        Map<Character, List<String>> map = new HashMap();
        List<String> result = new ArrayList<>();
        map.put('2', List.of("a", "b", "c"));
        map.put('3', List.of("d", "e", "f"));
        map.put('4', List.of("g", "h", "i"));
        map.put('5', List.of("j", "k", "l"));
        map.put('6', List.of("m", "n", "o"));
        map.put('7', List.of("p", "q", "r", "s"));
        map.put('8', List.of("t", "u", "v"));
        map.put('9', List.of("w", "x", "y", "z"));
        generate(0, digits, map, result, "");
        return result;
    }

    private void generate(int index, String s, Map<Character, List<String>> map, List<String> result,
                          String subResult) {
        if (index == s.length()) {
            result.add(new String(subResult.toCharArray()));
            return;
        }

        List<String> alphabets = map.get(s.charAt(index));
        for (String alphabet : alphabets) {
            subResult = subResult + alphabet;
            generate(index + 1, s, map, result, subResult);
            subResult=subResult.substring(0, subResult.length() - 1);
        }
    }
}
