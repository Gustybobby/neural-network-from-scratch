package policy;

import activation.Tanh;
import cost.SquaredError;
import layer.Layer;
import main.NeuralNetwork;
import tic_tac_toe.Board;
import tic_tac_toe.TicTacToe;

public class NeuralNetPolicy implements Policy{
    public double initGreed;
    public double greed;
    public NeuralNetwork nn;
    private Policy policy;

    public NeuralNetPolicy(double greed, double learningRate){
        this.initGreed = greed;
        this.greed = greed;
        Layer layer1 = new Layer(9, 16, new Tanh());
        Layer layer2 = new Layer(16, 16, new Tanh());
        Layer layer3 = new Layer(16, 16, new Tanh());
        Layer layer4 = new Layer(16, 16, new Tanh());
        Layer layer5 = new Layer(16, 1, new Tanh());
        Layer[] layers = {layer1,layer2,layer3,layer4,layer5};
        this.nn = new NeuralNetwork(layers, new SquaredError(), learningRate);
        this.policy = new RandomPolicy();
    }

    public double makeMove(int player, TicTacToe tTT){
        if(Math.random() < this.greed){
            return this.policy.makeMove(player, tTT);
        }
        int[] actionSpace = tTT.board.getEmptySlotIndices();
        int optimalMove = 0;
        double optimalPredictedValue = 0;
        for(int i=0;i<actionSpace.length;i++){
            int move = actionSpace[i];
            tTT.board.makeMove(player, move);
            double[] state = Board.getStateAsDouble(tTT.board.state);
            double predictedValue = this.nn.forward(state)[0];
            if(predictedValue > optimalPredictedValue){
                optimalMove = move;
                optimalPredictedValue = predictedValue;
            }
            else if(optimalMove == 0){
                optimalPredictedValue = predictedValue;
            }
            tTT.board.makeMove(0, move);
        }
        tTT.board.makeMove(player, optimalMove);
        return tTT.evaluateMove(optimalMove);
    }

    public double learn(double[][] states, double[][] expectedOutputs){
        return this.nn.batchTrain(states, expectedOutputs);
    }

    public void setCompeteParams(){
        this.greed = 0;
    }

    public void setTrainingParams(){
        this.greed = this.initGreed;
    }
}
