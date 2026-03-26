package lld;

import java.util.*;


public class BankingSystem {

    // =========================================================================
    //  INNER CLASSES
    // =========================================================================

    private static class Account {
        String id;
        int balance;
        int totalOutgoing;

        Account(String id) {
            this.id = id;
            this.balance = 0;
            this.totalOutgoing = 0;
        }
    }

    private enum PaymentStatus {
        SCHEDULED, PROCESSED, FAILED
    }

    private static class Payment {
        String paymentId;
        String fromId;
        String toId;
        int scheduledAt;
        int amount;
        double cashbackPct;
        PaymentStatus status;

        Payment(String paymentId, String fromId, String toId,
                int amount) {
            this.paymentId = paymentId;
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
            this.status = PaymentStatus.PROCESSED;
        }

         Payment(String paymentId, String fromId, String toId,
                int scheduledAt, int amount, double cashbackPct) {
            this.paymentId = paymentId;
            this.fromId = fromId;
            this.toId = toId;
            this.scheduledAt = scheduledAt;
            this.amount = amount;
            this.cashbackPct = cashbackPct;
            this.status = PaymentStatus.SCHEDULED;
        }
    }

    // =========================================================================
    //  STATE
    // =========================================================================

    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, Payment> payments = new HashMap<>();  // scheduled only
    private int paymentCounter = 0;
    private int lastTimestamp = 0;

    // =========================================================================
    //  HELPERS
    // =========================================================================

    private boolean checkInvalidTimestamp(int timestamp) {
        if (timestamp <= lastTimestamp) {
            return true;
        }
        lastTimestamp = timestamp;
        return false;
    }

    private boolean executeTransfer(String fromId, String toId, int amount) {
        if(fromId==null || toId==null) {
            return false;
        }
        if(Objects.equals(fromId, toId)){
            return false;
        }
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);
        if (fromAccount == null || toAccount == null) {
            return false;
        }
        if(fromAccount.balance<amount){
            return false;
        }
        fromAccount.balance -= amount;
        fromAccount.totalOutgoing += amount;
        toAccount.balance += amount;

        return true;
    }

    // =========================================================================
    //  LEVEL 1
    // =========================================================================

    public boolean createAccount(String accountId, int timestamp) {
        if (checkInvalidTimestamp(timestamp)) {
            return false;
        }
        if (accountId == null || accounts.containsKey(accountId)) {
            return false;
        }
        Account account = new Account(accountId);
        accounts.put(accountId, account);
        return true;
    }

    public Optional<Integer> deposit(String accountId, int timestamp, int amount) {
        if (checkInvalidTimestamp(timestamp)) {
            return Optional.empty();
        }
        if(accountId == null ) {
            return Optional.empty();
        }
        Account account = accounts.get(accountId);
        if(account == null) {
            return Optional.empty();
        }
        account.balance += amount;

        return Optional.of(account.balance);
    }

    public Optional<Integer> transfer(String fromId, String toId, int timestamp, int amount) {
        if(checkInvalidTimestamp(timestamp)) {
            return Optional.empty();
        }
        if(executeTransfer(fromId, toId, amount)) {
            String paymentId =nextPaymentId();
            payments.put(paymentId, new Payment(paymentId, fromId, toId, amount));
            return Optional.of(accounts.get(fromId).balance);

        }else{
            return Optional.empty();
        }

    }

    // =========================================================================
    //  LEVEL 2
    // =========================================================================

    public List<String> topSpenders(int timestamp, int n) {
        if(checkInvalidTimestamp(timestamp)) {
            return Collections.emptyList();
        }
        if(n<1){
            return Collections.emptyList();
        }
      List<String> topSpenders = new ArrayList<>();
        if(accounts.isEmpty()){
            return topSpenders;
        }
        List<Account> accountList= new ArrayList<>(accounts.values());
        accountList.sort((a, b) -> {
            if (a.totalOutgoing == b.totalOutgoing) {
                return a.id.compareTo(b.id);
            } else {
                return b.totalOutgoing - a.totalOutgoing;
            }

        });
        int limit = Math.min(n,accountList.size());
        for(int i=0;i<limit;i++){
            Account account=accountList.get(i);
            topSpenders.add(account.id+"("+account.totalOutgoing+")");
        }

        return topSpenders;
    }

    // =========================================================================
    //  LEVEL 3
    // =========================================================================

    public String schedulePayment(String accountId, String targetAccId,
                                  int timestamp, int amount, double cashbackPct) {
        if(checkInvalidTimestamp(timestamp)) {
            return null;
        }
        String paymentId = nextPaymentId();
        payments.put(paymentId, new Payment(paymentId, accountId, targetAccId,timestamp, amount,cashbackPct));
        return paymentId;
    }

    public String getPaymentStatus(String accountId, int timestamp, String paymentId) {
       if(accountId == null || !accounts.containsKey(accountId)) {
           return "NOT_FOUND";
       }
       if(paymentId == null||!payments.containsKey(paymentId)) {
           return "NOT_FOUND";
       }
      Payment payment=payments.get(paymentId);
       if(payment==null|| !payment.fromId.equals(accountId)) {
           return "NOT_FOUND";
       }

       return payment.status.name();

    }

    public void processScheduledPayments(int currentTimestamp) {
     List<Payment> paymentList= new ArrayList<>(payments.values());
     List<Payment> due= new ArrayList<>();
        for (Payment payment : paymentList) {
            if (payment != null && payment.status == PaymentStatus.SCHEDULED && payment.scheduledAt <= currentTimestamp) {
                due.add(payment);
            }
        }
     due.sort(Comparator.comparingInt(a -> a.scheduledAt));
     for(Payment payment:due){
         if(executeTransfer(payment.fromId, payment.toId, payment.amount)){
             int cashback=(int) (((double) payment.amount*payment.cashbackPct)/100.0d);
             Account account=accounts.get(payment.fromId);
             account.balance+=cashback;
             payments.get(payment.paymentId).status = PaymentStatus.PROCESSED;

         }else{
             payments.get(payment.paymentId).status = PaymentStatus.FAILED;
         }
     }
    }

    // =========================================================================
    //  LEVEL 4
    // =========================================================================

    public void mergeAccounts(String accountId1, String accountId2) {
        Account account1 = accounts.get(accountId1);
        Account account2 = accounts.get(accountId2);
        if (account1 != null && account2 != null && !Objects.equals(account1.id, account2.id)) {
            account1.totalOutgoing += account2.totalOutgoing;
            account1.balance += account2.balance;

            for (Payment payment : payments.values()) {
                if (payment.fromId.equals(account2.id)) {
                    payment.fromId = accountId1;
                }
                if (payment.toId.equals(account2.id)) {
                    payment.toId = accountId1;
                }
            }
            accounts.remove(accountId2);
        }
    }
    private String nextPaymentId() {
        paymentCounter++;
        return "payment"+paymentCounter;
    }

    // =========================================================================
    //  TESTS
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

    static void main() {

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 1");
        System.out.println("══════════════════════════════════════");

        {
            BankingSystem b = new BankingSystem();
            check("createAccount: new", b.createAccount("alice", 1), true);
            check("createAccount: duplicate", b.createAccount("alice", 2), false);
            check("createAccount: another", b.createAccount("bob", 3), true);
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            check("deposit: balance returned", b.deposit("alice", 2, 500), Optional.of(500));
            check("deposit: accumulates", b.deposit("alice", 3, 300), Optional.of(800));
            check("deposit: missing account", b.deposit("ghost", 4, 100), Optional.empty());
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 1000);
            check("transfer: success", b.transfer("alice", "bob", 4, 300), Optional.of(700));
            check("transfer: exact balance → 0", b.transfer("alice", "bob", 5, 700), Optional.of(0));
            check("transfer: insufficient", b.transfer("alice", "bob", 6, 1), Optional.empty());
            check("transfer: self", b.transfer("bob", "bob", 7, 100), Optional.empty());
            check("transfer: missing sender", b.transfer("ghost", "bob", 8, 100), Optional.empty());
            check("transfer: missing receiver", b.transfer("bob", "ghost", 9, 100), Optional.empty());
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 5);
            check("timestamp: same rejected", b.createAccount("bob", 5), false);
            check("timestamp: earlier rejected", b.createAccount("bob", 3), false);
            check("timestamp: greater ok", b.createAccount("bob", 6), true);
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 2");
        System.out.println("══════════════════════════════════════");

        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.createAccount("charlie", 3);
            b.deposit("alice", 4, 2000);
            b.deposit("bob", 5, 1000);
            b.deposit("charlie", 6, 500);
            b.transfer("alice", "bob", 7, 500);
            b.transfer("alice", "charlie", 8, 300);
            b.transfer("bob", "charlie", 9, 200);
            check("topSpenders: sorted desc", b.topSpenders(10, 3), Arrays.asList("alice(800)", "bob(200)", "charlie(0)"));
            check("topSpenders: n > accounts", b.topSpenders(11, 10), Arrays.asList("alice(800)", "bob(200)", "charlie(0)"));
            check("topSpenders: n=1", b.topSpenders(12, 1), List.of("alice(800)"));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("bob", 1);
            b.createAccount("alice", 2);
            b.deposit("alice", 3, 500);
            b.deposit("bob", 4, 500);
            b.transfer("alice", "bob", 5, 300);
            b.transfer("bob", "alice", 6, 300);
            check("topSpenders: tie-break by id", b.topSpenders(7, 2), Arrays.asList("alice(300)", "bob(300)"));
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 3");
        System.out.println("══════════════════════════════════════");

        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 1000);
            String pid = b.schedulePayment("alice", "bob", 5, 400, 10.0);
            check("schedule: paymentId", pid, "payment1");
            check("status: before due → SCHEDULED", b.getPaymentStatus("alice", 4, pid), "SCHEDULED");
            b.processScheduledPayments(5);
            check("status: after → PROCESSED", b.getPaymentStatus("alice", 6, pid), "PROCESSED");
            check("cashback: alice = 640", b.deposit("alice", 7, 0), Optional.of(640));
            check("cashback: bob = 400", b.deposit("bob", 8, 0), Optional.of(400));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 1000);
            b.schedulePayment("alice", "bob", 4, 300, 15.0);
            b.processScheduledPayments(4);
            check("cashback: floor(300*15/100)=45 → 745", b.deposit("alice", 5, 0), Optional.of(745));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 100);
            String pid = b.schedulePayment("alice", "bob", 4, 500, 10.0);
            b.processScheduledPayments(4);
            check("payment: insufficient at execution → FAILED", b.getPaymentStatus("alice", 5, pid), "FAILED");
            check("payment: balance unchanged", b.deposit("alice", 6, 0), Optional.of(100));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 500);
            String p1 = b.schedulePayment("alice", "bob", 4, 400, 0.0);
            String p2 = b.schedulePayment("alice", "bob", 5, 400, 0.0);
            b.processScheduledPayments(6);
            check("competing: earlier succeeds", b.getPaymentStatus("alice", 7, p1), "PROCESSED");
            check("competing: later fails (drained)", b.getPaymentStatus("alice", 7, p2), "FAILED");
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 500);
            String pid = b.schedulePayment("alice", "bob", 4, 100, 5.0);
            check("status: wrong accountId → NOT_FOUND", b.getPaymentStatus("bob", 5, pid), "NOT_FOUND");
            check("status: unknown paymentId → NOT_FOUND", b.getPaymentStatus("alice", 6, "payment999"), "NOT_FOUND");
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 1000);
            b.schedulePayment("alice", "bob", 4, 300, 0.0);
            b.processScheduledPayments(4);
            check("scheduled outgoing in topSpenders", b.topSpenders(5, 1), List.of("alice(300)"));
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  LEVEL 4");
        System.out.println("══════════════════════════════════════");

        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("alice", 3, 500);
            b.deposit("bob", 4, 300);
            b.mergeAccounts("alice", "bob");
            check("merge: balances combined", b.deposit("alice", 5, 0), Optional.of(800));
            check("merge: bob deleted", b.deposit("bob", 6, 100), Optional.empty());
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.createAccount("charlie", 3);
            b.deposit("alice", 4, 1000);
            b.deposit("bob", 5, 1000);
            b.transfer("alice", "charlie", 6, 400);
            b.transfer("bob", "charlie", 7, 200);
            b.mergeAccounts("alice", "bob");
            check("merge: totalOutgoing combined", b.topSpenders(8, 1), List.of("alice(600)"));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.deposit("alice", 2, 500);
            b.mergeAccounts("alice", "alice");
            check("merge: same id no-op", b.deposit("alice", 3, 0), Optional.of(500));
            b.mergeAccounts("alice", "ghost");
            check("merge: missing acc no-op", b.deposit("alice", 4, 0), Optional.of(500));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.createAccount("charlie", 3);
            b.deposit("bob", 4, 600);
            String pid = b.schedulePayment("bob", "charlie", 5, 200, 10.0);
            b.mergeAccounts("alice", "bob");
            check("merge: payment ownership → alice", b.getPaymentStatus("alice", 6, pid), "SCHEDULED");
            b.processScheduledPayments(5);
            check("merge: redirected payment processes", b.getPaymentStatus("alice", 7, pid), "PROCESSED");
            check("merge: alice balance after payment", b.deposit("alice", 8, 0), Optional.of(420));
        }
        {
            BankingSystem b = new BankingSystem();
            b.createAccount("alice", 1);
            b.createAccount("bob", 2);
            b.deposit("bob", 3, 500);
            String pid = b.schedulePayment("bob", "alice", 4, 200, 0.0);
            b.mergeAccounts("alice", "bob");
            b.processScheduledPayments(4);
            check("merge: bob→alice SCHEDULED becomes self-loop → FAILED",
                    b.getPaymentStatus("alice", 5, pid), "FAILED");
        }

        System.out.println("\n══════════════════════════════════════");
        System.out.printf("  RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println("══════════════════════════════════════\n");
    }
}