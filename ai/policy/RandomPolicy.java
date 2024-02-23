package policy;

import tic_tac_toe.TicTacToe;

public class RandomPolicy implements Policy {
    public double makeMove(int player, TicTacToe tTT){
        int[] actionSpace = tTT.board.getEmptySlotIndices();
        int randomAction = (int) Math.round(Math.random()*(actionSpace.length - 1));
        int index = actionSpace[randomAction];
        tTT.board.makeMove(player, index);
        return tTT.evaluateMove(index);
    }

    public double learn(double[][] states, double[][] expectedOutputs){
        return 0;
    }

    public void setCompeteParams(){
    }

    public void setTrainingParams(){
    }
}
