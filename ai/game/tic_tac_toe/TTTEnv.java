package game.tic_tac_toe;

import game.tic_tac_toe.player.TTTNeuralNet;
import game.tic_tac_toe.player.TTTPlayer;
import mdp.Agent;
import mdp.Environment;

public class TTTEnv extends Environment {
    public TTTPlayer playerX;
    public TTTPlayer playerO;

    public TTTEnv(TTTState initialState, TTTPlayer playerX, TTTPlayer playerO) {
        super(initialState);
        this.playerX = playerX;
        this.playerO = playerO;
        this.playerX.env = this;
        this.playerO.env = this;
    }

    public void process() {
        this.resetState();
        Agent agent = this.playerX;
        while (!this.currentState.isTerminal()) {
            this.recordState();
            this.traverse(agent);
            agent = agent == this.playerX ? this.playerO : this.playerX;
        }
        this.recordState();
    }

    public void play() {
        this.resetState();
        Agent agent = this.playerX;
        this.currentState.display();
        while (!this.currentState.isTerminal()) {
            this.traverse(agent);
            this.currentState.display();
            agent = agent == this.playerX ? this.playerO : this.playerX;
        }
        System.out.println("Winner: " + TTTState.intToSymbol((int) this.currentState.reward));
    }

    public double evaluateNeuralNetwork() {
        int xWins = 0;
        int oWins = 0;
        double greed = ((TTTNeuralNet) playerO).greed;
        ((TTTNeuralNet) playerO).greed = 0;
        for (int j = 1; j <= 1000; j++) {
            this.process();
            double reward = this.currentState.reward;
            xWins += reward == 1 ? 1 : 0;
            oWins += reward == -1 ? 1 : 0;
        }
        ((TTTNeuralNet) playerO).greed = greed;
        System.out.println("X: " + xWins + " O: " + oWins);
        return ((double) xWins) / 1000;
    }
}
