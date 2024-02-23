package tic_tac_toe;

import java.util.Arrays;

public class Board {
    int width;
    public int[] state;

    public Board(int width){
        this.width = width;
        this.state = new int[width*width];
        Arrays.fill(state, 0);
    }

    public void makeMove(int player, int index){
        this.state[index] = player;
    }

    public int directionIndex(int index, int direction){
        switch(direction){
            case 0:
                return this.northIndex(index);
            case 1:
                return this.northEastIndex(index);
            case 2:
                return this.eastIndex(index);
            case 3:
                return this.southEastIndex(index);
            case 4:
                return this.southIndex(index);
            case 5:
                return this.southWestIndex(index);
            case 6:
                return this.westIndex(index);
            case 7:
                return this.northWestIndex(index);
            default:
                return -1;
        }
    }

    public int eastIndex(int index){
        if(index == -1 || index%this.width == 0){
            return -1;
        }
        return index-1;
    }

    public int westIndex(int index){
        if(index == -1 || (index+1)%this.width == 0){
            return -1;
        }
        return index+1;
    }

    public int northIndex(int index){
        if(index == -1 || index - this.width < 0){
            return -1;
        }
        return index - this.width;
    }

    public int southIndex(int index){
        if(index == -1 || index + this.width >= this.width*this.width){
            return -1;
        }
        return index + this.width;
    }

    public int northEastIndex(int index){
        return this.northIndex(this.eastIndex(index));
    }

    public int northWestIndex(int index){
        return this.northIndex(this.westIndex(index));
    }

    public int southEastIndex(int index){
        return this.southIndex(this.eastIndex(index));
    }

    public int southWestIndex(int index){
        return this.southIndex(this.westIndex(index));
    }

    public boolean noEmptySlots(){
        return this.getEmptySlotIndices().length == 0;
    }

    public int[] getEmptySlotIndices(){
        int emptyCount = 0;
        for(int i=0;i<this.state.length;i++){
            if(this.state[i] == 0){
                emptyCount++;
            }
        }
        int[] emptyIndices = new int[emptyCount];
        emptyCount = 0;
        for(int i=0;i<this.state.length;i++){
            if(this.state[i] == 0){
                emptyIndices[emptyCount] = i;
                emptyCount++;
            }
        }
        return emptyIndices;
    }

    public static double[] getStateAsDouble(int[] state){
        double[] doubleState = new double[state.length];
        for(int i=0;i<state.length;i++){
            doubleState[i] = state[i];
        }
        return doubleState;
    }

    public void printState(){
        for(int i=0;i<this.width;i++){
            for(int j=0;j<this.width;j++){
                System.out.print(this.state[i*width+j]+" ");
            }
            System.out.println();
        }
    }
}
