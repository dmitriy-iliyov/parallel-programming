package com.company;


public class Matrix {
    private int [][] matrix;
    private int n;

    public Matrix(int n, int userValue) {
        this.n = n;
        this.matrix = generateMatrix(this.n, userValue);
    }

    private static int [][] generateMatrix(int n, int userValue) {
        int [][] matrix = new int [n][n];
        if (userValue != 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = userValue;
                }
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = (int)(Math.random()*100);
                }
            }
        }
        return matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

}
