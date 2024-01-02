package mancala;

public class NoSuchPlayerException extends Exception {
    private static final long serialVersionUID = -8180056164753576764L;
    public NoSuchPlayerException(final String message) {
        super(message);
    }    
}