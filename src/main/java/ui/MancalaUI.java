package ui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import mancala.AyoRules;
import mancala.KalahRules;
import mancala.MancalaGame;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;

public class MancalaUI extends JFrame {

    private Path assetsFolderPath;
    private File[] playerFiles = new File[2];
    private File kalahGameFile;
    private File ayoGameFile;

    private KalahRules kalahRules = new KalahRules();
    private AyoRules ayoRules = new AyoRules();

    private JPanel contentPane;
    private KalahBoardPanel boardKalahContainer;
    private AyoBoardPanel boardAyoContainer;
    private MainPanel mainContainer;

    private JMenuBar menuBar;
    private MancalaGame game;

    public MancalaUI(String title) {
        super();

        setAssetsFolder();
        game = new MancalaGame();

        Player playerOne = new Player();
        playerOne.getProfile().setProfileName("One");
        Player playerTwo = new Player();
        playerTwo.getProfile().setProfileName("Two");

        game.setBoard(ayoRules);
        game.setPlayers(playerOne, playerTwo);
        game.setBoard(kalahRules);
        game.setPlayers(playerOne, playerTwo);

        basicSetUp(title);

        makeMenu();
        setJMenuBar(menuBar);

        if (yesNoMessage("Player's Profiles", "Would you like to load player's profiles?")) {
            loadPlayerProfiles();
        }
    }

    public File getKalahGameFile() {
        return kalahGameFile;
    }

    public File getAyoGameFile() {
        return ayoGameFile;
    }

    public MainPanel getMainContainer() {
        return mainContainer;
    }

    private void basicSetUp(String title) {
        this.setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        contentPane = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(6, 10, 6, 10);
        contentPane.setBorder(padding);
        contentPane.setLayout(new CardLayout());

        mainContainer = new MainPanel(game, this);
        boardKalahContainer = new KalahBoardPanel(game, this);
        boardAyoContainer = new AyoBoardPanel(game, this);

        contentPane.add(mainContainer, "Main");
        contentPane.add(boardKalahContainer, "Kalah");
        contentPane.add(boardAyoContainer, "Ayo");

        add(contentPane, BorderLayout.CENTER);
    }

    protected void ShowMainPanel() {
        ((CardLayout) contentPane.getLayout()).first(contentPane);
    }

    protected void showKalahBoard() {
        game.setBoard(kalahRules);
        ((CardLayout) contentPane.getLayout()).show(contentPane, "Kalah");
    }

    protected void showAyoBoard() {
        game.setBoard(ayoRules);
        ((CardLayout) contentPane.getLayout()).show(contentPane, "Ayo");
    }

    private void makeMenu() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game Menu");

        JMenuItem item1 = new JMenuItem("Save Player 1 profile...");
        item1.addActionListener(e -> {
            mainContainer.savePlayerProfile(1, game.getPlayer(1));
        });
        menu.add(item1);
        JMenuItem item2 = new JMenuItem("Load Player 1 profile...");
        item2.addActionListener(e -> {
            mainContainer.loadPlayerProfile(1, game.getPlayer(1));
        });
        menu.add(item2);
        menu.addSeparator();
        JMenuItem item3 = new JMenuItem("Save Player 2 profile...");
        item3.addActionListener(e -> {
            mainContainer.savePlayerProfile(2, game.getPlayer(2));
        });
        menu.add(item3);
        JMenuItem item4 = new JMenuItem("Load Player 2 profile...");
        item4.addActionListener(e -> {
            mainContainer.loadPlayerProfile(2, game.getPlayer(2));
        });
        menu.add(item4);
        menu.addSeparator();
        JMenuItem itemClose = new JMenuItem("Close Mancala Game");
        itemClose.addActionListener(e -> closeGame());
        menu.add(itemClose);

        menuBar.add(menu);
    }

    public boolean yesNoMessage(String title, String message) {
        int selection = 0;
        selection = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return selection == JOptionPane.YES_OPTION;
    }

    protected void closeGame() {
        String message = "Are you sure you want to close Mancala Game?";
        String title = "Mancala Game";
        if (yesNoMessage(title, message)) {
            savePlayerProfiles();
            System.exit(0);
        }
    }

    private void setAssetsFolder() {
        // Get the current directory
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        // Define the folder name to check for
        String folderName = "assets";
        // Create a path for the 'assets' folder
        assetsFolderPath = currentDirectory.resolve(folderName);
        // Check if the 'assets' folder exists; if not, create it
        File directory = assetsFolderPath.toFile();
        if (!directory.exists()) {
            directory.mkdir();
        }
        playerFiles[0] = new File(assetsFolderPath + "/player_one.profile");
        playerFiles[1] = new File(assetsFolderPath + "/player_two.profile");

        kalahGameFile = new File(assetsFolderPath + "/kalah_game.saved");
        ayoGameFile = new File(assetsFolderPath + "/ayo_game.saved");
    }

    private void savePlayerProfiles() {
        if (yesNoMessage("Player's Profiles", "Would you like to save player's profiles?")) {
            mainContainer.savePlayerProfile(1, game.getPlayer(1));
            mainContainer.savePlayerProfile(2, game.getPlayer(2));
        }
    }

    private void loadPlayerProfiles() {
        boolean loaded = false;
        if (playerFiles[0].exists()) {
            loadPlayerProfile(1, game.getPlayer(1));
            loaded = true;
        }
        if (playerFiles[1].exists()) {
            loadPlayerProfile(2, game.getPlayer(2));
            loaded = true;
        }
        if (loaded) {
            JOptionPane.showMessageDialog(null, "Player's Profiles were loaded successfully.");
        }
    }

    public boolean loadPlayerProfile(int playerNum, Player player) {
        try {
            UserProfile loadedProfile = (UserProfile) Saver.loadObject(playerFiles[playerNum - 1].toString());
            player.setProfile(loadedProfile);
            mainContainer.setPlayerProfileView(playerNum, player);
            boardKalahContainer.setPlayers(game.getPlayer(1), game.getPlayer(2));
            boardAyoContainer.setPlayers(game.getPlayer(1), game.getPlayer(2));
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading Player's Profile: " + e.getMessage());
            return false;
        }
    }

    public boolean savePlayerProfile(int playerNum, Player player) {
        try {
            Saver.saveObject(player.getProfile(), playerFiles[playerNum - 1].toString());
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving Player's Profile: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        MancalaUI mancalaUI = new MancalaUI("Mancala Games");
        mancalaUI.setSize(900, 600);
        mancalaUI.setLocationRelativeTo(null);
        mancalaUI.setVisible(true);
    }
}