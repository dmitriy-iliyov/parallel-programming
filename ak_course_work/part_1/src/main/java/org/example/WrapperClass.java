package org.example;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


public class WrapperClass {
    private final int threadsCount;
    private final int objectSize;
    private final Resources resources;
    private final CyclicBarrier enteringBarrier;
    private final CyclicBarrier mainBarrier;
    private final CyclicBarrier endingBarrier;


    public WrapperClass(int objectSize, int threadsCount, CyclicBarrier endingBarrier) {
        this.threadsCount = threadsCount;
        this.objectSize = objectSize;
        this.resources = new Resources(objectSize, System.currentTimeMillis());
        this.enteringBarrier =  new CyclicBarrier(threadsCount);
        this.mainBarrier = new CyclicBarrier(threadsCount);
        this.endingBarrier = endingBarrier;
    }

    public void calculation(){

        Semaphore semaphore = new Semaphore(1);
        CyclicBarrier prepareToMinOperationBarrier = new CyclicBarrier(threadsCount);

        if(objectSize % threadsCount == 0){
            int step = objectSize/threadsCount;
            int start = 0;
            int end = step;
            for(int i = 0; i < threadsCount; i++){
                if(i == 0){
                    T1 t1 = new T1(start, end, resources, semaphore, enteringBarrier, mainBarrier, endingBarrier, prepareToMinOperationBarrier);
                    t1.start();
                    start += step;
                    end += step;
                }else if(i == threadsCount - 1){
                    Tp tp = new Tp(start, end, resources, semaphore, enteringBarrier, mainBarrier, prepareToMinOperationBarrier);
                    tp.start();
                    break;
                }else{
                    Ti ti = new Ti(start, end, resources, semaphore, enteringBarrier, mainBarrier, prepareToMinOperationBarrier);
                    ti.start();
                    start += step;
                    end += step;
                }
            }
        }
        else{
            int step = objectSize/threadsCount;
            int divisionRemainder = objectSize % threadsCount;
            int start = 0;
            int end = step;
            for(int i = 0; i < threadsCount; i++){
                if(i == 0){
                    T1 t1 = new T1(start, end, resources, semaphore, enteringBarrier, mainBarrier, endingBarrier, prepareToMinOperationBarrier);
                    t1.start();
                    start += step;
                    end += step;
                }else if(i == threadsCount - 1){
                    Tp tp = new Tp(start, end + divisionRemainder, resources, semaphore, enteringBarrier, mainBarrier, prepareToMinOperationBarrier);
                    tp.start();
                    break;
                }else{
                    Ti ti = new Ti(start, end, resources, semaphore, enteringBarrier, mainBarrier, prepareToMinOperationBarrier);
                    ti.start();
                    start += step;
                    end += step;
                }
            }
        }
    }

    @Override
    public String toString(){
        return  "RESULT         " + resources.a + "\n" +
                "THREADS        " + threadsCount + "\n" +
                "OBJECTS SIZE   " + objectSize + "\n" +
                "TIME           " + (System.currentTimeMillis() - resources.time) + " ms\n";
    }
}
