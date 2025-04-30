package uno;

import dgui.DFrame;
import dgui.DFocusTraversalPolicy;
import dgui.themes.DTheme;
import uno.core.Game;
import uno.core.Player;
import uno.gui.DWoodGamePanel;
import uno.gui.SoundManager;

/**
 * Enhanced UNO game with wood-themed graphics and improved UI
 * Fixed layout issues and enhanced appearance
 */
public class UnoCompleteDGUI {
    
    // Game window size
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    // Main frame
    private DFrame frame;
    
    // Game and its panel
    private Game game;
    private DWoodGamePanel gamePanel;
    
    // Sound manager
    private SoundManager soundManager;
    
    /**
     * Constructor
     */
    public UnoCompleteDGUI() {
        // Create the game
        createGame();
        
        // Create the GUI
        createGUI();
        
        // Initialize sounds
        initSounds();
    }
    
    /**
     * Initialize the game and players
     */
    private void createGame() {
        game = new Game();
        
        // Add players
        game.addPlayer(new Player("TheLegend27", false)); // Human player
        game.addPlayer(new Player("Daenerys Targaryen", true));  // AI player
        game.addPlayer(new Player("Forrest Gump", true));   // AI North
        game.addPlayer(new Player("Hannibal Lecter", true)); // AI player
    }
    
    /**
     * Create the GUI
     */
    private void createGUI() {
        // Create the window
        frame = new DFrame("UNO - Deluxe Edition");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(true);
        
        // Create a custom focus traversal policy
        DFocusTraversalPolicy focusPolicy = new DFocusTraversalPolicy();
        
        // Create the enhanced game panel
        gamePanel = new DWoodGamePanel(game);
        
        // Configure focus traversal policy
        frame.getComponent().setFocusTraversalPolicy(focusPolicy);
        frame.getComponent().setFocusTraversalPolicyProvider(true);
        
        // Add the game panel
        frame.getContentPane().add(gamePanel.getComponent());
        
        // Configure and display window
        frame.centerOnScreen();
        frame.setVisible(true);
    }
    
    /**
     * Initialize sound manager
     */
    private void initSounds() {
        try {
            soundManager = new SoundManager();
            soundManager.setVolume(0.7f); // 70% volume
        } catch (Exception e) {
            System.err.println("Error initializing sounds: " + e.getMessage());
        }
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        // Start the game and deal cards
        game.startGame();
        
        // Play game start sound
        if (soundManager != null) {
            soundManager.playSound("game_start");
        }
    }
    
    /**
     * Program entry point
     */
    public static void main(String[] args) {
        // Create and start game
        UnoCompleteDGUI game = new UnoCompleteDGUI();
        game.startGame();
    }
}