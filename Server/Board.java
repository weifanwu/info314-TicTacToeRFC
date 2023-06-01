import java.util.*;

public class Board {

    int[][] board;
    String firstPlayer;
    String secondPlayer;
    boolean start;
    String name;
    String turn;

    public Board(String firstPlayer, String name) {
        this.name = name;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = "";
        this.start = false;
        this.board = new int[3][3];
        this.turn = firstPlayer;
    }

    public void join(String secondPlayer) {
        this.start = true;
        this.secondPlayer = secondPlayer;
    }

    public boolean getStart() {
        return this.start;
    }

    public String getTurn() {
        return this.turn;
    }

    public void changeTurn() {
        if (this.turn.equals(this.firstPlayer)) {
            this.turn = secondPlayer;
        } else {
            this.turn = firstPlayer;
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean move(int x, int y, String player) {
        if (board[y - 1][x - 1] != 0) {
            return false;
        } else {
            if (player.equals(firstPlayer)) {
                board[y - 1][x - 1] = 1;
            } else {
                board[y - 1][x - 1] = -1;
            }
        }
        return true;
    }

    public String toString() {
        String result = "|";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    result += "X|";
                } else if (board[i][j] == -1) {
                    result += "O|";
                } else {
                    result += "*|";
                }
            }
        }
        return result;
    }
}
