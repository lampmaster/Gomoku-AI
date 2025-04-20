package player;

import model.GameStatus;
import model.Move;
import model.Player;
import model.Board;

import java.util.*;


public class SmartAIPlayerController implements PlayerController {
    Player player;

    public SmartAIPlayerController(Player player) {
        this.player = player;
    }

    @Override
    public Move makeMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> availableMoves = getAvailableMoves(board);
        for (Move move : availableMoves) {
            board.set(move.row(), move.col(), player.getSymbol());
            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;
            int currentScore = minimax(board, move, false, 2, alpha, beta);
            board.set(move.row(), move.col(), '.');

            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int evaluate(Board board, Player player) {
        int finalScore = 0;
        int[] dxArr = {1, 0, 1, 1}, dyArr = {0, 1, 1, -1};

        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.get(row, col) == player.getSymbol()) {
                    int searchRadius = 4;

                    for (int directIndex = 0; directIndex < dxArr.length; directIndex++) {
                        StringBuilder finalLine = new StringBuilder();
                        int symbolsInRow = 0;
                        int dxDir = dxArr[directIndex], dyDir = dyArr[directIndex];

                        int prevRow = row - dyDir, prevCol = col - dxDir;

                        if (isBound(prevRow, prevCol, board.size()) && board.get(prevRow, prevCol) == player.getSymbol()) {
                            continue;
                        }

                        for (int i = -searchRadius; i <= searchRadius; i++) {
                            int dx = i * dxDir, dy = i * dyDir;

                            int checkRow = row + dy, checkCol = col + dx;

                            if (checkRow < 0 || checkCol < 0) {
                                continue;
                            }

                            if (checkCol >= board.size() || checkRow >= board.size()) {
                                break;
                            }

                            finalLine.append(board.get(checkRow, checkCol));
                            if (board.get(checkRow, checkCol) == player.getSymbol()) {
                                symbolsInRow++;
                            } else {
                                finalScore += getScore(symbolsInRow);
                                symbolsInRow = 0;
                            }
                        }

                        finalScore += getScore(symbolsInRow);
                    }
                }
            }
        }

        return finalScore;
    }

    private int getScore(int symbolsInRow) {
        return switch (symbolsInRow) {
            case 0 -> 0;
            case 1 -> 10;
            case 2 -> 100;
            case 3 -> 100000;
            case 4 -> 100000000;
            default -> 1000000000;

        };
    }

    private boolean isBound(int row, int col, int boardSize) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    private int minimax(Board board, Move currentMove, boolean isMaxPlayer, int depth, int alpha, int beta) {
        Player currentPlayer = isMaxPlayer ? player : player.getOpponent();
        Player prevPlayer = currentPlayer.getOpponent();

        GameStatus gameStatus = getGameStatus(board, currentMove.row(), currentMove.col(), prevPlayer);

        if (gameStatus != GameStatus.IN_PROGRESS || depth == 0) {
            return evaluate(board, prevPlayer) - evaluate(board, currentPlayer);
        }

        int bestScore = isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> nextAvailableMoves = getAvailableMoves(board);

        for (Move nextMove : nextAvailableMoves) {
            board.set(nextMove.row(), nextMove.col(), currentPlayer.getSymbol());
            int currentScore = minimax(board, nextMove, !isMaxPlayer, depth - 1, alpha, beta);
            board.set(nextMove.row(), nextMove.col(), '.');

            if (isMaxPlayer) {
                bestScore = Math.max(bestScore, currentScore);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, currentScore);
                beta = Math.min(beta, bestScore);
            }
            if (beta <= alpha) break;
        }

        return bestScore;
    }

    private GameStatus getGameStatus(Board board, int row, int col, Player player) {
        if (board.isWinner(row, col, player.getSymbol())) {
            return player == Player.player_one ? GameStatus.PLAYER_ONE_WIN : GameStatus.PLAYER_TWO_WIN;
        }

        return board.isDraw() ? GameStatus.DRAW : GameStatus.IN_PROGRESS;
    }

    public List<Move> getAvailableMoves(Board board) {
        Set<Move> candidates = new HashSet<>();
        int center = board.size() / 2;
        int radius = 2;

        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                char cell = board.get(i,j);
                if (cell == Player.player_one.getSymbol() || cell == Player.player_two.getSymbol()) {
                    for (int nearRow = i - radius; nearRow <= i + radius; nearRow++) {
                        for (int nearCol = j - radius; nearCol <= j + radius; nearCol++) {
                            if (isBound(nearRow, nearCol, board.size()) && board.get(nearRow, nearCol) == '.') {
                                candidates.add(new Move(nearRow, nearCol));
                            }
                        }
                    }
                }
            }
        }

        if (candidates.isEmpty()) {
            return board.getAllAvailableMoves();
        }

        List<Move> sortedMoves = new ArrayList<>(candidates);

        sortedMoves.sort(Comparator.comparingInt(
                move -> Math.abs(move.row() - center) + Math.abs(move.col() - center)
        ));

        return sortedMoves;
    }
}
