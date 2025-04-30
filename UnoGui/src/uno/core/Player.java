package uno.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a UNO player.
 */
public class Player {
    private String name;
    private List<Card> hand;
    private boolean isAI;
    private boolean calledUno;
    
    /**
     * Create a new Player with the specified name
     */
    public Player(String name) {
        this(name, false);
    }
    
    /**
     * Create a new Player with the specified name and AI status
     */
    public Player(String name, boolean isAI) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isAI = isAI;
        this.calledUno = false;
    }
    
    /**
     * Get the player's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the player's name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the player's hand of cards
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    /**
     * Add a card to the player's hand
     */
    public void addCard(Card card) {
        hand.add(card);
        // When a player draws a card, they haven't called UNO
        if (hand.size() > 1) {
            calledUno = false;
        }
    }
    
    /**
     * Remove a card from the player's hand
     */
    public boolean removeCard(Card card) {
        return hand.remove(card);
    }
    
    /**
     * Play a card from the player's hand
     */
    public Card playCard(Card card) {
        if (hand.remove(card)) {
            return card;
        }
        return null;
    }
    
    /**
     * Check if the player has a specific card
     */
    public boolean hasCard(Card card) {
        return hand.contains(card);
    }
    
    /**
     * Check if the player has any card that can be played on the given card
     */
    public boolean hasPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get a playable card from the player's hand
     */
    public Card getPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                return card;
            }
        }
        return null;
    }
    
    /**
     * Get the number of cards in the player's hand
     */
    public int getCardCount() {
        return hand.size();
    }
    
    /**
     * Alias for getCardCount() - Get the number of cards in the player's hand
     */
    public int getHandSize() {
        return getCardCount();
    }
    
    /**
     * Check if the player has no cards left
     */
    public boolean hasWon() {
        return hand.isEmpty();
    }
    
    /**
     * Check if the player is an AI
     */
    public boolean isAI() {
        return isAI;
    }
    
    /**
     * Set whether the player is an AI
     */
    public void setAI(boolean isAI) {
        this.isAI = isAI;
    }
    
    /**
     * Call UNO when the player has one card left
     */
    public void callUno() {
        calledUno = true;
    }
    
    /**
     * Check if the player has called UNO
     */
    public boolean hasCalledUno() {
        return calledUno;
    }
    
    /**
     * Reset the UNO call status
     */
    public void resetUnoCall() {
        calledUno = false;
    }
    
    /**
     * Calculate the score of the player's hand
     */
    public int calculateHandScore() {
        int score = 0;
        for (Card card : hand) {
            score += card.getScoreValue();
        }
        return score;
    }
    
    /**
     * Get a string representation of the player
     */
    @Override
    public String toString() {
        return name + " (" + hand.size() + " cards)";
    }
}
