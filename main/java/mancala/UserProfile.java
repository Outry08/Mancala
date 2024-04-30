package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable{
    private static final long serialVersionUID = -3775444175216628887L;

    private String name;
    private int numKalahGames;
    private int numAyoGames;
    private int numKalahWins;
    private int numAyoWins;

    public UserProfile() {
        this("Player");
    }
    public UserProfile(final String userName) {
        name = userName;
        numKalahGames = 0;
        numAyoGames = 0;
        numKalahWins = 0;
        numAyoWins = 0;
    }

    public String getName() {
        return name;
    }
    public void setName(final String userName) {
        name = userName;
    }

    public int getNumKalahGames() {
        return numKalahGames;
    }
    public void setNumKalahGames(final int games) {
        numKalahGames = games;
    }
    public void incrementNumKalahGames() {
        numKalahGames++;
    }

    public int getNumAyoGames() {
        return numAyoGames;
    }
    public void setNumAyoGames(final int games) {
        numAyoGames = games;
    }
    public void incrementNumAyoGames() {
        numAyoGames++;
    }

    public int getNumKalahWins() {
        return numKalahWins;
    }
    public void setNumKalahWins(final int wins) {
        numKalahWins = wins;
    }
    public void incrementNumKalahWins() {
        numKalahWins++;
    }

    public int getNumAyoWins() {
        return numAyoWins;
    }
    public void setNumAyoWins(final int wins) {
        numAyoWins = wins;
    }
    public void incrementNumAyoWins() {
        numAyoWins++;
    }

    @Override
    public String toString() {
        return getName() + "\nKalah Games Played: " + getNumKalahGames() + "\nAyo   Games Played: " + getNumAyoGames() + "\nKalah Games Won: " + getNumKalahWins() + "\nAyo   Games Won: " + getNumAyoWins();
    }

}
