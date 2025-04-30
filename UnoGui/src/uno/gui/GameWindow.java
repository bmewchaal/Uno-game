package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;

public class GameWindow implements Game.GameEventListener {
    private DPanel gamePanel;
    private Game game;
    private DActionListener exitGameAction; // Pour retourner au menu
    
    private PlayerHandPanel playerHandPanel;
    private TablePanel tablePanel;
    private GameInfoPanel gameInfoPanel;
    
    private DPanel controlPanel;
    private DButton drawCardButton;
    private DButton playCardButton;
    private DButton callUnoButton;
    private DButton exitButton;
    
    private DPanel colorSelectionPanel;
    private DButton redButton;
    private DButton blueButton;
    private DButton greenButton;
    private DButton yellowButton;
    
    private List<Player> aiPlayers;
    private boolean waitingForColorSelection = false;
    
    public GameWindow(Game game) {
        this.game = game;
        game.addGameEventListener(this);
        
        aiPlayers = new ArrayList<>();
        
        initializeUI();
    }
    
    private void initializeUI() {
        // Créer le panneau principal qui contiendra tout le jeu
        gamePanel = new DPanel(new BorderLayout());
        
        // Créer les sous-panneaux
        DPanel mainPanel = new DPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(0, 100, 0)); // Fond vert foncé
        
        // Créer le panneau de main du joueur
        playerHandPanel = new PlayerHandPanel();
        
        // Créer le panneau de table
        tablePanel = new TablePanel();
        tablePanel.setGame(game);
        
        // Créer le panneau d'informations de jeu
        gameInfoPanel = new GameInfoPanel();
        gameInfoPanel.setGame(game);
        
        // Créer le panneau de contrôle
        createControlPanel();
        createColorSelectionPanel();
        
        // Ajouter les panneaux au panneau principal
        mainPanel.add(gameInfoPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(playerHandPanel, BorderLayout.SOUTH);
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Ajouter le panneau principal au panneau de jeu
        gamePanel.add(mainPanel, BorderLayout.CENTER);
        
        // Set up event handlers
        setupEventHandlers();
    }
    
    /**
     * Create the control panel with buttons
     */
    private void createControlPanel() {
        controlPanel = new DPanel(new GridLayout(3, 1, 10, 10));
        controlPanel.setPreferredSize(new Dimension(120, 200));
        
        // Create buttons
        drawCardButton = new DButton("Draw Card");
        playCardButton = new DButton("Play Card");
        callUnoButton = new DButton("Call UNO!");
        
        // Style buttons
        drawCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        playCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        callUnoButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add buttons to the control panel
        controlPanel.add(drawCardButton);
        controlPanel.add(playCardButton);
        controlPanel.add(callUnoButton);
    }
    
    /**
     * Create the color selection panel for wild cards
     */
    private void createColorSelectionPanel() {
        colorSelectionPanel = new DPanel(new GridLayout(2, 2, 10, 10));
        colorSelectionPanel.setPreferredSize(new Dimension(120, 120));
        
        // Create color buttons
        redButton = new DButton("Red");
        blueButton = new DButton("Blue");
        greenButton = new DButton("Green");
        yellowButton = new DButton("Yellow");
        
        // Style buttons
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        
        blueButton.setBackground(Color.BLUE);
        blueButton.setForeground(Color.WHITE);
        
        greenButton.setBackground(Color.GREEN);
        greenButton.setForeground(Color.BLACK);
        
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setForeground(Color.BLACK);
        
        // Add buttons to the color selection panel
        colorSelectionPanel.add(redButton);
        colorSelectionPanel.add(blueButton);
        colorSelectionPanel.add(greenButton);
        colorSelectionPanel.add(yellowButton);
        
        // Initially hide the color selection panel
        colorSelectionPanel.setVisible(false);
    }
    
    /**
     * Set up event handlers for the controls
     */
    private void setupEventHandlers() {
        // Draw card button
        drawCardButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent event) {
                if (game.isGameStarted() && !game.isGameEnded()) {
                    Player currentPlayer = game.getCurrentPlayer();
                    
                    if (!currentPlayer.isAI()) {
                        // Draw a card for the human player
                        Card drawnCard = game.drawCard(currentPlayer);
                        refreshPlayerHand();
                        
                        // Advance to the next player
                        game.advanceToNextPlayer();
                        
                        // Process AI turns
                        processAITurns();
                    }
                }
            }
        });
        
        // Play card button
        playCardButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent event) {
                if (game.isGameStarted() && !game.isGameEnded()) {
                    Player currentPlayer = game.getCurrentPlayer();
                    
                    if (!currentPlayer.isAI()) {
                        DCard selectedDCard = playerHandPanel.getSelectedCard();
                        
                        if (selectedDCard != null) {
                            // Find the corresponding game card
                            Card selectedCard = convertDCardToCard(selectedDCard);
                            
                            // Check if the card can be played
                            if (selectedCard.canPlayOn(game.getTopCard())) {
                                // Handle wild cards separately
                                if (selectedCard.getValue().isWildCard()) {
                                    waitingForColorSelection = true;
                                    showColorSelectionPanel();
                                    return;
                                }
                                
                                // Play the card
                                if (game.playCard(selectedCard)) {
                                    playerHandPanel.removeCard(selectedDCard);
                                    tablePanel.updateDiscardPile();
                                    
                                    // Process AI turns
                                    processAITurns();
                                }
                            }
                        }
                    }
                }
            }
        });
        
        // Call UNO button
        callUnoButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent event) {
                if (game.isGameStarted() && !game.isGameEnded()) {
                    Player currentPlayer = game.getCurrentPlayer();
                    
                    if (!currentPlayer.isAI() && currentPlayer.getCardCount() == 1) {
                        game.callUno();
                        gameInfoPanel.showMessage(currentPlayer.getName() + " called UNO!");
                    }
                }
            }
        });
        
        // Color selection buttons
        DActionListener colorListener = new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent event) {
                if (waitingForColorSelection) {
                    CardColor selectedColor = null;
                    
                    if (event.getSource() == redButton) {
                        selectedColor = CardColor.RED;
                    } else if (event.getSource() == blueButton) {
                        selectedColor = CardColor.BLUE;
                    } else if (event.getSource() == greenButton) {
                        selectedColor = CardColor.GREEN;
                    } else if (event.getSource() == yellowButton) {
                        selectedColor = CardColor.YELLOW;
                    }
                    
                    if (selectedColor != null) {
                        Player currentPlayer = game.getCurrentPlayer();
                        DCard selectedDCard = playerHandPanel.getSelectedCard();
                        
                        if (selectedDCard != null) {
                            // Find the corresponding game card
                            Card selectedCard = convertDCardToCard(selectedDCard);
                            
                            // Set the selected color
                            selectedCard.setColor(selectedColor);
                            
                            // Play the card
                            if (game.playCard(selectedCard)) {
                                playerHandPanel.removeCard(selectedDCard);
                                tablePanel.updateDiscardPile();
                                
                                waitingForColorSelection = false;
                                hideColorSelectionPanel();
                                
                                // Process AI turns
                                processAITurns();
                            }
                        }
                    }
                }
            }
        };
        
        redButton.addActionListener(colorListener);
        blueButton.addActionListener(colorListener);
        greenButton.addActionListener(colorListener);
        yellowButton.addActionListener(colorListener);
    }
    
    /**
     * Show the color selection panel
     */
    private void showColorSelectionPanel() {
        controlPanel.remove(drawCardButton);
        controlPanel.remove(playCardButton);
        controlPanel.remove(callUnoButton);
        
        controlPanel.add(colorSelectionPanel);
        colorSelectionPanel.setVisible(true);
        
        controlPanel.revalidate();
        controlPanel.repaint();
    }
    
    /**
     * Hide the color selection panel
     */
    private void hideColorSelectionPanel() {
        controlPanel.remove(colorSelectionPanel);
        colorSelectionPanel.setVisible(false);
        
        controlPanel.add(drawCardButton);
        controlPanel.add(playCardButton);
        controlPanel.add(callUnoButton);
        
        controlPanel.revalidate();
        controlPanel.repaint();
    }
    
    /**
     * Convert a DCard to a Card
     */
    private Card convertDCardToCard(DCard dCard) {
        // Find the corresponding game card based on color and value
        Player humanPlayer = getHumanPlayer();
        CardColor cardColor = null;
        CardValue cardValue = null;
        
        // Convert AWT Color to CardColor
        if (dCard.getCardColor().equals(Color.RED)) {
            cardColor = CardColor.RED;
        } else if (dCard.getCardColor().equals(Color.BLUE)) {
            cardColor = CardColor.BLUE;
        } else if (dCard.getCardColor().equals(Color.GREEN)) {
            cardColor = CardColor.GREEN;
        } else if (dCard.getCardColor().equals(Color.YELLOW)) {
            cardColor = CardColor.YELLOW;
        } else if (dCard.getCardColor().equals(Color.BLACK)) {
            cardColor = CardColor.WILD;
        }
        
        // Convert String value to CardValue
        String value = dCard.getCardValue();
        for (CardValue cv : CardValue.values()) {
            if (cv.getSymbol().equals(value)) {
                cardValue = cv;
                break;
            }
        }
        
        if (cardColor != null && cardValue != null) {
            for (Card card : humanPlayer.getHand()) {
                if (card.getColor() == cardColor && card.getValue() == cardValue) {
                    return card;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get the human player (assuming it's the first player)
     */
    private Player getHumanPlayer() {
        for (Player player : game.getPlayers()) {
            if (!player.isAI()) {
                return player;
            }
        }
        return null;
    }
    
    /**
     * Process turns for AI players
     */
    private void processAITurns() {
        // Continue processing turns until it's the human player's turn
        while (game.getCurrentPlayer().isAI() && !game.isGameEnded()) {
            Player aiPlayer = game.getCurrentPlayer();
            
            // Simple AI: Try to play a card, if can't then draw
            Card playableCard = aiPlayer.getPlayableCard(game.getTopCard());
            
            if (playableCard != null) {
                // AI has a playable card
                if (playableCard.getValue().isWildCard()) {
                    // Choose a random color for wild cards
                    CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
                    playableCard.setColor(colors[(int)(Math.random() * colors.length)]);
                }
                
                // Call UNO if this will leave AI with one card
                if (aiPlayer.getCardCount() == 2) {
                    aiPlayer.callUno();
                    gameInfoPanel.showMessage(aiPlayer.getName() + " called UNO!");
                }
                
                // Play the card
                game.playCard(playableCard);
                tablePanel.updateDiscardPile();
                
                gameInfoPanel.showMessage(aiPlayer.getName() + " played " + playableCard);
            } else {
                // AI has no playable card, draw one
                Card drawnCard = game.drawCard(aiPlayer);
                
                if (drawnCard != null && drawnCard.canPlayOn(game.getTopCard())) {
                    // Play the drawn card if it can be played
                    if (drawnCard.getValue().isWildCard()) {
                        // Choose a random color for wild cards
                        CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
                        drawnCard.setColor(colors[(int)(Math.random() * colors.length)]);
                    }
                    
                    gameInfoPanel.showMessage(aiPlayer.getName() + " drew and played " + drawnCard);
                    
                    // Play the card
                    game.playCard(drawnCard);
                    tablePanel.updateDiscardPile();
                } else {
                    gameInfoPanel.showMessage(aiPlayer.getName() + " drew a card");
                    game.advanceToNextPlayer();
                }
            }
            
            // Give a small delay to make AI moves visible
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Update the UI
        refreshUI();
    }
    
    /**
     * Add players to the game (one human, the rest AI)
     */
    public void addPlayers(String humanPlayerName, int numAIPlayers) {
        // Add human player
        Player humanPlayer = new Player(humanPlayerName, false);
        game.addPlayer(humanPlayer);
        
        // Add AI players
        for (int i = 0; i < numAIPlayers; i++) {
            Player aiPlayer = new Player("AI Player " + (i + 1), true);
            game.addPlayer(aiPlayer);
            aiPlayers.add(aiPlayer);
        }
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        game.start();
        
        // Update the UI
        refreshUI();
        
        // If the first player is an AI, process AI turns
        if (game.getCurrentPlayer().isAI()) {
            processAITurns();
        }
    }
    
    public DPanel getGamePanel() {
        return gamePanel;
    }
    
    public void setExitGameAction(DActionListener action) {
        this.exitGameAction = action;
    }
    
    /**
     * Refresh the player's hand display
     */
    private void refreshPlayerHand() {
        Player humanPlayer = getHumanPlayer();
        if (humanPlayer != null) {
            playerHandPanel.updateCards(humanPlayer.getHand());
        }
    }
    
    /**
     * Refresh the entire UI
     */
    private void refreshUI() {
        // Update player hand
        refreshPlayerHand();
        
        // Update discard pile
        tablePanel.updateDiscardPile();
        
        // Update game info
        gameInfoPanel.updateInfo();
    }
    
    // Game event listener methods
    
    @Override
    public void onGameStarted(Game game) {
        gameInfoPanel.showMessage("Game started!");
        refreshUI();
    }
    
    @Override
    public void onGameEnded(Game game, Player winner) {
        gameInfoPanel.showMessage("Game ended! " + winner.getName() + " wins!");
        
        // Disable controls
        drawCardButton.setEnabled(false);
        playCardButton.setEnabled(false);
        callUnoButton.setEnabled(false);
    }
    
    @Override
    public void onPlayerTurn(Game game, Player player) {
        gameInfoPanel.showMessage("It's " + player.getName() + "'s turn");
        gameInfoPanel.updateInfo();
    }
    
    @Override
    public void onPlayerSkipped(Game game, Player player) {
        gameInfoPanel.showMessage(player.getName() + " was skipped");
    }
    
    @Override
    public void onDirectionChanged(Game game, boolean isClockwise) {
        gameInfoPanel.showMessage("Direction changed to " + (isClockwise ? "clockwise" : "counter-clockwise"));
        gameInfoPanel.updateInfo();
    }
    
    @Override
    public void onPlayerDrewCards(Game game, Player player, int count) {
        gameInfoPanel.showMessage(player.getName() + " drew " + count + " card(s)");
    }
    
    @Override
    public void onDeckReshuffled(Game game) {
        gameInfoPanel.showMessage("Deck was reshuffled");
    }
    
    @Override
    public void onPlayerCalledUno(Game game, Player player) {
        gameInfoPanel.showMessage(player.getName() + " called UNO!");
    }
    
    @Override
    public void onPlayerForgotUno(Game game, Player player) {
        gameInfoPanel.showMessage(player.getName() + " forgot to call UNO!");
    }
    
    @Override
    public void onColorChanged(Game game, CardColor color) {
        gameInfoPanel.showMessage("Color changed to " + color);
    }
    
    @Override
    public void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged) {
        gameInfoPanel.showMessage(challenger.getName() + " successfully challenged " + challenged.getName() + "'s Wild Draw Four");
    }
    
    @Override
    public void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged) {
        gameInfoPanel.showMessage(challenger.getName() + " failed to challenge " + challenged.getName() + "'s Wild Draw Four");
    }
}
