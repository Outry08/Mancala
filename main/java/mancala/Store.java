package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable{
    private static final long serialVersionUID = -7031147019387450602L;
    
    private Player owner;
    private int numStones;

    public Store() {
        numStones = 0;
    }
    public Store(final Player player) {
        this();
        setOwner(player);
    }

    @Override
    public int getStoneCount() {
        return numStones;
    }

    public Player getOwner() {
        return owner;
    }
    public void setOwner(final Player player) {
        owner = player;
    }

    @Override
    public void addStone() {
        numStones++;
    }
    @Override
    public void addStones(final int amount) {
        numStones += amount;
    }
    @Override
    public int removeStones() {
        final int stonesRemoved = getStoneCount();
        numStones = 0;
        return stonesRemoved;
    }

    @Override
    public String toString() {
        return "[" + numStones + "]";
    }

}
