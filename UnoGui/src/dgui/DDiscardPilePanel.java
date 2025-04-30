package dgui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Stack;

/**
 * Custom panel for displaying the discard pile.
 */
public class DDiscardPilePanel extends DPanel {
    private Stack<DCard> cards = new Stack<>();
    private DCard topCard = null;
    
    /**
     * Create a new DDiscardPilePanel
     */
    public DDiscardPilePanel() {
        super();
        
        // Create a custom layout that stacks cards with a slight offset
        setLayout(null);
        
        // Set a minimum size for the discard pile
        setPreferredSize(new Dimension(120, 170));
        
        // Set a background color to indicate the discard area
        setBackground(new Color(240, 240, 240));
    }
    
    /**
     * Add a card to the discard pile
     */
    public void addCard(DCard card) {
        // Remove previous top card from view if it exists
        if (topCard != null) {
            ((JPanel) component).remove(topCard.getComponent());
        }
        
        // Add the new card
        cards.push(card);
        topCard = card;
        
        // Position the card in the center of the panel
        card.getComponent().setBounds(10, 10, 100, 150);
        
        // Add the card to the panel and make it face up
        card.setFaceUp(true);
        ((JPanel) component).add(card.getComponent());
        
        // Refresh the panel
        revalidate();
        repaint();
    }
    
    /**
     * Get the top card from the discard pile without removing it
     */
    public DCard peekTopCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.peek();
    }
    
    /**
     * Remove and return the top card from the discard pile
     */
    public DCard removeTopCard() {
        if (cards.isEmpty()) {
            return null;
        }
        
        DCard card = cards.pop();
        ((JPanel) component).remove(card.getComponent());
        
        // Update the top card reference
        topCard = cards.isEmpty() ? null : cards.peek();
        
        // If there's a new top card, add it to the view
        if (topCard != null) {
            topCard.getComponent().setBounds(10, 10, 100, 150);
            ((JPanel) component).add(topCard.getComponent());
        }
        
        // Refresh the panel
        revalidate();
        repaint();
        paintDiscardPile();
        
        return card;
    }
    
    /**
     * Clear all cards from the discard pile
     */
    public void clearCards() {
        cards.clear();
        ((JPanel) component).removeAll();
        topCard = null;
        revalidate();
        repaint();
        paintDiscardPile();
    }
    
    /**
     * Get all cards from the discard pile
     */
    public Stack<DCard> getCards() {
        return new Stack<DCard>() {{ 
            addAll(cards); 
        }};
    }
    
    /**
     * Get the number of cards in the discard pile
     */
    public int getCardCount() {
        return cards.size();
    }
    
    /**
     * Check if the discard pile is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        ((JPanel) component).setPreferredSize(preferredSize);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return ((JPanel) component).getPreferredSize();
    }
    
    /**
     * Paint the discard pile
     * This is a custom method that will be called when component needs painting
     */
    public void paintDiscardPile() {
        // If there are no cards, draw an empty pile indicator
        if (cards.isEmpty()) {
            Graphics g = ((JPanel) component).getGraphics();
            if (g != null) {
                g.setColor(new Color(220, 220, 220));
                g.fillRoundRect(10, 10, 100, 150, 20, 20);
                g.setColor(Color.GRAY);
                g.drawRoundRect(10, 10, 100, 150, 20, 20);
                g.drawString("Discard Pile", 30, 85);
                g.dispose();
            }
        }
    }
}
