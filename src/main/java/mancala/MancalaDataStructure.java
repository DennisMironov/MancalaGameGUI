package mancala;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Mancala data structure for the Mancala game.
 * Do not change the signature of any of the methods provided.
 * You may add methods if you need them.
 * Do not add game logic to this class
 */
public class MancalaDataStructure implements Serializable {
    private static final long serialVersionUID = 1753068377614415969L;
    /**
     *
     */
    private static final int PlayerOneNum = 1;
    private static final int PlayerTwoNum = 2;
    private static final int PlayerOne = 6;
    private static final int PlayerTwo = 13;
    private int StartStones = 4; // not final because we might want a different size board in the future

    private final List<Countable> data = new ArrayList<>();
    private int iteratorPos = 0;
    private int playerSkip = PlayerTwo;
    private int pitSkip = -1; // will never match the iteratorPos unless set specifically

    /**
     * Constructor to initialize the MancalaDataStructure.
     * 
     * @param startStones The number of stones to place in pits at the start of the
     *                    game. Default values is 4.
     */
    public MancalaDataStructure(final int startStones) {
        StartStones = startStones;
        for (int i = 0; i < PlayerOne; i++) {
            data.add(new Pit(i + 1));
        }
        data.add(new Store());
        for (int i = 7; i < PlayerTwo; i++) {
            data.add(new Pit(i));
        }
        data.add(new Store());
    }

    /**
     * Constructor to initialize the MancalaDataStructure.
     */
    public MancalaDataStructure() {
        this(4);
    }

    /**
     * Adds stones to a pit.
     *
     * @param pitNum   The number of the pit.
     * @param numToAdd The number of stones to add.
     * @return The current number of stones in the pit.
     */
    public int addStones(final int pitNum, final int numToAdd) {
        final Countable pit = data.get(pitPos(pitNum));
        pit.addStones(numToAdd);
        return pit.getStoneCount();
    }

    /**
     * Removes stones from a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones removed.
     */
    public int removeStones(final int pitNum) {
        final Countable pit = data.get(pitPos(pitNum));
        return pit.removeStones();
    }

    /**
     * Adds stones to a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @param numToAdd  The number of stones to add to the store.
     * @return The current number of stones in the store.
     */
    public int addToStore(final int playerNum, final int numToAdd) {
        final Countable store = data.get(storePos(playerNum));
        store.addStones(numToAdd);
        return store.getStoneCount();
    }

    /**
     * Gets the stone count in a player's store.
     *
     * @param playerNum The player number (1 or 2).
     * @return The stone count in the player's store.
     */
    public int getStoreCount(final int playerNum) {
        final Countable store = data.get(storePos(playerNum));
        return store.getStoneCount();
    }

    /**
     * Gets the stone count in a given pit.
     *
     * @param pitNum The number of the pit.
     * @return The stone count in the pit.
     */
    public int getNumStones(final int pitNum) {
        final Countable pit = data.get(pitPos(pitNum));
        return pit.getStoneCount();
    }

    /* helper method to convert 1 based pit numbers into array positions */
    private int pitPos(final int pitNum) {
        /*
         * Runtime execeptions don't need to be declared and are
         * automatically passed up the chain until caught. This can
         * replace the PitNotFoundException
         */
        if (pitNum < 1 || pitNum > 12) {
            throw new RuntimeException("Pit Number Out of Range");
        }
        int pos = pitNum;
        if (pos <= PlayerOne) {
            pos--;
        }
        return pos;
    }

    /* helper method to convert player number to an array position */
    private int storePos(final int playerNum) {
        if (playerNum < 1 || playerNum > 2) {
            throw new RuntimeException("Invalid Player Position");
        }

        int pos = PlayerOne;
        if (playerNum == 2) {
            pos = PlayerTwo;
        }
        return pos;
    }

    /**
     * Empties both players' stores.
     */
    public void emptyStores() {
        data.get(storePos(1)).removeStones();
        data.get(storePos(2)).removeStones();
    }

    /**
     * Sets up pits with a specified number of starting stones.
     *
     * @param startingStonesNum The number of starting stones for each pit.
     */
    public void setUpPits() {
        for (int i = 0; i < PlayerOne; i++) {
            data.get(i).removeStones();
            data.get(i).addStones(StartStones);
        }

        for (int i = 7; i < PlayerTwo; i++) {
            data.get(i).removeStones();
            data.get(i).addStones(StartStones);
        }
    }

    /**
     * Adds a store that is already connected to a Player.
     *
     * @param store     The store to set.
     * @param playerNum The player number (1 or 2).
     */
    public void setStore(final Countable store, final int playerNum) {
        data.set(storePos(playerNum), store);
    }

    /**
     * Gets a store that is connected to a Player.
     *
     * @param playerNum The player number (1 or 2).
     * @return The player's store.
     */
    public Countable getStore(final int playerNum) {
        return data.get(storePos(playerNum));
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    public boolean isSideEmpty(final int pitNum) {
        final int startIndex = (pitNum >= 1 && pitNum <= 6) ? 0 : 7;
        final int endIndex = (pitNum >= 1 && pitNum <= 6) ? 6 : 13;
        final int stoneCount = data
            .subList(startIndex, endIndex)
            .stream()
            .mapToInt(pit -> pit.getStoneCount())
            .sum();
        return stoneCount == 0;
    }

    /**
     * Check if pit is correct for player.
     *
     * @param pitNum The number of a pit.
     * @param playerNum The number of a player.
     * @return True if the pit is in player's side, false otherwise.
     */
    public boolean isPitOnPlayerSide(final int pitNum, final int playerNum) {
        return (playerNum == 1 && pitNum >= 1 && pitNum <= 6) || (playerNum == 2 && pitNum >= 7 && pitNum <= 12);
    }

    /**
     * Get pit number.
     *
     * @param pit The pit.
     * @return Pit number.
     */
    protected int getPitNum(final Countable pit) {
        int pitNum = data.indexOf(pit);

        if (pitNum >= 0 && pitNum <= 5) {
            pitNum += 1;
        }

        return pitNum;
    }    

    protected boolean isEmptyPitOnPlayerSide(final Countable pit, final int player) {
        final int pitNum = getPitNum(pit);
        return (pit.getClass() == Pit.class) && (pit.getStoneCount() == 1) &&
               (player == 1 ? (pitNum >= 1 && pitNum <= PlayerOne) : (pitNum > 7 && pitNum <= PlayerTwo));
    }

    /* helper method for wrapping the iterator around to the beginning again */
    private void loopIterator() {
        if (iteratorPos == PlayerTwo + 1) {
            iteratorPos = 0;
        }
    }

    private void skipPosition() {
        while (iteratorPos == playerSkip ||
                (playerSkip == 0 && (iteratorPos == storePos(1) || iteratorPos == storePos(2))) ||
                iteratorPos == pitSkip) {
            iteratorPos++;
            loopIterator();
        }
    }

    private void setSkipPlayer(final int playerNum) {
        playerSkip = storePos(PlayerOneNum);
        if (playerNum == PlayerTwoNum) {
            playerSkip = storePos(playerNum);
        }
    }

    private void setSkipPit(final int pitNum) {
        pitSkip = pitPos(pitNum);
    }

    /**
     * Sets the iterator position and positions to skip when iterating.
     *
     * @param startPos     The starting position for the iterator.
     * @param playerNum    The player number (1 or 2).
     * @param skipStartPit Whether to skip the starting pit.
     */
    public void setIterator(final int startPos, final int playerNum, final boolean skipStartPit) {
        iteratorPos = pitPos(startPos);
        setSkipPlayer(playerNum);
        if (skipStartPit) {
            setSkipPit(startPos);
        }
    }

    /**
     * Moves the iterator to the next position.
     *
     * @return The countable object at the next position.
     */
    public Countable next() {
        iteratorPos++;
        loopIterator(); // in case we've run off the end
        skipPosition(); // skip store and start position if necessary
        return data.get(iteratorPos);
    }
}
