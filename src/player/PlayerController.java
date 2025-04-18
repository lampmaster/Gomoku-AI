package player;

import model.Board;
import model.Move;

public interface PlayerController {
    Move makeMove(Board board);
}
