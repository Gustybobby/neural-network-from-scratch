package policy;

import tic_tac_toe.TicTacToe;

public class OneStepPolicy implements Policy{
    Policy policy;

    public OneStepPolicy(){
        this.policy = new RandomPolicy();
    }
    
    public double makeMove(int player, TicTacToe tTT){
        int[] actionSpace = tTT.board.getEmptySlotIndices();
        for(int i=0;i<actionSpace.length;i++){
            int move = actionSpace[i];
            tTT.board.makeMove(player, move);
            double score = tTT.evaluateMove(move);
            if(score == player){
                return tTT.evaluateMove(move);
            }
            tTT.board.makeMove(0, move);
        }
        return policy.makeMove(player, tTT);
    }

    public double learn(double[][] states, double[][] expectedOutputs){
        return 0;
    }

    public void setCompeteParams(){
    }

    public void setTrainingParams(){
    }
}
