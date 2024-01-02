package mancala;

public class AyoRules extends GameRules {
    private static final long serialVersionUID = -6826791938551669325L;
    private static final int LAST_PIECE_NUM = 1;

    /**
    * Constructor to initialize the game board.
    */
    public AyoRules() {
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
        int totalDstr = 0;
        int currentPit = startPit;
        gameBoard.setIterator(startPit, (currentPlayer % 2) + 1, true); // Ayo skips the start pit
        while (true) {
            // Empty the current pit
            final int stonesToDstr = gameBoard.removeStones(currentPit);
            totalDstr += stonesToDstr;
            // will iterate through the data structure, skipping opposite player's stores.
            Countable currentSpot = null;
            for (int i = 1; i <= stonesToDstr; i++) {
                currentSpot = gameBoard.next();
                currentSpot.addStone();
            }
            // When we distributed last stone into Player's Store need to finish the current
            // turn
            if (currentSpot.getClass() == Store.class) {
                break;
            } else {
                // check rule: If the last piece you drop was in empty pit on either side.
                if (currentSpot.getStoneCount() == LAST_PIECE_NUM) {
                    // Capture stones if last stone landed in the current player side.
                    if (isPlayerSide(currentPlayer, ((Pit) currentSpot).getNumber())) {
                        captureStones(((Pit) currentSpot).getNumber());
                    }
                    break;
                } else {
                    currentPit = ((Pit) currentSpot).getNumber();
                }
            }
        }
        return totalDstr;
    }

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    @Override
    /* default */ public int captureStones(final int stoppingPoint) {
        final int oppositePit = 13 - stoppingPoint;
        final int capturedStones = gameBoard.getNumStones(oppositePit);
        if (capturedStones > 0) {
            // put all captured stones into current player's store
            gameBoard.addToStore(currentPlayer, capturedStones);
            gameBoard.removeStones(oppositePit);
        }
        return capturedStones;
    }

    private boolean isPlayerSide(final int playerNum, final int pitNum) {
        return (pitNum > 0 && pitNum < 7 && playerNum == 1) || (pitNum > 6 && pitNum < 13 && playerNum == 2);
    }
}
