package game.tic_tac_toe.player;

import java.util.Scanner;

import mdp.Action;

public class TTTHuman extends TTTPlayer {
    public Scanner scanner;

    public TTTHuman(int turn, Scanner scanner, TTTPlayer opponent) {
        super(turn, opponent);
        this.scanner = scanner;
    }

    public Action action() {
        System.out.println("Play a move:");
        String input = scanner.nextLine();
        return new Action(Integer.parseInt(input), false);
    }
}
