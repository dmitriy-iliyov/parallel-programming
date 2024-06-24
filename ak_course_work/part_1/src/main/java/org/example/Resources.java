package org.example;

public class Resources {
    public int objectsSize;
    long time;

    //input data
    public int a;
    public int [] B;
    public int [] Z;
    public int [][] MR;
    public int [][] MX;

    //calculated data
    public int [][] MA;
    public int [] A;
    public int [] C;
    public int b;
    public int c;

    public Resources(int objectSize, long time) {
        this.objectsSize = objectSize;
        this.time = time;
        a = 0;
        b = 0;
        c = 0;
        A = new int [objectsSize];
        B = new int [objectsSize];
        C = new int [objectsSize];
        Z = new int [objectsSize];
        MR = new int[objectsSize][objectsSize];
        MX = new int[objectsSize][objectsSize];
        MA = new int[objectsSize][objectsSize];
    }

    public static String matrixToString(int [][] matrix){
        StringBuilder matrixInString = new StringBuilder();
        matrixInString.append("\n");
        for(int [] row : matrix){
            for(int val : row)
                matrixInString.append(val).append(" ");
            matrixInString.append("\n");
        }
        return matrixInString.toString();
    }

    public static String vectorToString(int [] vector){
        StringBuilder vectorInString = new StringBuilder();
        for(int val : vector){
            vectorInString.append(val).append(" ");
        }
        vectorInString.append("\n");
        return vectorInString.toString();
    }

    public void printMatrix(int [][] matrix){
        int spacing = 4;
        for(int [] row : matrix){
            for(int val : row)
                System.out.printf("%-" + spacing + "s", val + " ");
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public void printVector(int [] vector){
        for(int val : vector)
            System.out.println(val);
    }
}
