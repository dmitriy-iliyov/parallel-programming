package com.company;

public class Data {
    private int n;
    private Matrix MA;
    private Matrix ME;
    private Matrix MF;
    private Matrix MG;
    private Matrix MH;
    private Matrix ML;
    private Matrix MM;
    private Matrix MP;
    private Vector A;
    private Vector B;
    private Vector C;
    private Vector O;
    private Vector R;
    private Vector S;


    public Data(int userValue) {
        this.n = (int)(2 + Math.random()*100);
//        this.n = 3;
        this.MA = new Matrix(n, userValue);
        this.ME = new Matrix(n, userValue);
        this.MF = new Matrix(n, userValue);
        this.MG = new Matrix(n, userValue);
        this.MH = new Matrix(n, userValue);
        this.ML = new Matrix(n, userValue);
        this.MM = new Matrix(n, userValue);
        this.MP = new Matrix(n, userValue);
        this.A = new Vector(n, userValue);
        this.B = new Vector(n, userValue);
        this.C = new Vector(n, userValue);
        this.O = new Vector(n, userValue);
        this.R = new Vector(n, userValue);
        this.S = new Vector(n, userValue);
    }


    public void printVector(int [] vector) {
        for (int value : vector) {
            System.out.println(value);
        }
    }

    private int [][] transMatrix(int [][] matrix) {
        for (int i = 0; i < this.n; i++) {
            for (int j = i + 1; j < this.n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
        return matrix;
    }

    private int [][] multiplyMartix(int [][] m1, int [][] m2) {
        int [][] matrix = new int[m1.length][m2[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0;
                for (int k = 0; k < m1[0].length; k++) {
                    matrix[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return matrix;
    }

    private int [][] addMartix(int [][] m1, int [][] m2) {
        int [][] matrix = new int[m1.length][m2[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] =  m1[i][j] +  m2[i][j];
            }
        }
        return matrix;
    }

    private int maxMatrixElem(int [][] matrix) {
        int maxElem = matrix[0][0];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(maxElem < matrix[i][j]){
                    maxElem = matrix[i][j];
                }
            }
        }
        return maxElem;
    }

    private int [] multiplyVectorMartix(int [] vector, int [][] matrix) {
        int [] _vector = new int [this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                _vector[i] += matrix[i][j] * vector[j];
            }
        }
        return _vector;
    }

    private int multiplyVectors(int [] v1, int [] v2) {
        int scal = 0;
        for (int i = 0; i < this.n; i++) {
            scal += v1[i]*v2[i];
        }
        return scal;
    }

    private int [] addVectors(int [] v1, int [] v2) {
        int [] new_vector = new int [v1.length];
        for (int i = 0; i < v1.length; i++) {
            new_vector[i] = v1[i] + v2[i];
        }
        return new_vector;
    }

    private int [] sortVector(int [] vector) {
        for(int i = 1; i < vector.length; i++) {
            for(int k = i; k > 0 &&  vector[k-1] > vector[k]; k--) {
                int tmp = vector[k-1];
                vector[k-1] = vector[k];
                vector[k] = tmp;
            }
        }
        return vector;
    }

    private int minVectorElem(int [] vector) {
        int minElem = vector[0];
        for (int i = 0; i < n; i++) {
            if(minElem > vector[i]){
                minElem = vector[i];
            }
        }
        return minElem;
    }

    public int [] Funk1() {
        int [] D = multiplyVectorMartix(addVectors(sortVector(addVectors(this.A.getVector(), this.B.getVector())), this.C.getVector()), multiplyMartix(this.MA.getMatrix(), this.ME.getMatrix()));
        return D;
    }

    public int Funk2() {
        int h = maxMatrixElem(addMartix(this.MF.getMatrix(), multiplyMartix(this.MG.getMatrix(), multiplyMartix(this.MH.getMatrix(), this.ML.getMatrix()))));
        return h;
    }

    public int Funk3() {
        int s = minVectorElem(multiplyVectorMartix(this.O.getVector(), transMatrix(multiplyMartix(this.MP.getMatrix(), this.MM.getMatrix())))) + multiplyVectors(this.R.getVector(), sortVector(this.S.getVector()));
        return s;
    }
}
