package ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import mancala.MancalaGame;
import mancala.Player;

public class MainPanel extends JPanel {
    private MancalaGame gameModel;
    private MancalaUI gameUI;
    private Player playerOne;
    private Player playerTwo;

    private JTextField[] playerNameFields = new JTextField[2];

    private JLabel[] kalahGameCountLabels = new JLabel[2];
    private JLabel[] kalahWinCountLabels = new JLabel[2];
    private JLabel[] kalahLossesCountLabels = new JLabel[2];
    private JLabel[] ayoGameCountLabels = new JLabel[2];
    private JLabel[] ayoWinCountLabels = new JLabel[2];
    private JLabel[] ayoLossesCountLabels = new JLabel[2];

    public MainPanel(MancalaGame mancalaGameModel, MancalaUI mancalaGameUI) {
        gameModel = mancalaGameModel;
        gameUI = mancalaGameUI;
        playerOne = gameModel.getPlayer(1);
        playerTwo = gameModel.getPlayer(2);
        setUpPanel();
    }

    public void setPlayers(Player player1, Player player2) {
        playerOne = player1;
        playerTwo = player2;
        setPlayerProfileView(1, playerOne);
        setPlayerProfileView(2, playerTwo);
    }

    private void setUpPanel() {
        setLayout(new BorderLayout());

        // Player Panels
        JPanel player1Panel = createPlayerPanel(1, playerOne);
        JPanel player2Panel = createPlayerPanel(2, playerTwo);

        // Container for player panels
        JPanel playersContainer = new JPanel(new GridLayout(1, 2));
        playersContainer.add(player1Panel);
        playersContainer.add(player2Panel);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Buttons
        JButton leftButton1 = new JButton("Play Ayoayo");
        JButton leftButton2 = new JButton("Play Kalah");
        JButton rightButton = new JButton("Close");
        leftButton1.addActionListener(e -> {
            gameUI.showAyoBoard();
        });
        leftButton2.addActionListener(e -> {
            gameUI.showKalahBoard();
        });
        rightButton.addActionListener(e -> {
            gameUI.closeGame();
        });

        // Add buttons to the bottom panel
        JPanel buttonsLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsLeftPanel.add(leftButton1);
        buttonsLeftPanel.add(leftButton2);
        JPanel buttonsRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsRightPanel.add(rightButton);

        bottomPanel.add(buttonsLeftPanel, BorderLayout.WEST);
        bottomPanel.add(buttonsRightPanel, BorderLayout.EAST);

        // Add panels to the main frame
        add(playersContainer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createPlayerPanel(int playerNum, Player player) {
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player " + playerNum));

        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));

        playerNameFields[playerNum - 1] = new JTextField(10);
        playerNameFields[playerNum - 1].setMaximumSize(new Dimension(Integer.MAX_VALUE, playerNameFields[playerNum - 1].getPreferredSize().height));
        playerNameFields[playerNum - 1].setText(player.getName());
        playerNameFields[playerNum - 1].setFont(playerNameFields[playerNum - 1].getFont().deriveFont(Font.BOLD));

        JPanel playerNamePanel = new JPanel();
        playerNamePanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 70));
        playerNamePanel.setLayout(new BoxLayout(playerNamePanel, BoxLayout.X_AXIS));
        playerNamePanel.add(new JLabel("Player's Name: "));
        playerNamePanel.add(playerNameFields[playerNum - 1]);
        playerPanel.add(playerNamePanel);

        addPlayerCountPanels(playerNum, player, playerPanel);

        // Add buttons to the bottom panel
        JButton loadButton = new JButton("Load Profile");
        JButton saveButton = new JButton("Save Profile");
        loadButton.addActionListener(e -> {
            loadPlayerProfile(playerNum, player);
        });
        saveButton.addActionListener(e -> {
            savePlayerProfile(playerNum, player);
        });
        JPanel buttonsLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsLeftPanel.add(loadButton);
        buttonsLeftPanel.add(saveButton);
        playerPanel.add(buttonsLeftPanel);

        return playerPanel;
    }

    private void addPlayerCountPanels(int playerNum, Player player, JPanel parentPanel) {
        JPanel ayoGamePlayedPanel = new JPanel();
        ayoGamePlayedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ayoGamePlayedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ayoGameCountLabels[playerNum - 1] = new JLabel("Ayo Games Played: " + player.getProfile().getAyoGameCount());
        ayoGamePlayedPanel.add(ayoGameCountLabels[playerNum - 1]);
        parentPanel.add(ayoGamePlayedPanel);

        JPanel ayoGameWinsPanel = new JPanel();
        ayoGameWinsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ayoGameWinsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ayoWinCountLabels[playerNum - 1] = new JLabel("Ayo Games Wins: " + player.getProfile().getAyoWinCount());
        ayoGameWinsPanel.add(ayoWinCountLabels[playerNum - 1]);
        parentPanel.add(ayoGameWinsPanel);

        JPanel ayoGameLossesPanel = new JPanel();
        ayoGameLossesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ayoGameLossesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ayoLossesCountLabels[playerNum - 1] = new JLabel("Ayo Games Losses: " + (player.getProfile().getAyoLossCount()));
        ayoGameLossesPanel.add(ayoLossesCountLabels[playerNum - 1]);
        parentPanel.add(ayoGameLossesPanel);

        JPanel gamePlayedPanel = new JPanel();
        gamePlayedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        gamePlayedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        kalahGameCountLabels[playerNum - 1] = new JLabel("Kalah Games Played: " + player.getProfile().getKalahGameCount());
        gamePlayedPanel.add(kalahGameCountLabels[playerNum - 1]);
        parentPanel.add(gamePlayedPanel);

        JPanel gameWinsPanel = new JPanel();
        gameWinsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        gameWinsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        kalahWinCountLabels[playerNum - 1] = new JLabel("Kalah Games Wins: " + player.getProfile().getKalahWinCount());
        gameWinsPanel.add(kalahWinCountLabels[playerNum - 1]);
        parentPanel.add(gameWinsPanel);

        JPanel gameLossesPanel = new JPanel();
        gameLossesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        gameLossesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        kalahLossesCountLabels[playerNum - 1] = new JLabel("Kalah Games Losses: " + (player.getProfile().getKalahLossCount()));
        gameLossesPanel.add(kalahLossesCountLabels[playerNum - 1]);
        parentPanel.add(gameLossesPanel);
    }

    public void setPlayerProfileView(int playerNum, Player player) {
        playerNameFields[playerNum - 1].setText(player.getName());

        ayoGameCountLabels[playerNum - 1].setText("Ayo Games Played: " + player.getProfile().getAyoGameCount());
        ayoWinCountLabels[playerNum - 1].setText("Ayo Games Wins: " + player.getProfile().getAyoWinCount());
        ayoLossesCountLabels[playerNum - 1].setText("Ayo Games Losses: " + (player.getProfile().getAyoLossCount()));

        kalahGameCountLabels[playerNum - 1].setText("Kalah Games Played: " + player.getProfile().getKalahGameCount());
        kalahWinCountLabels[playerNum - 1].setText("Kalah Games Wins: " + player.getProfile().getKalahWinCount());
        kalahLossesCountLabels[playerNum - 1].setText("Kalah Games Losses: " + (player.getProfile().getKalahLossCount()));
    }

    public void savePlayerProfile(int playerNum, Player player) {
        player.getProfile().setProfileName(playerNameFields[playerNum - 1].getText());
        if (gameUI.savePlayerProfile(playerNum, player)) {
            JOptionPane.showMessageDialog(this, "Player " + playerNum + " Profile saved successfully.");
        }
    }

    public void loadPlayerProfile(int playerNum, Player player) {
        if (gameUI.loadPlayerProfile(playerNum, player)) {
            setPlayerProfileView(playerNum, player);
            JOptionPane.showMessageDialog(this, "Player's Profile loaded successfully.");
        }
    }
}
