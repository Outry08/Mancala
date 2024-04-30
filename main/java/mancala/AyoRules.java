package mancala;

public class AyoRules extends GameRules{
    private static final long serialVersionUID = 1408805899524772241L;

    private boolean storeJustPassed;

    public AyoRules() {
        super();
        storeJustPassed = false;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException{
        
        setPlayer(playerNum);
        setCaptured(false);

        if(!isPitValid(startPit) || !isPlayerSide(startPit)
                || getNumStones(startPit) == 0 || (playerNum != 1 && playerNum != 2)) {
            throw new InvalidMoveException();
        }
        
        final int initialStoreCount = getStoreCount(getPlayer());

        distributeStones(startPit);
        
        //No going again in Ayo
        
        setStoreJustPassed(false);

        return getStoreCount(getPlayer()) - initialStoreCount;

    }
    
    @Override
    protected int distributeStones(final int startingPoint){

        int numStones, totalStones = 0, pitNum = startingPoint;

        //Ayo loop for if a final stone lands in a pit with stones already in it.
        do {
            numStones = removeStones(pitNum);
            totalStones += numStones;
            
            while (numStones != 0) {

                if(!getStoreJustPassed()) {
                    pitNum++;
                }
                if(pitNum == startingPoint) {
                    pitNum++;
                }
                if(pitIsStore(pitNum) && !getStoreJustPassed()) {
                    addToStore(getPlayer(), 1);
                    setStoreJustPassed(true);
                } else {
                    //Ayo rule where it skips the starting pit
                    if(pitNum == 13) {
                        pitNum = 1;
                    }

                    if(pitNum == startingPoint) {
                        pitNum++;
                    }

                    addStones(pitNum, 1);
                    setStoreJustPassed(false);
                }
                
                numStones--;
            }
            if(pitNum == 13) {
                pitNum = 1;
            }
        } while(getNumStones(pitNum) > 1 && !getStoreJustPassed());

        totalStones += captureStones(pitNum);

        return totalStones;

    }

    @Override
    protected int captureStones(final int stoppingPoint){

        int stonesTaken = 0;

        if(getNumStones(stoppingPoint) == 1
                    && isPlayerSide(stoppingPoint)
                    && getNumStones(getOppositePit(stoppingPoint)) != 0) {

            stonesTaken += removeStones(getOppositePit(stoppingPoint));
            //Single stone from player side not included in Ayo
            setCaptured(true);
            addToStore(getPlayer(), stonesTaken);
        }

        return stonesTaken;
    }

    protected int getOppositePit(final int pitNum) {
        return 13-pitNum;
    }
    protected boolean isPlayerSide(final int pitNum) {
        return ((pitNum >= 1 && pitNum <= 6) && getPlayer() == 1) || ((pitNum >= 7 && pitNum <= 12) && getPlayer() == 2);
    }
    protected boolean pitIsStore(final int pitNum) {
        return (pitNum == 7 && getPlayer() == 1) || (pitNum == 13 && getPlayer() == 2);
    }

    /**
     * Get the store just passed boolean.
     *
     * @return Whether a store was just passed when distributing.
     */
    public boolean getStoreJustPassed() {
        return storeJustPassed;
    }
    /**
     * Set if the store was just passed boolean.
     *
     * @param storePassed The boolean for if a store was just passed when distributing.
     */
    public void setStoreJustPassed(final boolean storePassed) {
        storeJustPassed = storePassed;
    }
}
