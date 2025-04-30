package uno.core;

/**
 * Enumeration of UNO card values.
 */
public enum CardValue {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    SKIP("Skip"),
    REVERSE("Rev"),
    DRAW_TWO("+2"),
    WILD("Wild"),
    WILD_DRAW_FOUR("+4");
    
    private final String symbol;
    
    /**
     * Create a new CardValue with the specified display symbol
     */
    CardValue(String symbol) {
        this.symbol = symbol;
    }
    
    /**
     * Get the display symbol for this card value
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Check if this card value is a special action card
     */
    public boolean isActionCard() {
        return this == SKIP || this == REVERSE || this == DRAW_TWO || 
               this == WILD || this == WILD_DRAW_FOUR;
    }
    
    /**
     * Check if this card value is a wild card
     */
    public boolean isWildCard() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }
    
    /**
     * Get a string representation of the card value
     */
    @Override
    public String toString() {
        return symbol;
    }
}
