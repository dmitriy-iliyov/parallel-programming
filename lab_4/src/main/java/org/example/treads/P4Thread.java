package org.example.treads;

import org.example.Operations;

public class P4Thread extends Thread{
    int MAelement = 0;
    int Delement = 0;
    Operations operations;

    public P4Thread(Operations data) {
        this.operations = data;
    }

    @Override
    public void run(){

        operations.e = getValue(1);
        for(int i = 0; i < operations.N; i++){
            operations.B[i] = getValue(1);
            operations.Z[i] = getValue(1);
            operations.X[i] = getValue(1);
        }

        operations.signalE();
        operations.signalB();
        operations.signalZ();
        operations.signalX();

        operations.waitMM();
        operations.waitMC();
        operations.waitE();
        operations.waitX();

        for (int i = operations.H; i < operations.N; i++){
            operations.putAelement(operations.X[i] * operations.e, i);
        }

        for (int i = operations.H; i < operations.N; i++) {
            for (int j = 0; j < operations.N; j++) {
                for (int k = 0; k < operations.N; k++) {
                    MAelement += operations.MC[i][k] * operations.MM[k][j];
                }
                operations.putMAelement(i, j, MAelement);
                MAelement = 0;
            }
        }
        operations.signalThirdAndFourthOperationEnd();

        for (int i = operations.H; i < operations.N; i++) {
            for (int j = 0; j < operations.N; j++) {
                Delement += operations.MA[i][j] * operations.A[i];
            }
            operations.putDelement(Delement, i);
            Delement = 0;
        }
        operations.signaSixOperationEnd();
    }

    private int getValue(int userValue){
        return userValue;
    }

    private int getValue(){
        return (int) (Math.random() * 10);
    }
}
