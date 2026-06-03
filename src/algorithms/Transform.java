package algorithms;

public class Transform {

    // 1. Algoritma Matriks Translasi (MT)
    public static double[][] getTranslationMatrix(double tx, double ty) {
        return new double[][] {
            {1, 0, tx},
            {0, 1, ty},
            {0, 0, 1}
        };
    }

    // 2. Algoritma Matriks Rotasi (MR) 
    public static double[][] getRotationMatrix(double angleRadian) {
        return new double[][] {
            {Math.cos(angleRadian), -Math.sin(angleRadian), 0},
            {Math.sin(angleRadian),  Math.cos(angleRadian), 0},
            {0,                      0,                     1}
        };
    }

    // 3. Algoritma Matriks Penskalaan (MS)
    public static double[][] getScalingMatrix(double sx, double sy) {
        return new double[][] {
            {sx, 0,  0},
            {0,  sy, 0},
            {0,  0,  1}
        };
    }

    // Fungsi Perkalian Matriks 3x3 untuk membentuk Matriks Komposit (MC)
    public static double[][] multiplyMatrix(double[][] m1, double[][] m2) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return result;
    }

    // Fungsi mengaplikasikan Matriks Komposit ke Titik Koordinat (x, y)
    public static int[] applyTransformation(int x, int y, double[][] compositeMatrix) {
        // Konversi ke koordinat homogen [x, y, 1]
        double[] point = {x, y, 1};
        double[] newPoint = new double[3];

        // Kalikan vektor titik dengan matriks komposit
        for (int i = 0; i < 3; i++) {
            newPoint[i] = 0;
            for (int j = 0; j < 3; j++) {
                newPoint[i] += compositeMatrix[i][j] * point[j];
            }
        }

        // Kembalikan ke koordinat raster diskret (integer)
        return new int[]{(int) Math.round(newPoint[0]), (int) Math.round(newPoint[1])};
    }
}