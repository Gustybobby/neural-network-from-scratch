package mdp;

import java.util.ArrayList;

import easy_math.Calculation;

public abstract class Environment {
    State initialState;
    public State currentState;
    public ArrayList<State> stateList = new ArrayList<State>();

    public Environment(State initialState) {
        this.initialState = initialState;
        this.currentState = initialState;
    }

    public void traverse(Agent agent) {
        this.currentState = this.currentState.nextState(agent, agent.action());
    }

    public void recordState() {
        this.stateList.add(this.currentState);
    }

    public void resetState() {
        this.currentState = this.initialState;
        this.stateList = new ArrayList<State>();
    }

    public double[][] getTrainingStates(int start, boolean important) {
        double[][] states = new double[this.stateList.size()][this.stateList.get(0).getFlatState().length];
        int importantCutOff = 0;
        for (int i = this.stateList.size() - 1; i >= start; i--) {
            states[i] = this.stateList.get(i).getFlatState();
            if (important && i > importantCutOff && this.stateList.get(i).lastActionIsRandom) {
                importantCutOff = i;
            }
        }
        if (important && importantCutOff > 0) {
            return Calculation.slice(states, importantCutOff, states.length);
        }
        return states;
    }

    public double[][] getTrainingValues(int start, double discount, boolean important) {
        double[][] values = new double[this.stateList.size()][1];
        double g = 0;
        int importantCutOff = 0;
        for (int i = this.stateList.size() - 1; i >= start; i--) {
            g = this.stateList.get(i).reward + discount * g;
            values[i] = new double[] { g };
            if (important && i > importantCutOff && this.stateList.get(i).lastActionIsRandom) {
                importantCutOff = i;
            }
        }
        if (important && importantCutOff > 0) {
            return Calculation.slice(values, importantCutOff, values.length);
        }
        return values;
    }

    public abstract void process();
}
