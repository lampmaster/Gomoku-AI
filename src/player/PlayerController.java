package player;

import model.Move;

public interface PlayerController {
    Move makeMove(char[][] board);
}
