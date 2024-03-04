package game.tic_tac_toe.player;

import game.tic_tac_toe.TTTClassicalNode;
import game.tic_tac_toe.TTTState;
import mcts.mcts_model.classical.ClassicalMCTS;
import mdp.Action;

public class TTTMCTS extends TTTPlayer {
    ClassicalMCTS mcts;
    int iter;

    public TTTMCTS(int turn, int iter, TTTPlayer opponent) {
        super(turn, opponent);
        this.iter = iter;
    }

    public Action action() {
        this.mcts = new ClassicalMCTS();
        this.mcts.rootNode = new TTTClassicalNode(null, (TTTState) this.env.currentState, 0, this);
        this.mcts.iterate(this.iter);
        int move = ((TTTClassicalNode) this.mcts.rootNode.optimalNode()).lastAction;
        return new Action(move, false);
    }
}
