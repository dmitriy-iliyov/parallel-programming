package org.example;

public class TransferThread extends Thread{
    private Bank bank;
    private int accountCount;

    public TransferThread(Bank bank, int accountCount) {
        this.bank = bank;
        this.accountCount = accountCount;
    }

    @Override
    public void run(){
        int randFromAccountID = (int) (Math.random() * accountCount);
        int randToAccountID = (int) (Math.random() * accountCount);
        Account from = null;
        Account to = null;
        while (from == null || to == null){
            if (from == null)
                from = bank.getAccount(randFromAccountID);
            if (to == null)
                to = bank.getAccount(randToAccountID);
        }
        bank.transfer(from, to, (int) (Math.floor((Math.random() * from.getBalance()) * 100) / 100));
    }
}
