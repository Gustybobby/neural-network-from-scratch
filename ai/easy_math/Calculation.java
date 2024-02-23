package easy_math;
public final class Calculation {
    public static double[] arraySum(double[] array1, double[] array2){
        double[] arraySum = new double[array1.length];
        for(int i=0;i<array1.length;i++){
            arraySum[i] = array1[i] + array2[i];
        }
        return arraySum;
    }

    public static double[] arrayMul(double[] array1, double[] array2){
        double[] arrayMul = new double[array1.length];
        for(int i=0;i<array1.length;i++){
            arrayMul[i] = array1[i] * array2[i];
        }
        return arrayMul;
    }
}
