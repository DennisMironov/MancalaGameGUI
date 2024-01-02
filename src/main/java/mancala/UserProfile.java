package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 8966319766020109570L;

    private int kalahGameCount;
    private int kalahWinCount;
    private int kalahLossCount;
    private int ayoGameCount;
    private int ayoWinCount;
    private int ayoLossCount;
    private String profileName;

    public UserProfile() {
        kalahGameCount = 0;
        kalahWinCount = 0;
        kalahLossCount = 0;
        ayoGameCount = 0;
        ayoWinCount = 0;
        ayoLossCount = 0;
        profileName = "N/A";
    }

    public void setKalahGameCount(final int count) {
        kalahGameCount = count;
    }

    public int getKalahGameCount() {
        return kalahGameCount;
    }

    /**
     * Add one Kalah game played
     */
    public void addKalahGameCount() {
        kalahGameCount += 1;
    }

    public void setAyoGameCount(final int count) {
        ayoGameCount = count;
    }

    /**
     * Add one Ayo game played
     */
    public void addAyoGameCount() {
        ayoGameCount += 1;
    }

    public int getAyoGameCount() {
        return ayoGameCount;
    }

    public void setKalahWinCount(final int count) {
        kalahWinCount = count;
    }

    public int getKalahWinCount() {
        return kalahWinCount;
    }

    /**
     * Add one Kalah game won
     */
    public void addKalahWinCount() {
        kalahWinCount += 1;
    }
    
    public void setAyoWinCount(final int count) {
        ayoWinCount = count;
    }

    public int getAyoWinCount() {
        return ayoWinCount;
    }

    /**
     * Add one Ayo game won
     */
    public void addAyoWinCount() {
        ayoWinCount += 1;
    }

    public int getKalahLossCount() {
        return kalahLossCount;
    }

    public void setKalahLossCount(final int kalahLossCount) {
        this.kalahLossCount = kalahLossCount;
    }

    /**
     * Add one Kalah game loss
     */
    public void addKalahLossCount() {
        kalahLossCount += 1;
    }

    public int getAyoLossCount() {
        return ayoLossCount;
    }

    public void setAyoLossCount(final int ayoLossCount) {
        this.ayoLossCount = ayoLossCount;
    }

    /**
     * Add one Ayo game loss
     */
    public void addAyoLossCount() {
        ayoLossCount += 1;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(final String name) {
        profileName = name;
    }
}