package org.example;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts = new ArrayList<>();
    public int count;
    public int failsCount;

    public Account addAccount(){
        Account account = new Account();
        this.accounts.add(account);
        return account;
    }

    synchronized public void transfer(Account from, Account to, double amount) {
        if (from.transferFromHere(amount) && to.transferToHere(amount))
            this.count++;
        else
            this.failsCount++;
    }

    public double calculateTotalAmount(){
        double currenttotalAmount = 0;
        for (Account account : this.accounts)
            currenttotalAmount += account.getBalance();
        return Math.floor(currenttotalAmount * 100) / 100;
    }

    public Account getAccount(int i){
        try{
            return accounts.get(i);
        }catch (IndexOutOfBoundsException exception){
            System.out.println(exception.getMessage());
        }
        return null;
    }
}

