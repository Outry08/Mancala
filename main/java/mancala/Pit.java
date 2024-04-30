package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable{
    private static final long serialVersionUID = 5958191702933113632L;
    
    private int numStones;

    public Pit() {
        numStones = 0;
    }

    @Override
    public int getStoneCount() {
        return numStones;
    }

    @Override
    public void addStone() {
        numStones++;
    }
    @Override
    public void addStones(final int stones) {
        numStones += stones;
    }
    @Override
    public int removeStones() {
        final int stonesRemoved = numStones;
        numStones = 0;
        return stonesRemoved;
    }

    @Override
    public String toString() {
        return "[" + getStoneCount() + "]";
    }

}
