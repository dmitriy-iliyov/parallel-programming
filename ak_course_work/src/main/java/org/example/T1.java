package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;


public class T1 extends Thread{

    private final Resources resources;
    private final Semaphore sem = new Semaphore(1);
    private final CyclicBarrier enteringBarrier;
    private final CyclicBarrier mainBarrier;
    private final CyclicBarrier endingBarrier;

    private final Logger logger = Logger.getLogger("T1");


    public T1(Resources resources, CyclicBarrier enteringBarrier, CyclicBarrier mainBarrier, CyclicBarrier endingBarrier) {
        this.resources = resources;
        this.enteringBarrier = enteringBarrier;
        this.mainBarrier = mainBarrier;
        this.endingBarrier = endingBarrier;
    }

    @Override
    public void run(){
        //entering MX, B
        for (int i = 0; i < resources.objectsSize; i++) {
            resources.B[i] = getValue(1);
            for (int j = 0; j < resources.objectsSize; j++) {
                resources.MX[i][j] = getValue(1);
            }
        }
        try{
            enteringBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }

        logger.info("\nB : " + Resources.vectorToString(resources.B));
        logger.info("\nMX : " + Resources.matrixToString(resources.MX));

        try{
            mainBarrier.await();
        }catch(BrokenBarrierException | InterruptedException exc){
            System.out.println(exc.getMessage());
        }

        resources.a = resources.b + resources.c;

        try{
            endingBarrier.await();
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
