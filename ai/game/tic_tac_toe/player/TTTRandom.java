package game.tic_tac_toe.player;

import easy_math.Calculation;
import mdp.Action;

public class TTTRandom extends TTTPlayer {

    public TTTRandom(int turn, TTTPlayer opponent) {
        super(turn, opponent);
    }

    public Action action() {
        return new Action(Calculation.randomFromArrayList(this.env.currentState.getActionSpace()), false);
    }
}
