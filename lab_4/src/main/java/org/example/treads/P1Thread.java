package org.example.treads;

import org.example.Operations;

public class P1Thread extends Thread{

    int A2element = 0;
    Operations operations;

    public P1Thread(Operations data){
        this.operations = data;
    }

    @Override
    public void run(){

        for (int i = 0; i < operations.N; i++) {
            for (int j = 0; j < operations.N; j++) {
                operations.MV[i][j] = getValue(1);
                operations.MC[i][j] = getValue(1);
            }
        }

        operations.signalMC();
        operations.signalMV();

        operations.waitZ();
        operations.waitB();

        int halfZmax = operations.Z[0];
        for (int i = operations.H; i < operations.N; i++){
            if(halfZmax < operations.Z[i])
                halfZmax = operations.Z[i];
        }
        operations.putMaxZ(halfZmax);

        for (int i = operations.H; i < operations.N; i++) {
            for (int j = 0; j < operations.N; j++) {
                A2element += operations.MV[i][j] * operations.B[i];
            }
            operations.putA2element(A2element, i);
            A2element = 0;
        }
        operations.signalFirstAndSecondOperationEnd();

        for(int i = operations.H; i < operations.N; i++){
            operations.putCelement(operations.maxZ * operations.A2[i],i);
        }
        operations.signalFifthOperationEnd();

        for (int i = operations.H; i < operations.N; i++) {
            operations.putRelement(operations.C[i] + operations.D[i], i);
        }
        operations.signalLastOperationEnded();
    }

    private int getValue(int userValue){
        return userValue;
    }

    private int getValue(){
        return (int) (Math.random() * 10);
    }
}
