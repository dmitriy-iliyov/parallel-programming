/**
 "Паралельне програмування"
 Лабораторна №4 - "Мoнітори в мові Java"

 Ільйов Дмитро Андрійович
 ІО - 12
 12.12.2023
 */
package org.example;

import org.example.treads.P1Thread;
import org.example.treads.P2Thread;
import org.example.treads.P3Thread;
import org.example.treads.P4Thread;


public class Main {
    public static void main(String[] args) {
        Operations data = new Operations();
        P1Thread P1 = new  P1Thread(data);
        P2Thread P2 = new  P2Thread(data);
        P3Thread P3 = new  P3Thread(data);
        P4Thread P4 = new  P4Thread(data);
        P1.start();
        P2.start();
        P3.start();
        P4.start();
    }
}