package org.example.treads;

import org.example.Operations;


public class P2Thread extends Thread{
    int A2element = 0;
    Operations operations;

    public P2Thread(Operations data) {
        this.operations = data;
    }

    @Override
    public void run(){

        for (int i = 0; i < operations.N; i++) {
            for (int j = 0; j < operations.N; j++) {
                operations.MM[i][j] = getValue(1);
            }
        }

        operations.signalMM();

        operations.waitZ();
        operations.waitB();
        operations.waitMV();

        int halfZmax = operations.Z[0];
        for (int i = 0; i < operations.H; i++){
            if(halfZmax < operations.Z[i])
                halfZmax = operations.Z[i];
        }
        operations.putMaxZ(halfZmax);

        for (int i = 0; i < operations.H; i++) {
            for (int j = 0; j < operations.N; j++) {
                A2element += operations.MV[i][j] * operations.B[i];
            }
            operations.putA2element(A2element, i);
            A2element = 0;
        }
        operations.waitFirstAndSecondOperationEnd();

        for(int i = 0; i < operations.H; i++){
            operations.putCelement(operations.maxZ * operations.A2[i],i);
        }
        operations.waitFifthOperationEnd();
        operations.waitAllOperationEnded();

        for (int i = 0; i < operations.H; i++) {
            operations.putRelement(operations.C[i] + operations.D[i], i);
        }
        operations.waitLastOperationEnded();
        operations.printVector(operations.R);
    }

    private int getValue(int userValue){
        return userValue;
    }

    private int getValue(){
        return (int) (Math.random() * 10);
    }
}
