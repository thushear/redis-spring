package net.shopin.concurrency;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 说明:展示动态顺序死锁发生的类
 * User: kongming
 * Date: 14-6-20
 * Time: 上午11:16
 */
public class DynamicOrderDeadlock {

    //防止hash重复的加时锁
    private static final Object tieLock =  new Object();

    // Warning: deadlock-prone!
    //容易发生参数顺序死锁
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }


    /**
     * 线程安全
     * 通过给锁加顺序避免发生动态顺序死锁
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @throws InsufficientFundsException
     */
    public static void safeTransferMoney(final Account fromAccount,
                                         final Account toAccount,
                                         final DollarAmount amount) throws InsufficientFundsException{
        class Helper{
            public void transter()throws InsufficientFundsException{
                if(fromAccount.getBalance().compareTo(amount)<0)
                    throw new InsufficientFundsException();
                else{
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }

        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);
        if(fromHash < toHash){
            synchronized (fromAccount){
                synchronized (toAccount){
                    new Helper().transter();
                }
            }
        }  else if (fromHash >toHash){
            synchronized (toAccount){
                   synchronized (fromAccount){
                       new Helper().transter();
                   }
            }
        }else{
            synchronized (tieLock){
                synchronized (fromAccount){
                    synchronized (toAccount){
                        new Helper().transter();
                    }
                }
            }

        }


    }


    static class DollarAmount implements Comparable<DollarAmount> {
        // Needs implementation

        public DollarAmount(int amount) {
        }

        public DollarAmount add(DollarAmount d) {
            return null;
        }

        public DollarAmount subtract(DollarAmount d) {
            return null;
        }

        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }
    static class Account {
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            acctNo = sequence.incrementAndGet();
            balance = new DollarAmount(10000);
        }

        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAcctNo() {
            return acctNo;
        }
    }


    static class InsufficientFundsException extends Exception {
    }
}
