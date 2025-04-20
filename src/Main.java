import model.Board;
import model.GameStatus;
import model.Move;
import model.Player;
import player.HumanPlayerController;
import player.PlayerController;
import player.SmartAIPlayerController;

import java.util.Scanner;

public class Main {
    final private int boardSize = 10;
    private Board board;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;
    private final Scanner scanner = new Scanner(System.in);

    private Player currentPlayer = Player.player_one;
    private final PlayerController playerOneController = new HumanPlayerController(scanner);
    private final PlayerController playerTwoController = new RandomAIPlayerController();
    private final PlayerController playerTwoController = new SmartAIPlayerController(Player.player_two);

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("Welcome to Gomoku!");
        board = new Board(boardSize);

        while (gameStatus == GameStatus.IN_PROGRESS) {
            printBoard();
            System.out.println("Turn: " + currentPlayer);
            Move move = currentPlayer == Player.player_one ? playerOneController.makeMove(board) : playerTwoController.makeMove(board);
            board.set(move.row(), move.col(), currentPlayer.getSymbol());

            if (board.isWinner(move.row(), move.col(), currentPlayer.getSymbol())) {
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
                System.out.printf(" %c ", board.get(row, col));
            }

            System.out.println();
        }
    }
}