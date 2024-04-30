package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import mancala.KalahRules;
import mancala.AyoRules;
import mancala.MancalaGame;
import mancala.Saver;
import mancala.Player;

public class MancalaUI extends JFrame{
    
    private JButton storeButtonL;
    private JButton storeButtonR;
    private JButton playerSaveButton;
    private PositionAwareButton[][] pitButtons;
    private JLabel playerSaveMessage;
    private JLabel startupMessageLabel;
    private JLabel playerTurnText;
    private JLabel eventText;
    private JLabel winnerText;
    private JLabel profile1Text;
    private JLabel profile2Text;
    private JPanel gameContainer;
    private JPanel modeSelectPanel;
    private JPanel endGameSelectPanel;
    private JPanel gameMessagePanel;
    private JMenuBar menuBar;

    private MancalaGame game;

    private final int numRows = 2, numCols = 6;

    private boolean isGameLoading = false, isGameSaved = false;

    public MancalaUI(String title) {
        super();
        game = new MancalaGame();
        basicSetUp(title);
        setupGameContainer();
        add(startupMessage(), BorderLayout.NORTH);
        add(gameContainer, BorderLayout.CENTER);
        add(makeBottomPanel(), BorderLayout.AFTER_LAST_LINE);
        JOptionPane.showMessageDialog(null, "Welcome to Mancala!");
        if(!initLoadPlayers()) {
            String input = JOptionPane.showInputDialog("Player 1 name: ");
            if(input != null) {
                game.getPlayer(0).setName(input);
            } else {
                game.getPlayer(0).setName("Player 1");
            }
            input = JOptionPane.showInputDialog("Player 2 name: ");
            if(input != null) {
                game.getPlayer(1).setName(input);
            } else {
                game.getPlayer(1).setName("Player 2");
            }  
        }
            
        makeMenu();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);
        add(userProfile(2), BorderLayout.WEST);
        add(userProfile(1), BorderLayout.EAST);

        pack();
    }

    private void basicSetUp(String title){
        this.setTitle(title);
        gameContainer = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public static void main(String[] args) {
        MancalaUI example = new MancalaUI("Mancala");
        example.updateGame();
        example.setVisible(true);
    }

    private JLabel startupMessage() {
        JLabel label = new JLabel("!M A N C A L A!", SwingConstants.CENTER);
        startupMessageLabel = label;
        return label;
    }

    private JLabel userProfile(int playerNum) {
        JLabel label = new JLabel();
        label.setText("<html>" + ("Player " + playerNum + ": " + game.getPlayer(playerNum-1).getProfile()).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");

        if(playerNum == 1) {
            profile1Text = label;
        } else {
            profile2Text = label;
        }

        return label;
    }

    public void setupGameContainer() {
        gameContainer.add(makeStore1Button());
        gameContainer.add(makeMancalaGrid());
        gameContainer.add(makeStore2Button());
        gameContainer.setVisible(false);
    }

    void newGame() {
        
        if((!isGameSaved && JOptionPane.showConfirmDialog(null, "Are you sure you would like to leave the current game?\nUnsaved progress will be lost.") == 0) || (isGameSaved)) {
            
            gameContainer.setVisible(false);
            gameMessagePanel.setVisible(false);
            menuBar.setVisible(false);
            endGameSelectPanel.setVisible(false);
            modeSelectPanel.setVisible(true);
            startupMessageLabel.setText("!M A N C A L A!");

            pack();
        }
    }
    void newGame(String mode) {
        if(mode.equals("A")) {
            game.startNewGame(new AyoRules(), false);
            startupMessageLabel.setText("!A Y O!");
        } else if(mode.equals("K")) {
            game.startNewGame(new KalahRules(), true);
            startupMessageLabel.setText("!K A L A H!");
        } else if(mode.equals("L")) {
            isGameLoading = true;
            if(game.isKalahRules()) {
                startupMessageLabel.setText("!K A L A H!");
            } else {
                startupMessageLabel.setText("!A Y O!");
            }
        }
        gameContainer.setVisible(true);
        gameMessagePanel.setVisible(true);
        menuBar.setVisible(true);
        modeSelectPanel.setVisible(false);
        endGameSelectPanel.setVisible(false);
        playerTurnText.setVisible(true);

        updateGame();
        updateProfiles();

        isGameLoading = false;
    }

    void loadGame() {
        if((!isGameSaved && JOptionPane.showConfirmDialog(null, "Are you sure you would like to leave the current game?\nUnsaved progress will be lost.") == 0) || (isGameSaved)) {
            JFileChooser chooser = new JFileChooser();
            int valid = chooser.showOpenDialog(null);

            if(valid == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getAbsolutePath();
                try {
                    game = (MancalaGame)Saver.loadObject(fileName);
                } catch(IOException e) {
                    winnerText.setText("FATAL: UNABLE TO LOAD GAME");
                    winnerText.setVisible(true);
                }
                newGame("L");
            }
        }
    }
    void saveGame() {
        JFileChooser chooser = new JFileChooser();
        int valid = chooser.showSaveDialog(null);

        if(valid == JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getAbsolutePath();
            try {
                Saver.saveObject(game, fileName);
            } catch(IOException e) {
                winnerText.setText("FATAL: UNABLE TO SAVE GAME");
                winnerText.setVisible(true);
            }
            isGameSaved = true;
        }
        
    }
    void quitGame() {
        //0 = yes, 1 = no, 2 = cancel
        if((!isGameSaved && JOptionPane.showConfirmDialog(null, "Are you sure you would like to quit?\nUnsaved progress will be lost.") == 0) || (isGameSaved)) {
            System.exit(0);
        }
    }
    private JButton makeStore1Button() {
        JButton button = new JButton();
        storeButtonL = button;
        return button;
    }
    private JButton makeStore2Button() {
        JButton button = new JButton();
        storeButtonR = button;
        return button;
    }
    private JPanel makeMancalaGrid() {
        JPanel panel = new JPanel();
        pitButtons = new PositionAwareButton[numRows][numCols];
        panel.setLayout(new GridLayout(numRows, numCols));
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numCols; x++) {
                pitButtons[y][x] = new PositionAwareButton();
                pitButtons[y][x].setAcross(x + 1);
                pitButtons[y][x].setDown(y + 1);
                final int Y = y;
                final int X = x;
                pitButtons[y][x].addActionListener(e -> callMove(getPitNum(Y, X)));
                panel.add(pitButtons[y][x]);
            }
        }
        return panel;
    }
    private JPanel makeBottomPanel() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(3,1);
        layout.setVgap(10);
        panel.setLayout(layout);

        panel.add(makeModeSelectButtonPanel());
        panel.add(makeGameMessagePanel());
        panel.add(makeEndGameSelectPanel());

        return panel;

    }
    private JPanel makeModeSelectButtonPanel() {
        
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(2,2);
        layout.setHgap(10);
        panel.setLayout(layout);

        JLabel selectMessage = new JLabel("Select Game Mode:");
        JButton kalahButton = new JButton("Kalah");
        JButton ayoButton = new JButton("Ayo");

        kalahButton.addActionListener(e -> newGame("K"));
        ayoButton.addActionListener(e -> newGame("A"));

        panel.add(selectMessage);
        panel.add(new JLabel());
        panel.add(kalahButton);
        panel.add(ayoButton);

        modeSelectPanel = panel;

        return panel;

    }
    private JPanel makeEndGameSelectPanel() {
        
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(3,3);
        layout.setHgap(10);
        panel.setLayout(layout);

        JLabel selectMessage = new JLabel("What would you like to do now?", SwingConstants.CENTER);
        JLabel savePlayersMessage = new JLabel("Save your players before you go: ", SwingConstants.RIGHT);
        JButton savePlayers = new JButton("Save Players");
        JButton newGame = new JButton("New Game");
        JButton loadGame = new JButton("Load Game");
        JButton quitGame = new JButton("Quit Game");

        savePlayers.addActionListener(e -> savePlayersButton());
        newGame.addActionListener(e -> newGame());
        loadGame.addActionListener(e -> loadGame());
        quitGame.addActionListener(e -> quitGame());

        panel.add(new JLabel());
        panel.add(selectMessage);
        panel.add(new JLabel());
        panel.add(savePlayersMessage);
        panel.add(savePlayers);
        panel.add(new JLabel());
        panel.add(newGame);
        panel.add(loadGame);
        panel.add(quitGame);

        playerSaveMessage = savePlayersMessage;
        playerSaveButton = savePlayers;
        endGameSelectPanel = panel;

        panel.setVisible(false);

        return panel;

    }
    private JPanel makeGameMessagePanel() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(3,1);

        layout.setVgap(10);
        panel.setLayout(layout);

        JLabel playerNum = new JLabel("PLAYER 1's TURN", SwingConstants.CENTER);
        JLabel event = new JLabel("EVENT", SwingConstants.CENTER);
        JLabel winner = new JLabel("The winner is...", SwingConstants.CENTER);
        // event.setVisible(false);
        playerTurnText = playerNum;
        eventText = event;
        winnerText = winner;
        winnerText.setVisible(false);

        panel.add(event);
        panel.add(winner);
        panel.add(playerNum);

        gameMessagePanel = panel;

        panel.setVisible(false);
        
        return panel;
    }
    
    private void updateGame() {

        if(!isGameLoading) {
            isGameSaved = false;
        }
        if(game.getBoard().getCaptured()) {
            eventText.setText("Capture!");
            eventText.setVisible(true);
        } else {
            eventText.setVisible(false);
        }
        if(game.isGameOver() && !isGameLoading) {
            endGame();
        } else {
            winnerText.setVisible(false);
        }

        if(game.getBoard().getGoAgain()) {
            playerTurnText.setText(game.getCurrentPlayer().getName() + "'s Turn Again!");
        } else {
            playerTurnText.setText(game.getCurrentPlayer().getName() + "'s Turn");
        }

        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numCols; x++) {
                pitButtons[y][x].setText(((Integer)game.getBoard().getNumStones(getPitNum(y, x))).toString());
            }
        }

        storeButtonL.setText(((Integer)game.getPlayerStoreCount(1)).toString());
        storeButtonR.setText(((Integer)game.getPlayerStoreCount(0)).toString());

        pack();
    }

    private void callMove(int startPit) {
        if(!game.isGameOver()) {
            try {
                game.move(startPit);
                updateGame();
            } catch(InvalidMoveException e) {
                eventText.setText("Invalid move. Try Again.");
                eventText.setVisible(true);
            }
        }
    }

    private void endGame() {
        
        Player winner = null;
        try {
            game.getBoard().dumpAllStonesToStore(1);
            game.getBoard().dumpAllStonesToStore(2);
            winner = game.getWinner();
        } catch(GameNotOverException e) {
            winnerText.setText("The game's not over.");
            return;
        }

        if(winner != null) {
            winnerText.setText(winner.getName() + " WINS!");
        } else {
            winnerText.setText("IT'S A TIE!");
        }
        winnerText.setVisible(true);
        
        playerTurnText.setVisible(false);
        menuBar.setVisible(false);
        endGameSelectPanel.setVisible(true);
        playerSaveButton.setVisible(true);
        playerSaveMessage.setVisible(true);

        incrementProfiles(winner);
    }

    private void updateProfiles() {
        updateProfile(1);
        updateProfile(2);
    }
    private void updateProfile(int playerNum) {
        if(playerNum == 1) {
            profile1Text.setText("<html>" + ("Player " + playerNum + ": " + game.getPlayer(playerNum-1).getProfile()).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        } else {
            profile2Text.setText("<html>" + ("Player " + playerNum + ": " + game.getPlayer(playerNum-1).getProfile()).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        }
    }
    private void incrementProfiles(Player winner) {

        if(game.isKalahRules()) {
            game.getPlayer(0).getProfile().incrementNumKalahGames();
            game.getPlayer(1).getProfile().incrementNumKalahGames();
            if(winner != null) {
                winner.getProfile().incrementNumKalahWins();
            }
        } else {
            game.getPlayer(0).getProfile().incrementNumAyoGames();
            game.getPlayer(1).getProfile().incrementNumAyoGames();
            if(winner != null) {
                winner.getProfile().incrementNumAyoWins();
            }
        }

        updateProfiles();

    }

    private int getPitNum(int y, int x) {
        return 2*x*y-x-11*y+12;
    }

    private void makeMenu() {
        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu playersMenu = new JMenu("Players");
        
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> newGame());

        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.addActionListener(e -> saveGame());

        JMenuItem loadGame = new JMenuItem("Load Game");
        loadGame.addActionListener(e -> loadGame());

        JMenuItem quitGame = new JMenuItem("Quit Game");
        quitGame.addActionListener(e -> quitGame());

        JMenu changePlayer = new JMenu("Change Name");
        JMenuItem changePlayerOne = new JMenuItem("Player 1: " + game.getPlayer(0).getName());
        changePlayerOne.addActionListener(e -> setPlayerName(1));
        JMenuItem changePlayerTwo = new JMenuItem("Player 2: " + game.getPlayer(1).getName());
        changePlayerTwo.addActionListener(e -> setPlayerName(2));
        JMenuItem changeBothPlayers = new JMenuItem("Both");
        changeBothPlayers.addActionListener(e -> setPlayerNames());

        JMenu savePlayer = new JMenu("Save Player");
        JMenuItem savePlayerOne = new JMenuItem("Player 1: " + game.getPlayer(0).getName());
        savePlayerOne.addActionListener(e -> savePlayer(1));
        JMenuItem savePlayerTwo = new JMenuItem("Player 2: " + game.getPlayer(1).getName());
        savePlayerTwo.addActionListener(e -> savePlayer(2));
        JMenuItem saveBothPlayers = new JMenuItem("Both");
        saveBothPlayers.addActionListener(e -> savePlayers());

        JMenu loadPlayer = new JMenu("Load Player");
        JMenuItem loadPlayerOne = new JMenuItem("Into Player 1: " + game.getPlayer(0).getName());
        loadPlayerOne.addActionListener(e -> loadPlayer(1));
        JMenuItem loadPlayerTwo = new JMenuItem("Into Player 2: " + game.getPlayer(1).getName());
        loadPlayerTwo.addActionListener(e -> loadPlayer(2));

        gameMenu.add(newGame);
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.add(quitGame);

        changePlayer.add(changePlayerOne);
        changePlayer.add(changePlayerTwo);
        changePlayer.add(changeBothPlayers);
        playersMenu.add(changePlayer);

        savePlayer.add(savePlayerOne);
        savePlayer.add(savePlayerTwo);
        playersMenu.add(savePlayer);

        loadPlayer.add(loadPlayerOne);
        loadPlayer.add(loadPlayerTwo);
        playersMenu.add(loadPlayer);

        menuBar.add(gameMenu);
        menuBar.add(playersMenu);
    }

    private void setPlayerNames() {
        setPlayerName(1);
        setPlayerName(2);
    }
    private void setPlayerName(int playerNum) {
        String input = JOptionPane.showInputDialog("New name to replace " + game.getPlayer(playerNum-1).getName() + ": ");
        if(input != null) {
            game.getPlayer(playerNum-1).setName(input);
            updateProfile(playerNum);
            updateGame();
            makeMenu();
            setJMenuBar(menuBar);
        }
    }

    private void savePlayersButton() {
        savePlayers();
        playerSaveButton.setVisible(false);
        playerSaveMessage.setVisible(false);
        isGameSaved = true;
    }
    private void savePlayers() {
        savePlayer(1);
        savePlayer(2);
    }
    private void savePlayer(int playerNum) {
        JFileChooser chooser = new JFileChooser();
        int valid = chooser.showSaveDialog(null);
        String fileName = "assets/playerSave";

        if(valid == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getAbsolutePath();
            try {
                Saver.saveObject(game.getPlayer(playerNum-1), fileName);
            } catch(IOException e) {
                winnerText.setText("FATAL: UNABLE TO SAVE PLAYER " + game.getPlayer(playerNum-1));
                winnerText.setVisible(true);
            }
        }
    }
    
    private boolean initLoadPlayers() {

        boolean success = false;

        if(JOptionPane.showConfirmDialog(null, "Would you like to load existing players?") == 0) {
            JFileChooser chooser = new JFileChooser();
            int valid;
            String fileName = "assets/playerSave";
            Player loadedPlayer;
            
            success = true;
            for(int i = 0; i < 2; i++) {
                valid = chooser.showOpenDialog(null);
                if(valid == JFileChooser.APPROVE_OPTION) {
                    fileName = chooser.getSelectedFile().getAbsolutePath();
                    try {
                        loadedPlayer = (Player)Saver.loadObject(fileName);
                        game.getPlayer(i).setName(loadedPlayer.getName());
                        game.getPlayer(i).setProfile(loadedPlayer.getProfile());
                    } catch(IOException e) {
                        success = false;
                        break;
                    }
                } else {
                    success = false;
                    break;
                }
            }
        }

        return success;
        
    }
    private void loadPlayer(int playerNum) {
        JFileChooser chooser = new JFileChooser();
        int valid = chooser.showOpenDialog(null);
        String fileName = "assets/playerSave";
        Player loadedPlayer;

        if(valid == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getAbsolutePath();
            try {
                loadedPlayer = (Player)Saver.loadObject(fileName);
                game.getPlayer(playerNum-1).setName(loadedPlayer.getName());
                game.getPlayer(playerNum-1).setProfile(loadedPlayer.getProfile());
            } catch(IOException e) {
                winnerText.setText("FATAL: UNABLE TO LOAD PLAYER" + game.getPlayer(playerNum-1));
                winnerText.setVisible(true);
            }
        
            isGameLoading = true;
            updateProfile(playerNum);
            updateGame();
            makeMenu();
            setJMenuBar(menuBar);
            isGameLoading = false;
        }
    }

}
