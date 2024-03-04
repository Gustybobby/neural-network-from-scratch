package mdp;

import java.util.ArrayList;

public abstract class State {
    public double reward;
    public boolean lastActionIsRandom;

    public State(boolean lastActionIsRandom) {
        this.lastActionIsRandom = lastActionIsRandom;
    }

    public abstract State nextState(Agent agent, Action action);

    public abstract boolean isTerminal();

    public abstract ArrayList<Integer> getActionSpace();

    public abstract double calculateReward();

    public abstract double[] getFlatState();

    public abstract void display();
}
