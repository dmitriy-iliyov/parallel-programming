package org.example;

import java.util.concurrent.CyclicBarrier;


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
        this.enteringBarrier =  new CyclicBarrier(threadsCount + 2);
        this.mainBarrier = new CyclicBarrier(threadsCount + 1);
        this.endingBarrier = endingBarrier;
    }

    public void calculation(){

        T1 t1 = new T1(resources, enteringBarrier, mainBarrier, endingBarrier);
        Tp tp = new Tp(resources, enteringBarrier);

        t1.start();
        tp.start();

        CyclicBarrier firstOperationBarrier = new CyclicBarrier(threadsCount);
        CyclicBarrier secondOperationBarrier = new CyclicBarrier(threadsCount);
        CyclicBarrier thirdOperationBarrier = new CyclicBarrier(threadsCount);
        CyclicBarrier forthOperationBarrier = new CyclicBarrier(threadsCount);
        CyclicBarrier fifthOperationBarrier = new CyclicBarrier(threadsCount);

        int step = objectSize/threadsCount;
        int start = 0;
        int end = step;
        for(int i = 0; i < threadsCount; i++){
            Ti ti = new Ti(start, end, resources, enteringBarrier, mainBarrier, firstOperationBarrier,
                    secondOperationBarrier, thirdOperationBarrier, forthOperationBarrier, fifthOperationBarrier);
            ti.start();
            start += step;
            end += step;
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
