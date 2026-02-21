package dsa;

import java.util.*;

public class MergeKSortedLists {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        List<ListNode> allLists= new ArrayList<>(Arrays.asList(lists));
        List<Integer> lengths=new ArrayList<>();

        for (int i = 0; i < lists.length; i++) {
            ListNode curr = lists[i];
            int count = 0;
            while (curr != null) {
                curr = curr.next;
                count++;
            }
            lengths.add(count);
            pq.offer(new int[]{i, count});
        }
            while (pq.size() >1) {
                int[] p1 = pq.poll();
                int[] p2 = pq.poll();
                ListNode l1 = allLists.get(p1[0]);
                assert p2 != null;
                ListNode l2 = allLists.get(p2[0]);
                ListNode merged=mergeTwoLists(l1,l2);
                allLists.add(merged);
                int lenAfterMerging=lengths.get(p1[0])+lengths.get(p2[0]);
                lengths.add(allLists.size()-1,lenAfterMerging);
                pq.offer(new int[]{allLists.size()-1, lenAfterMerging});
            }
        return allLists.get(Objects.requireNonNull(pq.poll())[0]);
    }

    private ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                current.next = list1;
                list1 = list1.next;

            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        if (list1 != null) {
            current.next = list1;
        }
        if (list2 != null) {
            current.next = list2;
        }
        return dummy.next;
    }
}
