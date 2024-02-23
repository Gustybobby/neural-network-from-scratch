package cost;

public class SquaredError extends Cost{
    public double calculateCost(double value, double expectedValue){
        return Math.pow(value-expectedValue,2);
    }
    public double calculateGradient(double value, double expectedValue){
        return 2*(value-expectedValue);
    }
}
