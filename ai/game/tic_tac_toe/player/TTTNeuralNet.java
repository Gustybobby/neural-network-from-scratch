package game.tic_tac_toe.player;

import java.util.ArrayList;

import easy_math.Calculation;
import mdp.Action;
import neural_network.NeuralNetwork;

public class TTTNeuralNet extends TTTPlayer {
    NeuralNetwork nn;
    public double greed;

    public TTTNeuralNet(int turn, NeuralNetwork nn, double greed, TTTPlayer opponent) {
        super(turn, opponent);
        this.nn = nn;
        this.greed = greed;
    }

    public Action action() {
        ArrayList<Integer> actionSpace = this.env.currentState.getActionSpace();
        int optimalMove = Calculation.randomFromArrayList(actionSpace);
        if (Math.random() >= this.greed) {
            double[] nextState = this.env.currentState.nextState(this, new Action(optimalMove, true)).getFlatState();
            double optimalStateValue = this.nn.forward(nextState)[0];
            for (Integer move : actionSpace) {
                nextState = this.env.currentState.nextState(this, new Action(move, false)).getFlatState();
                double stateValue = this.nn.forward(nextState)[0];
                if ((double) turn * stateValue > (double) turn * optimalStateValue) {
                    optimalMove = move;
                    optimalStateValue = stateValue;
                }
            }
            return new Action(optimalMove, false);
        }
        return new Action(optimalMove, true);
    }
}
