package neural_network;

import neural_network.component.Neuron;
import neural_network.cost.Cost;
import neural_network.layer.Layer;

public class NeuralNetwork {
    Layer[] layers;
    Cost costFunction;
    public double learningRate;

    public NeuralNetwork(Layer[] layers, Cost costFunction, double learningRate) {
        this.layers = layers;
        this.costFunction = costFunction;
        this.learningRate = learningRate;
        this.connectLayers();
    }

    public double batchTrain(double[][] inputs, double[][] expectedOutputs) {
        double averageLoss = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] output = this.train(inputs[i], expectedOutputs[i]);
            averageLoss += this.loss(output, expectedOutputs[i]);
        }
        return averageLoss / (double) inputs.length;
    }

    public double[] train(double[] input, double[] expectedOutput) {
        double[] output = this.forward(input);
        this.backward(expectedOutput);
        return output;
    }

    public double[] forward(double[] input) {
        double[] output = input;
        for (int i = 0; i < this.layers.length; i++) {
            output = this.layers[i].forward(output);
        }
        return output;
    }

    // call this after forwarding only
    public void backward(double[] expectedOutput) {
        this.updateOutputLayerBackProps(expectedOutput);
        this.updateLayersBackProps();
        this.updateGradients();
    }

    public void gradientDescent() {
        this.applyGradients();
        this.clearGradients();
    }

    public double loss(double[] output, double[] expectedOutput) {
        double totalLoss = 0;
        for (int i = 0; i < output.length; i++) {
            double loss = this.costFunction.calculateCost(output[i], expectedOutput[i]);
            totalLoss += loss;
        }
        return totalLoss;
    }

    public void updateOutputLayerBackProps(double[] expectedOutput) {
        Layer outputLayer = this.layers[this.layers.length - 1];
        for (int i = 0; i < outputLayer.size; i++) {
            Neuron neuron = outputLayer.neurons[i];
            neuron.backPropValue = this.costFunction.calculateGradient(neuron.savedActivatedInput, expectedOutput[i]);
            neuron.backPropValue *= neuron.activationFunction.gradient(neuron.savedActivatedInput);
        }
    }

    public void updateLayersBackProps() {
        for (int i = this.layers.length - 2; i >= 0; i--) {
            Layer layer = this.layers[i];
            layer.updateBackProps();
        }
    }

    public void updateGradients() {
        for (int i = this.layers.length - 1; i >= 0; i--) {
            Layer layer = this.layers[i];
            layer.updateGradients();
        }
    }

    public void applyGradients() {
        for (int i = this.layers.length - 1; i >= 0; i--) {
            Layer layer = this.layers[i];
            layer.applyGradients(this.learningRate);
        }
    }

    public void clearGradients() {
        for (int i = this.layers.length - 1; i >= 0; i--) {
            Layer layer = this.layers[i];
            layer.clearGradients();
        }
    }

    private void connectLayers() {
        for (int i = 0; i < this.layers.length - 1; i++) {
            Layer.connects(this.layers[i], this.layers[i + 1]);
        }
    }

    public int getLayersLength() {
        return this.layers.length;
    }

    public int getLayerSize(int i) {
        return this.layers[i].neurons.length;
    }
}