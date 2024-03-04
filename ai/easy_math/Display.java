package easy_math;

public class Display {
    public static void printArray(double[] array) {
        System.out.print("[ ");
        for (int j = 0; j < array.length; j++) {
            System.out.print(array[j] + ", ");
        }
        System.out.println("]");
    }

    public static void printArray(int[] array) {
        System.out.print("[ ");
        for (int j = 0; j < array.length; j++) {
            System.out.print(array[j] + ", ");
        }
        System.out.println("]");
    }
}
