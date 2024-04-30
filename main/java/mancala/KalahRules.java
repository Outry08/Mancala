package mancala;

public class KalahRules extends GameRules{
    private static final long serialVersionUID = 8277436795127317957L;

    private boolean storeJustPassed;
    private boolean goAgain;

    public KalahRules() {
        super();
        storeJustPassed = false;
        goAgain = false;
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
        setGoAgain(false);
        setCaptured(false);

        if(!isPitValid(startPit) || !isPlayerSide(startPit)
                || getNumStones(startPit) == 0 || (playerNum != 1 && playerNum != 2)) {
            throw new InvalidMoveException();
        }
        
        int initialStoreCount;

        initialStoreCount = getStoreCount(getPlayer());

        distributeStones(startPit);
        
        try {
            if(getStoreJustPassed() && !isSideEmpty(getPlayer()*6)) {
                setGoAgain(true);
            }
        } catch(PitNotFoundException e) {
            setGoAgain(false);
        }
        
        setStoreJustPassed(false);

        return getStoreCount(getPlayer()) - initialStoreCount;

    }
    
    @Override
    protected int distributeStones(final int startingPoint){

        int numStones = removeStones(startingPoint);
        int totalStones = numStones;
        int pitNum = startingPoint;

        while (numStones != 0) {

            if(!getStoreJustPassed()) {
                pitNum++;
            }
            
            if(pitIsStore(pitNum) && !getStoreJustPassed()) {
                addToStore(getPlayer(), 1);
                setStoreJustPassed(true);
            } else {
                if(pitNum == 13) {
                    pitNum = 1;
                }

                addStones(pitNum, 1);
                setStoreJustPassed(false);
            }
            
            numStones--;

        }

        if(pitNum == 13) {
            pitNum = 1;
        }

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
            stonesTaken += removeStones(stoppingPoint);
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

    /**
     * Get the go again boolean.
     *
     * @return Whether the player should go again.
     */
    @Override
    public boolean getGoAgain() {
        return goAgain;
    }
    /**
     * Set the go again boolean.
     *
     * @param again The boolean for if the player should go again.
     */
    public void setGoAgain(final boolean again) {
        goAgain = again;
    }
}
