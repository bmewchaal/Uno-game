package uno;

import uno.core.*;
import uno.gui.*;
import dgui.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * Classe principale du jeu UNO avec interface en bois
 */
public class UnoWoodGame {
    private static final String MENU_PANEL = "MENU";
    private static final String GAME_PANEL = "GAME";
    private static final String SETTINGS_PANEL = "SETTINGS";
    
    private DFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private MainMenuPanel menuPanel;
    private SettingsPanel settingsPanel;
    private DWoodGamePanel gamePanel;
    private Game game;
    
    /**
     * Crée une nouvelle instance du jeu UNO
     */
    public UnoWoodGame() {
        initializeUI();
        setupEventHandlers();
        showMainMenu();
    }
    
    /**
     * Initialise l'interface utilisateur
     */
    private void initializeUI() {
        frame = new DFrame("UNO - Table en Bois");
        frame.setSize(900, 700);
        frame.setResizable(true);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        menuPanel = new MainMenuPanel();
        settingsPanel = new SettingsPanel();
        
        cardPanel.add(menuPanel.getComponent(), MENU_PANEL);
        cardPanel.add(settingsPanel.getComponent(), SETTINGS_PANEL);
        
        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
        frame.centerOnScreen();
        frame.setVisible(true);
    }
    
    /**
     * Configure les gestionnaires d'événements
     */
    private void setupEventHandlers() {
        menuPanel.setStartGameAction(e -> startNewGame());
        menuPanel.setSettingsAction(e -> showSettings());
        menuPanel.setExitAction(e -> System.exit(0));
        
        settingsPanel.setBackAction(e -> showMainMenu());
    }
    
    /**
     * Affiche le menu principal
     */
    private void showMainMenu() {
        cardLayout.show(cardPanel, MENU_PANEL);
    }
    
    /**
     * Affiche les paramètres
     */
    private void showSettings() {
        cardLayout.show(cardPanel, SETTINGS_PANEL);
    }
    
    /**
     * Démarre une nouvelle partie
     */
    private void startNewGame() {
        game = new Game();
        gamePanel = new DWoodGamePanel(game);
        gamePanel.initializeGame("Joueur 1", 3);
        gamePanel.startGame();
        
        cardPanel.add(gamePanel.getComponent(), GAME_PANEL);
        cardLayout.show(cardPanel, GAME_PANEL);
    }
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new UnoWoodGame();
        });
    }
}