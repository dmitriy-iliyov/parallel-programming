package org.example;

public class Resources {
    public final int N = 1000;
    public final int H = N/2;
    long time;

    public int d = 0;
    public int a = 0;
    public int [] B = new int [N];
    public int [] Z = new int [N];
    public int [][] MZ = new int[N][N];
    public int [][] MM = new int[N][N];
    public int [][] MR = new int[N][N];
    public int [][] MC = new int[N][N];


    public int [][] MA = new int[N][N];
    public int [][] MB = new int[N][N];
    public int [][] MA2 = new int[N][N];
    public int [][] MB2 = new int[N][N];
    public int [][] MX = new int[N][N];

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
