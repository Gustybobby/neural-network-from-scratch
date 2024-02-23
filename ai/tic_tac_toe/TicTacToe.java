package tic_tac_toe;

public class TicTacToe {
    public int streakToWin;
    public Board board;

    public TicTacToe(int streakToWin, int width){
        this.streakToWin = streakToWin;
        this.board = new Board(width);
    }

    public int evaluateScore(){
        for(int i=0;i<this.board.state.length;i++){
            int score = evaluateMove(i);
            if(score != 0){
                return score;
            }
        }
        return 0;
    }

    public int evaluateMove(int index){
        int player = this.board.state[index];
        if(player == 0){
            return 0;
        }
        int vt = this.superImposeDirections(index, player, 0, 4);
        if(vt >= streakToWin){
            return player;
        }
        int hz = this.superImposeDirections(index, player, 2, 6);
        if(hz >= streakToWin){
            return player;
        }
        int dg1 = this.superImposeDirections(index, player, 1, 5);
        if(dg1 >= streakToWin){
            return player;
        }
        int dg2 = this.superImposeDirections(index, player, 3, 7);
        if(dg2 >= streakToWin){
            return player;
        }
        return 0;
    }

    public int searchStreak(int index, int streakedPlayer, int direction){
        int player = this.board.state[index];
        if(player != streakedPlayer){
            return 0;
        }
        int newIndex = this.board.directionIndex(index, direction);
        if(newIndex == -1){
            return 1;
        }
        return 1 + this.searchStreak(newIndex, streakedPlayer, direction);
    }

    public int superImposeDirections(int index, int player, int direction1, int direction2){
        int streak1 = this.searchStreak(index, player, direction1);
        int streak2 = this.searchStreak(index, player, direction2);
        return this.superImposeStreaks(streak1, streak2);
    }

    public int superImposeStreaks(int streak1, int streak2){
        if(streak1 > 0 && streak2 > 0){
            return streak1 + streak2 - 1;
        }
        return streak1 + streak2;
    }
}
