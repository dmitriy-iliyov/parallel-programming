package org.example;

import mpi.*;

public class Main {
    public static void main(String[] args) throws MPIException {
        long start = System.currentTimeMillis();
        MPI.Init(args);
        int threadCount = MPI.COMM_WORLD.Rank();
        Ti.calculation(threadCount);
        MPI.Finalize();

        if (threadCount == 0) {
            long end = System.currentTimeMillis();
            long duration = end - start;
            System.out.print("calculation time: " + duration + " ms");
        }
    }
}