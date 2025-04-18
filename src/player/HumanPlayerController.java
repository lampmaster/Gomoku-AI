package player;

import model.Board;
import model.Move;
import model.Player;

import java.util.Scanner;

public class HumanPlayerController implements PlayerController {
    private final Scanner scanner;

    public HumanPlayerController(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move makeMove(Board board) {
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

                if (row < 1 || row > board.size() || col < 1 || col > board.size()) {
                    System.out.println("Invalid move. Please enter numbers between 1 and " + board.size());
                    continue;
                }

                if (board.get(row - 1,col - 1) == Player.player_two.getSymbol() || board.get(row - 1,col - 1) == Player.player_one.getSymbol()) {
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
}
