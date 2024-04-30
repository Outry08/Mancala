package mancala;

import java.util.ArrayList;
import java.io.Serializable;

public class MancalaGame implements Serializable{
    private static final long serialVersionUID = -777287522273671325L;

    //Fields
    private GameRules gameRules;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private boolean isKalahRules;

    //Constructors
    public MancalaGame() {
        setBoard(new KalahRules());
        setIsKalahRules(true);
        getBoard().getDataStructure().setUpPits();
        setPlayers(new Player(), new Player());
    }

    //Methods
    public void startNewGame(final GameRules rules, final boolean isKalah) {
        setBoard(rules);
        setIsKalahRules(isKalah);
        getBoard().registerPlayers(getPlayer(0), getPlayer(1));
        getBoard().resetBoard();
        setCurrentPlayer(getPlayer(0));
    }

    public int move(final int startPit) throws InvalidMoveException{

        int numStones; 

        getBoard().moveStones(startPit, getPlayerNum(getCurrentPlayer()));
        
        numStones = getBoard().getPlayerSideCount(getPlayerNum(getCurrentPlayer()));

        if(!getBoard().getGoAgain()) {
            swapCurrentPlayer();
        }
        return numStones;
    }

    public GameRules getBoard() {
        return gameRules;
    }
    public void setBoard(final GameRules theBoard) {
        gameRules = theBoard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(final Player player) {
        currentPlayer = player;
    }
    public void swapCurrentPlayer() {
        if(getCurrentPlayer() == getPlayer(0)) {
            setCurrentPlayer(getPlayer(1));
        }
        else {
            setCurrentPlayer(getPlayer(0));
        }
    }

    public int getPlayerNum(final Player player) {
        if(player == getPlayer(1)) {
            return 2;
        }
        return 1;

    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Player getPlayer(final int index) {
        return getPlayers().get(index);
    }

    public int getPlayerStoreCount(final int index) {
        return getPlayer(index).getStoreCount();
    }

    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        players = new ArrayList<Player>();
        players.add(onePlayer);
        players.add(twoPlayer);
        getBoard().registerPlayers(onePlayer, twoPlayer);
        setCurrentPlayer(getPlayer(0));
    }

    public int getNumStones(final int pitNum) throws PitNotFoundException{


        if(!getBoard().isPitValid(pitNum)) {
            throw new PitNotFoundException();
        }

        return getBoard().getNumStones(pitNum);
    }
    public int getStoreCount(final Player player) throws NoSuchPlayerException{
        if(player != getPlayer(0) && player != getPlayer(1)) {
            throw new NoSuchPlayerException();
        }

        return player.getStoreCount();
    }

    public Player getWinner() throws GameNotOverException{
        if(!isGameOver()) {
            throw new GameNotOverException();
        }

        Player winner = null;
        
        if(getPlayerStoreCount(0) > getPlayerStoreCount(1)) {
            winner = getPlayer(0);
        } else if(getPlayerStoreCount(0) < getPlayerStoreCount(1)) {
            winner = getPlayer(1);
        }
        
        return winner;
    }

    public boolean isGameOver() {
        try {
            return getBoard().isSideEmpty(1) || getBoard().isSideEmpty(7);
        } catch(PitNotFoundException e) {
            return false;
        }
    }

    public boolean isKalahRules() {
        return isKalahRules;
    }
    public void setIsKalahRules(final boolean isKalah) {
        isKalahRules = isKalah;
    }

    @Override
    public String toString() {
        return getBoard().toString();
    }

}
