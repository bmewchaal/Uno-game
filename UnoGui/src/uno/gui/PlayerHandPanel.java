package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel for displaying the player's hand of cards.
 */
public class PlayerHandPanel extends DPanel {
    private DHandPanel handPanel;
    private List<DCard> cards;
    
    /**
     * Create a new PlayerHandPanel
     */
    public PlayerHandPanel() {
        super(new BorderLayout());
        
        setPreferredSize(new Dimension(800, 180));
        
        // Create hand panel
        handPanel = new DHandPanel();
        handPanel.setPreferredSize(new Dimension(800, 180));
        
        // Keep track of cards
        cards = new ArrayList<>();
        
        // Add hand panel to this panel
        add(handPanel, BorderLayout.CENTER);
    }
    
    /**
     * Update the cards in the hand
     */
    public void updateCards(List<Card> playerCards) {
        // Clear existing cards
        handPanel.clearCards();
        cards.clear();
        
        // Add new cards
        for (Card card : playerCards) {
            // Convert game card to display card
            DCard dCard = createDCardFromCard(card);
            cards.add(dCard);
            handPanel.addCard(dCard);
        }
        
        // Refresh layout
        handPanel.refreshLayout();
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
    
    /**
     * Get the selected card
     */
    public DCard getSelectedCard() {
        return handPanel.getSelectedCard();
    }
    
    /**
     * Remove a card from the hand
     */
    public void removeCard(DCard card) {
        handPanel.removeCard(card);
        cards.remove(card);
    }
    
    /**
     * Add a card to the hand
     */
    public void addCard(DCard card) {
        cards.add(card);
        handPanel.addCard(card);
    }
    
    /**
     * Get all cards in the hand
     */
    public List<DCard> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Get the number of cards in the hand
     */
    public int getCardCount() {
        return cards.size();
    }
}
