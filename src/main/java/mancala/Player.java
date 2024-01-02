package mancala;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = -1173933624459243263L;

    private Store playerStore;
    private UserProfile profile;

    public Player() {
        profile = new UserProfile();
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(final UserProfile userProfile) {
        profile = userProfile;
    }

    public String getName() {
        return profile.getProfileName();
    }
    
    public void setStore(final Store store) {
        playerStore = store;
    }

    public int getStoreCount() {
        return playerStore.getStoneCount();
    }

    @Override
    public String toString() {
        return getName();
    }
}