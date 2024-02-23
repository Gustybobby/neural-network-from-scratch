package policy;

import tic_tac_toe.TicTacToe;

public interface Policy {
    public abstract double makeMove(int player, TicTacToe tTT);
    public abstract double learn(double[][] states, double[][] expectedOutputs);
    public abstract void setCompeteParams();
    public abstract void setTrainingParams();
}
