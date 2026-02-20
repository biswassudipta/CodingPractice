package dsa;

import java.util.*;

public class WordSearch {
    public boolean existBFS(char[][] board, String word) {
        List<int[]> list = new ArrayList<>();
        boolean result = false;
        char start = word.charAt(0);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == start) {
                    list.add(new int[]{i, j, 0});
                }
            }
        }
        if (list.isEmpty()) {
            return false;
        }
        if (list.size() == 1 & word.length() == 1) {
            return true;
        }

        for (int[] sp : list) {
            if (!result) {

                Queue<Node> queue = new ArrayDeque<>();
                Node node = new Node(0, sp[0], sp[1], new HashSet<>());
                queue.offer(node);
                int[][] dirs = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

                while (!queue.isEmpty()) {
                    Node neighbor = queue.poll();
                    int i = neighbor.row;
                    int j = neighbor.col;
                    int letterIndex = neighbor.index;
                    Set<Integer> precedingElements = neighbor.precedingElements;
                    int rowMajorAddress = i * board[0].length + j;
                    if (letterIndex == word.length()) {
                        result = true;
                        break;
                    }
                    if (i < 0 || j < 0 || i > board.length - 1 || j > board[0].length - 1 || precedingElements.contains(rowMajorAddress)) {
                        continue;
                    }


                    if (word.charAt(letterIndex) == board[i][j]) {
                        precedingElements.add(rowMajorAddress);
                        for (int[] dir : dirs) {
                            int newIndex = 1 + letterIndex;
                            int newR = i + dir[0];
                            int newC = j + dir[1];
                            //int newNowMajorAddress=newR*board[0].length+newC;
                            Set<Integer> newPrecedingElements = new HashSet<>(precedingElements);
                            queue.offer(new Node(newIndex, newR, newC, newPrecedingElements));
                        }

                    }
                }
            }
        }
        return result;
    }

    private static class Node {
        int index;
        int row;
        int col;
        Set<Integer> precedingElements;

        public Node(int index, int row, int col, Set<Integer> precedingElements) {
            this.index = index;
            this.row = row;
            this.col = col;
            this.precedingElements = precedingElements;
        }
    }
    public boolean existDFS(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0) && dfs(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int index) {
        if (index == word.length()) return true;

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c] != word.charAt(index)) {
            return false;
        }

        char temp = board[r][c];
        board[r][c] = '#';

        boolean found = dfs(board, word, r + 1, c, index + 1) ||
                dfs(board, word, r - 1, c, index + 1) ||
                dfs(board, word, r, c + 1, index + 1) ||
                dfs(board, word, r, c - 1, index + 1);

        board[r][c] = temp;
        return found;
    }
}
