package org.example;

import mpi.MPI;

import java.util.Arrays;

public class Ti {

/*
    a = min(B + Z * (MR * MX)) + (B * Z)

    1. MA = MR * MX
    2. A = Z * MA
    3. C = B + A
    4. b = min(C)
    5. c = B * Z
    6. a = b + c
 */

    public static void calculation(int rank) {
        int threadCount = Resources.threadCount;
        int step = Resources.step;
        int objectsSize = Resources.objectsSize;

        if (rank == 0) {
            int[] B = new int[objectsSize];
            int[] Z = new int[objectsSize];
            int[] Zn = new int[step];
            int[][] MX = new int[objectsSize][objectsSize];
            int[][] MR = new int[objectsSize][objectsSize];
            int[][] maPart;                                    // MA = MR * MX
            int[] aVectorPart;                                 // A = Z * MA
            int[] cVectorPart;                                 // C = B + A
            int cPart;                                          // c = B * Z
            int[] cPartSent = new int[1];
            int bPart = 0;                                      // b = min(C)
            int[] bPartSent = new int[1];
            int c = 0;
            int b = 0;
            int a = 0;

            Resources.generateVector(B);
            Resources.generateMatrix(MX);

            int[] BnToSend = Arrays.copyOfRange(B, step, objectsSize);
            int[][] MXnToSend = new int[objectsSize - step][objectsSize];
            for (int i = step; i < objectsSize; i++) {
                for (int j = 0; j < objectsSize; j++) {
                    MXnToSend[i - step][j] = MX[i][j];
                }
            }

            MPI.COMM_WORLD.Send(B, 0, objectsSize, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Send(BnToSend, 0, objectsSize - step, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Send(MXnToSend, 0, objectsSize - step, MPI.OBJECT, rank + 1, 0);

            int[][] MXn = Arrays.copyOfRange(MX, 0, step);
            int[] Bn = Arrays.copyOfRange(B, 0, step);

            MPI.COMM_WORLD.Recv(Z, 0, objectsSize, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(Zn, 0, step * (rank + 1), MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(MR, 0, objectsSize, MPI.OBJECT, rank + 1, 0);

            maPart = Resources.multipleMatrix(MXn, MR);
            aVectorPart = Resources.multipleVectorAndMatrix(maPart, Z);
            cVectorPart = Resources.concatenateVectors(B, aVectorPart);
            bPart = Resources.findMin(cVectorPart);
            cPart = Resources.multipleVectors(Bn, Zn);

            MPI.COMM_WORLD.Recv(bPartSent, 0, 1, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(cPartSent, 0, 1, MPI.INT, rank + 1, 0);

            if (bPartSent[0] < bPart) {
                b = bPartSent[0];
            } else {
                b = bPart;
            }
            c = cPart + cPartSent[0];

            a = b + c;

            System.out.println("result : " + a);
        }
        else if(rank == threadCount - 1){
            int [] B = new int[objectsSize];
            int [] Bn = new int [objectsSize - step * rank];
            int [] Z = new int [objectsSize];
            int [][] MXn = new int[step][objectsSize];
            int [][] MR = new int [objectsSize][objectsSize];
            int [][] maPart;                                    // MA = MR * MX
            int [] aVectorPart;                                 // A = Z * MA
            int [] cVectorPart;                                 // C = B + A
            int cPart;                                          // c = B * Z
            int bPart = 0;                                      // b = min(C)

            Resources.generateVector(Z);
            Resources.generateMatrix(MR);

            MPI.COMM_WORLD.Recv(B, 0, objectsSize, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Recv(Bn, 0, objectsSize - step * rank, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Recv(MXn, 0, objectsSize - step * rank, MPI.OBJECT, rank - 1, 0);

            int[] ZnToSend = Arrays.copyOfRange(Z, 0, step * rank);

            MPI.COMM_WORLD.Send(Z, 0, objectsSize, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(ZnToSend, 0, step * rank, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(MR, 0, objectsSize, MPI.OBJECT, rank - 1, 0);

            int [] Zn = Arrays.copyOfRange(Z, step * rank, step * (rank + 1));

            maPart = Resources.multipleMatrix(MXn, MR);
            aVectorPart = Resources.multipleVectorAndMatrix(maPart, Z);
            cVectorPart = Resources.concatenateVectors(B, aVectorPart);
            bPart = Resources.findMin(cVectorPart);
            cPart = Resources.multipleVectors(Bn, Zn);

            MPI.COMM_WORLD.Send(new int[] {bPart}, 0, 1, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(new int[] {cPart}, 0, 1, MPI.INT, rank - 1, 0);

        }
        else{
            int [] B = new int[objectsSize];
            int [] BnRecv = new int [objectsSize - step * rank];
            int [] Z = new int[objectsSize];
            int [] ZnRecv = new int [step * (rank + 1)];
            int [][] MXnRecv= new int[objectsSize - step * rank][objectsSize];
            int [][] MR = new int [objectsSize][objectsSize];
            int [][] maPart;                                    // MA = MR * MX
            int [] aVectorPart;                                 // A = Z * MA
            int [] cVectorPart;                                 // C = B + A
            int cPart = 0;
            int [] cPartSent = new int[1];
            int bPart = 0;
            int [] bPartSent = new int[1];

            MPI.COMM_WORLD.Recv(B, 0, objectsSize, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Recv(BnRecv, 0, objectsSize - step * rank, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Recv(MXnRecv, 0, objectsSize - step * rank, MPI.OBJECT, rank - 1, 0);

            int[] BnToSend = Arrays.copyOfRange(B, step, objectsSize);
            int[][] MXnToSend = new int[objectsSize - step * (rank + 1)][objectsSize];
            for (int i = step * (rank + 1); i < objectsSize; i++) {
                for (int j = 0; j < objectsSize; j++) {
                    MXnToSend[i - step * (rank + 1)][j] = MXnRecv[i - step * (rank + 1)][j];
                }
            }

            MPI.COMM_WORLD.Send(B, 0, objectsSize, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Send(BnToSend, 0, objectsSize - step * (rank + 1), MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Send(MXnToSend, 0, objectsSize - step * (rank + 1), MPI.OBJECT, rank + 1, 0);

            int [][] MXn = Arrays.copyOfRange(MXnRecv, 0, objectsSize - step * (rank + 1));
            int [] Bn = Arrays.copyOfRange(B, 0, objectsSize - step * (rank + 1));

            MPI.COMM_WORLD.Recv(Z, 0, objectsSize, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(ZnRecv, 0, step * (rank + 1), MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(MR, 0, objectsSize, MPI.OBJECT, rank + 1, 0);

            int [] ZnToSend = Arrays.copyOfRange(ZnRecv, 0, step * rank);

            MPI.COMM_WORLD.Send(Z, 0, objectsSize, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(ZnToSend, 0, step * rank, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(MR, 0, objectsSize, MPI.OBJECT, rank - 1, 0);

            int [] Zn = Arrays.copyOfRange(ZnRecv, step * rank, step * (rank + 1));

            maPart = Resources.multipleMatrix(MXn, MR);
            aVectorPart = Resources.multipleVectorAndMatrix(maPart, Z);
            cVectorPart = Resources.concatenateVectors(B, aVectorPart);
            bPart = Resources.findMin(cVectorPart);
            cPart = Resources.multipleVectors(Bn, Zn);

            MPI.COMM_WORLD.Recv(bPartSent, 0, 1, MPI.INT, rank + 1, 0);
            MPI.COMM_WORLD.Recv(cPartSent, 0, 1, MPI.INT, rank + 1, 0);

            bPart += bPartSent[0];
            cPart += cPartSent[0];

            MPI.COMM_WORLD.Send(new int[] {bPart}, 0, 1, MPI.INT, rank - 1, 0);
            MPI.COMM_WORLD.Send(new int[] {cPart}, 0, 1, MPI.INT, rank - 1, 0);
        }
    }
}
