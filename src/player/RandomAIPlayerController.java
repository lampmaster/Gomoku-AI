package player;

import model.Move;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAIPlayerController implements PlayerController {
    @Override
    public Move makeMove(char[][] board) {
        int row = ThreadLocalRandom.current().nextInt(0, board.length);
        int col = ThreadLocalRandom.current().nextInt(0, board.length);

        return new Move(row, col);
    }
}
