/**
 "Паралельне програмування"
 Лабораторна №6 - "MPI бібліотека в мові Java"

 Ільйов Дмитро Андрійович
 ІО - 12
 01.01.2024
 */
package org.example;
import mpi.*;


public class Main {

    public static void main(String[] args) throws InterruptedException {
//        a= min(C*MZ) + max(D*(MX*MR))
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        final int N = 4;



        int [] A = new int [N];
        int [][] MA = new int [N][N];
        int b = 0;
        int [] D = new int [N];
        int [] B = new int [N];
        int c = 0;
        int a = 0;

        //c mz
        if (rank == 0) {
            int [] C = randVector(N);
            int [][] MZ;

            MPI.COMM_WORLD.Send(C, 0, N, MPI.INT, 1, 0);
            int [] vecMZ = new int[N*N];
            MPI.COMM_WORLD.Recv(vecMZ, 0, N*N, MPI.INT, 1, 6);
            MZ = convertToMatrix(vecMZ);

            for(int i = 0; i < N/2; i++){
                for(int j = 0; j < N; j++){
                    A[i] = C[i] * MZ[i][j];
                }
            }
            MPI.COMM_WORLD.Send(A, 0, N, MPI.INT, 1, 7);
            int [] partA = new int[N];
            MPI.COMM_WORLD.Recv(partA, 0, N, MPI.INT, 1, 8);
            A = partA;
        }
        else if (rank == 1) {
            int [][] MX = randMatrix(N);
            int [] C = new int[N];
            int [][] MZ;

            MPI.COMM_WORLD.Recv(C, 0, N, MPI.INT, 0, 0);
            int [] vecMX = convertToVec(MX);
            MPI.COMM_WORLD.Send(vecMX, 0, N*N, MPI.INT, 2, 1);
            int [] vecMZ = new int[N*N];
            MPI.COMM_WORLD.Recv(vecMZ, 0, N*N, MPI.INT, 2, 5);
            MZ = convertToMatrix(vecMZ);
            MPI.COMM_WORLD.Send(vecMZ, 0, N*N, MPI.INT, 0, 6);

            for(int i = N/2; i < N; i++){
                for(int j = 0; j < N; j++){
                    A[i] = C[i] * MZ[i][j];
                }
            }
            int [] partA = new int[N];
            MPI.COMM_WORLD.Recv(partA, 0, N, MPI.INT, 0, 7);
            for (int i = 0; i < N; i++)
                A[i] = A[i] + partA[i];
            MPI.COMM_WORLD.Send(A, 0, N, MPI.INT, 0, 8);

        }
        //d mx mr
        else if (rank == 2) {
            D = randVector(N);
            int [][] MR = randMatrix(N);
            int [][] MZ = randMatrix(N);
            int [][] MX;

            MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 3, 2);
            int [] vecMR = convertToVec(MR);
            MPI.COMM_WORLD.Send(vecMR, 0, N*N, MPI.INT, 3, 3);
            int [] vecMX = new int [N*N];
            MPI.COMM_WORLD.Recv(vecMX, 0, N*N, MPI.INT, 1, 1);
            MX = convertToMatrix(vecMX);
            MPI.COMM_WORLD.Send(vecMX, 0, N*N, MPI.INT, 3, 4);
            int [] vecMZ = convertToVec(MZ);
            MPI.COMM_WORLD.Send(vecMZ, 0, N*N, MPI.INT, 1, 5);

            int MAelement = 0;
            for (int i = 0; i < N/2; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < N; k++) {
                        MAelement += MX[i][k] * MR[k][j];
                    }
                    MA[i][j] = MAelement;
                    MAelement = 0;
                }
            }
            MPI.COMM_WORLD.Send(convertToVec(MA), 0, N*N, MPI.INT, 3, 9);
            int [] vecMA = new int [N*N];
            MPI.COMM_WORLD.Recv(vecMA, 0, N*N, MPI.INT, 3, 10);
            MA = convertToMatrix(vecMA);
        }
        else if (rank == 3) {
            D = new int [N];
            int [][] MR;
            int [][] MX;

            MPI.COMM_WORLD.Recv(D, 0, N, MPI.INT, 2, 2);
            int [] vecMR = new int [N*N];
            MPI.COMM_WORLD.Recv(vecMR, 0, N*N, MPI.INT, 2, 3);
            MR = convertToMatrix(vecMR);
            int [] vecMX = new int [N*N];
            MPI.COMM_WORLD.Recv(vecMX, 0, N*N, MPI.INT, 2, 4);
            MX = convertToMatrix(vecMX);

            int MAelement = 0;
            for (int i = N/2; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < N; k++) {
                        MAelement += MX[i][k] * MR[k][j];
                    }
                    MA[i][j] = MAelement;
                    MAelement = 0;
                }
            }
            int [] vecMA = new int [N*N];
            MPI.COMM_WORLD.Recv(vecMA, 0, N*N, MPI.INT, 2, 9);
            int [][] partMA = convertToMatrix(vecMA);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    MA[i][j] += partMA[i][j];
                }
            }
            MPI.COMM_WORLD.Send(convertToVec(MA),0, N*N, MPI.INT, 2, 10);
        }

        MPI.COMM_WORLD.Barrier();

        if (rank == 0) {
            int min = A[0];
            for(int i = 0; i < N/2; i++){
                if(A[i] < min)
                    min = A[i];
            }
            MPI.COMM_WORLD.Send(new int[] {min},0, 1, MPI.INT, 1, 11);
            int [] halfAmin = new int[1];
            MPI.COMM_WORLD.Recv(halfAmin,0, 1, MPI.INT, 1, 12);
            if(halfAmin[0] < min){
                min = halfAmin[0];
            }
            b = min;
        }else if (rank == 1) {
            int min = A[0];
            for(int i = N/2; i < N; i++){
                if(A[i] < min)
                    min = A[i];
            }
            int [] halfAmin = new int[1];
            MPI.COMM_WORLD.Recv(halfAmin,0, 1, MPI.INT, 0, 11);
            if(halfAmin[0] < min){
                min = halfAmin[0];
            }
            MPI.COMM_WORLD.Send(new int[] {min},0, 1, MPI.INT, 0, 12);
            b = min;
        }else if (rank == 2) {
            for(int i = 0; i < N/2; i++){
                for(int j = 0; j < N; j++){
                    B[i] = D[i] * MA[i][j];
                }
            }
            MPI.COMM_WORLD.Send(B, 0, N, MPI.INT, 3, 13);
            int [] partB = new int[N];
            MPI.COMM_WORLD.Recv(partB, 0, N, MPI.INT, 3, 14);
            B = partB;
        }else if (rank == 3) {
            for(int i = N/2; i < N; i++){
                for(int j = 0; j < N; j++){
                    B[i] = D[i] * MA[i][j];
                }
            }
            int [] partB = new int[N];
            MPI.COMM_WORLD.Recv(partB, 0, N, MPI.INT, 2, 13);
            for (int i = 0; i < N; i++)
                B[i] = B[i] + partB[i];
            MPI.COMM_WORLD.Send(B, 0, N, MPI.INT, 2, 14);
        }

        MPI.COMM_WORLD.Barrier();

        if (rank == 2) {
            int max = B[0];
            for(int i = 0; i < N/2; i++){
                if(B[i] > max)
                    max = B[i];
            }
            MPI.COMM_WORLD.Send(new int[] {max},0, 1, MPI.INT, 3, 15);
            int [] halfBmax = new int[1];
            MPI.COMM_WORLD.Recv(halfBmax,0, 1, MPI.INT, 3, 16);
            if(halfBmax[0] > max){
                max = halfBmax[0];
            }
            c = max;
        }else if (rank == 3) {
            int max = B[0];
            for(int i = N/2; i < N; i++){
                if(B[i] > max)
                    max = B[i];
            }
            int [] halfBmax = new int[1];
            MPI.COMM_WORLD.Recv(halfBmax,0, 1, MPI.INT, 2, 15);
            if(halfBmax[0] > max){
                max = halfBmax[0];
            }
            MPI.COMM_WORLD.Send(new int[] {max},0, 1, MPI.INT, 2, 16);
            c = max;
            MPI.COMM_WORLD.Send(new int[] {max},0, 1, MPI.INT, 1, 17);
        }else if (rank == 1){
            int [] maxDB = new int[1];
            MPI.COMM_WORLD.Recv(maxDB,0, 1, MPI.INT, 3, 17);
            c = maxDB[0];
        }

        MPI.COMM_WORLD.Barrier();

        if(rank == 1){
            a = b + c;
            System.out.println(a);
        }

        MPI.Finalize();
    }

    private static int [] randVector(int n){
        int [] vector = new int[n];
        for (int i = 0;i < n;i++)
            vector[i] = (int) (Math.random()*10);
        return vector;
    }

    private static int [][] randMatrix(int n){
        int [][] matrix = new int[n][n];
        for (int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                matrix[i][j] = (int) (Math.random()*10);
        return matrix;
    }

    private static int [] convertToVec(int [][] matrix){
        int [] vec = new int [matrix.length * matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, vec, i * matrix.length, matrix.length);
        }
        return vec;
    }

    private static int [][] convertToMatrix(int [] vec ){
        int vecLength = (int) Math.sqrt(vec.length);
        int [][] matrix = new int [vecLength][vecLength];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(vec, i * matrix.length, matrix[i], 0, matrix.length);
        }
        return matrix;
    }

    private static void printVector(int [] vector){
        for (int i = 0;i < vector.length;i++)
            System.out.print(vector[i] + " ");
        System.out.println(" ");
    }
    private static void printMatrix(int [][] matrix){
        for (int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++)
                System.out.print(matrix[i][j] + " ");
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}