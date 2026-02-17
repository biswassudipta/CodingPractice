package dsa;

import java.util.*;

public class AccountMerging {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, String> parents = new HashMap<>();
        Map<String, String> owner = new HashMap<>();
        Map<String, TreeSet<String>> unions = new HashMap<>();

        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                if (!parents.containsKey(email)) {
                    parents.put(email, email);
                    owner.put(email, name);
                }

                if (i > 1) {
                    union(account.get(1), email, parents);
                }
            }
        }

        for (List<String> account : accounts) {
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                String rootParent = find(email, parents);

                unions.putIfAbsent(rootParent, new TreeSet<>());
                unions.get(rootParent).add(email);
            }
        }

        List<List<String>> result = new ArrayList<>();
        for (String rootEmail : unions.keySet()) {
            List<String> emails = new ArrayList<>(unions.get(rootEmail));
            String name = owner.get(rootEmail);
            emails.addFirst(name);
            result.add(emails);
        }

        result.sort(Comparator.comparing(a -> a.get(0)));

        return result;
    }

    private String find(String s, Map<String, String> parents) {
        if (!parents.get(s).equals(s)) {
            parents.put(s, find(parents.get(s), parents));
        }
        return parents.get(s);
    }

    private void union(String s1, String s2, Map<String, String> parents) {
        String p1 = find(s1, parents);
        String p2 = find(s2, parents);
        if (!p1.equals(p2)) {
            parents.put(p1, p2);
        }
    }


}
