import dsa.*;

import static java.lang.IO.println;

static void addEdge(List<List<int[]>> adj, int u, int v, int w) {
    adj.get(u).add(new int[]{v, w});
    adj.get(v).add(new int[]{u, w});
}

static int INF = Integer.MAX_VALUE;

void main() {
    Node head = new Node(1);
    head.next = new Node(2);
    head.next.next = new Node(3);
    head.next.next.next = new Node(4);
    head.next.next.next.next = new Node(5);
    Node lastNode = ReverseLinkedList.reverseLinkedIstIteratively(head);
    Node.printList(lastNode);

    println("\n ---------------- \n");
    Node.printList(ReverseLinkedList.reverseLinkedIstIRecursively(lastNode));

    println("\n ---------------- \n");

    DoubleNode doubleHead = new DoubleNode(1);
    doubleHead.next = new DoubleNode(2);
    doubleHead.next.prev = doubleHead;
    doubleHead.next.next = new DoubleNode(3);
    doubleHead.next.next.prev = doubleHead.next;

    DoubleNode.printList(ReverseLinkedList.reverseDoublyLinkedList(doubleHead));


    println("\n ---------------- \n");


    Integer[] inputArray = {91, 87, 42, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q1 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q2 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q3 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q4 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q5 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};
    int[] q6 = {91, 14, 63, 2, 78, 25, 49, 10, 88, 36, 5, 52, 97, 19, 71, 33, 8, 44, 66, 12, 80, 21, 58, 93, 4, 30, 75, 41, 1, 56, 82, 17, 69, 3, 48, 85, 9, 37, 62, 13, 99, 24, 73, 46, 7, 54, 95, 29, 60, 20, 84, 39, 6, 50, 92, 15, 67, 31, 79, 45, 11, 89, 34, 100, 23, 70, 51, 18, 59, 90, 27, 81, 43, 65, 16, 55, 96, 32, 76, 47, 83, 38, 22, 61, 94, 28, 74, 53, 26, 68, 86, 40, 57, 98, 35, 77, 64, 72};

    List<Integer> inputList = List.of(inputArray);
    println(inputArray.length + "size");
    Heap heap = new Heap(inputList);
    heap.heapify();
    HeapPrinter.printHeapTree(heap.getHeapList());
    heap.insert(66);
    HeapPrinter.printHeapTree(heap.getHeapList());
    heap.heapify();
    HeapPrinter.printHeapTree(heap.getHeapList());
    heap.sort();
    println("\n ---------------- \n");
    heap.resetSize();
    heap.heapify();
    heap.delete();
    heap.delete();
    heap.sort();

    heap.resetSize();
    heap.heapify();
    HeapPrinter.printHeapTree(heap.getHeapList());

    println(Series.findBaselSolution());

    println("\n ---------------- \n");

    println(LambertFunction.findLambertFunctionRoot(7.0d, 1.0d));
    println(LambertFunction.findLambertFunctionRoot(BigDecimal.valueOf(7), BigDecimal.valueOf(1)));

    println("\n ---------------- \n");

    QuickSort quickSort = new QuickSort();
    println("Quick sort l \n");
    quickSort.quickSort(q1, 0, q1.length - 1, "l");
    for (int integer : q1) IO.print(integer + ",");
    println("\n ---------------- \n");
    IO.print("Quick sort h \n");
    quickSort.quickSort(q2, 0, q1.length - 1, "h");
    for (int integer : q2) IO.print(integer + ",");
    MergeSort mergeSort = new MergeSort();
    mergeSort.mergeSort(q3, 0, q3.length - 1);
    println("\n ---------------- \n");
    println("Merge sort \n");
    for (int integer : q3) IO.print(integer + ",");
    println("\n ---------------- \n");
    BasicSort basicSort = new BasicSort();
    basicSort.bubbleSort(q4);
    println("Bubble sort l \n");
    for (int integer : q4) IO.print(integer + ",");
    println("\n ---------------- \n");
    println("Insertion sort l \n");
    basicSort.insertionSort(q5);
    for (int integer : q5) IO.print(integer + ",");
    println("\n ---------------- \n");
    println("Selection sort l \n");
    basicSort.selectionSort(q6);
    for (int integer : q6) IO.print(integer + ",");
    println("\n --------Huffman coding testing-------- \n");

    String text = "This is the text to be encoded";
    HuffmanCode huffmanCode = new HuffmanCodeImpl();
    String encodedText = huffmanCode.encode(text);
    println("Encoded Text " + encodedText);
    println("Decoded Text " + huffmanCode.decode(encodedText));


    Graph graph = new Graph();

    int V = 5;
    int src = 0;

    List<List<int[]>> adj = new ArrayList<>();
    for (int i = 0; i < V; i++) {
        adj.add(new ArrayList<>());
    }

    addEdge(adj, 0, 1, 4);
    addEdge(adj, 0, 2, 8);
    addEdge(adj, 1, 4, 6);
    addEdge(adj, 1, 2, 3);
    addEdge(adj, 2, 3, 2);
    addEdge(adj, 3, 4, 10);

    List<Integer> result = graph.findShortestPathDijkstra(src, V, adj);
    for (int d : result)
        System.out.print(d + " ");
    System.out.println();

    println(graph.findMSTCostPrims(V, adj));
    println(graph.findMSTCostKruskal(V, adj));


    int[][] multiStageGraph = new int[][]{
            {INF, 1, 2, 5, INF, INF, INF, INF},
            {INF, INF, INF, INF, 4, 11, INF, INF},
            {INF, INF, INF, INF, 9, 5, 16, INF},
            {INF, INF, INF, INF, INF, INF, 2, INF},
            {INF, INF, INF, INF, INF, INF, INF, 18},
            {INF, INF, INF, INF, INF, INF, INF, 13},
            {INF, INF, INF, INF, INF, INF, INF, 2},
            {INF, INF, INF, INF, INF, INF, INF, INF}
    };
    MultiStageGraphShortestPath multiStageGraphShortestPath = new MultiStageGraphShortestPath();
    println(multiStageGraphShortestPath.findShortestPath(multiStageGraph));
}



