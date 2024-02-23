package model;

import policy.Policy;
import tic_tac_toe.Board;
import tic_tac_toe.TicTacToe;

public class Model {
    public Agent playerX;
    public Agent playerO;
    int width;
    int streakToWin;

    public Model(int width, int streakToWin, Policy policyX, Policy policyO){
        this.playerX = new Agent(1, policyX);
        this.playerO = new Agent(-1, policyO);
        this.width = width;
        this.streakToWin = streakToWin;
    }

    public void train(int iter, int logsEvery, int midCompete){
        for(int i=0;i<iter;i++){
            Episode episode = this.simulateEpisode();
            episode.calculateStateValues(1);
            this.playerX.policy.learn(episode.getStates(), episode.getStateValues());
            episode.calculateStateValues(-1);
            this.playerO.policy.learn(episode.getStates(), episode.getStateValues());
            if((i+1)%logsEvery==0){
                System.out.println("Completed iteration: "+(i+1));
                this.compete(midCompete);
                System.out.println("-------------------------------------------------");
            }
        }
    }

    public Episode simulateEpisode(){
        TicTacToe tTT = new TicTacToe(this.streakToWin, this.width);
        boolean xTurn = true;
        double score = tTT.evaluateScore();
        Episode episode = new Episode();
        while(true){
            episode.stateList.add(Board.getStateAsDouble(tTT.board.state));
            double[] scoreArray = {(double)score};
            episode.rewardList.add(scoreArray);
            boolean gameDrawn = tTT.board.noEmptySlots();
            if(gameDrawn || score != 0){
                break;
            }
            double nextStateScore = xTurn? this.playerX.makeMove(tTT) : this.playerO.makeMove(tTT);
            score = nextStateScore;
            xTurn = !xTurn;
        }
        return episode;
    }

    public void compete(int sampleSize){
        this.playerX.policy.setCompeteParams();
        this.playerO.policy.setCompeteParams();
        int xWins = 0;
        int oWins = 0;
        for(int i=0;i<sampleSize;i++){
            Episode episode = this.simulateEpisode();
            double score = episode.rewardList.get(episode.rewardList.size()-1)[0];
            if(score == 1){
                xWins++;
            }
            if(score == -1){
                oWins++;
            }
        }
        double xWinRate = (double)xWins/(double)sampleSize*100;
        double oWinRate = (double)oWins/(double)sampleSize*100;
        System.out.println("X win rate: "+(double)Math.round(xWinRate*100)/100+"% O win rate: "+(double)Math.round(oWinRate*100)/100+"%");
        this.playerX.policy.setTrainingParams();
        this.playerO.policy.setTrainingParams();
    }
}
