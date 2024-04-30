package mancala;

import java.io.Serializable;

public class Player implements Serializable{
    private static final long serialVersionUID = -213033726685782580L;

    private String playerName;
    private Store playerStore;
    private UserProfile profile;

    public Player() {
        this("Player");
    }
    public Player(final String name) {
        playerName = name;
        profile = new UserProfile(name);
    }

    public String getName() {
        return playerName;
    }
    public void setName(final String name) {
        playerName = name;
        getProfile().setName(name);
    }

    public Store getStore() {
        return playerStore;
    }
    public void setStore(final Store store) {
        playerStore = store;
    }

    public int getStoreCount() {
        return playerStore.getStoneCount();
    }

    public UserProfile getProfile() {
        return profile;
    }
    public void setProfile(final UserProfile prof) {
        profile = prof;
    }

    @Override
    public String toString() {
        return playerName;
    }

}
