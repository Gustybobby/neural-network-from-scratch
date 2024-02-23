package activation;

public abstract class Activation {
    public abstract double activate(double input);
    public abstract double gradient(double activatedValue);
}
