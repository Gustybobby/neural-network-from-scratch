package game.tic_tac_toe;

import easy_math.Calculation;
import game.tic_tac_toe.player.TTTPlayer;
import mcts.Node;
import mdp.Action;

public class TTTClassicalNode extends Node {
    public TTTPlayer player;
    public int lastAction;

    public TTTClassicalNode(TTTClassicalNode parent, TTTState state, int lastAction, TTTPlayer player) {
        super(parent, state);
        this.player = player;
        this.lastAction = lastAction;
    }

    public double selectionScore() {
        // UCB score
        // since the parent chooses the children value must be view from parent
        // perspective
        double turnFactor = (double) ((TTTClassicalNode) this.parent).player.turn;
        return turnFactor * this.value + Math.sqrt(Math.log(this.parent.visit) / (double) this.visit);
    }

    public Node selectNode() {
        Node selectedChild = Calculation.randomFromArrayList(this.children);
        double maxScore = selectedChild.selectionScore();
        for (Node child : this.children) {
            double score = child.selectionScore();
            if (score > maxScore) {
                maxScore = score;
                selectedChild = child;
            }
        }
        return selectedChild;
    }

    public Node optimalNode() {
        Node optimalChild = Calculation.randomFromArrayList(this.children);
        double maxScore = optimalChild.value;
        for (Node child : this.children) {
            double score = child.value;
            if ((double) this.player.turn * score > (double) this.player.turn * maxScore) {
                maxScore = score;
                optimalChild = child;
            }
        }
        return optimalChild;
    }

    public void expandNode() {
        if (this.hasChildren()) {
            System.err.println("Already expanded node");
            System.exit(0);
        }
        for (Integer move : this.state.getActionSpace()) {
            Action action = new Action(move, false);
            TTTState nextState = (TTTState) this.state.nextState(player, action);
            TTTClassicalNode nextNode = new TTTClassicalNode(this, nextState, action.action(), player.opponent);
            this.children.add(nextNode);
        }
    }

    public double calculateValue(double childValue) {
        if (this.isTerminal()) {
            return this.getReward();
        }
        if (!this.hasChildren()) {
            return this.rollOutValue();
        }
        return this.value + (childValue - this.value) / (double) this.visit;
    }

    public double update(double childValue) {
        this.value = this.calculateValue(childValue);
        this.visit++;
        // no children means the node value is a roll out
        return this.hasChildren() ? childValue : this.value;
    }

    public double rollOutValue() {
        TTTState state = (TTTState) this.state;
        TTTPlayer currPlayer = this.player;
        while (!state.isTerminal()) {
            Action action = new Action(Calculation.randomFromArrayList(state.getActionSpace()), true);
            state = (TTTState) state.nextState(currPlayer, action);
            currPlayer = currPlayer.opponent;
        }
        return state.reward;
    }
}
