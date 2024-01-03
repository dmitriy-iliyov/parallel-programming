package org.example;
import java.util.Currency;


public class Account {
    private static int id = 1;
    private int currentID;
    private double balance = Math.floor((Math.random() * 100) * 100) / 100;

    public Account() {
        this.currentID = id;
        id++;
    }

     public boolean transferFromHere(double amount){
        if(balance > amount){
            balance -= amount;
            return true;
        }
        return false;
    }

     public boolean transferToHere(double amount){
        if(amount > 0){
            balance += amount;
            return true;
        }
        return false;
    }

    public int getID(){
        return this.currentID;
    }

    public double getBalance(){
        return this.balance;
    }
}
