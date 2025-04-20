package model;

public class Board {
    private final char[][] board;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.board = createBoard(size);
    }

    private char[][] createBoard(int size) {
        char[][] board = new char[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = '.';
            }
        }

        return board;
    }

    public void set(int row, int col, char value) {
        board[row][col] = value;
    }

    public char get(int row, int col) {
        return board[row][col];
    }

    public int size() {
        return size;
    }

    public boolean isWinner(int row, int col, char winner) {
        int[] dxArr = {1, 0, 1, 1}, dyArr = {0, 1, 1, -1};
        int cellsToWin = 5;

        for (int directIndex = 0; directIndex < dxArr.length; directIndex++) {
            int count = 0;

            for (int i = -4; i < cellsToWin; i++) {
                int dx = i * dxArr[directIndex], dy = i * dyArr[directIndex];

                if (col + dx < 0 || row + dy < 0) {
                    continue;
                }

                if (col + dx >= board[0].length || row + dy >= board.length) {
                    break;
                }

                count = board[row + dy][col + dx] == winner ? count + 1 : 0;
                if (count == cellsToWin) return true;
            }
        }

        return false;
    }

    public boolean isDraw() {
        for (char[] row : board) {
            for (char cell : row) {
                if (!(cell == Player.player_one.getSymbol()) && !(cell == Player.player_two.getSymbol())) {
                    return false;
                }
            }
        }
        return true;
    }
}
