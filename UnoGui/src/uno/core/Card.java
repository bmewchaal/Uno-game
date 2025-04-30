package uno.core;

/**
 * Class representing a UNO card.
 */
public class Card {
    private CardColor color;
    private CardValue value;
    private CardColor chosenColor; // Pour les cartes WILD
    
    /**
     * Create a new Card with the specified color and value
     */
    public Card(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
        
        // Ensure wild cards have the WILD color
        if (value != null && value.isWildCard()) {
            this.color = CardColor.WILD;
        }
    }
    
    /**
     * Get the color of the card
     */
    public CardColor getColor() {
        return color;
    }
    
    /**
     * Set the color of the card (mainly for changing wild card colors after play)
     */
    public void setColor(CardColor color) {
        this.color = color;
    }
    
    /**
     * Get the value of the card
     */
    public CardValue getValue() {
        return value;
    }
    
    /**
     * Set the value of the card
     */
    public void setValue(CardValue value) {
        this.value = value;
        
        // Ensure wild cards have the WILD color
        if (value != null && value.isWildCard()) {
            this.color = CardColor.WILD;
        }
    }
    
    /**
     * Get the chosen color for wild cards
     */
    public CardColor getChosenColor() {
        return chosenColor != null ? chosenColor : color;
    }
    
    /**
     * Set the chosen color for wild cards
     */
    public void setChosenColor(CardColor chosenColor) {
        this.chosenColor = chosenColor;
    }
    
    /**
     * Check if this card can be played on top of the specified card
     */
    public boolean canPlayOn(Card otherCard) {
        // Null safety checks
        if (this.value == null || otherCard == null || otherCard.value == null) {
            return false;
        }
        
        // Wild cards can be played on any card
        if (this.value.isWildCard()) {
            return true;
        }
        
        // Cards can be played if they match the color or value of the previous card
        return this.color == otherCard.color || this.value == otherCard.value;
    }
    
    /**
     * Check if this card is a wild card
     */
    public boolean isWild() {
        return this.value != null && this.value.isWildCard();
    }
    
    /**
     * Get the score value of this card (used when calculating points at the end of a round)
     */
    public int getScoreValue() {
        if (value == null) {
            return 0; // Valeur par défaut pour une carte sans valeur
        }
        
        switch (value) {
            case SKIP:
            case REVERSE:
            case DRAW_TWO:
                return 20;
            case WILD:
            case WILD_DRAW_FOUR:
                return 50;
            default:
                // Number cards are worth their face value
                return value.ordinal(); // 0-9 for number cards
        }
    }
    
    /**
     * Get a string representation of the card
     */
    @Override
    public String toString() {
        return color + " " + (value != null ? value : "Card");
    }
    
    /**
     * Check if this card equals another object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Card other = (Card) obj;
        if (value == null && other.value == null) {
            return color == other.color;
        }
        if (value == null || other.value == null) {
            return false;
        }
        return color == other.color && value == other.value;
    }
    
    /**
     * Get the hash code for this card
     */
    @Override
    public int hashCode() {
        int colorHash = color != null ? color.hashCode() : 0;
        int valueHash = value != null ? value.hashCode() : 0;
        return 31 * colorHash + valueHash;
    }
}
