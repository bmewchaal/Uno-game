package uno;

import dgui.DFrame;
import dgui.DFocusTraversalPolicy;
import dgui.themes.DTheme;
import uno.core.Game;
import uno.core.Player;
import uno.gui.DWoodGamePanelPure;
import uno.gui.SoundManager;

/**
 * Jeu UNO complet avec une interface graphique en bois
 * Cette version utilise exclusivement dgui sans dépendances Swing
 * et respecte toutes les règles du jeu UNO original.
 */
public class UnoCompleteDGUI {
    
    // Taille de la fenêtre du jeu
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    // La fenêtre principale
    private DFrame frame;
    
    // Le jeu et son panneau
    private Game game;
    private DWoodGamePanelPure gamePanel;
    
    // Le gestionnaire de sons
    private SoundManager soundManager;
    
    /**
     * Constructeur
     */
    public UnoCompleteDGUI() {
        // Pas besoin d'initialiser le thème, il est déjà disponible
        
        // Créer le jeu
        createGame();
        
        // Créer l'interface graphique
        createGUI();
        
        // Initialiser les sons
        initSounds();
    }
    
    /**
     * Initialise le jeu et les joueurs
     */
    private void createGame() {
        game = new Game();
        
        // Ajouter des joueurs
        game.addPlayer(new Player("TheLegend27", false)); // Joueur humain
        game.addPlayer(new Player("Daenerys Targaryen", true));  // IA
        game.addPlayer(new Player("Forrest Gump", true));   // IA nord
        game.addPlayer(new Player("Hannibal Lecter", true)); // IA
    }
    
    /**
     * Crée l'interface graphique
     */
    private void createGUI() {
        // Créer la fenêtre
        frame = new DFrame("UNO - Version Deluxe");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(true);
        
        // Créer une politique de traversée de focus personnalisée
        DFocusTraversalPolicy focusPolicy = new DFocusTraversalPolicy();
        
        // Créer le panneau de jeu
        gamePanel = new DWoodGamePanelPure(game);
        
        // Configurer la politique de traversée de focus
        frame.getComponent().setFocusTraversalPolicy(focusPolicy);
        frame.getComponent().setFocusTraversalPolicyProvider(true);
        
        // Ajouter le panneau de jeu
        frame.getContentPane().add(gamePanel.getComponent());
        
        // Configurer et afficher la fenêtre
        frame.centerOnScreen();
        frame.setVisible(true);
    }
    
    /**
     * Initialise le gestionnaire de sons
     */
    private void initSounds() {
        try {
            soundManager = new SoundManager();
            soundManager.setVolume(0.7f); // 70% du volume
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des sons: " + e.getMessage());
        }
    }
    
    /**
     * Démarrer le jeu
     */
    public void startGame() {
        // Démarrer le jeu et distribuer les cartes
        game.startGame();
        
        // Jouer un son de début de partie
        if (soundManager != null) {
            soundManager.playSound("game_start");
        }
    }
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        // Créer et démarrer le jeu
        UnoCompleteDGUI game = new UnoCompleteDGUI();
        game.startGame();
    }
}