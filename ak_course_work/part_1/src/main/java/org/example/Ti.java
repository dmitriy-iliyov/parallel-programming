package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/*
    a = min(B + Z * (MR * MX)) + (B * Z)

    1. MA = MR * MX
    2. A = Z * MA
    3. C = B + A
    4. b = min(C)
    5. c = B * Z
    6. a = b + c
 */

public class Ti extends Thread{

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

    private final Logger logger = Logger.getLogger("Ti");


    public Ti(int start, int end, Resources resources, Semaphore sem, CyclicBarrier enteringBarrier,
              CyclicBarrier mainBarrier, CyclicBarrier prepareToMinOperationBarrier) {
        this.start = start;
        this.end = end;
        this.resources = resources;
        this.sem = sem;
        this.enteringBarrier = enteringBarrier;
        this.prepareToMinOperationBarrier = prepareToMinOperationBarrier;
        this.mainBarrier = mainBarrier;
    }

    @Override
    public void run(){
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
}
