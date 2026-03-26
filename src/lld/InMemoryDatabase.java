package lld;

import java.util.*;

/**
 * CodeSignal ICA — In-Memory Database (Levels 1–4)
 * <p>
 * Structure:
 * db[key][field] = FieldEntry { value, expireAt }
 * expireAt == -1  →  no TTL (lives forever)
 * expireAt == t   →  alive during [setTimestamp, t), dead at t
 * <p>
 * Refactoring: all scan/get/delete variants share a single private helper.
 * scanInternal(key, prefix, timestamp)   → null means "no filter"
 * getInternal(key, field, timestamp)     → null means "no TTL check"
 * deleteInternal(key, field, timestamp)  → null means "no TTL check"
 */
public class InMemoryDatabase {

    // =========================================================================
    //  INNER CLASS
    // =========================================================================

    private static class FieldEntry {
        String value;
        int expireAt;   // -1 = no expiry

        FieldEntry(String value, int expireAt) {
            this.value = value;
            this.expireAt = expireAt;
        }

        boolean isAlive(int timestamp) {
            return expireAt == -1 || timestamp < expireAt;
        }
    }

    // =========================================================================
    //  STATE
    // =========================================================================

    private final Map<String, Map<String, FieldEntry>> db = new HashMap<>();
    private final List<Integer> backupTimestamps = new ArrayList<>();
    private final List<Map<String, Map<String, FieldEntry>>> backupSnapshots = new ArrayList<>();

    // =========================================================================
    //  PRIVATE HELPERS
    // =========================================================================

    private Map<String, FieldEntry> getOrCreate(String key) {
        return db.computeIfAbsent(key, k -> new HashMap<>());
    }

    /**
     * Core get — shared by get() and getAt().
     * timestamp == null → skip TTL check (Level 1/2 behaviour)
     * timestamp != null → only return entry if alive at timestamp
     */
    private String getInternal(String key, String field, Integer timestamp) {
        Map<String, FieldEntry> fields = db.get(key);
        if (fields == null) {
            return null;
        }
        FieldEntry entry = fields.get(field);
        if (entry == null) {
            return null;
        }
        if (timestamp != null && !entry.isAlive(timestamp)) {
            return null;
        }

        return entry.value;

    }

    /**
     * Core delete — shared by delete() and deleteAt().
     * timestamp == null → delete unconditionally (Level 1/2 behaviour)
     * timestamp != null → only delete if entry is alive at timestamp
     */
    private boolean deleteInternal(String key, String field, Integer timestamp) {
        if (key == null || field == null || !db.containsKey(key)) {
            return false;
        }
        Map<String, FieldEntry> fields = db.get(key);
        if (fields == null) {
            return false;
        }
        FieldEntry entry = fields.get(field);
        if (entry == null) {
            return false;
        }
        if (timestamp != null && !entry.isAlive(timestamp)) {
            return false;
        }
        fields.remove(field);
        if (fields.isEmpty()) {
            db.remove(key);
        }
        return true;
    }

    /**
     * Core scan — shared by scan(), scanByPrefix(), scanAt(), scanByPrefixAt().
     * prefix    == null → include all fields
     * timestamp == null → skip TTL check
     */
    private List<String> scanInternal(String key, String prefix, Integer timestamp) {
        List<String> result = new ArrayList<>();
        Map<String, FieldEntry> fields = db.get(key);
        if (fields == null) {
            return result;
        }
        for (Map.Entry<String, FieldEntry> entry : fields.entrySet()) {
            String field = entry.getKey();
            FieldEntry value = entry.getValue();
            if (timestamp != null && !value.isAlive(timestamp)) continue;
            if (prefix != null && !field.startsWith(prefix)) continue;
            result.add(field + "(" + value.value + ")");
        }
        Collections.sort(result);


        return result;
    }

    /**
     * Deep-copies the current db into a new snapshot map.
     */
    private Map<String, Map<String, FieldEntry>> snapshot() {
        if(db.isEmpty()) return new HashMap<>();
        Map<String, Map<String, FieldEntry>> outer = new HashMap<>();

       for(Map.Entry<String, Map<String, FieldEntry>> entry:db.entrySet()) {
           Map<String,FieldEntry> inner = new HashMap<>();
           for(Map.Entry<String, FieldEntry> e:entry.getValue().entrySet()) {
               String key = e.getKey();
               FieldEntry value = new FieldEntry(e.getValue().value, e.getValue().expireAt);
               inner.put(key,value);

           }
           String outerKey = entry.getKey();
           outer.put(outerKey,inner);


       }
       return outer;
    }

    /**
     * Deep-copies a snapshot map back into db.
     */
    private void restoreFrom(Map<String, Map<String, FieldEntry>> snap) {
        db.clear();
        for (Map.Entry<String, Map<String, FieldEntry>> entry:snap.entrySet()) {
            String key = entry.getKey();
            Map<String, FieldEntry> inner =new HashMap<>();
            for(Map.Entry<String, FieldEntry> e:entry.getValue().entrySet()) {
                String field = e.getKey();
                FieldEntry value = new FieldEntry(e.getValue().value, e.getValue().expireAt);
                inner.put(field,value);
            }
            db.put(key,inner);

        }
    }

    // =========================================================================
    //  LEVEL 1 — Basic Operations
    // =========================================================================

    public void set(String key, String field, String value) {
        getOrCreate(key).put(field, new FieldEntry(value, -1));
    }

    public String get(String key, String field) {

        return getInternal(key, field, null);
    }

    public boolean delete(String key, String field) {

        return deleteInternal(key, field, null);
    }

    public List<String> scan(String key) {
        return scanInternal(key, null, null);
    }

    // =========================================================================
    //  LEVEL 2 — Scan By Prefix
    // =========================================================================

    public List<String> scanByPrefix(String key, String prefix) {
        return scanInternal(key, prefix, null);
    }

    // =========================================================================
    //  LEVEL 3 — TTL Support
    // =========================================================================

    public void setAt(String key, String field, String value, int timestamp) {
        getOrCreate(key).put(field, new FieldEntry(value, -1));
    }

    public void setAtWithTtl(String key, String field, String value, int timestamp, int ttl) {
        getOrCreate(key).put(field, new FieldEntry(value, timestamp + ttl));
    }

    public String getAt(String key, String field, int timestamp) {
        return getInternal(key, field, timestamp);
    }

    public boolean deleteAt(String key, String field, int timestamp) {
        return deleteInternal(key, field, timestamp);
    }

    public List<String> scanAt(String key, int timestamp) {
        return scanInternal(key, null, timestamp);
    }

    public List<String> scanByPrefixAt(String key, String prefix, int timestamp) {
        return scanInternal(key, prefix, timestamp);
    }

    // =========================================================================
    //  LEVEL 4 — Backup & Restore
    // =========================================================================

    public void backup(int timestamp) {
        Map<String, Map<String, FieldEntry>> snapshot= snapshot();
        for(int i=0;i<backupTimestamps.size();i++) {
            if(timestamp == backupTimestamps.get(i)) {
                backupSnapshots.set(i,snapshot);
                backupTimestamps.set(i,timestamp);
            }
        }
        backupTimestamps.add(timestamp);
        backupSnapshots.add(snapshot);

    }

    public void restore(int timestamp) {
       int bestIdx=-1;
       int bestTimestamp=Integer.MIN_VALUE;
       for(int i=0;i<backupTimestamps.size();i++) {
           int ts = backupTimestamps.get(i);
           if(timestamp>=ts && timestamp>bestTimestamp) {
                bestIdx = i;
                bestTimestamp = ts;
           }
       }
       if(bestIdx!=-1) {
           restoreFrom(backupSnapshots.get(bestIdx));
       }

    }

    // =========================================================================
    //  TEST HARNESS
    // =========================================================================

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, Object actual, Object expected) {
        if (Objects.equals(actual, expected)) {
            System.out.printf("  [PASS] %s%n", name);
            passed++;
        } else {
            System.out.printf("  [FAIL] %s%n", name);
            System.out.printf("         expected : %s%n", expected);
            System.out.printf("         actual   : %s%n", actual);
            failed++;
        }
    }

    // =========================================================================
    //  MAIN
    // =========================================================================

    public static void main(String[] args) {

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 1 — Basic Operations");
        System.out.println("══════════════════════════════════════");

        // set + get
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            check("get: existing field", db.get("A", "B"), "C");
            check("get: missing key", db.get("Z", "B"), null);
            check("get: missing field", db.get("A", "Z"), null);
            db.set("A", "B", "NEW");
            check("set: overwrite", db.get("A", "B"), "NEW");
            db.set("A", "D", "E");
            check("set: second field same key", db.get("A", "D"), "E");
        }

        // delete
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            check("delete: existing → true", db.delete("A", "B"), true);
            check("delete: already gone → false", db.delete("A", "B"), false);
            check("delete: missing key → false", db.delete("Z", "B"), false);
            check("get: after delete → null", db.get("A", "B"), null);
        }

        // scan
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            db.set("A", "D", "E");
            db.set("A", "A", "Z");
            check("scan: sorted alphabetically", db.scan("A"), Arrays.asList("A(Z)", "B(C)", "D(E)"));
            check("scan: missing key → empty", db.scan("Z"), new ArrayList<>());
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 2 — Scan By Prefix");
        System.out.println("══════════════════════════════════════");

        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "BC", "1");
            db.set("A", "BD", "2");
            db.set("A", "CD", "3");
            db.set("A", "B", "4");
            check("scanByPrefix: 'B'", db.scanByPrefix("A", "B"), Arrays.asList("B(4)", "BC(1)", "BD(2)"));
            check("scanByPrefix: 'BC'", db.scanByPrefix("A", "BC"), Arrays.asList("BC(1)"));
            check("scanByPrefix: no match → empty", db.scanByPrefix("A", "Z"), new ArrayList<>());
            check("scanByPrefix: missing key → empty", db.scanByPrefix("Z", "B"), new ArrayList<>());
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 3 — TTL");
        System.out.println("══════════════════════════════════════");

        // setAt (no TTL — lives forever)
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAt("A", "B", "C", 1);
            check("getAt: lives forever", db.getAt("A", "B", 100), "C");
            check("getAt: missing key → null", db.getAt("Z", "B", 1), null);
        }

        // setAtWithTtl — expireAt = timestamp + ttl
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAtWithTtl("A", "B", "C", 1, 10);   // alive [1, 11)
            check("getAt: alive at t=5", db.getAt("A", "B", 5), "C");
            check("getAt: alive at last tick t=10", db.getAt("A", "B", 10), "C");
            check("getAt: expired at boundary t=11", db.getAt("A", "B", 11), null);
            check("getAt: expired well beyond t=99", db.getAt("A", "B", 99), null);
        }

        // TTL refresh — re-set replaces entry entirely
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAtWithTtl("A", "B", "C", 1, 5);    // alive [1, 6)
            db.setAtWithTtl("A", "B", "C", 5, 10);   // renewed: alive [5, 15)
            check("TTL refresh: alive at t=12", db.getAt("A", "B", 12), "C");
            check("TTL refresh: expired at t=15", db.getAt("A", "B", 15), null);
        }

        // deleteAt
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAtWithTtl("A", "B", "C", 1, 10);
            check("deleteAt: live → true", db.deleteAt("A", "B", 5), true);
            check("deleteAt: already deleted → false", db.deleteAt("A", "B", 6), false);
            db.setAtWithTtl("A", "D", "E", 1, 5);  // alive [1,6), expired at 6
            check("deleteAt: expired → false", db.deleteAt("A", "D", 6), false);
        }

        // scanAt
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAt("A", "B", "C", 1);
            db.setAtWithTtl("A", "D", "E", 4, 10);  // alive [4, 14)
            check("scanAt: both live at t=5", db.scanAt("A", 5), Arrays.asList("B(C)", "D(E)"));
            check("scanAt: D expired at t=14", db.scanAt("A", 14), Arrays.asList("B(C)"));
        }

        // scanByPrefixAt
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.setAtWithTtl("A", "BC", "E", 1, 9);   // alive [1,10)
            db.setAtWithTtl("A", "BC", "E", 5, 10);  // renewed: alive [5,15)
            db.setAt("A", "BD", "F", 5);
            check("scanByPrefixAt: both at t=14", db.scanByPrefixAt("A", "B", 14), Arrays.asList("BC(E)", "BD(F)"));
            check("scanByPrefixAt: BC expired at t=15", db.scanByPrefixAt("A", "B", 15), Arrays.asList("BD(F)"));
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 4 — Backup & Restore");
        System.out.println("══════════════════════════════════════");

        // basic backup + restore
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            db.backup(1);
            db.set("A", "B", "CHANGED");
            check("before restore: CHANGED", db.get("A", "B"), "CHANGED");
            db.restore(1);
            check("after restore: C", db.get("A", "B"), "C");
        }

        // restore picks most recent backup at or before timestamp
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "V1");
            db.backup(1);
            db.set("A", "B", "V2");
            db.backup(3);
            db.set("A", "B", "V3");
            db.restore(2);
            check("restore(2) picks t=1 backup → V1", db.get("A", "B"), "V1");
            db.restore(5);
            check("restore(5) picks t=3 backup → V2", db.get("A", "B"), "V2");
        }

        // no backup before timestamp → no-op
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            db.backup(10);
            db.set("A", "B", "CHANGED");
            db.restore(5);
            check("restore: no backup before ts → no-op", db.get("A", "B"), "CHANGED");
        }

        // backup is a deep copy
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            db.backup(1);
            db.set("A", "B", "MUTATED");
            db.restore(1);
            check("backup: deep copy — mutation doesn't corrupt snapshot", db.get("A", "B"), "C");
        }

        // restore removes keys added after backup
        {
            InMemoryDatabase db = new InMemoryDatabase();
            db.set("A", "B", "C");
            db.backup(1);
            db.set("X", "Y", "Z");
            db.restore(1);
            check("restore: removes keys added after backup", db.get("X", "Y"), null);
            check("restore: original key intact", db.get("A", "B"), "C");
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.printf("  RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println("══════════════════════════════════════\n");
    }
}