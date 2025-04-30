package uno;

import uno.core.*;
import uno.gui.*;
import dgui.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

public class UnoGame {
    private static final String MENU_PANEL = "MENU";
    private static final String GAME_PANEL = "GAME";
    private static final String SETTINGS_PANEL = "SETTINGS";
    
    private DFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private MainMenuPanel menuPanel;
    private SettingsPanel settingsPanel;
    private GameWindow gameWindow;
    private Game game;
    
    public UnoGame() {
        initializeUI();
        setupEventHandlers();
        showMainMenu();
    }
    
    private void initializeUI() {
        frame = new DFrame("UNO Game");
        frame.setSize(800, 600);
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
    
    private void setupEventHandlers() {
        menuPanel.setStartGameAction(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                startNewGame();
            }
        });
        
        menuPanel.setSettingsAction(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                showSettings();
            }
        });
        
        menuPanel.setExitAction(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                System.exit(0);
            }
        });
        
        settingsPanel.setBackAction(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                showMainMenu();
            }
        });
    }
    
    private void showMainMenu() {
        cardLayout.show(cardPanel, MENU_PANEL);
    }
    
    private void showSettings() {
        cardLayout.show(cardPanel, SETTINGS_PANEL);
    }
    
    private void startNewGame() {
        game = new Game();
        gameWindow = new GameWindow(game);
        gameWindow.addPlayers("Joueur 1", 3);
        gameWindow.startGame();
        
        cardPanel.add(gameWindow.getGamePanel().getComponent(), GAME_PANEL);
        cardLayout.show(cardPanel, GAME_PANEL);
    }
    
    public static void main(String[] args) {
        new UnoGame();
    }
}
