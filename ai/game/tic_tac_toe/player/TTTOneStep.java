package game.tic_tac_toe.player;

import java.util.ArrayList;

import easy_math.Calculation;
import mdp.Action;
import mdp.State;

public class TTTOneStep extends TTTPlayer {

    public TTTOneStep(int turn, TTTPlayer opponent) {
        super(turn, opponent);
    }

    public Action action() {
        ArrayList<Integer> actionSpace = this.env.currentState.getActionSpace();
        for (Integer move : actionSpace) {
            Action action = new Action(move, false);
            State nextState = this.env.currentState.nextState(this, new Action(move, false));
            if (nextState.reward == this.turn) {
                return action;
            }
        }
        return new Action(Calculation.randomFromArrayList(actionSpace), false);
    }
}
