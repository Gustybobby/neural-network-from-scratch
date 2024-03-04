package neural_network;

import neural_network.activation.Tanh;
import neural_network.cost.SquaredError;
import neural_network.layer.Layer;

public class TestNeuralNetwork {
    public static void main(String[] args) {
        Layer[] layers = {
                new Layer(4, 64, new Tanh()),
                new Layer(64, 64, new Tanh()),
                new Layer(64, 1, new Tanh())
        };
        NeuralNetwork nn = new NeuralNetwork(layers, new SquaredError(), 0.01);
        double[] a = { 0, 0, 0, 0 };
        double[] b = { 0, 0, 1, 0 };
        double[] c = { 0, 1, 0, 0 };
        double[] d = { 1, 1, 1, 1 };
        double[][] inputs = { a, b, c, d };
        double[] out0 = { 0 };
        double[] out1 = { 1 };
        double[][] outputs = { out1, out0, out0, out1 };
        for (int i = 0; i < 50000; i++) {
            nn.batchTrain(inputs, outputs);
        }
        System.out.println(nn.forward(a)[0]);
        System.out.println(nn.forward(b)[0]);
        System.out.println(nn.forward(c)[0]);
        System.out.println(nn.forward(d)[0]);
    }
}
