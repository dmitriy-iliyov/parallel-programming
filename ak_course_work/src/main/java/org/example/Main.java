/**
 * Курсова робота з предмету Архітектура Комп'ютерів
 * Ільйов Дмитро Андрійович
 * ІО - 12
 */

package org.example;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {

        CyclicBarrier endingBarrier = new CyclicBarrier(2);

        WrapperClass wrapperClass = new WrapperClass(6, 3, endingBarrier);
        wrapperClass.calculation();
        try{
            endingBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
           System.out.println(exc.getMessage());
        }
        System.out.println(wrapperClass);

    }
}