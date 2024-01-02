package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable {
    private static final long serialVersionUID = -7034160360326702540L;

    private int stoneCount;
    private int number;

    public Pit() {
        stoneCount = 0;
        number = 0;
    }

    public Pit(final int pitNum) {
        stoneCount = 0;
        number = pitNum;
    }

    /**
     * Get the number of the pit.
     *
     * @return The number of this pit.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set a specified number of this pit.
     *
     * @param pitNum The number of this pit.
     */
    public void setNumber(final int pitNum) {
        number = pitNum;
    }

    /**
     * Get the count of stones in the object.
     *
     * @return The count of stones.
     */
    @Override
    public int getStoneCount() {
        return stoneCount;
    }

    /**
     * Add one stone to the object.
     */
    @Override
    public void addStone() {
        stoneCount++;
    }

    /**
     * Add a specified number of stones to the object.
     *
     * @param numToAdd The number of stones to add.
     */
    @Override
    public void addStones(final int numToAdd) {
        stoneCount += numToAdd;
    }

    /**
     * Remove stones from the object.
     *
     * @return The number of stones removed.
     */
    @Override
    public int removeStones() {
        final int removedStones = stoneCount;
        stoneCount = 0;
        return removedStones;
    }

    /**
     * Returns a string representation of the pit.
     *
     * @return  a string representation of the pit.
     */
    @Override
    public String toString() {
        return "Pit: " + getNumber() + " (" + stoneCount + " stones)";
    }
}