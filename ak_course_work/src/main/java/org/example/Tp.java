package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;


public class Tp extends Thread{

    private final Resources resources;
    private final CyclicBarrier enteringBarrier;
    private final Logger logger = Logger.getLogger("Tp");


    public Tp(Resources resources, CyclicBarrier enteringBarrier) {
        this.resources = resources;
        this.enteringBarrier = enteringBarrier;
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

        logger.info("\nZ : " + Resources.vectorToString(resources.Z));
        logger.info("\nMR : " + Resources.matrixToString(resources.MR));
    }

    private int getValue(int userValue){
        return userValue;
    }

    private int getValue(){
        return (int) (Math.random() * 10);
    }
}
