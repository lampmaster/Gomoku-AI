import model.Move;
import model.Player;
import player.HumanPlayerController;
import player.PlayerController;
import player.RandomAIPlayerController;

import java.util.Scanner;

public class Main {
    final private int boardSize = 10;
    private int emptyCells = boardSize * boardSize;
    private char[][] board;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;
    private final Scanner scanner = new Scanner(System.in);

    private Player currentPlayer = Player.player_one;
    private final PlayerController playerOneController = new HumanPlayerController(scanner);
    private final PlayerController playerTwoController = new RandomAIPlayerController();

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("Welcome to Gomoku!");
        board = createBoard();

        while (gameStatus == GameStatus.IN_PROGRESS) {
            printBoard();
            System.out.println("Turn: " + currentPlayer);
            Move move = currentPlayer == Player.player_one ? playerOneController.makeMove(board) : playerTwoController.makeMove(board);
            board[move.row()][move.col()] = currentPlayer.getSymbol();
            emptyCells =- 1;

            if (isWinner(move.row(), move.col())) {
                gameStatus = currentPlayer == Player.player_one ? GameStatus.PLAYER_ONE_WIN : GameStatus.PLAYER_TWO_WIN;
                printBoard();
                System.out.println("Game Over! " + currentPlayer + " wins!");
                break;
            }

            if (isDraw()) {
                gameStatus = GameStatus.DRAW;
                printBoard();
                System.out.println("Game Over! It's a draw!");
                break;
            }

            currentPlayer = currentPlayer.next();
        }
    }

    public char[][] createBoard() {
        char[][] board = new char[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = '.';
            }
        }

        return board;
    }

    private void printBoard() {
        System.out.print("   ");

        for (int i = 1; i <= boardSize; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();
        System.out.print("   ");
        System.out.print("---".repeat(boardSize));

        System.out.println();

        for (int row = 0; row < boardSize; row++) {
            System.out.printf("%2d|", row + 1);

            for (int col = 0; col < boardSize; col++) {
                System.out.printf(" %c ", board[row][col]);
            }

            System.out.println();
        }
    }

    private boolean isDraw() {
        return emptyCells == 0;
    }

    private boolean isWinner(int row, int col) {
        int[] dxArr = {1, 0, 1, 1}, dyArr = {0, 1, 1, -1};
        int cellsToWin = 5;

        for (int directIndex = 0; directIndex < 4; directIndex++) {
            int count = 0;

            for (int i = -4; i < cellsToWin; i++) {
                int dx = i * dxArr[directIndex], dy = i * dyArr[directIndex];

                if (col + dx < 0 || row + dy < 0) {
                    continue;
                }

                if (col + dx >= boardSize || row + dy >= boardSize) {
                    break;
                }

                count = board[row + dy][col + dx] == currentPlayer.getSymbol() ? count + 1 : 0;
                if (count == cellsToWin) return true;
            }
        }

        return false;
    }
}