package easy_math;

import java.util.ArrayList;

public final class Calculation {
    public static double[] arraySum(double[] array1, double[] array2) {
        double[] arraySum = new double[array1.length];
        for (int i = 0; i < array1.length; i++) {
            arraySum[i] = array1[i] + array2[i];
        }
        return arraySum;
    }

    public static double[] arrayMul(double[] array1, double[] array2) {
        double[] arrayMul = new double[array1.length];
        for (int i = 0; i < array1.length; i++) {
            arrayMul[i] = array1[i] * array2[i];
        }
        return arrayMul;
    }

    public static int randomFromArray(int[] array) {
        return array[(int) Math.round(Math.random() * (array.length - 1))];
    }

    public static <T> T randomFromArrayList(ArrayList<T> list) {
        return list.get((int) Math.round(Math.random() * (list.size() - 1)));
    }

    public static double[][] slice(double[][] originalArray, int start, int stop) {
        double[][] slicedArray = new double[stop - start][originalArray[0].length];
        for (int i = start; i < stop; i++) {
            slicedArray[i - start] = originalArray[i];
        }
        return slicedArray;
    }
}
