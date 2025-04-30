package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Panel for displaying game information.
 */
public class GameInfoPanel extends DPanel {
    private Game game;
    private DLabel currentPlayerLabel;
    private DLabel directionLabel;
    private DLabel deckCountLabel;
    private DLabel messageLabel;
    
    /**
     * Create a new GameInfoPanel
     */
    public GameInfoPanel() {
        super(new BorderLayout());
        
        // Set preferred size
        setPreferredSize(new Dimension(800, 60));
        
        // Create a panel for game state information
        DPanel infoPanel = new DPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        
        // Create labels
        currentPlayerLabel = new DLabel("Current Player: -");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        directionLabel = new DLabel("Direction: Clockwise");
        directionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        deckCountLabel = new DLabel("Cards in Deck: 0");
        deckCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add labels to the info panel
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(directionLabel);
        infoPanel.add(deckCountLabel);
        
        // Create a message label for game messages
        messageLabel = new DLabel("");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBackground(new Color(50, 50, 50));
        messageLabel.setTextAlignmentCenter();
        
        // Create a panel for the message
        DPanel messagePanel = new DPanel(new BorderLayout());
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        messagePanel.setBackground(new Color(50, 50, 50));
        
        // Add panels to this panel
        add(infoPanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);
    }
    
    /**
     * Set the game
     */
    public void setGame(Game game) {
        this.game = game;
        updateInfo();
    }
    
    /**
     * Update the displayed information
     */
    public void updateInfo() {
        if (game != null) {
            try {
                // Update current player
                Player currentPlayer = game.getCurrentPlayer();
                if (currentPlayer != null) {
                    currentPlayerLabel.setText("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getCardCount() + " cards)");
                } else {
                    currentPlayerLabel.setText("Current Player: -");
                }
                
                // Update direction
                directionLabel.setText("Direction: " + (game.isClockwise() ? "Clockwise" : "Counter-clockwise"));
                
                // Update deck count
                deckCountLabel.setText("Cards in Deck: " + game.getDeckSize());
            } catch (IndexOutOfBoundsException e) {
                // No players added yet
                currentPlayerLabel.setText("Current Player: -");
                directionLabel.setText("Direction: Clockwise");
                deckCountLabel.setText("Cards in Deck: 0");
            }
        }
    }
    
    /**
     * Show a message in the message label
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }
    
    /**
     * Clear the message label
     */
    public void clearMessage() {
        messageLabel.setText("");
    }
}
