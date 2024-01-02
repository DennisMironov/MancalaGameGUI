package ui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import mancala.MancalaGame;
import mancala.PitNotFoundException;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;

public class AyoBoardPanel extends JPanel {
    private MancalaGame gameModel;
    private MancalaUI gameUI;
    private Player playerOne;
    private Player playerTwo;
    private JLabel playerOneLabel;
    private JLabel playerTwoLabel;
    private JLabel storeOneCountLabel;
    private JLabel storeTwoCountLabel;
    private JLabel storeOneCountNumLabel;
    private JLabel storeTwoCountNumLabel;
    private PositionAwareButton[][] pits = new PositionAwareButton[2][6];

    public AyoBoardPanel(MancalaGame mancalaGameModel, MancalaUI mancalaGameUI) {
        gameModel = mancalaGameModel;
        gameUI = mancalaGameUI;
        setUpPanel();
        setPlayers(gameModel.getPlayer(1), gameModel.getPlayer(2));
        updateBoard();
    }

    public void setPlayers(Player player1, Player player2) {
        playerOne = player1;
        playerTwo = player2;
        updateBoard();
    }

    private void updateBoard() {
        boolean isCurPlayerOne = gameModel.getCurrentPlayer() == playerOne;
        try {
            if (isCurPlayerOne) {
                playerOneLabel.setText(
                        String.format("<html><p><font color='red'><b>Player One: %s  (Your turn)</b></font></p></html>",
                                playerOne.getName()));
                playerTwoLabel.setText("Player Two: " + playerTwo.getName());
            } else {
                playerTwoLabel.setText(
                        String.format("<html><p><font color='red'><b>Player Two: %s  (Your turn)</b></font></p></html>",
                                playerTwo.getName()));
                playerOneLabel.setText("Player One: " + playerOne.getName());
            }
            storeOneCountNumLabel.setText(String.format("(%s)", gameModel.getStoreCount(playerOne)));
            storeTwoCountNumLabel.setText(String.format("(%s)", gameModel.getStoreCount(playerTwo)));
            storeOneCountLabel.setText(getStonesAsText(gameModel.getStoreCount(playerOne)));
            storeTwoCountLabel.setText(getStonesAsText(gameModel.getStoreCount(playerTwo)));

            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 6; x++) {
                    int pitNum = (y == 1) ? x + 1 : 12 - x;
                    pits[y][x].setText(getStonesAsText(gameModel.getNumStones(pitNum)));
                    boolean enabled = (isCurPlayerOne && (pitNum < 7) && (gameModel.getNumStones(pitNum) > 0)) ||
                                (!isCurPlayerOne && (pitNum > 6) && (gameModel.getNumStones(pitNum) > 0));
                    pits[y][x].setEnabled(enabled);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error showing the game board. " + e.getMessage());
        }
    }

    private void setUpPanel() {
        setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("A Y O A Y O   G A M E", SwingConstants.CENTER));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(getPlayerTwoPanel(), BorderLayout.PAGE_START);
        mainPanel.add(getStore2Panel(), BorderLayout.LINE_START);
        try {
            mainPanel.add(getBoardPanel(), BorderLayout.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error showing the game board. " + e.getMessage());
        }
        mainPanel.add(getStore1Panel(), BorderLayout.LINE_END);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(getButtonPanel(), BorderLayout.SOUTH);
        panel.add(getPlayerOnePanel(), BorderLayout.CENTER);
        mainPanel.add(panel, BorderLayout.PAGE_END);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel,  BorderLayout.CENTER);
    }

    private JPanel getBoardPanel() throws PitNotFoundException {
        JPanel panel = new JPanel(new GridLayout(2, 6, 4, 4));
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 6; x++) {
                int pitNum = (y == 1) ? x + 1 : 12 - x;
                pits[y][x] = new PositionAwareButton();
                pits[y][x].setAcross(x + 1);
                pits[y][x].setDown(y + 1);
                pits[y][x].setEnabled(pitNum < 7);
                pits[y][x].setForeground(Color.blue);
                pits[y][x].setFont(new Font(pits[y][x].getFont().getName(), Font.BOLD, 32));
                pits[y][x].setText(getStonesAsText(gameModel.getNumStones(pitNum)));
                pits[y][x].addActionListener(e -> {
                    PositionAwareButton pit = (PositionAwareButton) e.getSource();
                    moveStones((pit.getDown() == 2) ? pit.getAcross() : 13 - pit.getAcross());
                });
                panel.add(pits[y][x]);
            }
        }

        return panel;
    }

    private void moveStones(int pitNum) {
        try {
            gameModel.move(pitNum);
            updateBoard();

            if (gameModel.isGameOver()) {
                Player winner = gameModel.getWinner();
                updateBoard();
                if (winner != null) {
                    JOptionPane.showMessageDialog(this, "Congratulations! " + winner.getName() + " you won!!!");
                } else {
                    JOptionPane.showMessageDialog(this, "Game is over. There is no winner. It is a tie.");
                }
                updatePlayerProfile(playerOne, playerOne == winner, winner == null);
                updatePlayerProfile(playerTwo, playerTwo == winner, winner == null);
            } else {
                gameModel.switchCurrentPlayer();
                updateBoard();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error " + e.getMessage());
        }
    }

    private void updatePlayerProfile(Player player, boolean isWinner, boolean isTie) {
        UserProfile profile = player.getProfile();
        profile.addAyoGameCount();
        if (isWinner) {
            profile.addAyoWinCount();
        } else if (!isTie) {
            profile.addAyoLossCount();
        }
        gameUI.getMainContainer().setPlayerProfileView(player == playerOne ? 1 : 2, player);
    }

    private String getStonesAsText(int stoneCount) {
        return String.format("<html><p>%s</p></html>", "&#8226 ".repeat(stoneCount));
    }

    private JPanel getPlayerOnePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));
        playerOneLabel = new JLabel("Player One", SwingConstants.CENTER);
        panel.add(playerOneLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getPlayerTwoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));
        playerTwoLabel = new JLabel("Player Two", SwingConstants.CENTER);
        panel.add(playerTwoLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getStore1Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        Border compound = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.darkGray),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
        panel.setBorder(compound);
        panel.setPreferredSize(new Dimension(100, Integer.MAX_VALUE));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Store One", SwingConstants.CENTER));
        storeOneCountNumLabel = new JLabel("(0)", SwingConstants.CENTER);
        topPanel.add(storeOneCountNumLabel);
        panel.add(topPanel, BorderLayout.NORTH);
        storeOneCountLabel = new JLabel(getStonesAsText(4), SwingConstants.CENTER);
        storeOneCountLabel.setForeground(Color.blue);
        storeOneCountLabel.setFont(new Font(storeOneCountLabel.getFont().getName(), Font.BOLD, 32));
        panel.add(storeOneCountLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getStore2Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        Border compound = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.darkGray),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
        panel.setBorder(compound);
        panel.setPreferredSize(new Dimension(100, Integer.MAX_VALUE));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Store Two", SwingConstants.CENTER));
        storeTwoCountNumLabel = new JLabel("(0)", SwingConstants.CENTER);
        topPanel.add(storeTwoCountNumLabel);
        panel.add(topPanel, BorderLayout.NORTH);
        storeTwoCountLabel = new JLabel(getStonesAsText(4), SwingConstants.CENTER);
        storeTwoCountLabel.setForeground(Color.blue);
        storeTwoCountLabel.setFont(new Font(storeTwoCountLabel.getFont().getName(), Font.BOLD, 32));
        panel.add(storeTwoCountLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getButtonPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton leftButton1 = new JButton("<- Back");
        JButton leftButton2 = new JButton("Start New Game");
        JButton leftButton3 = new JButton("Save Game");
        JButton leftButton4 = new JButton("Load Game");
        JButton rightButton = new JButton("Close");
        leftButton1.addActionListener(e -> {
            gameUI.ShowMainPanel();
        });
        leftButton2.addActionListener(e -> {
            gameModel.startNewGame();
            updatePlayerProfile(playerOne, false, true);
            updatePlayerProfile(playerTwo, false, true);
            updateBoard();
        });
        leftButton3.addActionListener(e -> {
            saveGame();
        });
        leftButton4.addActionListener(e -> {
            loadGame();
        });
        rightButton.addActionListener(e -> {
            gameUI.closeGame();
        });

        // Add buttons to the bottom panel
        JPanel buttonsLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsLeftPanel.add(leftButton1);
        buttonsLeftPanel.add(leftButton2);
        buttonsLeftPanel.add(leftButton3);
        buttonsLeftPanel.add(leftButton4);
        JPanel buttonsRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsRightPanel.add(rightButton);

        bottomPanel.add(buttonsLeftPanel, BorderLayout.WEST);
        bottomPanel.add(buttonsRightPanel, BorderLayout.EAST);
        return bottomPanel;
    }

    private void saveGame() {
        try {
            Saver.saveObject(gameModel, gameUI.getAyoGameFile().toString());
            JOptionPane.showMessageDialog(this, "Ayo Game saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving Ayo Game: " + e.getMessage());
        }
    }

    private void loadGame() {
        try {
            gameModel = (MancalaGame) Saver.loadObject(gameUI.getAyoGameFile().toString());
            setPlayers(gameModel.getPlayer(1), gameModel.getPlayer(2));
            gameUI.getMainContainer().setPlayers(gameModel.getPlayer(1), gameModel.getPlayer(2));
            JOptionPane.showMessageDialog(this, "Ayo Game loaded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading Ayo Game: " + e.getMessage());
        }
    }
}
