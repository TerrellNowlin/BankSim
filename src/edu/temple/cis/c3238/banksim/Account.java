package edu.temple.cis.c3238.banksim;
import java.util.concurrent.*;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
public class Account {

    private volatile int balance;
    private final int id;
    private final Bank myBank;
    private static Semaphore mutex;

    public Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
        mutex = new Semaphore(1);
    }

    public int getBalance() {
        return balance;
    }

    
    public boolean withdraw(int amount) {
        boolean ret = false;
        synchronized(this)
        {
        try{
            mutex.acquire();
            try{
            
                if (amount <= balance) {
                    int currentBalance = balance;
        //            Thread.yield(); // Try to force collision
                    int newBalance = currentBalance - amount;
                    balance = newBalance;
                    ret = true;
                } else {
                    ret = false;
                }
            }
            finally{
                mutex.release();
            }
        }
        catch(InterruptedException e) {

            e.printStackTrace();
        }
        }
        return ret;
    }

    public void deposit(int amount) {
        try{
            mutex.acquire();
            try{
                int currentBalance = balance;
                //Thread.yield();   // Try to force collision
                int newBalance = currentBalance + amount;
                balance = newBalance;
            }
            finally{
                mutex.release();
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
    
    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}
