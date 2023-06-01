import java.util.*;

public class Board {

    int[][] board;
    String firstPlayer;
    String secondPlayer;

    public Board(String firstPlayer, String secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        board = new int[3][3];
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
