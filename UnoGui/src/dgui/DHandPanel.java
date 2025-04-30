package dgui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom panel for displaying a player's hand of cards.
 */
public class DHandPanel extends DPanel {
    private List<DCard> cards = new ArrayList<>();
    private DCard selectedCard = null;
    private DActionListener cardSelectedListener = null;
    
    /**
     * Create a new DHandPanel
     */
    public DHandPanel() {
        super();
        ((JPanel) component).setLayout(new DHandLayout());
        
        // Add mouse listener to handle card selection
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DCard clickedCard = getCardAt(e.getX(), e.getY());
                if (clickedCard != null) {
                    selectCard(clickedCard);
                }
            }
        });
    }
    
    /**
     * Add a card to the hand
     */
    public void addCard(DCard card) {
        cards.add(card);
        ((JPanel) component).add(card.getComponent());
        refreshLayout();
    }
    
    /**
     * Remove a card from the hand
     */
    public void removeCard(DCard card) {
        if (cards.remove(card)) {
            ((JPanel) component).remove(card.getComponent());
            if (selectedCard == card) {
                selectedCard = null;
            }
            refreshLayout();
        }
    }
    
    /**
     * Clear all cards from the hand
     */
    public void clearCards() {
        cards.clear();
        ((JPanel) component).removeAll();
        selectedCard = null;
        refreshLayout();
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
    
    /**
     * Get the selected card
     */
    public DCard getSelectedCard() {
        return selectedCard;
    }
    
    /**
     * Set the card selected listener
     */
    public void setCardSelectedListener(DActionListener listener) {
        this.cardSelectedListener = listener;
    }
    
    /**
     * Refresh the layout of the cards
     */
    public void refreshLayout() {
        ((JPanel) component).revalidate();
        ((JPanel) component).repaint();
    }
    
    /**
     * Get the card at the specified coordinates
     */
    private DCard getCardAt(int x, int y) {
        Component component = ((JPanel) this.component).getComponentAt(x, y);
        for (DCard card : cards) {
            if (card.getComponent() == component) {
                return card;
            }
        }
        return null;
    }
    
    /**
     * Select a card and notify the listener
     */
    private void selectCard(DCard card) {
        // Deselect the previously selected card
        if (selectedCard != null && selectedCard != card) {
            selectedCard.setSelected(false);
        }
        
        // Select or deselect the clicked card
        if (selectedCard == card) {
            card.setSelected(false);
            selectedCard = null;
        } else {
            card.setSelected(true);
            selectedCard = card;
        }
        
        // Notify the listener
        if (cardSelectedListener != null) {
            cardSelectedListener.actionPerformed(new DActionEvent(this, "cardSelected"));
        }
        
        refreshLayout();
    }
    
    /**
     * Set the preferred size of the panel
     */
    public void setPreferredSize(Dimension preferredSize) {
        ((JPanel) component).setPreferredSize(preferredSize);
    }
    
    /**
     * Get the preferred size of the panel
     */
    public Dimension getPreferredSize() {
        return ((JPanel) component).getPreferredSize();
    }
}
