package uno.core;

import java.awt.Color;

/**
 * Enumeration of UNO card colors.
 */
public enum CardColor {
    RED(Color.RED),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    WILD(Color.BLACK);
    
    private final Color color;
    
    /**
     * Create a new CardColor with the specified AWT Color
     */
    CardColor(Color color) {
        this.color = color;
    }
    
    /**
     * Get the AWT Color corresponding to this card color
     */
    public Color getAwtColor() {
        return color;
    }
    
    /**
     * Get a string representation of the card color
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
