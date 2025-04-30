package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

/**
 * Panel for displaying the game table (discard pile and deck).
 */
public class TablePanel extends DPanel {
    private Game game;
    private DDiscardPilePanel discardPilePanel;
    private DPanel deckPanel;
    private DLabel deckLabel;
    
    /**
     * Create a new TablePanel
     */
    public TablePanel() {
        super(new BorderLayout());
        
        setPreferredSize(new Dimension(800, 200));
        
        // Create a center panel to hold deck and discard pile
        DPanel centerPanel = new DPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        
        // Create discard pile panel
        discardPilePanel = new DDiscardPilePanel();
        discardPilePanel.setPreferredSize(new Dimension(120, 170));
        
        // Create deck panel
        deckPanel = new DPanel(new BorderLayout());
        deckPanel.setPreferredSize(new Dimension(120, 170));
        
        // Create deck face down card
        DCard deckCard = new DCard(Color.BLACK, "UNO");
        deckCard.setFaceUp(false);
        
        // Create deck label
        deckLabel = new DLabel("Draw Pile");
        deckLabel.setFont(new Font("Arial", Font.BOLD, 12));
        deckLabel.setTextAlignmentCenter();
        
        // Add components to deck panel
        // We need to add the component directly to the underlying JPanel
        ((JPanel) deckPanel.getComponent()).add(deckCard.getComponent(), BorderLayout.CENTER);
        deckPanel.add(deckLabel, BorderLayout.SOUTH);
        
        // Add panels to center panel
        centerPanel.add(deckPanel);
        centerPanel.add(discardPilePanel);
        
        // Add center panel to this panel
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Set the game
     */
    public void setGame(Game game) {
        this.game = game;
        updateDiscardPile();
    }
    
    /**
     * Update the discard pile display
     */
    public void updateDiscardPile() {
        if (game != null) {
            Card topCard = game.getTopCard();
            
            if (topCard != null) {
                // Clear existing cards
                discardPilePanel.clearCards();
                
                // Create a DCard for the top card
                DCard dCard = createDCardFromCard(topCard);
                
                // Add it to the discard pile
                discardPilePanel.addCard(dCard);
            }
            
            // Update deck count label
            deckLabel.setText("Draw Pile (" + game.getDeckSize() + ")");
        }
    }
    
    /**
     * Create a DCard from a Card
     */
    private DCard createDCardFromCard(Card card) {
        // Convert CardColor to AWT Color
        Color color;
        switch (card.getColor()) {
            case RED:
                color = Color.RED;
                break;
            case BLUE:
                color = Color.BLUE;
                break;
            case GREEN:
                color = Color.GREEN;
                break;
            case YELLOW:
                color = Color.YELLOW;
                break;
            default:
                color = Color.BLACK; // For WILD
                break;
        }
        
        // Get the card value symbol
        String value = card.getValue().getSymbol();
        
        // Create the DCard
        return new DCard(color, value);
    }
}
