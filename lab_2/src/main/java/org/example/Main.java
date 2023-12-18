/**
 "Паралельне програмування"
 Лабораторна №3 - "Семафори, критичні секції, атомік-змінні, бар’єри"

 Ільйов Дмитро Андрійович
 ІО - 12
 12.12.2023
 */

package org.example;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {

        Semaphore sem = new Semaphore(1);
        Resources resources = new Resources();
        resources.time = System.currentTimeMillis();

        CyclicBarrier enteringBarrier = new CyclicBarrier(4);
        CyclicBarrier fifthOperationBarrier = new CyclicBarrier(2);
        CyclicBarrier sixthOperationBarrier = new CyclicBarrier(4);
        CyclicBarrier mainBarrier = new CyclicBarrier(4);

        P1Thread P1 = new P1Thread(sem, resources, enteringBarrier, mainBarrier, fifthOperationBarrier, sixthOperationBarrier);
        P2Thread P2 = new P2Thread(sem, resources, enteringBarrier, mainBarrier, fifthOperationBarrier, sixthOperationBarrier);
        P3Thread P3 = new P3Thread(sem, resources, enteringBarrier, mainBarrier, sixthOperationBarrier);
        P4Thread P4 = new P4Thread(sem, resources, enteringBarrier, mainBarrier, sixthOperationBarrier);

        P1.start();
        P2.start();
        P3.start();
        P4.start();
    }
}