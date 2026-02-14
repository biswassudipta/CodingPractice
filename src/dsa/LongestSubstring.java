package dsa;



public class LongestSubstring {
    int max = 0;

    public int lengthOfLongestSubstringUsingBackTracking(String s) {
        boolean[] visited = new boolean[256];
        findSubstrings(s, 0, 0, visited, "");
        return max;

    }

    private void findSubstrings(String s, int index, int len, boolean[] visited, String subString) {
        max = Math.max(max, len);
        if (index == s.length()) {
            return;
        }

        for (int i = index; i < s.length(); i++) {
            if (visited[s.charAt(i)] || (i > 0 && !subString.isEmpty() && subString.charAt(subString.length() - 1) != s.charAt(i - 1))) {
                break;
            }
            visited[s.charAt(i)] = true;
            subString = subString + s.charAt(i);
            findSubstrings(s, i + 1, len + 1, visited, subString);
            subString = subString.substring(0, subString.length() - 1);
            visited[s.charAt(i)] = false;


        }
    }

    public int lengthOfLongestSubstring(String s) {

        int maxVal = 0;
        for (int i = 0; i < s.length(); i++) {
            boolean[] visited = new boolean[256];
            int len = 0;
            for (int j = i; j < s.length(); j++) {

                if (!visited[s.charAt(j)]) {
                    len++;
                    visited[s.charAt(j)] = true;
                } else {
                    maxVal = Math.max(maxVal, len);
                    break;
                }
            }
            maxVal = Math.max(maxVal, len);
        }
        return maxVal;
    }
    public int lengthOfLongestSubstringSlidingWindow(String s) {
        boolean[] visited = new boolean[256];
        int maxVal = 0;
        int left = 0;
        int right = 0;
        while (right < s.length()) {
                while ( visited[s.charAt(right)]) {
                    visited[s.charAt(left)] = false;
                    left++;
                }



                visited[s.charAt(right)]=true;
                maxVal = Math.max(maxVal, right - left + 1);
                right++;
        }
        return maxVal;
    }
}
