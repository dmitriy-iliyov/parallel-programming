package org.example;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static java.lang.Math.round;

public class ParallelMonteCarloPi {
    private double pi;
    private int threads;
    private int iterations = 10000000;
    private double startTime;
    private Resources resources;

    public ParallelMonteCarloPi(int threads) {
        this.threads = threads;
        this.startTime = System.currentTimeMillis();
        this.resources = new Resources(this.iterations);
    }

    public void calculation(){
        Semaphore sem = new Semaphore(1);
        CyclicBarrier pointInCircleCount = new CyclicBarrier(this.threads, new calculatePi());
        int cnt = this.iterations/this.threads;
        cnt  += (this.iterations >  cnt * this.threads) ? 1 :0;
        this.iterations = this.threads * cnt;
        for(int i = 0; i < this.iterations; i += cnt){
            new PartOfMonteCarloPi(this.resources, sem, pointInCircleCount, i, i + cnt).start();
        }
    }

    @Override
    public String toString(){
        return "PI is " + this.pi + "\n" +
                "THREADS " + this.threads + "\n" +
                "ITERATIONS " + this.iterations + "\n" +
                "TIME " + (System.currentTimeMillis() - startTime) + " ms\n";

    }

    private class calculatePi extends Thread{
        @Override
        public void run(){
            pi = ((double) resources.pointInCircleCount/resources.iterations)*4;
            System.out.println(ParallelMonteCarloPi.this);
        }
    }
}



