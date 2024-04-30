package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable{
    private static final long serialVersionUID = -1632228511284644607L;

    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private boolean captured;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
        GameRules rules = new GameRules();
        resetBoard();
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
    public int getStoreCount(final int playerNum) {
        return gameBoard.getStoreCount(playerNum);
    }
    public int removeStones(final int pitNum) {
        return gameBoard.removeStones(pitNum);
    }
    public void addToStore(final int playerNum, final int numStones) {
        gameBoard.addToStore(playerNum, numStones);
    }
    public void addStones(final int pitNum, final int numStones) {
        gameBoard.addStones(pitNum, numStones);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    protected MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    public boolean isSideEmpty(final int pitNum) throws PitNotFoundException{

        if(!isPitValid(pitNum)) {
            throw new PitNotFoundException();
        }

        boolean isEmpty = true;
        if(pitNum >= 1 && pitNum <= 6) {
            for(int i = 1; i < 7; i++) {
                if(getNumStones(i) != 0) {
                    isEmpty = false;
                    break;
                }
            }
        } else {
            for(int i = 7; i < 13; i++) {
                if(getNumStones(i) != 0) {
                    isEmpty = false;
                    break;
                }
            }
        }

        return isEmpty;
    }
    public boolean isPitValid(final int pitNum) {
        return (pitNum >= 1 && pitNum <= 12);
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
     * Get the current player.
     *
     * @return The player number (1 or 2) of the current player.
     */
    public int getPlayer() {
        return currentPlayer;
    }

    public int getPlayerSideCount(final int player) {
        int offset = 0;
        int count = 0;

        if(player == 2) {
            offset = 6;
        }
        for(int i = 1 + offset; i < 7 + offset; i++) {
            count += getNumStones(i);
        }
        return count;
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
    protected abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    protected abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.

        final Store store1 = new Store();
        final Store store2 = new Store();

        store1.setOwner(one);
        one.setStore(store1);
        store2.setOwner(two);
        two.setStore(store2);

        gameBoard.setStore(store1, 1);
        gameBoard.setStore(store2, 2);
        
        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
    }

    public void dumpAllStonesToStore(final int player) {
        
        final int totalStones = getDataStructure().getPlayerSideCount(player);

        addToStore(player, totalStones);

        for(int i = (1+6*(player-1)); i < (7+6*(player-1)); i++) {
            removeStones(i);
        }

    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    public boolean getGoAgain() {
        return false;
    }

    public boolean getCaptured() {
        return captured;
    }
    public void setCaptured(final boolean cap) {
        captured = cap;
    }

    @Override
    public String toString() {
        // Implement toString() method logic here.
        String topRow;
        String middleRow;
        String bottomRow;

        topRow = writeTopRow();
        middleRow = getStoreCount(2) + "\t\t\t\t\t";
        middleRow += getStoreCount(1);
        bottomRow = writeBottomRow();

        return topRow + "\n" + middleRow + "\n" + bottomRow + "\n";
        
    }

    protected String writeBottomRow() {
        String row = "\t";
        for(int i = 1; i < 7; i++) {
            row += " [";
            row += getNumStones(i);
            row += "] ";
        }
        return row;
    }
    protected String writeTopRow() {
        String row = "\t";
        for(int i = 12; i > 6; i--) {
            row += " [";
            row += getNumStones(i);
            row += "] ";
        }
        return row;
    }
   
}
