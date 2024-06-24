package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;


public class Tp extends Thread{
    int start;
    int end;

    private int partOfb = 0;
    private int partOfc = 0;
    private int aElement = 0;
    private int cElement = 0;
    private int maElement = 0;

    private final Semaphore sem;
    private final Resources resources;
    private final CyclicBarrier enteringBarrier;
    private final CyclicBarrier prepareToMinOperationBarrier;
    private final CyclicBarrier mainBarrier;
    private final Logger logger = Logger.getLogger("Tp");


    public Tp(int start, int end, Resources resources, Semaphore sem, CyclicBarrier enteringBarrier,
              CyclicBarrier mainBarrier, CyclicBarrier prepareToMinOperationBarrier) {
        this.start = start;
        this.end = end;
        this.resources = resources;
        this.sem = sem;
        this.enteringBarrier = enteringBarrier;
        this.mainBarrier = mainBarrier;
        this.prepareToMinOperationBarrier = prepareToMinOperationBarrier;
    }

    @Override
    public void run(){
        //entering Z, MR
        for (int i = 0; i < resources.objectsSize; i++) {
            resources.Z[i] = getValue(1);
            for (int j = 0; j < resources.objectsSize; j++) {
                resources.MR[i][j] = getValue(1);
            }
        }
        try{
            enteringBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }

        for (int i = start; i < end; i++) {
            for (int j = 0; j < resources.objectsSize; j++) {
                for (int k = 0; k < resources.objectsSize; k++) {
                    maElement += resources.MR[i][k] * resources.MX[k][j];
                }
                try{
                    sem.acquire();
                    resources.MA[i][j] = maElement;
                    maElement = 0;
                }catch(InterruptedException exc){
                    System.out.println(exc.getMessage());
                }finally {
                    sem.release();
                }
            }
        }
//        try {
//            firstOperationBarrier.await();
//        }catch (BrokenBarrierException | InterruptedException e){
//            System.out.println(e.getMessage());
//        }

        for (int i = start; i < end; i++) {
            for (int j = 0; j < resources.objectsSize; j++) {
                aElement += resources.MA[i][j] * resources.Z[i];
            }
            try{
                sem.acquire();
                resources.A[i] = aElement;
                aElement = 0;
            }catch(InterruptedException exc){
                System.out.println(exc.getMessage());
            }finally {
                sem.release();
            }
        }
//        try {
//            secondOperationBarrier.await();
//        }catch (BrokenBarrierException | InterruptedException e){
//            System.out.println(e.getMessage());
//        }

        for (int i = start; i < end; i++){
            cElement = resources.A[i] + resources.B[i];
            try{
                sem.acquire();
                resources.C[i] = cElement;
                cElement = 0;
            }catch(InterruptedException exc){
                System.out.println(exc.getMessage());
            }finally {
                sem.release();
            }
        }
//        try{
//            thirdOperationBarrier.await();
//        }catch (BrokenBarrierException | InterruptedException exc){
//            System.out.println(exc.getMessage());
//        }

        for (int i = start; i < end; i++)
            partOfc = partOfc + resources.B[i] * resources.Z[i];
        try{
            sem.acquire();
            resources.c += partOfc;
        }catch(InterruptedException exc){
            System.out.println(exc.getMessage());
        }finally {
            sem.release();
        }

        partOfb = resources.C[0];
        for (int i = start; i < end; i++){
            if(partOfb < resources.C[i])
                partOfb = resources.C[i];
        }
        try{
            sem.acquire();
            resources.b = partOfb;
        }catch(InterruptedException exc){
            System.out.println(exc.getMessage());
        }finally {
            sem.release();
        }
        try {
            prepareToMinOperationBarrier.await();
        } catch (BrokenBarrierException | InterruptedException exc) {
            System.out.println(exc.getMessage());
        }
        try{
            mainBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }

    }

    private int getValue(int userValue){
        return userValue;
    }

    private int getValue(){
        return (int) (Math.random() * 10);
    }
}
