package mancala;

public class PitNotFoundException extends Exception {
    private static final long serialVersionUID = 6916836353185132623L;
    public PitNotFoundException(final String message) {
        super(message);
    }
}
