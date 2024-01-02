package mancala;

public class InvalidMoveException extends Exception {
    private static final long serialVersionUID = 5719407642088793732L;
    public InvalidMoveException(final String message) {
        super(message);
    }
}
