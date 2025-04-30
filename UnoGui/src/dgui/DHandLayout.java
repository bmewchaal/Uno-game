package dgui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom layout manager for arranging cards in a hand.
 */
public class DHandLayout extends DLayoutManager {
    private int cardOverlap = 30; // How much cards overlap
    private int verticalOffset = 0; // How much selected cards are raised
    
    /**
     * Create a new DHandLayout with default settings
     */
    public DHandLayout() {
        // Default constructor
    }
    
    /**
     * Create a new DHandLayout with the specified card overlap
     */
    public DHandLayout(int cardOverlap) {
        this.cardOverlap = cardOverlap;
    }
    
    /**
     * Create a new DHandLayout with the specified card overlap and vertical offset
     */
    public DHandLayout(int cardOverlap, int verticalOffset) {
        this.cardOverlap = cardOverlap;
        this.verticalOffset = verticalOffset;
    }
    
    /**
     * Set the card overlap
     */
    public void setCardOverlap(int cardOverlap) {
        this.cardOverlap = cardOverlap;
    }
    
    /**
     * Get the card overlap
     */
    public int getCardOverlap() {
        return cardOverlap;
    }
    
    /**
     * Set the vertical offset for selected cards
     */
    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }
    
    /**
     * Get the vertical offset for selected cards
     */
    public int getVerticalOffset() {
        return verticalOffset;
    }
    
    /**
     * Calculate the preferred size of the container
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(parent);
    }
    
    /**
     * Calculate the minimum size of the container
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(parent);
    }
    
    /**
     * Calculate the size needed for the layout
     */
    private Dimension calculateLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int n = parent.getComponentCount();
            
            if (n == 0) {
                return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
            }
            
            // Get the size of the first component to estimate the card size
            Component firstCard = parent.getComponent(0);
            Dimension cardSize = firstCard.getPreferredSize();
            
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom + cardSize.height;
            
            if (n > 1) {
                // Add width for each card minus the overlap
                width += cardSize.width + (n - 1) * (cardSize.width - cardOverlap);
            } else {
                width += cardSize.width;
            }
            
            return new Dimension(width, height);
        }
    }
    
    /**
     * Layout the components in the container
     */
    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int n = parent.getComponentCount();
            
            if (n == 0) {
                return;
            }
            
            // Get components for easier manipulation
            List<Component> components = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                components.add(parent.getComponent(i));
            }
            
            // Calculate the size of each card (assume all cards are the same size)
            Dimension cardSize = components.get(0).getPreferredSize();
            
            // Get available width for layout
            int availableWidth = parent.getWidth() - insets.left - insets.right;
            
            // Calculate the actual overlap to use based on available space
            int effectiveOverlap = cardOverlap;
            
            // Calculate total width needed for all cards with current overlap
            int totalWidth = cardSize.width + (n - 1) * (cardSize.width - effectiveOverlap);
            
            // If the total width exceeds available width, increase the overlap
            if (totalWidth > availableWidth && n > 1) {
                // Calculate the minimum required overlap
                int minRequiredOverlap = cardSize.width - (availableWidth - cardSize.width) / (n - 1);
                
                // Ensure overlap is at least 10 pixels (to show card edges)
                effectiveOverlap = Math.min(cardSize.width - 10, Math.max(minRequiredOverlap, cardOverlap));
                
                // Recalculate total width with new overlap
                totalWidth = cardSize.width + (n - 1) * (cardSize.width - effectiveOverlap);
            }
            
            // Calculate the fan angle (for visual effect)
            double fanAngleRange = 5.0; // degrees
            
            // Calculate starting X position to center the hand
            int startX = insets.left + (availableWidth - totalWidth) / 2;
            if (startX < insets.left) startX = insets.left; // Ensure it doesn't go past the left edge
            
            // Position each card
            for (int i = 0; i < n; i++) {
                Component card = components.get(i);
                
                // Calculate the X position for this card
                int x = startX + i * (cardSize.width - effectiveOverlap);
                
                // Check if the card is selected
                boolean isSelected = false;
                if (card instanceof javax.swing.JComponent) {
                    Object selected = ((javax.swing.JComponent) card).getClientProperty("selected");
                    isSelected = (selected != null && (Boolean) selected);
                    
                    // For our custom DCard component - using reflection to check isSelected
                    try {
                        java.lang.reflect.Method method = card.getClass().getMethod("isSelected");
                        Object result = method.invoke(card);
                        if (result instanceof Boolean) {
                            isSelected = (Boolean) result;
                        }
                    } catch (Exception e) {
                        // No isSelected method or error calling it
                    }
                }
                
                // Calculate the Y position, adjusting for selected cards and slight fan effect
                int y = insets.top;
                
                // Apply a slight fan angle
                double fanAngle = 0;
                if (n > 1) { // Avoid division by zero when there's only one card
                    fanAngle = -fanAngleRange/2 + (fanAngleRange * i / (n-1));
                }
                int fanOffset = (int)(cardSize.height * Math.sin(Math.toRadians(fanAngle)) * 0.1);
                
                // Add fan offset and selection offset if applicable
                y += fanOffset;
                if (isSelected && verticalOffset > 0) {
                    y -= verticalOffset;
                }
                
                // Ensure card is not positioned off the container
                if (x + cardSize.width > parent.getWidth() - insets.right) {
                    x = parent.getWidth() - insets.right - cardSize.width;
                }
                
                // Set the component's bounds
                card.setBounds(x, y, cardSize.width, cardSize.height);
            }
        }
    }
}
