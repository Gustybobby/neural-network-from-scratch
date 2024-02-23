package model;

import java.util.ArrayList;
import java.util.Arrays;

import easy_math.Calculation;
import easy_math.Display;

public class Episode {
    public ArrayList<double[]> stateList = new ArrayList<double[]>();
    public ArrayList<double[]> rewardList = new ArrayList<double[]>();
    public double[][] states;
    public double[][] values;

    public void calculateStateValues(int player){
        double[][] stateArray = new double[this.stateList.size()][stateList.get(0).length];
        for(int i=0;i<this.stateList.size();i++){
            stateArray[i] = this.stateList.get(i);
        }
        this.states = stateArray;
        double[][] valueArray = new double[this.states.length][rewardList.get(0).length];
        for(int i=this.states.length-1;i>=0;i--){
            if(i == this.states.length-1){
                double[] factorArray = new double[rewardList.get(0).length];
                Arrays.fill(factorArray, player);
                valueArray[i] = Calculation.arrayMul(this.rewardList.get(i), factorArray);
                continue;
            }
            valueArray[i] = Calculation.arraySum(this.rewardList.get(i), valueArray[i+1]);
        }
        this.values = valueArray;
    }

    public double[][] getStates(){
        return this.states;
    }

    public double[][] getStateValues(){
        return this.values;
    }

    public void prints(){
        for(int i=0;i<stateList.size();i++){
            double[] state = this.stateList.get(i);
            double[] reward = this.rewardList.get(i);
            System.out.print("State: ");
            Display.printArray(state);
            System.out.print("Reward: ");
            Display.printArray(reward);
        }
    }

    public void printCalculatedStates(){
        for(int i=0;i<this.states.length;i++){
            System.out.print("State: ");
            Display.printArray(this.states[i]);
            System.out.print("State Value: ");
            Display.printArray(this.values[i]);
        }
    }
}
