package uno.gui;

import dgui.DAnimator;
import dgui.DButton;
import dgui.DComponent;
import dgui.DLabel;
import dgui.DPanel;
import dgui.themes.DTheme;
import uno.core.Card;
import uno.core.CardColor;
import uno.core.CardValue;
import uno.core.Game;
import uno.core.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

/**
 * Enhanced game panel with wood texture and improved UI
 * Fixes dimension issues and enhances visual appearance
 */
public class DWoodGamePanel extends DWoodPanel {
    private Game game;
    private List<DUnoCard> playerCards = new ArrayList<>();
    private Map<Player, List<DUnoCard>> aiPlayerCards = new HashMap<>();
    private Map<String, DPanel> playerPanels = new HashMap<>();
    
    // Game cards
    private DUnoCard topCard;
    private DUnoCard deckCard;
    
    // Layout panels
    private DPanel centerPanel;
    private DPanel playerPanel;
    private DPanel controlPanel;
    private DPanel topPlayerPanel;
    private DPanel leftPlayerPanel;
    private DPanel rightPlayerPanel;
    
    // Game controls
    private DButton drawButton;
    private DButton playButton;
    private DButton unoButton;
    private DButton colorRedButton;
    private DButton colorBlueButton;
    private DButton colorGreenButton;
    private DButton colorYellowButton;
    private DPanel colorSelectionPanel;
    
    // Game status indicators
    private DLabel turnIndicator;
    private DLabel gameStatus;
    
    // Game state
    private boolean waitingForColorSelection = false;
    private DUnoCard selectedCard = null;
    private CardColor selectedWildColor = null;
    
    // Animation constants
    private static final int ANIMATION_DURATION = 300;
    
    /**
     * Create a new enhanced wood game panel
     */
    public DWoodGamePanel(Game game) {
        super(new BorderLayout(10, 10));
        this.game = game;
        this.game.addGameEventListener(new GameEventHandler());
        
        initUI();
        setupEventHandlers();
    }
    
    /**
     * Initialize the UI
     */
    private void initUI() {
        // Configure main panel with wood texture
        createWoodTexture(DARK_WOOD);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // First, create the panels with explicit sizes to avoid 0x0 dimensions
        createCenterPanel();
        createPlayerPanel();
        createControlPanel();
        createColorSelectionPanel();
        
        // Add panels to main layout
        add(centerPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
        
        // Initialize visible cards
        updateTopCard();
        updateDeckCard();
    }
    
    /**
     * Create the center game panel
     */
    private void createCenterPanel() {
        centerPanel = new DPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setPreferredSize(new Dimension(600, 400)); // Explicit size
        
        // Card played at center
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardPanel.setOpaque(false);
        
        // Create initial card
        Card initialCard = game.getTopCard();
        if (initialCard == null) {
            initialCard = new Card(CardColor.RED, CardValue.ZERO);
        }
        topCard = new DUnoCard(initialCard);
        cardPanel.add(topCard.getComponent());
        
        // Draw pile next to discard pile
        JPanel deckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deckPanel.setOpaque(false);
        deckCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
        deckCard.setFaceUp(false);
        deckPanel.add(deckCard.getComponent());
        
        // Turn indicator
        turnIndicator = new DLabel("It's your turn!");
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        turnIndicator.setForeground(Color.WHITE);
        JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        turnPanel.setOpaque(false);
        turnPanel.add(turnIndicator.getComponent());
        
        // Game status
        gameStatus = new DLabel("");
        gameStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        gameStatus.setForeground(Color.WHITE);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusPanel.add(gameStatus.getComponent());
        
        // UNO button
        unoButton = new DButton("Call UNO!");
        unoButton.setBackground(new Color(255, 60, 0)); // Bright orange-red
        unoButton.setForeground(Color.WHITE);
        JPanel unoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        unoPanel.setOpaque(false);
        unoPanel.add(unoButton.getComponent());
        
        // Organize layout
        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setOpaque(false);
        topInfoPanel.setLayout(new BoxLayout(topInfoPanel, BoxLayout.Y_AXIS));
        topInfoPanel.add(turnPanel);
        topInfoPanel.add(statusPanel);
        
        JPanel centerCardPanel = new JPanel(new BorderLayout());
        centerCardPanel.setOpaque(false);
        centerCardPanel.add(deckPanel, BorderLayout.EAST);
        centerCardPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Add panels to center panel
        centerPanel.add(topInfoPanel, BorderLayout.NORTH);
        centerPanel.add(centerCardPanel, BorderLayout.CENTER);
        centerPanel.add(unoPanel, BorderLayout.SOUTH);
        
        // Add AI player panels
        createAIPlayerPanels();
    }
    
    /**
     * Create AI player panels
     */
    private void createAIPlayerPanels() {
        // Top player (across)
        topPlayerPanel = createAIPlayerPanel("Forrest Gump", BorderLayout.NORTH);
        centerPanel.add(topPlayerPanel, BorderLayout.NORTH);
        
        // Left player
        leftPlayerPanel = createAIPlayerPanel("Daenerys Targaryen", BorderLayout.WEST);
        centerPanel.add(leftPlayerPanel, BorderLayout.WEST);
        
        // Right player
        rightPlayerPanel = createAIPlayerPanel("Hannibal Lecter", BorderLayout.EAST);
        centerPanel.add(rightPlayerPanel, BorderLayout.EAST);
        
        playerPanels.put("Forrest Gump", topPlayerPanel);
        playerPanels.put("Daenerys Targaryen", leftPlayerPanel);
        playerPanels.put("Hannibal Lecter", rightPlayerPanel);
    }
    
    /**
     * Create an AI player panel
     */
    private DPanel createAIPlayerPanel(String name, String position) {
        DPanel panel = new DPanel();
        panel.setOpaque(false);
        
        boolean isHorizontal = position.equals(BorderLayout.NORTH) || position.equals(BorderLayout.SOUTH);
        
        if (isHorizontal) {
            // Set explicit size for horizontal panels
            panel.setPreferredSize(new Dimension(600, 120));
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.Y_AXIS));
            
            // Player name
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            panel.add(playerName);
            
            // Card area
            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -25, 0));
            cardsPanel.setOpaque(false);
            panel.add(cardsPanel);
            
        } else {
            // Set explicit size for vertical panels
            panel.setPreferredSize(new Dimension(120, 400));
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.X_AXIS));
            
            // Vertical organization
            DPanel verticalPanel = new DPanel();
            verticalPanel.setOpaque(false);
            verticalPanel.setLayout(new BoxLayout(verticalPanel.getPanel(), BoxLayout.Y_AXIS));
            
            // Player name
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            verticalPanel.add(playerName);
            
            // Card area
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setOpaque(false);
            verticalPanel.add(cardsPanel);
            
            panel.add(verticalPanel);
        }
        
        return panel;
    }
    
    /**
     * Create human player panel
     */
    private void createPlayerPanel() {
        playerPanel = new DPanel();
        playerPanel.setOpaque(false);
        playerPanel.setPreferredSize(new Dimension(800, 150)); // Explicit size
        playerPanel.setLayout(new BoxLayout(playerPanel.getPanel(), BoxLayout.Y_AXIS));
        
        // Player name
        DLabel playerName = new DLabel("TheLegend27");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerPanel.add(playerName);
        
        // Player cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -15, 5));
        cardsPanel.setOpaque(false);
        playerPanel.add(cardsPanel);
    }
    
    /**
     * Create control panel
     */
    private void createControlPanel() {
        controlPanel = new DPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel.getPanel(), BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(150, 400)); // Explicit size
        
        // Control buttons
        drawButton = new DButton("Draw Card");
        drawButton.setBackground(new Color(60, 170, 60)); // Brighter green
        drawButton.setForeground(Color.WHITE);
        
        playButton = new DButton("Play Card");
        playButton.setBackground(new Color(60, 100, 220)); // Brighter blue
        playButton.setForeground(Color.WHITE);
        
        // Add buttons to panel
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(drawButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(playButton);
        controlPanel.add(Box.createVerticalStrut(20));
    }
    
    /**
     * Create color selection panel for wild cards
     */
    private void createColorSelectionPanel() {
        colorSelectionPanel = new DPanel(new GridLayout(2, 2, 10, 10));
        colorSelectionPanel.setOpaque(false);
        colorSelectionPanel.setPreferredSize(new Dimension(150, 150)); // Explicit size
        
        // Color buttons with enhanced appearance
        colorRedButton = new DButton("Red");
        colorRedButton.setBackground(new Color(220, 40, 30));
        colorRedButton.setForeground(Color.WHITE);
        
        colorBlueButton = new DButton("Blue");
        colorBlueButton.setBackground(new Color(30, 80, 200));
        colorBlueButton.setForeground(Color.WHITE);
        
        colorGreenButton = new DButton("Green");
        colorGreenButton.setBackground(new Color(30, 180, 50));
        colorGreenButton.setForeground(Color.WHITE);
        
        colorYellowButton = new DButton("Yellow");
        colorYellowButton.setBackground(new Color(240, 200, 40));
        colorYellowButton.setForeground(Color.BLACK);
        
        // Add buttons to panel
        colorSelectionPanel.add(colorRedButton);
        colorSelectionPanel.add(colorBlueButton);
        colorSelectionPanel.add(colorGreenButton);
        colorSelectionPanel.add(colorYellowButton);
        
        // Initially hide the panel
        colorSelectionPanel.setVisible(false);
        controlPanel.add(colorSelectionPanel);
    }
    
    /**
     * Set up event handlers
     */
    private void setupEventHandlers() {
        // Draw button handler
        drawButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded()) {
                Card drawnCard = game.drawCard(game.getCurrentPlayer());
                updatePlayerHand();
                
                // Get the latest card added to player's hand
                DUnoCard latestCard = playerCards.get(playerCards.size() - 1);
                Point deckPos = getComponentScreenPosition(deckCard);
                Point handPos = getComponentScreenPosition(latestCard);
                
                // Animate card draw
                animateCardDraw(latestCard, deckPos, handPos);
                
                // Play sound
                playSound("card_draw");
                
                // Advance to next player
                game.advanceToNextPlayer();
                updateTurnIndicator();
            }
        });
        
        // Play button handler
        playButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded() && selectedCard != null) {
                // Check if card can be played
                Card cardToPlay = selectedCard.getCard();
                
                if (cardToPlay.canPlayOn(game.getTopCard())) {
                    if (cardToPlay.isWild()) {
                        waitingForColorSelection = true;
                        colorSelectionPanel.setVisible(true);
                        return;
                    }
                    
                    playSelectedCard();
                } else {
                    showMessage("This card cannot be played!");
                }
            }
        });
        
        // UNO button handler
        unoButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded()) {
                if (game.getCurrentPlayer().getHandSize() == 1) {
                    game.callUno(game.getCurrentPlayer());
                    showMessage("You called UNO!");
                    
                    // Play sound
                    playSound("uno_call");
                } else {
                    showMessage("You can only call UNO when you have one card left!");
                }
            }
        });
        
        // Color selection button handlers
        colorRedButton.addActionListener(e -> selectWildColor(CardColor.RED));
        colorBlueButton.addActionListener(e -> selectWildColor(CardColor.BLUE));
        colorGreenButton.addActionListener(e -> selectWildColor(CardColor.GREEN));
        colorYellowButton.addActionListener(e -> selectWildColor(CardColor.YELLOW));
    }
    
    /**
     * Select a color for a wild card
     */
    private void selectWildColor(CardColor color) {
        if (waitingForColorSelection && selectedCard != null) {
            selectedWildColor = color;
            colorSelectionPanel.setVisible(false);
            waitingForColorSelection = false;
            
            // Update card color
            Card cardToPlay = selectedCard.getCard();
            cardToPlay.setChosenColor(color);
            
            playSelectedCard();
        }
    }
    
    /**
     * Play the selected card
     */
    private void playSelectedCard() {
        if (selectedCard != null) {
            Card cardToPlay = selectedCard.getCard();
            
            // Animate card from hand to center
            Point handPos = getComponentScreenPosition(selectedCard);
            Point tablePos = getComponentScreenPosition(topCard);
            
            animateCardPlay(selectedCard, handPos, tablePos, () -> {
                // Play the card and update UI
                if (game.playCard(cardToPlay)) {
                    removeCardFromPlayerHand(selectedCard);
                    updateTopCard();
                    
                    // Play sound
                    playSound("card_place");
                    
                    // Check for game end
                    if (game.getCurrentPlayer().getHandSize() == 0) {
                        showMessage("You win!");
                        playSound("game_win");
                        return;
                    }
                    
                    // Advance to next player
                    game.advanceToNextPlayer();
                    updateTurnIndicator();
                }
            });
        }
    }
    
    /**
     * Play a sound effect
     */
    private void playSound(String soundName) {
        try {
            // Try to play sound through SoundManager
            SoundManager soundManager = new SoundManager();
            soundManager.playSound(soundName);
        } catch (Exception e) {
            // Silently ignore sound errors
        }
    }
    
    /**
     * Animate card draw from deck to hand
     */
    private void animateCardDraw(DUnoCard card, Point start, Point end) {
        // Initially hide the card
        card.setVisible(false);
        
        // Create a temporary card for animation
        DUnoCard animCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
        animCard.setFaceUp(false);
        ((JPanel)centerPanel.getComponent()).add(animCard.getComponent());
        
        // Position at deck location
        animCard.getComponent().setBounds(start.x, start.y, 
                                         card.getComponent().getWidth(), 
                                         card.getComponent().getHeight());
        
        // Animation timer
        Timer timer = new Timer(16, null);
        final int steps = ANIMATION_DURATION / 16;
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float)step[0] / steps;
            
            // Apply easing function for smoother animation
            float easedProgress = easeOutQuad(progress);
            
            // Calculate new position
            int x = start.x + (int)((end.x - start.x) * easedProgress);
            int y = start.y + (int)((end.y - start.y) * easedProgress);
            
            // If halfway through, flip the card
            if (step[0] == steps / 2) {
                animCard.setFaceUp(true);
            }
            
            // Update position
            animCard.getComponent().setLocation(x, y);
            
            // Animation complete
            if (step[0] >= steps) {
                ((Timer)e.getSource()).stop();
                
                // Remove animation card
                ((JPanel)centerPanel.getComponent()).remove(animCard.getComponent());
                
                // Show the actual card in hand
                card.setVisible(true);
                
                // Refresh display
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });
        
        timer.start();
    }
    
    /**
     * Animate playing a card from hand to table
     */
    private void animateCardPlay(DUnoCard card, Point start, Point end, Runnable onComplete) {
        // Create a temporary card for animation
        DUnoCard animCard = new DUnoCard(card.getCard());
        ((JPanel)centerPanel.getComponent()).add(animCard.getComponent());
        
        // Position at hand location
        animCard.getComponent().setBounds(start.x, start.y, 
                                         card.getComponent().getWidth(), 
                                         card.getComponent().getHeight());
        
        // Hide original card during animation
        card.setVisible(false);
        
        // Animation timer
        Timer timer = new Timer(16, null);
        final int steps = ANIMATION_DURATION / 16;
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float)step[0] / steps;
            
            // Apply easing function for smoother animation
            float easedProgress = easeOutQuad(progress);
            
            // Calculate new position with slight arc
            int x = start.x + (int)((end.x - start.x) * easedProgress);
            
            // Add an arc to the y movement (rise then fall)
            float arcHeight = 50; // Max height of arc
            float arcProgress = (float)(Math.sin(Math.PI * easedProgress));
            int y = start.y + (int)((end.y - start.y) * easedProgress - arcHeight * arcProgress);
            
            // Update position
            animCard.getComponent().setLocation(x, y);
            
            // Animation complete
            if (step[0] >= steps) {
                ((Timer)e.getSource()).stop();
                
                // Remove animation card
                ((JPanel)centerPanel.getComponent()).remove(animCard.getComponent());
                
                // Complete the action
                if (onComplete != null) {
                    onComplete.run();
                }
                
                // Refresh display
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });
        
        timer.start();
    }
    
    /**
     * Ease-out quadratic function for smooth animation
     */
    private float easeOutQuad(float x) {
        return 1 - (1 - x) * (1 - x);
    }
    
    /**
     * Get screen position of a component
     */
    private Point getComponentScreenPosition(DComponent component) {
        Point p = component.getComponent().getLocationOnScreen();
        return new Point(p.x, p.y);
    }
    
    /**
     * Update the top card display
     */
    private void updateTopCard() {
        Card newTopCard = game.getTopCard();
        
        // Handle null top card (can happen during initialization)
        if (newTopCard == null) {
            newTopCard = new Card(CardColor.RED, CardValue.ZERO);
            
            if (topCard == null) {
                topCard = new DUnoCard(newTopCard);
                return;
            }
        }
        
        if (topCard != null) {
            topCard.getCard().setColor(newTopCard.getColor());
            topCard.getCard().setValue(newTopCard.getValue());
            topCard.getCard().setChosenColor(newTopCard.getChosenColor());
            topCard.setFaceUp(true);
            topCard.getComponent().repaint();
        }
    }
    
    /**
     * Update the deck card display
     */
    private void updateDeckCard() {
        deckCard.setFaceUp(false);
        deckCard.getComponent().repaint();
    }
    
    /**
     * Update the player's hand display
     */
    private void updatePlayerHand() {
        // Clear current hand
        playerCards.clear();
        JPanel cardsPanel = (JPanel) playerPanel.getComponent().getComponent(1);
        cardsPanel.removeAll();
        
        // Get current player's cards
        Player humanPlayer = getHumanPlayer();
        if (humanPlayer != null) {
            for (Card card : humanPlayer.getHand()) {
                DUnoCard unoCard = new DUnoCard(card);
                
                // Add click listener for card selection
                unoCard.setClickListener(e -> {
                    if (selectedCard != null) {
                        selectedCard.setSelected(false);
                    }
                    unoCard.setSelected(true);
                    selectedCard = unoCard;
                });
                
                playerCards.add(unoCard);
                cardsPanel.add(unoCard.getComponent());
            }
        }
        
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    
    /**
     * Remove a card from player's hand
     */
    private void removeCardFromPlayerHand(DUnoCard card) {
        JPanel cardsPanel = (JPanel) playerPanel.getComponent().getComponent(1);
        cardsPanel.remove(card.getComponent());
        playerCards.remove(card);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        
        // Reset selection
        selectedCard = null;
    }
    
    /**
     * Update the AI players' hands display
     */
    private void updateAIPlayersHands() {
        // For each AI player
        for (Player player : game.getPlayers()) {
            if (player.isAI()) {
                String playerName = player.getName();
                DPanel playerPanel = playerPanels.get(playerName);
                
                if (playerPanel != null) {
                    // Get proper container for cards based on panel orientation
                    JPanel cardsContainer = null;
                    
                    if (playerPanel == topPlayerPanel) {
                        // Top player panel has cards in second component
                        cardsContainer = (JPanel) playerPanel.getComponent().getComponent(1);
                    } else {
                        // Side panels have more complex structure
                        JPanel verticalPanel = (JPanel) playerPanel.getComponent().getComponent(0);
                        cardsContainer = (JPanel) verticalPanel.getComponent(1);
                    }
                    
                    // Clear current cards
                    cardsContainer.removeAll();
                    
                    // Add face-down cards for each card in player's hand
                    for (int i = 0; i < player.getHandSize(); i++) {
                        DUnoCard card = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
                        card.setFaceUp(false);
                        
                        if (playerPanel == topPlayerPanel || playerPanel == leftPlayerPanel || playerPanel == rightPlayerPanel) {
                            // For top player, add cards horizontally with overlap
                            if (playerPanel == topPlayerPanel) {
                                cardsContainer.add(card.getComponent());
                            } else {
                                // For side players, add cards vertically with overlap
                                JPanel cardHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -15));
                                cardHolder.setOpaque(false);
                                cardHolder.add(card.getComponent());
                                cardsContainer.add(cardHolder);
                            }
                        }
                    }
                    
                    cardsContainer.revalidate();
                    cardsContainer.repaint();
                }
            }
        }
    }
    
    /**
     * Update the turn indicator
     */
    private void updateTurnIndicator() {
        if (isPlayerTurn()) {
            turnIndicator.setText("It's your turn!");
            turnIndicator.setForeground(Color.WHITE);
            
            // Enable player controls
            drawButton.setEnabled(true);
            playButton.setEnabled(true);
            unoButton.setEnabled(true);
        } else {
            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer != null) {
                turnIndicator.setText(currentPlayer.getName() + "'s turn");
                turnIndicator.setForeground(Color.YELLOW);
                
                // Disable player controls during AI turns
                drawButton.setEnabled(false);
                playButton.setEnabled(false);
                unoButton.setEnabled(false);
                
                // Process AI turn with a delay - using a single timer to prevent overlapping
                // Each AI player will wait for their turn
                Timer timer = new Timer(1000, e -> {
                    playAITurn(currentPlayer);
                    ((Timer)e.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    // Flag to prevent multiple AI turns processing at once
    private boolean processingAITurn = false;
    
    /**
     * Play a turn for an AI player
     */
    private void playAITurn(Player aiPlayer) {
        if (game.isGameEnded() || processingAITurn) return;
        
        // Set the flag to prevent other AI turns from starting
        processingAITurn = true;
        
        try {
            // Find a playable card
            Card playableCard = null;
            for (Card card : aiPlayer.getHand()) {
                if (card.canPlayOn(game.getTopCard())) {
                    playableCard = card;
                    // Preference for non-wild cards if available
                    if (!card.isWild()) {
                        break;
                    }
                }
            }
            
            // If AI has a playable card
            if (playableCard != null) {
                // For wild cards, select a random color
                if (playableCard.isWild()) {
                    CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
                    CardColor selectedColor = colors[(int)(Math.random() * 4)];
                    playableCard.setChosenColor(selectedColor);
                    // Also set the actual color for correct display
                    playableCard.setColor(selectedColor);
                }
                
                // UNO call if this is the next-to-last card
                if (aiPlayer.getHandSize() == 2) {
                    aiPlayer.callUno();
                    showMessage(aiPlayer.getName() + " called UNO!");
                    playSound("uno_call");
                }
                
                // Play the card
                final Card cardToPlay = playableCard;
                
                // Show animation
                animateAICardPlay(aiPlayer, () -> {
                    game.playCard(cardToPlay);
                    updateTopCard();
                    updateAIPlayersHands();
                    playSound("card_place");
                    
                    // Win condition
                    if (aiPlayer.getHandSize() == 0) {
                        showMessage(aiPlayer.getName() + " wins!");
                        playSound("game_win");
                        processingAITurn = false;
                        return;
                    }
                    
                    // Next player
                    game.advanceToNextPlayer();
                    
                    // Now that animation is complete, release the flag and update turn
                    processingAITurn = false;
                    updateTurnIndicator();
                });
            } else {
                // AI has no playable card, draw one
                showMessage(aiPlayer.getName() + " draws a card");
                
                // Animate drawing
                animateAICardDraw(aiPlayer, () -> {
                    Card drawnCard = game.drawCard(aiPlayer);
                    updateAIPlayersHands();
                    playSound("card_draw");
                    
                    // Check if drawn card can be played
                    if (drawnCard != null && drawnCard.canPlayOn(game.getTopCard())) {
                        // For wild cards, select a random color
                        if (drawnCard.isWild()) {
                            CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
                            CardColor selectedColor = colors[(int)(Math.random() * 4)];
                            drawnCard.setChosenColor(selectedColor);
                            // Also set the actual color for correct display
                            drawnCard.setColor(selectedColor);
                        }
                        
                        // Delay a bit then play the drawn card
                        Timer timer = new Timer(500, e -> {
                            showMessage(aiPlayer.getName() + " plays drawn card");
                            game.playCard(drawnCard);
                            updateTopCard();
                            updateAIPlayersHands();
                            playSound("card_place");
                            
                            // Win condition
                            if (aiPlayer.getHandSize() == 0) {
                                showMessage(aiPlayer.getName() + " wins!");
                                playSound("game_win");
                                processingAITurn = false;
                                return;
                            }
                            
                            // Next player
                            game.advanceToNextPlayer();
                            
                            // Now that animation is complete, release the flag and update turn
                            processingAITurn = false;
                            updateTurnIndicator();
                            ((Timer)e.getSource()).stop();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        // Next player
                        game.advanceToNextPlayer();
                        
                        // Now that animation is complete, release the flag and update turn
                        processingAITurn = false;
                        updateTurnIndicator();
                    }
                });
            }
        } catch (Exception e) {
            // In case of an error, make sure we release the flag
            processingAITurn = false;
            System.err.println("Error processing AI turn: " + e.getMessage());
            e.printStackTrace();
            
            // Move to next player if there was an error
            game.advanceToNextPlayer();
            updateTurnIndicator();
        }
    }
    
    /**
     * Animate AI player drawing a card
     */
    private void animateAICardDraw(Player aiPlayer, Runnable onComplete) {
        // Get positions
        Point deckPos = getComponentScreenPosition(deckCard);
        
        // Create a temporary card for animation - using fixed size to avoid too large cards
        DUnoCard animCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
        animCard.setFaceUp(false);
        ((JPanel)centerPanel.getComponent()).add(animCard.getComponent());
        
        // Position at deck location with consistent size
        int cardWidth = 80;  // Fixed card width
        int cardHeight = 120; // Fixed card height
        animCard.getComponent().setBounds(deckPos.x, deckPos.y, cardWidth, cardHeight);
        
        // Find target position based on AI player
        Point endPos = new Point();
        
        // Determine which player panel to use
        String playerName = aiPlayer.getName();
        DPanel playerPanel = playerPanels.get(playerName);
        
        if (playerPanel == topPlayerPanel) {
            // Top player
            endPos.x = topPlayerPanel.getX() + topPlayerPanel.getWidth() / 2;
            endPos.y = topPlayerPanel.getY() + 50;
        } else if (playerPanel == leftPlayerPanel) {
            // Left player
            endPos.x = leftPlayerPanel.getX() + 50;
            endPos.y = leftPlayerPanel.getY() + leftPlayerPanel.getHeight() / 2;
        } else if (playerPanel == rightPlayerPanel) {
            // Right player
            endPos.x = rightPlayerPanel.getX() + 50;
            endPos.y = rightPlayerPanel.getY() + rightPlayerPanel.getHeight() / 2;
        }
        
        // Animation timer
        Timer timer = new Timer(16, null);
        final int steps = ANIMATION_DURATION / 16;
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float)step[0] / steps;
            
            // Apply easing
            float easedProgress = easeOutQuad(progress);
            
            // Update position
            int x = deckPos.x + (int)((endPos.x - deckPos.x) * easedProgress);
            int y = deckPos.y + (int)((endPos.y - deckPos.y) * easedProgress);
            animCard.getComponent().setLocation(x, y);
            
            // Animation complete
            if (step[0] >= steps) {
                ((Timer)e.getSource()).stop();
                
                // Remove animation card
                ((JPanel)centerPanel.getComponent()).remove(animCard.getComponent());
                centerPanel.revalidate();
                centerPanel.repaint();
                
                // Run completion action
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animate AI player playing a card
     */
    private void animateAICardPlay(Player aiPlayer, Runnable onComplete) {
        // Find start position based on AI player
        Point startPos = new Point();
        Point endPos = getComponentScreenPosition(topCard);
        
        // Determine which player panel to use
        String playerName = aiPlayer.getName();
        DPanel playerPanel = playerPanels.get(playerName);
        
        if (playerPanel == topPlayerPanel) {
            // Top player
            startPos.x = topPlayerPanel.getX() + topPlayerPanel.getWidth() / 2;
            startPos.y = topPlayerPanel.getY() + 50;
        } else if (playerPanel == leftPlayerPanel) {
            // Left player
            startPos.x = leftPlayerPanel.getX() + 50;
            startPos.y = leftPlayerPanel.getY() + leftPlayerPanel.getHeight() / 2;
        } else if (playerPanel == rightPlayerPanel) {
            // Right player
            startPos.x = rightPlayerPanel.getX() + 50;
            startPos.y = rightPlayerPanel.getY() + rightPlayerPanel.getHeight() / 2;
        }
        
        // Create a temporary card for animation - using fixed size for consistency
        DUnoCard animCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
        animCard.setFaceUp(false);
        ((JPanel)centerPanel.getComponent()).add(animCard.getComponent());
        
        // Position at start location with consistent size
        int cardWidth = 80;  // Fixed card width
        int cardHeight = 120; // Fixed card height
        animCard.getComponent().setBounds(startPos.x, startPos.y, cardWidth, cardHeight);
        
        // Animation timer
        Timer timer = new Timer(16, null);
        final int steps = ANIMATION_DURATION / 16;
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float)step[0] / steps;
            
            // Apply easing
            float easedProgress = easeOutQuad(progress);
            
            // Flip card halfway
            if (step[0] == steps / 2) {
                animCard.setFaceUp(true);
            }
            
            // Calculate position with arc
            int x = startPos.x + (int)((endPos.x - startPos.x) * easedProgress);
            
            // Add arc to y-movement
            float arcHeight = 50;
            float arcProgress = (float)(Math.sin(Math.PI * easedProgress));
            int y = startPos.y + (int)((endPos.y - startPos.y) * easedProgress - arcHeight * arcProgress);
            
            // Update position
            animCard.getComponent().setLocation(x, y);
            
            // Animation complete
            if (step[0] >= steps) {
                ((Timer)e.getSource()).stop();
                
                // Remove animation card
                ((JPanel)centerPanel.getComponent()).remove(animCard.getComponent());
                centerPanel.revalidate();
                centerPanel.repaint();
                
                // Run completion action
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Check if it's the human player's turn
     */
    private boolean isPlayerTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer != null && !currentPlayer.isAI();
    }
    
    /**
     * Get the human player
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
     * Show a message in the status area
     */
    private void showMessage(String message) {
        gameStatus.setText(message);
        
        // Create a timer to clear the message after a delay
        Timer timer = new Timer(5000, e -> {
            gameStatus.setText("");
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Initialize the game
     */
    public void initializeGame(String playerName, int aiPlayerCount) {
        updatePlayerHand();
        updateAIPlayersHands();
        updateTurnIndicator();
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        showMessage("Game started!");
        updatePlayerHand();
        updateAIPlayersHands();
        updateTopCard();
        updateDeckCard();
        updateTurnIndicator();
    }
    
    /**
     * Game event handler class
     */
    private class GameEventHandler implements Game.GameEventListener {
        
        @Override
        public void onGameStarted(Game game) {
            updateStatusMessage("Game started!");
            updateUI();
        }
        
        @Override
        public void onGameEnded(Game game, Player winner) {
            updateStatusMessage("Game over! " + winner.getName() + " wins!");
            updateUI();
        }
        
        @Override
        public void onPlayerTurn(Game game, Player player) {
            updateTurnIndicator();
            updateStatusMessage("It's " + player.getName() + "'s turn");
            updateUI();
        }
        
        @Override
        public void onPlayerSkipped(Game game, Player player) {
            updateStatusMessage(player.getName() + " skips a turn!");
            updateUI();
        }
        
        @Override
        public void onDirectionChanged(Game game, boolean isClockwise) {
            updateStatusMessage("Direction changed!");
            updateUI();
        }
        
        @Override
        public void onPlayerDrewCards(Game game, Player player, int count) {
            updateStatusMessage(player.getName() + " draws " + count + " cards");
            updateUI();
        }
        
        @Override
        public void onDeckReshuffled(Game game) {
            updateStatusMessage("The deck has been reshuffled");
            updateUI();
        }
        
        @Override
        public void onPlayerCalledUno(Game game, Player player) {
            updateStatusMessage(player.getName() + " called UNO!");
            updateUI();
        }
        
        @Override
        public void onPlayerForgotUno(Game game, Player player) {
            updateStatusMessage(player.getName() + " forgot to call UNO and draws 2 cards!");
            updateUI();
        }
        
        @Override
        public void onColorChanged(Game game, CardColor color) {
            updateStatusMessage("Color changed to: " + getColorName(color));
            updateTopCard();
            updateUI();
        }
        
        @Override
        public void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged) {
            updateStatusMessage(challenger.getName() + " successfully challenged " + challenged.getName() + "'s +4!");
            updateUI();
        }
        
        @Override
        public void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged) {
            updateStatusMessage(challenger.getName() + " failed to challenge " + challenged.getName() + "'s +4!");
            updateUI();
        }
    }
    
    /**
     * Update status message
     */
    private void updateStatusMessage(String message) {
        if (gameStatus != null) {
            gameStatus.setText(message);
            showMessage(message);
        }
    }
    
    /**
     * Update all UI elements
     */
    private void updateUI() {
        updatePlayerHand();
        updateAIPlayersHands();
        updateTopCard();
        updateDeckCard();
        updateTurnIndicator();
    }
    
    /**
     * Get readable color name
     */
    private String getColorName(CardColor color) {
        switch (color) {
            case RED: return "Red";
            case BLUE: return "Blue";
            case GREEN: return "Green";
            case YELLOW: return "Yellow";
            case WILD: return "Wild";
            default: return "Unknown";
        }
    }
}