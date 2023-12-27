package org.example;
import mpi.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        a= min(C*MZ) + max( D*(MX*MR))
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        switch (MPI.COMM_WORLD.Rank()){
            case 0:
                System.out.println(MPI.COMM_WORLD.Rank());
                int i = 123;
                int destinationRank = 1;
                int tag = 123;
                MPI.COMM_WORLD.Send(i, 0,1, MPI.INT, destinationRank, tag);
                break;
            case 1:
                System.out.println(MPI.COMM_WORLD.Rank());
                int sourceRank = 0;
                int tag2 = 123;
                int receivedData = 0;
                MPI.COMM_WORLD.Recv(receivedData, 0,1, MPI.INT, sourceRank, tag2);
                System.out.println(receivedData);
                break;
            case 2:
                System.out.println(MPI.COMM_WORLD.Rank());
                break;
            case 3:
                System.out.println(MPI.COMM_WORLD.Rank());
                break;
            default:
                System.out.println(MPI.COMM_WORLD.Size());
                break;
        }
        MPI.Finalize();
    }
}