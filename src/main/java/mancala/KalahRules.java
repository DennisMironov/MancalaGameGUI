package mancala;

public class KalahRules extends GameRules {
    private static final long serialVersionUID = 304585189515622663L;
    /**
     * Constructor to initialize the game board.
     */
    public KalahRules() {
        super();
        setPlayer(1);
    }

    @Override
    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        setAnotherTurnIsRequired(false);
        if (!gameBoard.isPitOnPlayerSide(startPit, currentPlayer)) {
            throw new InvalidMoveException("Invalid pit number for player " + playerNum);
        }
        final int stoneCount = getNumStones(startPit);
        if (stoneCount == 0) {
            throw new InvalidMoveException("Selected pit is empty.");
        }
        final Countable store = gameBoard.getStore(playerNum);
        final int initialCount = store.getStoneCount();
        this.distributeStones(startPit);
        final int finalCount = store.getStoneCount();
        return finalCount - initialCount;
    }

    @Override
    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    /* default */ public int distributeStones(final int startPit) {
        // Empty the start pit
        final int stonesToDstr = gameBoard.removeStones(startPit);
        gameBoard.setIterator(startPit, (currentPlayer % 2) + 1, false); // kalah doesn’t skip the start pit
        Countable currentSpot;
        // will iterate through the data structure, skipping the opposite players store.
        for (int i = 1; i <= stonesToDstr; i++) {
            currentSpot = gameBoard.next();
            currentSpot.addStone();
            // check rule: If the last piece you drop is in your own Store, you capture the
            // last stone and take another turn.
            if ((i == stonesToDstr) && currentSpot.getClass() == Store.class) {
                setAnotherTurnIsRequired(true);
            } else if ((i == stonesToDstr) && gameBoard.isEmptyPitOnPlayerSide(currentSpot, currentPlayer)) {
                // check rule: If the last piece you drop is in an empty hole on your side.
                captureStones(gameBoard.getPitNum(currentSpot));
            }
        }

        return stonesToDstr;
    }

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    @Override
    /* default */ public int captureStones(final int stoppingPoint) {
        int capturedStones = 0;
        // in the last move in stoppinPoint pit should be one stone, otherwise do not
        // capture
        if (gameBoard.getNumStones(stoppingPoint) == 1) {
            final int oppositePit = 13 - stoppingPoint;
            capturedStones = gameBoard.getNumStones(oppositePit) + gameBoard.getNumStones(stoppingPoint);

            // put all captured stones into current player's store
            gameBoard.addToStore(currentPlayer, capturedStones);

            // set number of stones to 0
            gameBoard.removeStones(stoppingPoint);
            gameBoard.removeStones(oppositePit);
        }

        return capturedStones;
    }
}
