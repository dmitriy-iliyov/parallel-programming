/**
 * "Паралельне програмування"
 * Лабораторна №1 - "Дослідження потоків у мові Java"
 *
 * F1: D = (SORT(A + B) + C) *(MA*ME)
 * F2: h = MAX(MF + MG*(MH*ML))
 * F3: s = MIN(O*TRANS(MP*MM)) + (R*SORT(S))
 *
 * Ільйов Дмитро Андрійович
 * ІО - 12
 * 20.09.2023
 */

package com.company;

import java.util.Scanner;

public class Lab1 {

    public static void main(String[] args) throws Exception{
        Thread_1 T1 = new Thread_1();
        T1.setPriority(10);
        Thread_2 T2 = new Thread_2();
        T2.setPriority(5);
        Thread_3 T3 = new Thread_3();
        T3.setPriority(1);
        T1.start();
        T1.join();
        T2.start();
        T3.start();
    }
}

class Thread_1 extends Thread
{
    @Override
    public void run()
    {
        System.out.println("Enter value for thread - 1");
        Scanner uV1 = new Scanner(System.in);
        Data data = new Data(uV1.nextInt());
        int [] funk1_result = data.Funk1();
        System.out.println("Func 1 result:");
        data.printVector(funk1_result);
    }
}

class Thread_2 extends Thread
{
    @Override
    public void run()
    {
        System.out.println("Enter value for thread - 2");
        Scanner uV2 = new Scanner(System.in);
        Data data = new Data(uV2.nextInt());
        int funk2_result = data.Funk2();
        System.out.println("Func 2 result:");
        System.out.println(funk2_result);
    }
}

class Thread_3 extends Thread
{
    @Override
    public void run()
    {
        System.out.println("Enter value for thread - 3");
        Scanner uV3 = new Scanner(System.in);
        Data data = new Data(uV3.nextInt());
        int funk3_result = data.Funk3();
        System.out.println("Func 3 result:");
        System.out.println(funk3_result);
    }
}
