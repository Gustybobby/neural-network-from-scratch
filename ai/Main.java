import activation.Tanh;
import cost.SquaredError;
import layer.Layer;
import main.NeuralNetwork;

public class Main {
    public static void main(String[] args){
        Layer[] layers = {
            new Layer(4, 8, new Tanh()),
            new Layer(8, 8, new Tanh()),
            new Layer(8, 1, new Tanh())
        };
        NeuralNetwork nn = new NeuralNetwork(layers, new SquaredError(), 0.01);
        double[] a = {0,0,0,0};
        double[] b = {0,0,1,0};
        double[] c = {0,1,0,0};
        double[] d = {1,1,1,1};
        double[][] inputs = {a,b,c,d};
        double[] out0 = {0};
        double[] out1 = {1};
        double[][] outputs = {out1, out0, out0, out1};
        for(int i=0;i<10000;i++){
            nn.batchTrain(inputs, outputs);
        }
        System.out.println(nn.forward(a)[0]);
        System.out.println(nn.forward(b)[0]);
        System.out.println(nn.forward(c)[0]);
        System.out.println(nn.forward(d)[0]);
    }
}
