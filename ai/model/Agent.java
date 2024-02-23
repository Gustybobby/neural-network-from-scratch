package model;

import policy.Policy;
import tic_tac_toe.TicTacToe;

public class Agent {
    public int player;
    public Policy policy;

    public Agent(int player, Policy policy){
        this.player = player;
        this.policy = policy;
    }

    public double makeMove(TicTacToe tTT){
        return policy.makeMove(player, tTT);
    }
}
