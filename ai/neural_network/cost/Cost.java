package neural_network.cost;

public abstract class Cost {
    public abstract double calculateCost(double value, double expectedValue);

    public abstract double calculateGradient(double value, double expectedValue);
}
