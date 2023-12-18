package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class P3Thread extends Thread{
    private int MBelement;
    private int MXelement;
    private CyclicBarrier enteringBarrier;
    private CyclicBarrier sixthOperationBarrier;
    private CyclicBarrier mainBarrier;
    private Semaphore sem;
    private Resources resources;

    public P3Thread(Semaphore sem, Resources resources, CyclicBarrier enteringBarrier, CyclicBarrier mainBarrier, CyclicBarrier sixthOperationBarrier) {
        this.sem = sem;
        this.resources = resources;
        this.enteringBarrier = enteringBarrier;
        this.sixthOperationBarrier = sixthOperationBarrier;
        this.mainBarrier = mainBarrier;
    }

    @Override
    public void run(){
//entering MR, B
        for (int i = 0; i < resources.N; i++) {
            resources.B[i] = getValue();
            for (int j = 0; j < resources.N; j++){
                resources.MR[i][j] = getValue();
            }
        }
        try{
            enteringBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }
//third operation - (MR*MC) -> MB;
        for (int i = 0; i < resources.H; i++) {
            for (int j = 0; j < resources.N; j++) {
                for (int k = 0; k < resources.N; k++) {
                    MBelement += resources.MC[i][k] * resources.MR[k][j];
                }
                try{
                    sem.acquire();
                    resources.MB[i][j] = MBelement;
                    MBelement = 0;
                }catch(InterruptedException exc){
                    System.out.println(exc.getMessage());
                }finally {
                    sem.release();
                }
            }
        }
//fours operation - MB*d -> MB2
        int d = resources.d;
        int [][] MB = resources.MB;

        for (int i = 0; i < resources.H; i++) {
            for (int j = 0; j < resources.N; j++) {
                MBelement = d * MB[i][j];
                try{
                    sem.acquire();
                    resources.MB2[i][j] = MBelement;
                    MBelement = 0;
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
        for (int i = resources.H; i < resources.H + resources.H/2; i++) {
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
