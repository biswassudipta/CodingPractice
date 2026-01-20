package dsa;

import java.awt.*;
import java.util.List;

public class HeapPrinter {

    /**
     * Prints an array representation of a heap in a visual tree format to the console.
     *
     * @param input The integer List representing the heap (0-indexed).
     */
    public static void printHeapTree(List<Integer> input) {

        int[] heap = new int[input.size()];
        for (int i = 0; i < input.size(); i++) {
            heap[i] = input.get(i);
        }
        int n = heap.length;
        if (n == 0) {
            System.out.println("Heap is empty.");
            return;
        }

        // Calculate the height of the heap
        int height = (int) (Math.log(n) / Math.log(2)) + 1;

        // Determine the maximum width needed for formatting
        // This is a simple estimation and can be improved for better alignment
        int maxWidth = (int) (Math.pow(2, height) * 2);

        int currentLevel = -1;
        for (int i = 0; i < n; i++) {
            // Check if a new level begins
            // The first node of level 'l' is at index 2^l - 1 (approximately, or just check log2(i+1))
            if ((int) (Math.log(i + 1) / Math.log(2)) > currentLevel) {
                currentLevel++;
                System.out.println(); // Start a new line for a new level
                // Add initial spacing for the new level
                printSpaces(maxWidth / (int) Math.pow(2, currentLevel + 1));
            }

            // Print the element with spacing
            System.out.print(heap[i]);
            // Add spacing between elements on the same level
            printSpaces(maxWidth / (int) Math.pow(2, currentLevel));
        }
        System.out.println();
        IO.println("\n ---------------- \n");
    }

    private static void printSpaces(int count) {
        for (int k = 0; k < count; k++) {
            System.out.print(" ");
        }
    }
}