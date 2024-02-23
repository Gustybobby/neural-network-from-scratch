package activation;

public class Tanh extends Activation {
    public double activate(double input){
        return Math.tanh(input);
    }
    public double gradient(double activatedValue){
        return 1 - Math.pow(activatedValue,2);
    }
}
