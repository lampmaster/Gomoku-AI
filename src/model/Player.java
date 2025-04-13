package model;

public enum Player {
    player_one('X'),
    player_two('O');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public Player next() {
        return this == player_one ? player_two : player_one;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
