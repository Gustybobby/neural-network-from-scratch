package neural_network.activation;

public class Linear extends Activation {
    public double activate(double input) {
        return input;
    }

    public double gradient(double activatedValue) {
        return 1;
    }
}
