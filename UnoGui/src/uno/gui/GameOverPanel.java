package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.util.List;

/**
 * Panel displayed at the end of a game showing results and allowing to play again.
 */
public class GameOverPanel extends DPanel {
    private DButton playAgainButton;
    private DButton quitButton;
    
    /**
     * Event listener for game over actions
     */
    public interface GameOverListener {
        void onPlayAgain();
        void onQuit();
    }
    
    private GameOverListener listener;
    
    /**
     * Create a game over panel with the winner and all players
     * 
     * @param winner the winning player
     * @param allPlayers all players in the game
     */
    public GameOverPanel(Player winner, List<Player> allPlayers) {
        super(new BorderLayout(10, 20));
        setBackground(new Color(0, 50, 0)); // Dark green background
        
        // Create title panel
        DPanel titlePanel = new DPanel(new BorderLayout());
        titlePanel.setBackground(new Color(0, 50, 0));
        
        // Create winner label
        DLabel winnerLabel = new DLabel(winner.getName() + " has won the game!");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setForeground(Color.YELLOW);
        winnerLabel.setTextAlignmentCenter();
        titlePanel.add(winnerLabel, BorderLayout.CENTER);
        
        // Create scores panel
        DPanel scoresPanel = new DPanel(new GridLayout(0, 3, 10, 5));
        scoresPanel.setBackground(new Color(0, 50, 0));
        
        // Add column headers
        DLabel playerHeader = new DLabel("Player");
        playerHeader.setFont(new Font("Arial", Font.BOLD, 16));
        playerHeader.setForeground(Color.WHITE);
        
        DLabel cardsHeader = new DLabel("Cards Left");
        cardsHeader.setFont(new Font("Arial", Font.BOLD, 16));
        cardsHeader.setForeground(Color.WHITE);
        
        DLabel scoreHeader = new DLabel("Score");
        scoreHeader.setFont(new Font("Arial", Font.BOLD, 16));
        scoreHeader.setForeground(Color.WHITE);
        
        scoresPanel.add(playerHeader);
        scoresPanel.add(cardsHeader);
        scoresPanel.add(scoreHeader);
        
        // Add player scores
        for (Player player : allPlayers) {
            // Player name
            DLabel nameLabel = new DLabel(player.getName());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            nameLabel.setForeground(Color.WHITE);
            
            // Cards left
            DLabel cardsLabel = new DLabel(String.valueOf(player.getCardCount()));
            cardsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            cardsLabel.setForeground(Color.WHITE);
            
            // Score
            int score = calculateScore(player);
            DLabel scoreLabel = new DLabel(String.valueOf(score));
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            scoreLabel.setForeground(Color.WHITE);
            
            // Highlight the winner
            if (player == winner) {
                nameLabel.setForeground(Color.YELLOW);
                cardsLabel.setForeground(Color.YELLOW);
                scoreLabel.setForeground(Color.YELLOW);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                cardsLabel.setFont(new Font("Arial", Font.BOLD, 14));
                scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
            
            scoresPanel.add(nameLabel);
            scoresPanel.add(cardsLabel);
            scoresPanel.add(scoreLabel);
        }
        
        // Create buttons panel
        DPanel buttonsPanel = new DPanel(new GridLayout(1, 2, 20, 0));
        buttonsPanel.setBackground(new Color(0, 50, 0));
        
        // Create buttons
        playAgainButton = new DButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 16));
        playAgainButton.setBackground(new Color(50, 150, 50));
        playAgainButton.setForeground(Color.WHITE);
        
        quitButton = new DButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.setBackground(new Color(150, 50, 50));
        quitButton.setForeground(Color.WHITE);
        
        // Add action listeners
        playAgainButton.addActionListener(e -> {
            if (listener != null) {
                listener.onPlayAgain();
            }
        });
        
        quitButton.addActionListener(e -> {
            if (listener != null) {
                listener.onQuit();
            }
        });
        
        // Add buttons to panel with some padding containers for better spacing
        DPanel buttonsPadding = new DPanel(new BorderLayout());
        buttonsPadding.setBackground(new Color(0, 50, 0));
        
        DPanel leftPadding = new DPanel();
        leftPadding.setPreferredSize(new Dimension(100, 0));
        leftPadding.setBackground(new Color(0, 50, 0));
        
        DPanel rightPadding = new DPanel();
        rightPadding.setPreferredSize(new Dimension(100, 0));
        rightPadding.setBackground(new Color(0, 50, 0));
        
        buttonsPanel.add(playAgainButton);
        buttonsPanel.add(quitButton);
        
        buttonsPadding.add(leftPadding, BorderLayout.WEST);
        buttonsPadding.add(buttonsPanel, BorderLayout.CENTER);
        buttonsPadding.add(rightPadding, BorderLayout.EAST);
        
        // Add padding around the entire panel
        DPanel paddingNorth = new DPanel();
        paddingNorth.setPreferredSize(new Dimension(0, 20));
        paddingNorth.setBackground(new Color(0, 50, 0));
        
        DPanel paddingSouth = new DPanel();
        paddingSouth.setPreferredSize(new Dimension(0, 20));
        paddingSouth.setBackground(new Color(0, 50, 0));
        
        // Add all components to the main panel
        add(paddingNorth, BorderLayout.NORTH);
        add(titlePanel, BorderLayout.NORTH);
        add(scoresPanel, BorderLayout.CENTER);
        add(buttonsPadding, BorderLayout.SOUTH);
        add(paddingSouth, BorderLayout.SOUTH);
    }
    
    /**
     * Set the listener for game over actions
     * 
     * @param listener the listener to set
     */
    public void setGameOverListener(GameOverListener listener) {
        this.listener = listener;
    }
    
    /**
     * Calculate a player's score based on their remaining cards
     * 
     * @param player the player
     * @return the player's score
     */
    private int calculateScore(Player player) {
        int score = 0;
        
        for (Card card : player.getHand()) {
            CardValue value = card.getValue();
            
            switch (value) {
                case SKIP:
                case REVERSE:
                case DRAW_TWO:
                    score += 20;
                    break;
                case WILD:
                case WILD_DRAW_FOUR:
                    score += 50;
                    break;
                default:
                    try {
                        // For number cards, the score is the number
                        score += Integer.parseInt(value.getSymbol());
                    } catch (NumberFormatException e) {
                        // Default to 0 if not a number (shouldn't happen)
                        score += 0;
                    }
                    break;
            }
        }
        
        return score;
    }
}