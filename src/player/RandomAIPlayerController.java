package player;

import model.Board;
import model.Move;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAIPlayerController implements PlayerController {
    @Override
    public Move makeMove(Board board) {
        int row = ThreadLocalRandom.current().nextInt(0, board.size());
        int col = ThreadLocalRandom.current().nextInt(0, board.size());

        return new Move(row, col);
    }
}
