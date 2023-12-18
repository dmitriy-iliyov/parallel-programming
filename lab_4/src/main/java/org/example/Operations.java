package org.example;


//R = max(Z)*(B*MV) + e*X*(MM*MC)
// MV, MC
// MM, R
// -
// B,X, e, Z

public class Operations {
    public final int N = 1000;
    public final int H = N/2;

    public int e = 0;
    public int [] B = new int [N];
    public int [] Z = new int [N];
    public int [] X = new int [N];
    public int [][] MV = new int[N][N];
    public int [][] MM = new int[N][N];
    public int [][] MC = new int[N][N];

    public int maxZ = 0;
    public int [] A = new int[N];
    public int [] A2 = new int[N];
    public int [] C = new int[N];
    public int [] D = new int[N];
    public int [][] MA = new int[N][N];
    public int [] R = new int[N];


    private int flagE = 0;
    private int flagB = 0;
    private int flagZ = 0;
    private int flagX = 0;
    private int flagMV = 0;
    private int flagMM = 0;
    private int flagMC = 0;

    private int firstAndSecondOperationEnd = 0;
    private int thirdAndFourthOperationEnd = 0;
    private int fifthOperationEnd = 0;
    private int sixOperationEnd = 0;
    private int allOperationEnded = 0;
    private int lastOperationFlag = 0;


    public synchronized void putMaxZ(int halfMax){
        if (halfMax > maxZ)
            maxZ = halfMax;
    }
    public synchronized void putAelement(int value, int i){
        this.A[i] = value;
    }
    public synchronized void putA2element(int value, int i){
        this.A2[i] = value;
    }
    public synchronized void putCelement(int value, int i){
        this.C[i] = value;
    }
    public synchronized void putDelement(int value, int i){
        this.D[i] = value;
    }
    public synchronized void putMAelement(int i, int j, int x){ MA[i][j] = x;}
    public synchronized void putRelement(int value, int i){
        this.R[i] = value;
    }




    public synchronized void waitE(){
        try{
            if (flagE == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalE(){
        notify();
        flagE = 1;
    }
    public synchronized void waitZ(){
        try{
            if (flagZ == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalZ(){
        notify();
        flagZ = 1;
    }
    public synchronized void waitB(){
        try{
            if (flagB == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalB(){
        notify();
        flagB = 1;
    }
    public synchronized void waitX(){
        try{
            if (flagX == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalX(){
        notify();
        flagX = 1;
    }
    public synchronized void waitMV(){
        try{
            if (flagMV == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalMV(){
        notify();
        flagMV = 1;
    }
    public synchronized void waitMM(){
        try{
            if (flagMM == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalMM(){
        notify();
        flagMM = 1;
    }
    public synchronized void waitMC(){
        try{
            if (flagMC == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalMC(){
        notify();
        flagMC = 1;
    }

    public synchronized void waitFirstAndSecondOperationEnd(){
        try{
            if (firstAndSecondOperationEnd == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalFirstAndSecondOperationEnd(){
        notify();
        firstAndSecondOperationEnd = 1;
    }

    public synchronized void waitThirdAndFourthOperationEnd(){
        try{
            if (thirdAndFourthOperationEnd == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalThirdAndFourthOperationEnd(){
        notify();
        thirdAndFourthOperationEnd = 1;
    }
    public synchronized void waitFifthOperationEnd(){
        try{
            if (fifthOperationEnd == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalFifthOperationEnd(){
        notify();
        fifthOperationEnd = 1;
    }
    public synchronized void waitSixOperationEnd(){
        try{
            if (sixOperationEnd == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signaSixOperationEnd(){
        notify();
        sixOperationEnd = 1;
    }
    public synchronized void waitLastOperationEnded(){
        try{
            if (lastOperationFlag == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalLastOperationEnded(){
        notify();
        lastOperationFlag = 1;
    }
    public synchronized void waitAllOperationEnded(){
        try{
            if (allOperationEnded == 0) wait();
        }catch(Exception e) {}
    }
    public synchronized void signalAllOperationEnded(){
        notify();
        allOperationEnded = 1;
    }


    public synchronized int [][] getMA() {
        return this.MA;
    }


    public synchronized void printMatrx(int [][] matrix){
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    public synchronized void printVector(int [] vector){
        for (int i = 0; i < this.N; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println(" ");
    }

}

