package model;

public record Move(int row, int col) {
    public Move {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Row and column must be non-negative");
        }
    }
}