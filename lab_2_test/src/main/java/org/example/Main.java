package org.example;


public class Main {
    public static void main(String[] args) {
        Resources resources = new Resources();
        resources.time = System.currentTimeMillis();

        resources.d = getValue();
        for (int i = 0; i < resources.N; i++)
            resources.Z[i] = getValue();
        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++)
                resources.MM[i][j] = getValue();
        }
        for (int i = 0; i < resources.N; i++) {
            resources.B[i] = getValue();
            for (int j = 0; j < resources.N; j++)
                resources.MR[i][j] = getValue();
        }
        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                resources.MC[i][j] = getValue();
                resources.MZ[i][j] = getValue();
            }
        }
        for (int i = 0; i < resources.N; i++)
            resources.a = resources.a + resources.B[i] * resources.Z[i];

//second operation - (MZ*MM) -> MA
        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                for (int k = 0; k < resources.N; k++) {
                    resources.MA[i][j] += resources.MZ[i][k] * resources.MM[k][j];
                }
            }
        }
//fifth operation - (MA*a) -> MA2
        int a = resources.a;
        int [][] MA = resources.MA;

        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                resources.MA2[i][j] = a * MA[i][j];
            }
        }
        //third operation - (MR*MC) -> MB;
        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                for (int k = 0; k < resources.N; k++) {
                    resources.MB[i][j] += resources.MC[i][k] * resources.MR[k][j];
                }
            }
        }
//fours operation - MB*d -> MB2
        int d = resources.d;
        int [][] MB = resources.MB;

        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                resources.MB2[i][j] = d * MB[i][j];
            }
        }
//sixth operation - (MA2-MB2) -> MX
        int [][] MA2 = resources.MA2;
        int [][] MB2 = resources.MB2;
        for (int i = 0; i < resources.N; i++) {
            for (int j = 0; j < resources.N; j++) {
                resources.MX[i][j] = MA2[i][j]-MB2[i][j];
            }
        }
        System.out.println((double) (System.currentTimeMillis() - resources.time)/1000 + " sec");
    }

    private static int getValue(int userValue){
        return userValue;
    }

    private static int getValue(){
        return (int) (Math.random() * 10);
    }
}