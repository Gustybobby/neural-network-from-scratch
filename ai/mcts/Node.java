package mcts;

import java.util.ArrayList;

import mdp.State;

public abstract class Node {
    public State state;
    public Node parent;
    public ArrayList<Node> children = new ArrayList<Node>();
    public int visit;
    public double value;

    public Node(Node parent, State state) {
        this.parent = parent;
        this.state = state;
        this.visit = 1;
    }

    public boolean isTerminal() {
        return this.state.isTerminal();
    }

    public double getReward() {
        return this.state.reward;
    }

    public boolean hasChildren() {
        return this.children.size() != 0;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public abstract double selectionScore();

    public abstract Node selectNode();

    public abstract Node optimalNode();

    public abstract void expandNode();

    public abstract double calculateValue(double childValue);

    public abstract double update(double childValue);
}
