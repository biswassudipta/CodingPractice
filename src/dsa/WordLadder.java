package dsa;

import java.util.*;

public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        List<String> sources = new ArrayList<>();
        int minLen = Integer.MAX_VALUE;

        Map<String, Set<String>> adj = new HashMap<>();
        for (String word : wordList) {
            Set<String> neighbours = new HashSet<>();
            for (String neighbour : wordList) {
                if (!word.equals(neighbour) && checkMatch(word, neighbour)) {
                    neighbours.add(neighbour);
                }
            }
            adj.put(word, neighbours);

        }
        for (String source : wordList) {
            if (checkMatch(source, beginWord)) {
                sources.add(source);
            }
        }
        for (String source : sources) {
            Queue<String> queue = new ArrayDeque<>();
            Set<String> visited = new HashSet<>();
            queue.offer(source);
            int level = 1;
            while (!queue.isEmpty()) {
                level++;
                if(level == minLen){
                    break;
                }
                for (int i = queue.size(); i > 0; i--) {
                    String nextWord = queue.poll();
                    if (visited.contains(nextWord)) {
                        continue;
                    }
                    visited.add(nextWord);
                    assert nextWord != null;
                    if (endWord.equals(nextWord)) {
                        minLen = Math.min(minLen, level);
                    } else {
                        for (String neighbour : adj.get(nextWord)) {
                            if (!visited.contains(neighbour)) {
                                if (checkMatch(nextWord, neighbour)) {
                                    queue.offer(neighbour);
                                }
                            }
                        }
                    }

                }
            }

        }


        if (minLen == Integer.MAX_VALUE) {
            return 0;
        } else {
            return minLen;
        }

    }

    private boolean checkMatch(String word, String beginWord) {
        int count = 0;
        for (int i = 0; i < beginWord.length(); i++) {
            if (word.charAt(i) != beginWord.charAt(i)) {
                count++;
                if (count > 1) return false;
            }
        }
        if (count == word.length()) {
            return true;
        }
        return count == 1;
    }
}
