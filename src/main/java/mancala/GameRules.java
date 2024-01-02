package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable {
    private static final long serialVersionUID = -7179957211946545101L;
    protected MancalaDataStructure gameBoard;
    protected int currentPlayer = 1; // Player number (1 or 2)
    private boolean turnIsRequired = false;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    /*default */ public MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    /* default */ public boolean isSideEmpty(final int pitNum) {
        return gameBoard.isSideEmpty(pitNum);
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    /* default */ public abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    /* default */ public abstract int captureStones(int stoppingPoint);

    /**
     * Capture remaining stones from pits on the player's side to the player's store.
     * @param pitNum Pit number from the side to capture stones.
     */
    public void captureRemainingStonesOnPlayerSide(final int pitNum) {
        final int startIndex = (pitNum >= 1 && pitNum <= 6) ? 1 : 7;
        final int endIndex = (pitNum >= 1 && pitNum <= 6) ? 6 : 12;
        final int playerNum = (pitNum >= 1 && pitNum <= 6) ? 1 : 2;

        for (int i = startIndex; i <= endIndex; i++) {
            final int stoneCount = gameBoard.getNumStones(i);
            gameBoard.getStore(playerNum).addStones(stoneCount);
            gameBoard.removeStones(i);
        }
    }

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        final Store storeOne = (Store) gameBoard.getStore(1);
        storeOne.setOwner(one);
        final Store storeTwo = (Store) gameBoard.getStore(2);
        storeTwo.setOwner(two);
        resetBoard();
    }

    public boolean isAnotherTurnIsRequired() {
        return turnIsRequired;
    }

    public void setAnotherTurnIsRequired(final boolean isTurnRequired) {
        turnIsRequired = isTurnRequired;
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
