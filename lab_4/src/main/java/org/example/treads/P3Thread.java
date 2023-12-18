package org.example.treads;

import org.example.Operations;

public class P3Thread extends Thread{
    int MAelement = 0;
    int Delement = 0;
    Operations operations;

    public P3Thread(Operations data) {
        this.operations = data;
    }

    @Override
    public void run(){

        operations.waitMM();
        operations.waitMC();
        operations.waitE();
        operations.waitX();

        for (int i = 0; i < operations.H; i++){
           operations.putAelement(operations.X[i] * operations.e, i);
        }

        for (int i = 0; i < operations.H; i++) {
            for (int j = 0; j < operations.N; j++) {
                for (int k = 0; k < operations.N; k++) {
                    MAelement += operations.MC[i][k] * operations.MM[k][j];
                }
                operations.putMAelement(i, j, MAelement);
                MAelement = 0;
            }
        }
        operations.waitThirdAndFourthOperationEnd();

        for (int i = 0; i < operations.H; i++) {
            for (int j = 0; j < operations.N; j++) {
                Delement += operations.MA[i][j] * operations.A[i];
            }
            operations.putDelement(Delement, i);
            Delement = 0;
        }
        operations.waitSixOperationEnd();
        operations.signalAllOperationEnded();
    }
}
