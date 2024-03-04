package neural_network.layer;

import neural_network.activation.Activation;
import neural_network.component.Neuron;

public class Layer {
    int inSize;
    public int size;
    public Neuron[] neurons;
    Activation activationFunction;

    public Layer(int inSize, int size, Activation activationFunction) {
        this.inSize = inSize;
        this.size = size;
        this.neurons = new Neuron[size];
        for (int i = 0; i < size; i++) {
            this.neurons[i] = new Neuron(inSize, i, activationFunction);
        }
        this.activationFunction = activationFunction;
    }

    public static void connects(Layer layerIn, Layer layerOut) {
        for (Neuron neuron : layerIn.neurons) {
            neuron.outNeurons = layerOut.neurons;
        }
        for (Neuron neuron : layerOut.neurons) {
            neuron.inNeurons = layerIn.neurons;
        }
    }

    public double[] forward(double[] input) {
        if (input.length != this.inSize) {
            System.err.println("Incorrect Input Dimension");
            System.exit(0);
        }
        double[] output = new double[this.size];
        for (int i = 0; i < this.size; i++) {
            output[i] = this.neurons[i].forward(input);
        }
        return output;
    }

    public void updateBackProps() {
        for (int i = 0; i < this.size; i++) {
            this.neurons[i].updateBackProp();
        }
    }

    public void updateGradients() {
        for (int i = 0; i < this.size; i++) {
            this.neurons[i].updateGradients();
        }
    }

    public void applyGradients(double learningRate) {
        for (int i = 0; i < this.size; i++) {
            this.neurons[i].applyGradients(learningRate);
        }
    }

    public void clearGradients() {
        for (int i = 0; i < this.size; i++) {
            this.neurons[i].clearGradients();
        }
    }
}
