package dsa;

public class QuickSort {
    public int lomutoPartition(int[] arr, int low, int high) {


        int pivot = arr[high];
        int i = low - 1;
        int j;
        for (j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);

        return i + 1;
    }

    public int hoarePartition(int[] arr, int low, int high) {

        int pivot = arr[low];
        int i = low - 1;
        int j = high + 1;
        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);

            do {
                j--;
            } while (arr[j] > pivot);

            if (i >= j) {
                return j;
            }
            swap(arr, i, j);
        }

    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public void quickSort(int[] arr, int l, int h, String algo) {
        if (l < h) {
            int pivotIndex;
            if (algo.equals("l")) {
                pivotIndex = lomutoPartition(arr, l, h);
                quickSort(arr, l, pivotIndex - 1, algo);
                quickSort(arr, pivotIndex + 1, h, algo);

            } else {
                pivotIndex = hoarePartition(arr, l, h);
                quickSort(arr, l, pivotIndex, algo);
                quickSort(arr, pivotIndex + 1, h, algo);
            }
        }


    }


}
