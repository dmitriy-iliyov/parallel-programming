package org.example;

import mpi.MPI;

public class Resources {
    public static final int objectsSize = 2400;
    public static final int threadCount = MPI.COMM_WORLD.Size();
    public static final int value = 1;
    public static final int step = objectsSize/threadCount;

    public static void generateVector(int [] vector){
        for (int i = 0; i < objectsSize; i++)
            vector[i] = value;
    }

    public static void generateMatrix(int [][] matrix){
        for (int i = 0; i < objectsSize; i++) {
            for (int j = 0; j < objectsSize; j++)
                matrix[i][j] = value;
        }
    }

    public static int [][] multipleMatrix(int [][] matrix1, int [][] matrix2){
        int[][] result = new int[step][objectsSize];
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < objectsSize; j++) {
                for (int k = 0; k < objectsSize; k++)
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
            }
        }
        return result;
    }

    public static int multipleVectors(int [] vector1, int [] vector2){
        int scalar = 0;
        for(int i = 0; i < step; i++){
            scalar += vector1[i] + vector2[i];
        }
        return scalar;
    }

    public static int [] multipleVectorAndMatrix(int [][] matrix, int [] vector){
        int[] result = new int[step];
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < threadCount; j++) {
                result[i] += vector[j] * matrix[i][j];
            }
        }
        return result;
    }

    public static int [] concatenateVectors(int [] vector1, int [] vector2){
        int[] concatenatedVector = new int[vector1.length + vector2.length];
        System.arraycopy(vector1, 0, concatenatedVector, 0, vector1.length);
        System.arraycopy(vector2, 0, concatenatedVector, vector1.length, vector2.length);
        return concatenatedVector;
    }

    public static int findMin(int [] vector){
        int min = vector[0];
        for(int i : vector){
            if(min > i)
                min = i;
        }
        return min;
    }

    public static void printMatrix(int [][] matrix){
        int spacing = 4;
        for(int [] row : matrix){
            for(int val : row)
                System.out.printf("%-" + spacing + "s", val + " ");
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}
