package game.tic_tac_toe.player;

import mdp.Agent;

public abstract class TTTPlayer extends Agent {
    public int turn;
    public TTTPlayer opponent;

    public TTTPlayer(int turn, TTTPlayer opponent) {
        this.turn = turn;
        this.opponent = opponent;
    }
}
