package game.tic_tac_toe;

import java.util.ArrayList;

import game.tic_tac_toe.player.TTTPlayer;
import mdp.Action;
import mdp.Agent;
import mdp.State;

public class TTTState extends State {
    int[][] board;
    int width;
    int height;
    int streak;
    static int[][] directions = new int[][] { new int[] { 0, 1 }, new int[] { 1, 0 }, new int[] { 1, 1 },
            new int[] { 1, -1 } };

    public TTTState(int[][] board, int width, int height, int streak, boolean lastActionIsRandom) {
        super(lastActionIsRandom);
        this.board = board;
        this.width = width;
        this.height = height;
        this.streak = streak;
        this.reward = this.calculateReward();
    }

    public static TTTState initialState(int width, int height, int streak) {
        return new TTTState(new int[height][width], width, height, streak, false);
    }

    public static String intToSymbol(int cell) {
        switch (cell) {
            case 0:
                return "-";
            case 1:
                return "X";
            case -1:
                return "O";
            default:
                System.err.println("Invalid Cell Integer");
                System.exit(0);
                return "";
        }
    }

    public State nextState(Agent player, Action action) {
        int[][] nextBoard = new int[this.height][this.width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i * height + j == action.action()) {
                    if (this.board[i][j] != 0) {
                        System.err.println("Illegal Move");
                        System.exit(0);
                    }
                    nextBoard[i][j] = ((TTTPlayer) player).turn;
                    continue;
                }
                nextBoard[i][j] = this.board[i][j];
            }
        }
        return new TTTState(nextBoard, this.width, this.height, this.streak, action.isRandom());
    }

    public double calculateReward() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j] == 0) {
                    continue;
                }
                for (int[] direction : directions) {
                    int streak = this.searchStreak(i, j, direction[0], direction[1], board[i][j])
                            + this.searchStreak(i, j, -direction[0], -direction[1], board[i][j]) - 1;
                    if (streak >= this.streak) {
                        return board[i][j];
                    }
                }
            }
        }
        return 0;
    }

    int searchStreak(int row, int col, int dRow, int dCol, int turn) {
        if (row < 0 || col < 0 || row >= this.height || col >= this.width || this.board[row][col] != turn) {
            return 0;
        }
        return 1 + searchStreak(row + dRow, col + dCol, dRow, dCol, turn);
    }

    public boolean isTerminal() {
        if (this.getActionSpace().size() == 0) {
            return true;
        }
        return this.reward != 0;
    }

    public ArrayList<Integer> getActionSpace() {
        ArrayList<Integer> actionSpace = new ArrayList<Integer>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] == 0) {
                    actionSpace.add(i * height + j);
                }
            }
        }
        return actionSpace;
    }

    public double[] getFlatState() {
        double[] flatState = new double[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flatState[i * height + j] = board[i][j];
            }
        }
        return flatState;
    }

    public void display() {
        System.out.println("=".repeat(3 * width));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print("|" + intToSymbol(this.board[i][j]) + "|");
            }
            System.out.println();
        }
        System.out.println("=".repeat(3 * width));
    }

}
