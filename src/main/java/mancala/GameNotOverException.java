package mancala;

public class GameNotOverException extends Exception {
    private static final long serialVersionUID = 7550159075989300301L;
    public GameNotOverException(final String message) {
        super(message);
    }    
}