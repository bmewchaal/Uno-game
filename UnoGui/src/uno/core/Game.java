package uno.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Random;

/**
 * Class representing a UNO game.
 */
public class Game {
    private List<Player> players;
    private Stack<Card> deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private boolean isClockwise;
    private boolean gameStarted;
    private boolean gameEnded;
    private Random random;
    
    // Game event listeners
    private List<GameEventListener> eventListeners;
    
    /**
     * Create a new UNO game
     */
    public Game() {
        players = new ArrayList<>();
        deck = new Stack<>();
        discardPile = new Stack<>();
        eventListeners = new ArrayList<>();
        random = new Random();
        isClockwise = true;
        gameStarted = false;
        gameEnded = false;
    }
    
    /**
     * Initialize the game
     */
    public void init() {
        // Ensure we have at least 2 players
        if (players.size() < 2) {
            throw new IllegalStateException("At least 2 players are required to start a game");
        }
        
        // Initialize the deck
        initializeDeck();
        
        // Shuffle the deck
        Collections.shuffle(deck, random);
        
        // Deal 7 cards to each player
        dealInitialCards();
        
        // Put the top card of the deck into the discard pile
        Card topCard = deck.pop();
        
        // If the first card is a wild card, assign it a random color
        if (topCard.getValue().isWildCard()) {
            CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
            topCard.setColor(colors[random.nextInt(colors.length)]);
        }
        
        discardPile.push(topCard);
        
        // Set the first player randomly
        currentPlayerIndex = random.nextInt(players.size());
        
        // Set game state
        gameStarted = true;
        gameEnded = false;
        
        // Notify listeners that the game has started
        notifyGameStarted();
    }
    
    /**
     * Initialize the deck with all UNO cards
     */
    private void initializeDeck() {
        deck.clear();
        
        // Add number cards (0-9) for each color
        for (CardColor color : new CardColor[]{CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW}) {
            // Add one 0 card
            deck.push(new Card(color, CardValue.ZERO));
            
            // Add two of each number card (1-9)
            for (int i = 0; i < 2; i++) {
                deck.push(new Card(color, CardValue.ONE));
                deck.push(new Card(color, CardValue.TWO));
                deck.push(new Card(color, CardValue.THREE));
                deck.push(new Card(color, CardValue.FOUR));
                deck.push(new Card(color, CardValue.FIVE));
                deck.push(new Card(color, CardValue.SIX));
                deck.push(new Card(color, CardValue.SEVEN));
                deck.push(new Card(color, CardValue.EIGHT));
                deck.push(new Card(color, CardValue.NINE));
            }
            
            // Add action cards (Skip, Reverse, Draw Two)
            for (int i = 0; i < 2; i++) {
                deck.push(new Card(color, CardValue.SKIP));
                deck.push(new Card(color, CardValue.REVERSE));
                deck.push(new Card(color, CardValue.DRAW_TWO));
            }
        }
        
        // Add wild cards
        for (int i = 0; i < 4; i++) {
            deck.push(new Card(CardColor.WILD, CardValue.WILD));
            deck.push(new Card(CardColor.WILD, CardValue.WILD_DRAW_FOUR));
        }
    }
    
    /**
     * Deal 7 cards to each player
     */
    private void dealInitialCards() {
        for (Player player : players) {
            player.getHand().clear();
            for (int i = 0; i < 7; i++) {
                player.addCard(deck.pop());
            }
        }
    }
    
    /**
     * Add a player to the game
     */
    public void addPlayer(Player player) {
        if (gameStarted) {
            throw new IllegalStateException("Cannot add players after the game has started");
        }
        players.add(player);
    }
    
    /**
     * Get the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    /**
     * Get the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Get the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    /**
     * Check if the game is running in a clockwise direction
     */
    public boolean isClockwise() {
        return isClockwise;
    }
    
    /**
     * Get the top card of the discard pile
     */
    public Card getTopCard() {
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.peek();
    }
    
    /**
     * Play a card from the current player's hand
     */
    public boolean playCard(Card card) {
        Player currentPlayer = getCurrentPlayer();
        
        // Check if the card can be played
        if (!card.canPlayOn(getTopCard())) {
            return false;
        }
        
        // Remove the card from the player's hand
        if (!currentPlayer.removeCard(card)) {
            return false;
        }
        
        // Add the card to the discard pile
        discardPile.push(card);
        
        // Handle special cards
        handleSpecialCard(card);
        
        // Check if the player has won
        if (currentPlayer.getCardCount() == 0) {
            gameEnded = true;
            notifyGameEnded(currentPlayer);
            return true;
        }
        
        // Check if the player has one card left and hasn't called UNO
        if (currentPlayer.getCardCount() == 1 && !currentPlayer.hasCalledUno()) {
            // The player forgot to call UNO, they should draw 2 cards
            // But for simplicity, we'll just notify the listeners
            notifyPlayerForgotUno(currentPlayer);
        }
        
        // Advance to the next player
        advanceToNextPlayer();
        
        return true;
    }
    
    /**
     * Handle the effects of special cards
     */
    private void handleSpecialCard(Card card) {
        CardValue value = card.getValue();
        
        switch (value) {
            case SKIP:
                // Skip the next player
                advanceToNextPlayer();
                notifyPlayerSkipped(getCurrentPlayer());
                break;
                
            case REVERSE:
                // Reverse the direction of play
                isClockwise = !isClockwise;
                notifyDirectionChanged(isClockwise);
                break;
                
            case DRAW_TWO:
                // Next player draws 2 cards and misses their turn
                advanceToNextPlayer();
                Player nextPlayer = getCurrentPlayer();
                drawCards(nextPlayer, 2);
                notifyPlayerDrewCards(nextPlayer, 2);
                advanceToNextPlayer();
                break;
                
            case WILD:
                // Color change is handled by the UI
                notifyColorChanged(card.getColor());
                break;
                
            case WILD_DRAW_FOUR:
                // Next player draws 4 cards and misses their turn
                advanceToNextPlayer();
                Player drawFourPlayer = getCurrentPlayer();
                drawCards(drawFourPlayer, 4);
                notifyPlayerDrewCards(drawFourPlayer, 4);
                advanceToNextPlayer();
                
                // Color change is handled by the UI
                notifyColorChanged(card.getColor());
                break;
                
            default:
                // Regular number card, nothing special happens
                break;
        }
    }
    
    /**
     * Advance to the next player
     */
    public void advanceToNextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        
        notifyPlayerTurn(getCurrentPlayer());
    }
    
    /**
     * Draw a card from the deck for the specified player
     */
    public Card drawCard(Player player) {
        // Check if the deck is empty
        if (deck.isEmpty()) {
            // If the deck is empty, shuffle the discard pile (except the top card)
            // and use it as the new deck
            reshuffleDeck();
        }
        
        // If the deck is still empty after reshuffling, return null
        if (deck.isEmpty()) {
            return null;
        }
        
        // Draw a card from the deck
        Card card = deck.pop();
        player.addCard(card);
        
        notifyPlayerDrewCards(player, 1);
        
        return card;
    }
    
    /**
     * Draw multiple cards for a player
     */
    private void drawCards(Player player, int count) {
        for (int i = 0; i < count; i++) {
            drawCard(player);
        }
    }
    
    /**
     * Reshuffle the discard pile into the deck
     */
    private void reshuffleDeck() {
        // Vérifier si la pile de défausse est vide
        if (discardPile.isEmpty()) {
            return;  // Rien à faire si la pile de défausse est vide
        }
        
        // Keep the top card of the discard pile
        Card topCard = discardPile.pop();
        
        // Move all cards from the discard pile to the deck
        deck.addAll(discardPile);
        discardPile.clear();
        
        // Shuffle the deck
        Collections.shuffle(deck, random);
        
        // Put the top card back on the discard pile
        discardPile.push(topCard);
        
        notifyDeckReshuffled();
    }
    
    /**
     * The current player calls UNO
     */
    public void callUno() {
        Player currentPlayer = getCurrentPlayer();
        
        if (currentPlayer.getCardCount() == 1) {
            currentPlayer.callUno();
            notifyPlayerCalledUno(currentPlayer);
        }
    }
    
    /**
     * The specified player calls UNO
     */
    public void callUno(Player player) {
        if (player != null && player.getCardCount() == 1) {
            player.callUno();
            notifyPlayerCalledUno(player);
        }
    }
    
    /**
     * Challenge a Wild Draw Four card
     */
    public boolean challengeWildDrawFour(Player challenger, Player challenged) {
        // Get the previous card before the Wild Draw Four
        if (discardPile.size() < 2) {
            return false; // Can't challenge if there's no previous card
        }
        
        Card wildDrawFour = discardPile.peek();
        if (wildDrawFour.getValue() != CardValue.WILD_DRAW_FOUR) {
            return false; // Can't challenge if the top card is not a Wild Draw Four
        }
        
        // Check if the challenged player had a card matching the color of the previous card
        // (This is simplified - in a real game, you'd need to track the previous color)
        boolean challengeSuccessful = false;
        
        // If the challenge is successful, the challenged player draws 4 cards
        // If the challenge fails, the challenger draws 6 cards
        if (challengeSuccessful) {
            drawCards(challenged, 4);
            notifyWildDrawFourChallengeSucceeded(challenger, challenged);
        } else {
            drawCards(challenger, 6);
            notifyWildDrawFourChallengeFailed(challenger, challenged);
        }
        
        return challengeSuccessful;
    }
    
    /**
     * Get the number of cards in the deck
     */
    public int getDeckSize() {
        return deck.size();
    }
    
    /**
     * Get the number of cards in the discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
    
    /**
     * Check if the game has started
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    /**
     * Check if the game has ended
     */
    public boolean isGameEnded() {
        return gameEnded;
    }
    
    /**
     * Start the game
     */
    public void start() {
        if (gameStarted) {
            throw new IllegalStateException("Game has already started");
        }
        init();
    }
    
    /**
     * Alias for start() - Start the game
     */
    public void startGame() {
        start();
    }
    
    /**
     * Restart the game
     */
    public void restart() {
        gameStarted = false;
        gameEnded = false;
        start();
    }
    
    // Event listener methods
    
    /**
     * Add a game event listener
     */
    public void addGameEventListener(GameEventListener listener) {
        eventListeners.add(listener);
    }
    
    /**
     * Remove a game event listener
     */
    public void removeGameEventListener(GameEventListener listener) {
        eventListeners.remove(listener);
    }
    
    /**
     * Notify listeners that the game has started
     */
    private void notifyGameStarted() {
        for (GameEventListener listener : eventListeners) {
            listener.onGameStarted(this);
        }
    }
    
    /**
     * Notify listeners that the game has ended
     */
    private void notifyGameEnded(Player winner) {
        for (GameEventListener listener : eventListeners) {
            listener.onGameEnded(this, winner);
        }
    }
    
    /**
     * Notify listeners that it's a player's turn
     */
    private void notifyPlayerTurn(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerTurn(this, player);
        }
    }
    
    /**
     * Notify listeners that a player was skipped
     */
    private void notifyPlayerSkipped(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerSkipped(this, player);
        }
    }
    
    /**
     * Notify listeners that the direction of play has changed
     */
    private void notifyDirectionChanged(boolean isClockwise) {
        for (GameEventListener listener : eventListeners) {
            listener.onDirectionChanged(this, isClockwise);
        }
    }
    
    /**
     * Notify listeners that a player drew cards
     */
    private void notifyPlayerDrewCards(Player player, int count) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerDrewCards(this, player, count);
        }
    }
    
    /**
     * Notify listeners that the deck was reshuffled
     */
    private void notifyDeckReshuffled() {
        for (GameEventListener listener : eventListeners) {
            listener.onDeckReshuffled(this);
        }
    }
    
    /**
     * Notify listeners that a player called UNO
     */
    private void notifyPlayerCalledUno(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerCalledUno(this, player);
        }
    }
    
    /**
     * Notify listeners that a player forgot to call UNO
     */
    private void notifyPlayerForgotUno(Player player) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerForgotUno(this, player);
        }
    }
    
    /**
     * Notify listeners that the card color has changed
     */
    private void notifyColorChanged(CardColor color) {
        for (GameEventListener listener : eventListeners) {
            listener.onColorChanged(this, color);
        }
    }
    
    /**
     * Notify listeners that a Wild Draw Four challenge succeeded
     */
    private void notifyWildDrawFourChallengeSucceeded(Player challenger, Player challenged) {
        for (GameEventListener listener : eventListeners) {
            listener.onWildDrawFourChallengeSucceeded(this, challenger, challenged);
        }
    }
    
    /**
     * Notify listeners that a Wild Draw Four challenge failed
     */
    private void notifyWildDrawFourChallengeFailed(Player challenger, Player challenged) {
        for (GameEventListener listener : eventListeners) {
            listener.onWildDrawFourChallengeFailed(this, challenger, challenged);
        }
    }
    
    /**
     * Interface for game event listeners
     */
    public interface GameEventListener {
        void onGameStarted(Game game);
        void onGameEnded(Game game, Player winner);
        void onPlayerTurn(Game game, Player player);
        void onPlayerSkipped(Game game, Player player);
        void onDirectionChanged(Game game, boolean isClockwise);
        void onPlayerDrewCards(Game game, Player player, int count);
        void onDeckReshuffled(Game game);
        void onPlayerCalledUno(Game game, Player player);
        void onPlayerForgotUno(Game game, Player player);
        void onColorChanged(Game game, CardColor color);
        void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged);
        void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged);
    }
}
