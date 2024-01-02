package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable {
    private static final long serialVersionUID = -8832743088141602486L;

    private Player owner;
    private int totalStones;

    /**
     * Constructor to initialize the Store.
     */
    public Store() {
        totalStones = 0;
    }

    /**
     * Constructor to initialize the Store.
     * 
     * @param player The owner of the store.
     */
    public Store(final Player player) {
        this.setOwner(player);
        totalStones = 0;
    }

    /**
     * Set a specified Owner to the store.
     *
     * @param player The owner of the store to set.
     */
    protected void setOwner(final Player player) {
        owner = player;
        player.setStore(this);
    }

    /**
     * Get the Owner of the Store.
     *
     * @return The owner of the store.
     */
    protected Player getOwner() {
        return owner;
    }

    /**
     * Get the count of stones in the object.
     *
     * @return The count of stones.
     */
    @Override
    public int getStoneCount() {
        return totalStones;
    }

    /**
     * Add a specified number of stones to the object.
     *
     * @param numToAdd The number of stones to add.
     */
    @Override
    public void addStone() {
        totalStones++;
    }

    /**
     * Add a specified number of stones to the object.
     *
     * @param numToAdd The number of stones to add.
     */
    @Override
    public void addStones(final int numToAdd) {
        totalStones += numToAdd;
    }

    /**
     * Remove stones from the object.
     *
     * @return The number of stones removed.
     */
    @Override
    public int removeStones() {
        final int removedStones = totalStones;
        totalStones = 0;
        return removedStones;
    }

    /**
     * Returns a string representation of the store.
     *
     * @return  a string representation of the store.
     */
    @Override
    public String toString() {
        return "Store" + (owner != null ? " for player " + owner.getName() : "") + " (" + totalStones + " stones)";
    }
}