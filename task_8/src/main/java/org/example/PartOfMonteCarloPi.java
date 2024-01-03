package org.example;


import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class PartOfMonteCarloPi extends Thread{
    private final ArrayList<Point> pointsList;
    private Resources resources;
    private Semaphore sem;
    private CyclicBarrier countPointsBarrier;

    private int from;
    private int to;

    public PartOfMonteCarloPi(Resources resources, Semaphore sem, CyclicBarrier countPointsBarrier, int from, int to) {
        this.resources = resources;
        this.sem = sem;
        this.countPointsBarrier = countPointsBarrier;
        this.from = 0;
        this.to = to-from;
        this.pointsList = new ArrayList<>(this.to);
    }

    @Override
    public void run(){
        for(int i = from; i < to; i++){
            Point currentPoint = new Point();
            try{
                sem.acquire();
                this.pointsList.add(currentPoint);
            }catch(InterruptedException exc){
                System.out.println(exc.getMessage());
            }finally {
                sem.release();
            }
        }
        int thisPartPointInCircleCount = 0;
        for (int i = from; i < to; i++){
            if(getRange(i) <= 1)
                thisPartPointInCircleCount++;
        }
        try{
            sem.acquire();
            resources.pointInCircleCount += thisPartPointInCircleCount;
        }catch(InterruptedException exc){
            System.out.println(exc.getMessage());
        }finally {
            sem.release();
        }

        try {
            this.countPointsBarrier.await();
        }catch (BrokenBarrierException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    private double getRange(int i){
        return Math.sqrt(Math.pow(this.pointsList.get(i).getX() - 0, 2) + Math.pow(this.pointsList.get(i).getY() - 0, 2));
    }
}
