import dsa.*;

import static dsa.NodeWithRandomPointer.printListWithRandomPointer;
import static java.lang.IO.println;

static void addEdge(List<List<int[]>> adj, int u, int v, int w) {
    adj.get(u).add(new int[]{v, w});
    adj.get(v).add(new int[]{u, w});
}

public static int[][] generateTSPInput(int n) {
    int[][] dist = new int[n][n];
    Random random = new Random();

    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            // Random distance between 10 and 1000
            int distance = random.nextInt(991) + 10;

            // Set both [i][j] and [j][i] because distance is usually symmetric
            dist[i][j] = distance;
            dist[j][i] = distance;
        }
        // dist[i][i] is always 0 (distance to self)
        dist[i][i] = 0;
    }
    return dist;
}

static int INF = Integer.MAX_VALUE;

void main() {
    Node head = new Node(1);
    head.next = new Node(2);
    head.next.next = new Node(3);
    head.next.next.next = new Node(4);
    head.next.next.next.next = new Node(5);
    Node lastNode = LinkedList.reverseLinkedIstIteratively(head);
    Node.printList(lastNode);

    println("\n ---------------- \n");
    Node.printList(LinkedList.reverseLinkedIstIRecursively(lastNode));

    println("\n ---------------- \n");

    DoubleNode doubleHead = new DoubleNode(1);
    doubleHead.next = new DoubleNode(2);
    doubleHead.next.prev = doubleHead;
    doubleHead.next.next = new DoubleNode(3);
    doubleHead.next.next.prev = doubleHead.next;

    DoubleNode.printList(LinkedList.reverseDoublyLinkedList(doubleHead));


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
        IO.print(d + " ");
    println();

    println(graph.findMSTCostPrims(V, adj));
    println(graph.findMSTCostKruskal(V, adj));


    int[][] multiStageGraph = new int[][]{{0, 1, 2, 5, INF, INF, INF, INF},
            {INF, 0, INF, INF, 4, 11, INF, INF},
            {INF, INF, 0, INF, 9, 5, 16, INF},
            {INF, INF, INF, 0, INF, INF, 2, INF},
            {INF, INF, INF, INF, 0, INF, INF, 18},
            {INF, INF, INF, INF, INF, 0, INF, 13},
            {INF, INF, INF, INF, INF, INF, 0, 2},
            {INF, INF, INF, INF, INF, INF, INF, 0}};
    MultiStageGraphShortestPath multiStageGraphShortestPath = new MultiStageGraphShortestPath();
    println(multiStageGraphShortestPath.findShortestPath(multiStageGraph));

    int[][] floydWarshallGraph = {{0, 4, INF, 5, INF},
            {INF, 0, 1, INF, 6},
            {2, INF, 0, 3, INF},
            {INF, INF, 1, 0, 2},
            {1, INF, INF, 4, 0}};
    FloydWarshallShortestPath floydWarshallShortestPath = new FloydWarshallShortestPath();
    int[][] dist = floydWarshallShortestPath.findShortestPathPairs(floydWarshallGraph);
    for (int[] ints : dist) {
        for (int j = 0; j < dist.length; j++) {
            IO.print(ints[j] + " ");
        }
        println();
    }

    NodeWithRandomPointer headNodeWithRandomPointer = new NodeWithRandomPointer(1);
    headNodeWithRandomPointer.next = new NodeWithRandomPointer(2);
    headNodeWithRandomPointer.next.next = new NodeWithRandomPointer(3);
    headNodeWithRandomPointer.next.next.next = new NodeWithRandomPointer(4);
    headNodeWithRandomPointer.next.next.next.next = new NodeWithRandomPointer(5);
    headNodeWithRandomPointer.random = headNodeWithRandomPointer.next.next;
    headNodeWithRandomPointer.next.random = headNodeWithRandomPointer;
    headNodeWithRandomPointer.next.next.random = headNodeWithRandomPointer.next.next.next.next;
    headNodeWithRandomPointer.next.next.next.random = headNodeWithRandomPointer.next.next;
    headNodeWithRandomPointer.next.next.next.next.random = headNodeWithRandomPointer.next;

    // Print the original list
    println("Original linked list:");
    printListWithRandomPointer(headNodeWithRandomPointer);

    NodeWithRandomPointer clonedList = LinkedList.clone(headNodeWithRandomPointer);

    println("Cloned linked list:");
    printListWithRandomPointer(clonedList);

    MatrixChainMultiplication matrixChainMultiplication = new MatrixChainMultiplication();
    int[] arr = new int[]{5, 4, 6, 2, 7};
    int res = matrixChainMultiplication.findMinimumMultiplication(arr);
    println("Minimum no of multiplication" + res + "\n");
    println("\n ---------------- \n");


    // Edge list representation: {source, destination, weight}
    int[][] edges = new int[][]{{1, 3, 2},
            {4, 3, -1},
            {2, 4, 1},
            {1, 2, 1},
            {0, 1, 5}};

    // Source vertex for Bellman-Ford algorithm
    int source = 0;
    BellmanFordSingleSourceShortestPath bellmanFordSingleSourceShortestPath = new BellmanFordSingleSourceShortestPath();
    // Run Bellman-Ford algorithm from the source vertex

    int vertexNo = 5;
    for (int distance : bellmanFordSingleSourceShortestPath.findBellmanFordSingleSourceShortestPaths(vertexNo, edges, source))
        System.out.print(distance + " ");

    println("\n ---------------- \n");

    ZeroOneKnapsack zeroOneKnapsack = new ZeroOneKnapsack();
    int[] val = {1, 2, 5, 6};
    int[] wt = {2, 3, 4, 5};
    int W = 8;
    int maxProfit = zeroOneKnapsack.findMaxProfit(val, wt, W);
    println("Maximum profit: " + maxProfit);
    println("\n ---------------- \n");

    TravellingSalesman travellingSalesman = new TravellingSalesman();

    println("Minimum distance: " + travellingSalesman.findMinimumDistance(0, generateTSPInput(15)));
    println("\n ---------------- \n");

    ListNode lst11 = new ListNode(1);
    ListNode lst12 = new ListNode(2);
    ListNode lst13 = new ListNode(4);
    lst11.next = lst12;
    lst11.next.next = lst13;
    ListNode lst21 = new ListNode(1);
    ListNode lst22 = new ListNode(2);
    ListNode lst23 = new ListNode(3);
    lst21.next = lst22;
    lst21.next.next = lst23;

    MergeLinkedLists mergeLinkedLists = new MergeLinkedLists();
    ListNode.printList(mergeLinkedLists.mergeTwoLists(lst11, lst21));
    println("\n ---------------- \n");

    ClimbingStairs climbingStairs = new ClimbingStairs();
    int noOfStairCas = 9;
    climbingStairs.climbStairsUsingDP(noOfStairCas);
    climbingStairs.climbStairsUsingCombinations(noOfStairCas);
    climbingStairs.climbStairsUsingBruteForce(noOfStairCas);
    println("\n ---------------- \n");

    BinaryAddition binaryAddition = new BinaryAddition();
    String sum = binaryAddition.addBinary("11", "1");
    println("sum of binary numbers: " + sum);
    println("\n ---------------- \n");


    List<Integer> path1 = Arrays.asList(0, 1);
    List<Integer> path2 = Arrays.asList(1, 2);
    List<Integer> path3 = Arrays.asList(2, 0);
    List<Integer> path4 = Arrays.asList(1, 3);

    List<List<Integer>> paths = Arrays.asList(path1, path2, path3, path4);

    CriticalPathTarjanAlgo criticalPathTarjanAlgo = new CriticalPathTarjanAlgo();
    List<List<Integer>> criticalPaths = criticalPathTarjanAlgo.criticalConnections(4, paths);
    for (List<Integer> path : criticalPaths) {
        println("\n This is critical path from vertex: " + path.get(0) + " to vertex: " + path.get(1) + " \n");
    }
    SubsetBackTracking subsetBackTracking = new SubsetBackTracking();

    List<List<Integer>> subsets = subsetBackTracking.subsets(new int[]{1, 2, 2});
    println("all subsets: \n" + subsets);
    println("\n ---------------- \n");
    List<List<Integer>> subsetsWithDuplicates = subsetBackTracking.subsetsWithDuplicates(new int[]{1, 2, 1, 2});
    println("all subsets: \n" + subsetsWithDuplicates);
    println("\n ---------------- \n");
    PermutationBackTracking permutationBackTracking = new PermutationBackTracking();
    List<List<Integer>> permutations = permutationBackTracking.permute(new int[]{1, 2, 3});
    println("all permutations: \n" + permutations);
    println("\n ---------------- \n");
    List<List<Integer>> uniquePermutations = permutationBackTracking.permuteUnique(new int[]{1, 2, 2});
    println("all unique permutations: \n" + uniquePermutations);
    println("\n ---------------- \n");
    CombinationBackTracking combinationBackTracking = new CombinationBackTracking();
    List<List<Integer>> combinationSum1 = combinationBackTracking.combinationSum(new int[]{2, 3, 6, 7}, 7);
    println("all combinations selecting one  element more than once: \n" + combinationSum1);
    println("\n ---------------- \n");

    List<List<Integer>> combinationSum2 = combinationBackTracking.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
    println("all combinations selecting one element only once: \n" + combinationSum2);
    int k = 3;
    int n = 7;
    List<List<Integer>> combinationSum3 = combinationBackTracking.combinationSum3(k, n);
    println("all combinations selecting " + k + " element/elements only once form 1-9 totalling sum: " + n + " \n" + combinationSum3);
    println("\n ---------------- \n");


    int combinaionSum4 = combinationBackTracking.combinationSum4(new int[]{1, 2, 3}, 4);
    println("total combinations \n" + combinaionSum4);
    println("\n ---------------- \n");

    PalindromePartitioning palidromePartioning = new PalindromePartitioning();
    List<List<String>> setOfAllPalindromes = palidromePartioning.partition("aab");
    println("total palindromes \n" + setOfAllPalindromes);
    println("\n ---------------- \n");

    int[][] intervals = new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10},{12,16}};
    int [] newInterval=new int[]{4,8};
    InsertInterval insertInterval = new InsertInterval();
    int[][] newIntervals=insertInterval.insert(intervals, newInterval);
    println("new set of intervals \n" + Arrays.deepToString(newIntervals));
    println("\n ---------------- \n");

    String txt = "aabaacaadaabaaba";
    String pat = "aaba";
    StringMatcher matcher = new StringMatcher();
    List<Integer> matchesKMP=matcher.findMatchesKnuthMorris(txt, pat);

    List<Integer> matchesRK=matcher.findMatchesRabinKarp(txt, pat);

    println("match for pattern over text using Knuth-Morris starts at this/these index/indices..:"+matchesKMP+"\n");
    println("\n ---------------- \n");
    println("match for pattern over text using Rabin-Karp starts at this/these index/indices..:"+matchesRK+"\n");
    println("\n ---------------- \n");

    AVLTreeNode  root= new AVLTreeNode(10);
    AVLTreeImplementation avlTreeImplementation = new AVLTreeImplementation();


    root = avlTreeImplementation.insert(root, 10);
    TreePrinter.print(root);
    println("\n ---------------- \n");
    root = avlTreeImplementation.insert(root, 20);
    TreePrinter.print(root);
    println("\n ---------------- \n");
    root = avlTreeImplementation.insert(root, 30);
    TreePrinter.print(root);
    println("\n ---------------- \n");
    root = avlTreeImplementation.insert(root, 40);
    TreePrinter.print(root);
    println("\n ---------------- \n");
    root = avlTreeImplementation.insert(root, 50);
    TreePrinter.print(root);
    println("\n ---------------- \n");
    root = avlTreeImplementation.insert(root, 25);
    TreePrinter.print(root);
    println("\n ---------------- \n");



}



