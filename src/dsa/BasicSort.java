package dsa;

public class BasicSort {
    public void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean isSorted = false;

        while (!isSorted) {
            isSorted = true;
            for (int j = 1; j < n; j++) {
                int i = j - 1;
                if (arr[i] > arr[j]) {
                    swap(arr, i, j);
                    isSorted = false;
                }
            }
        }
    }

    public void insertionSort(int[] arr) {
        int n = arr.length;
        for (int j = 1; j < n; j++) {
            for (int i = j; i > 0; i--) {
                if (arr[i] < arr[i - 1]) {
                    swap(arr, i, i - 1);
                }
            }

        }

    }

    public void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int j = i + 1;
            int minIndex = findMin(arr, j, n);
            if (arr[i] > arr[minIndex]) swap(arr, i, minIndex);

        }

    }

    private int findMin(int[] arr, int i, int n) {
        int res = Integer.MAX_VALUE;
        int min_index = 0;
        while (i < n) {
            if (res > arr[i]) {
                res = arr[i];
                min_index = i;
            }
            i++;
        }
        return min_index;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
