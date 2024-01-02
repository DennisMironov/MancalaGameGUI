package mancala;

import java.util.ArrayList;

public class MancalaGame implements java.io.Serializable {
    private static final long serialVersionUID = -1493777239815568588L;

    private ArrayList<Player> players;
    private Player currentPlayer;
    private GameRules gameBoard;

    /**
     * Constructor to initialize the MancalaGame.
     */
    public MancalaGame() {
        players = new ArrayList<>();
    }

    /**
     * Get the current game board.
     *
     * @return The game board.
     */
    public GameRules getBoard() {
        return gameBoard;
    }

    /**
     * Get the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) throws PitNotFoundException {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the number of stones in the player's store.
     *
     * @param player The player.
     * @return The number of stones in the player's store.
     */
    public int getStoreCount(final Player player) throws NoSuchPlayerException {
        if (!players.contains(player)) {
            throw new NoSuchPlayerException("Player not found.");
        }
        return player.getStoreCount();
    }

    public Player getWinner() throws GameNotOverException {
        Player winner = null;
        if (isGameOver()) {
            if (gameBoard.isSideEmpty(1)) {
                gameBoard.captureRemainingStonesOnPlayerSide(7);
            } else if (gameBoard.isSideEmpty(7)) {
                gameBoard.captureRemainingStonesOnPlayerSide(1);
            }
            if (players.get(0).getStoreCount() > players.get(1).getStoreCount()) {
                winner = players.get(0);
            } else if (players.get(1).getStoreCount() > players.get(0).getStoreCount()) {
                winner = players.get(1);
            }
        } else {
            throw new GameNotOverException("Game is not over yet");
        }
        return winner;
    }

    public boolean isGameOver() {
        return gameBoard.isSideEmpty(1) || gameBoard.isSideEmpty(7);
    }

    public int move(final int startPit) throws InvalidMoveException {
        int stonesMoved;
        try {
            stonesMoved = gameBoard.moveStones(startPit, playerNum(currentPlayer));
        } catch (NoSuchPlayerException e) {
            throw new InvalidMoveException("Invalid move. Player not found.");
        }
        return stonesMoved;
    }

    public void setBoard(final GameRules theBoard) {
        gameBoard = theBoard;
    }

    public void setCurrentPlayer(final Player player) throws NoSuchPlayerException {
        currentPlayer = player;
        gameBoard.setPlayer(playerNum(player));
    }

    public void switchCurrentPlayer() throws NoSuchPlayerException {
        final int oppositePlayer = getOppositePlayerNum(playerNum(currentPlayer));
        gameBoard.setPlayer(oppositePlayer);
        currentPlayer = players.get(oppositePlayer - 1);
    }

    public int getOppositePlayerNum(final int playerNum) {
        return (playerNum % 2) + 1;
    }

    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        players = new ArrayList<>();
        players.add(onePlayer);
        players.add(twoPlayer);
        currentPlayer = onePlayer;
        gameBoard.registerPlayers(onePlayer, twoPlayer);
    }

    public Player getPlayer(final int playerNum) {
        return players.get(playerNum - 1);
    }

    public void startNewGame() {
        currentPlayer = players.get(0);
        gameBoard.resetBoard();
        gameBoard.setPlayer(1);
    }

    /* helper method to get player number */
    public int playerNum(final Player player) throws NoSuchPlayerException {
        if (!players.contains(player)) {
            throw new NoSuchPlayerException("Player not found.");
        }
        return players.indexOf(player) + 1;
    }
}