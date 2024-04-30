package mancala;

/**
 * Interface representing objects that can have stones counted, added, and removed.
 */
public interface Countable {

    /**
     * Get the count of stones in the object.
     *
     * @return The count of stones.
     */
    int getStoneCount();

    /**
     * Add one stone to the object.
     */
    void addStone();

    /**
     * Add a specified number of stones to the object.
     *
     * @param numToAdd The number of stones to add.
     */
    void addStones(int numToAdd);

    /**
     * Remove stones from the object.
     *
     * @return The number of stones removed.
     */
    int removeStones();
}

