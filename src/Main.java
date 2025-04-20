import model.Board;
import model.GameStatus;
import model.Move;
import model.Player;
import player.HumanPlayerController;
import player.PlayerController;
import player.SmartAIPlayerController;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    final private int boardSize = 10;
    private Board board;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;
    private final Scanner scanner = new Scanner(System.in);

    private Player currentPlayer = Player.player_one;
    private final PlayerController playerOneController = new HumanPlayerController(scanner);
    private final PlayerController playerTwoController = new SmartAIPlayerController(Player.player_two);
    private Move lastMove;

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("Welcome to Gomoku!");
        board = new Board(boardSize);

        while (gameStatus == GameStatus.IN_PROGRESS) {
            printBoard();
            System.out.println("Turn: " + currentPlayer);
            lastMove = currentPlayer == Player.player_one ? playerOneController.makeMove(board) : playerTwoController.makeMove(board);
            board.set(lastMove.row(), lastMove.col(), currentPlayer.getSymbol());

            if (board.isWinner(lastMove.row(), lastMove.col(), currentPlayer.getSymbol())) {
                gameStatus = currentPlayer == Player.player_one ? GameStatus.PLAYER_ONE_WIN : GameStatus.PLAYER_TWO_WIN;
                printBoard();
                System.out.println("Game Over! " + currentPlayer + " wins!");
                break;
            }

            if (board.isDraw()) {
                gameStatus = GameStatus.DRAW;
                printBoard();
                System.out.println("Game Over! It's a draw!");
                break;
            }

            currentPlayer = currentPlayer.getOpponent();
        }
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
                char symbol = board.get(row, col);
                String symbolToPrint = Objects.equals(lastMove, new Move(row, col)) ? "\u001B[32m" + symbol + "\u001B[0m" : String.valueOf(symbol);
                System.out.printf(" %s ", symbolToPrint);
            }

            System.out.println();
        }
    }
}