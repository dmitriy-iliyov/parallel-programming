package org.example;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class P1Thread extends Thread{
    private int a1;
    private int MAelement;
    private int MXelement;
    private CyclicBarrier enteringBarrier;
    private CyclicBarrier fifthOperationBarrier;
    private CyclicBarrier sixthOperationBarrier;
    private CyclicBarrier mainBarrier;
    private Semaphore sem;
    private Resources resources;

    public P1Thread(Semaphore sem, Resources resources, CyclicBarrier enteringBarrier, CyclicBarrier mainBarrier, CyclicBarrier fifthOperationBarrier, CyclicBarrier sixthOperationBarrier) {
        this.sem = sem;
        this.resources = resources;
        this.enteringBarrier = enteringBarrier;
        this.fifthOperationBarrier = fifthOperationBarrier;
        this.sixthOperationBarrier = sixthOperationBarrier;
        this.mainBarrier = mainBarrier;
    }

    @Override
    public void run(){
//entering Z, d
        resources.d = getValue();
        for (int i = 0; i < resources.N; i++)
            resources.Z[i] = getValue();
        try{
            enteringBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }
//first operation - (B*Z) -> a
        for (int i = 0; i < resources.H; i++)
            a1 = a1 + resources.B[i] * resources.Z[i];

        try{
            sem.acquire();
            resources.a += a1;
        }catch(InterruptedException exc){
            System.out.println(exc.getMessage());
        }finally {
            sem.release();
        }
//second operation - (MZ*MM) -> MA
        for (int i = 0; i < resources.H; i++) {
            for (int j = 0; j < resources.N; j++) {
                for (int k = 0; k < resources.N; k++) {
                    MAelement += resources.MZ[i][k] * resources.MM[k][j];
                }
                try{
                    sem.acquire();
                    resources.MA[i][j] = MAelement;
                    MAelement = 0;
                }catch(InterruptedException exc){
                    System.out.println(exc.getMessage());
                }finally {
                    sem.release();
                }
            }
        }
        try {
            fifthOperationBarrier.await();
        }catch (BrokenBarrierException | InterruptedException e){
            System.out.println(e.getMessage());
        }
//fifth operation - (MA*a) -> MA2
        int a = resources.a;
        int [][] MA = resources.MA;

        for (int i = 0; i < resources.H; i++) {
            for (int j = 0; j < resources.N; j++) {
                MAelement = a * MA[i][j];
                try{
                    sem.acquire();
                    resources.MA2[i][j] = MAelement;
                    MAelement = 0;
                }catch(InterruptedException exc){
                    System.out.println(exc.getMessage());
                }finally {
                    sem.release();
                }
            }
        }
        try{
            sixthOperationBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }
//sixth operation - (MA2-MB2) -> MX
        int [][] MA2 = resources.MA2;
        int [][] MB2 = resources.MB2;
        for (int i = 0; i < resources.H/2; i++) {
            for (int j = 0; j < resources.N; j++) {
                MXelement = MA2[i][j]-MB2[i][j];
                try{
                    sem.acquire();
                    resources.MX[i][j] = MXelement;
                    MXelement = 0;
                }catch(InterruptedException exc){
                    System.out.println(exc.getMessage());
                }finally {
                    sem.release();
                }
            }
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
