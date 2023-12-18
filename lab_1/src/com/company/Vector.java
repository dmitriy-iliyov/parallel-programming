package com.company;


public class Vector {
    private int [] vector;
    private int n;

    public Vector(int n, int userValue) {
        this.n = n;
        this.vector = generateVector(this.n, userValue);
    }

    private static int [] generateVector(int n, int userValue) {
        int [] vector = new int [n];
        if (userValue != 0) {
            for (int i = 0; i < n; i++) {
                vector[i] = userValue;
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                vector[i] = (int) (Math.random() * 10);
            }
        }
        return vector;
    }

    public int[] getVector() {
        return vector;
    }
}
