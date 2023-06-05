
public class Board {

    int[][] board;
    int playerOneID;
    int playerTwoID;
    boolean start;
    int gameID;
    int turn;
    int winnerID;
    CombinedSocket playerOne;
    CombinedSocket playerTwo;

    public Board(int firstPlayer, int gameID, CombinedSocket playerOne) {
        this.gameID = gameID;
        this.playerOneID = firstPlayer;
        this.playerTwoID = 0;
        this.start = false;
        this.board = new int[3][3];
        this.turn = playerOneID;
        this.winnerID = 0;
        this.playerOne = playerOne;
    }

    public void join(int secondPlayer, CombinedSocket playerTwo) {
        this.start = true;
        this.playerTwoID = secondPlayer;
        this.playerTwo = playerTwo;
    }

    public boolean getStart() {
        return this.start;
    }

    public int getTurn() {
        return this.turn;
    }

    public void changeTurn() {
        if (this.turn == this.playerOneID) {
            this.turn = playerTwoID;
        } else {
            this.turn = playerOneID;
        }
    }

    public String getName() {
        return String.valueOf(this.gameID);
    }

    public boolean move(int x, int y, int playerID) {
        if (board[3 - y][x - 1] == 0) {
            board[3 - y][x - 1] = playerID;
            changeTurn();
            isWon();
            return true;
        }
        return false;

    }

    public boolean move(int value, int playerID) {
        value -= 1;
        if (board[value / 3][value % 3] == 0) {
            board[value / 3][value % 3] = playerID;
            changeTurn();
            isWon();
            return true;
        }
        return false;

    }

    public boolean isFull() {
        boolean result = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    result = true;
                }
            }
        }
        return result;

    }

    public boolean isWon() {
        // checking main diagonal
        if ((board[0][0] == playerOneID) && (board[1][1] == playerOneID) && (board[2][2] == playerOneID)) {
            winnerID = playerOneID;
            return true;
        }

        if ((board[0][0] == playerTwoID) && (board[1][1] == playerTwoID) && (board[2][2] == playerTwoID)) {
            winnerID = playerTwoID;
            return true;
        }

        // checking second diagonal
        if ((board[0][2] == playerOneID) && (board[1][1] == playerOneID) && (board[2][0] == playerOneID)) {
            winnerID = playerOneID;
            return true;
        }

        if ((board[0][2] == playerTwoID) && (board[1][1] == playerTwoID) && (board[2][0] == playerTwoID)) {
            winnerID = playerTwoID;
            return true;
        }

        // checking rows
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == playerOneID) && (board[i][1] == playerOneID) && (board[i][2] == playerOneID)) {
                winnerID = playerOneID;
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == playerTwoID) && (board[i][1] == playerTwoID) && (board[i][2] == playerTwoID)) {
                winnerID = playerTwoID;
                return true;
            }
        }

        // checking columns
        for (int j = 0; j < 3; j++) {
            if ((board[0][j] == playerOneID) && (board[1][j] == playerOneID) && (board[2][j] == playerOneID)) {
                winnerID = playerOneID;
                return true;
            }

        }

        for (int j = 0; j < 3; j++) {
            if ((board[0][j] == playerTwoID) && (board[1][j] == playerTwoID) && (board[2][j] == playerTwoID)) {
                winnerID = playerTwoID;
                return true;
            }

        }
        return false;
    }

    public String toString() {
        String result = "BORD ";
        if (start) {
            result += gameID + " ";
            result += playerOneID + " ";
            result += playerTwoID + " ";
            result += turn + " ";
            String gameInfo = "|";
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == playerOneID) {
                        gameInfo += "X|";
                    } else if (board[i][j] == playerTwoID) {
                        gameInfo += "O|";
                    } else {
                        gameInfo += "*|";
                    }
                }
            }
            result += gameInfo + " ";
            if (winnerID != 0) {
                result += winnerID + " ";
            }
            result += "\n";
        } else {
            result += gameID + " ";
            result += playerOneID + "\n";

        }
        return result;
    }
}
