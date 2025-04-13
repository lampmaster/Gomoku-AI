import java.util.Scanner;

public class Main {
    final private int boardSize = 3;
    private int emptyCells = boardSize * boardSize;
    private char[][] board;
    private Player currentPlayer = Player.player_one;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;

    private final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        System.out.println("Welcome to Gomoku!");
        board = createBoard();

        while (gameStatus == GameStatus.IN_PROGRESS) {
            printBoard();
            System.out.println("Turn: " + currentPlayer);
            Move move = processUserInput();
            board[move.row][move.col] = currentPlayer.getSymbol();
            emptyCells =- 1;

            if (isWinner(move.row, move.col)) {
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

    public Move processUserInput() {
        int row;
        int col;

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            if (parts.length != 2) {
                System.out.println("Please enter exactly two number: row col");
                continue;
            }
            try {
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);

                if (row < 1 || row > boardSize || col < 1 || col > boardSize) {
                    System.out.println("Invalid move. Please enter numbers between 1 and " + boardSize);
                    continue;
                }

                if (board[row - 1][col - 1] == Player.player_two.getSymbol() || board[row - 1][col - 1] == Player.player_one.getSymbol()) {
                    System.out.println("Cell is already taken. Please choose another cell.");
                    continue;
                }

                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter two integers.");
            }
        }

        return new Move(row - 1, col - 1);
    }

    public record Move(int row, int col) {}

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
        int cellsToWin = 3;

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