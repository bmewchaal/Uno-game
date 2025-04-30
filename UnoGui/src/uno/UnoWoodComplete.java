package uno;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dgui.DPanel;
import dgui.themes.DTheme;
import uno.core.Game;
import uno.core.Player;
import uno.gui.DWoodGamePanel;
import uno.gui.SoundManager;

/**
 * Jeu UNO complet avec une interface graphique en bois
 * Cette version intègre toutes les fonctionnalités de UnoGame, UnoWoodDemo et UnoWoodGame
 * dans une seule version unifiée et jouable respectant toutes les règles du jeu UNO original.
 */
public class UnoWoodComplete {
    
    // Taille de la fenêtre du jeu
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    // La fenêtre principale
    private JFrame frame;
    
    // Le jeu et son panneau
    private Game game;
    private DWoodGamePanel gamePanel;
    
    // Le gestionnaire de sons
    private SoundManager soundManager;
    
    /**
     * Constructeur
     */
    public UnoWoodComplete() {
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
        game.addPlayer(new Player("Vous", false)); // Joueur humain
        game.addPlayer(new Player("Nord", true));  // IA
        game.addPlayer(new Player("Est", true));   // IA
        game.addPlayer(new Player("Ouest", true)); // IA
        
        // Le jeu UNO utilise 7 cartes par défaut, pas besoin de configuration spéciale
    }
    
    /**
     * Crée l'interface graphique
     */
    private void createGUI() {
        // Créer la fenêtre
        frame = new JFrame("UNO - Version Deluxe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        
        // Créer le panneau de jeu
        gamePanel = new DWoodGamePanel(game);
        frame.add(gamePanel.getComponent());
        
        // Afficher la fenêtre
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
        // Utiliser le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Créer et démarrer le jeu dans l'EDT de Swing
        SwingUtilities.invokeLater(() -> {
            UnoWoodComplete game = new UnoWoodComplete();
            game.startGame();
        });
    }
}