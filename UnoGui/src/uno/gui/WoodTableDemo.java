package uno.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dgui.DButton;
import dgui.DFrame;
import dgui.DLabel;
import dgui.DPanel;
import uno.core.Card;
import uno.core.CardColor;
import uno.core.CardValue;

/**
 * Démo d'une table de jeu UNO avec un fond en bois
 */
public class WoodTableDemo {
    
    private DFrame frame;
    private DWoodPanel mainPanel;
    private List<DUnoCard> playerCards = new ArrayList<>();
    
    /**
     * Crée une nouvelle démo de table de jeu UNO
     */
    public WoodTableDemo() {
        createAndShowGUI();
    }
    
    /**
     * Crée et affiche l'interface utilisateur
     */
    private void createAndShowGUI() {
        // Créer la fenêtre principale
        frame = new DFrame("UNO - Table de jeu en bois");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        // Créer le panneau principal avec un fond en bois
        mainPanel = new DWoodPanel(new BorderLayout(10, 10));
        mainPanel.createWoodTexture(DWoodPanel.LIGHT_WOOD);
        
        // Ajouter les zones de jeu
        addPlayArea();
        
        // Ajouter les cartes du joueur
        addPlayerCards();
        
        // Ajouter les autres joueurs (IA)
        addOtherPlayers();
        
        // Finaliser et afficher
        frame.add(mainPanel);
        frame.setResizable(false);
        frame.centerOnScreen();
        frame.setVisible(true);
    }
    
    /**
     * Ajoute la zone de jeu centrale
     */
    private void addPlayArea() {
        // Panneau central pour les cartes jouées et la pioche
        DPanel centerPanel = new DPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Carte jouée au centre
        DUnoCard playedCard = new DUnoCard(new Card(CardColor.GREEN, CardValue.ONE));
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardPanel.setOpaque(false);
        cardPanel.add(playedCard.getComponent());
        centerPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Pioche à côté
        DUnoCard deckCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
        deckCard.setFaceUp(false);
        JPanel deckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deckPanel.setOpaque(false);
        deckPanel.add(deckCard.getComponent());
        centerPanel.add(deckPanel, BorderLayout.EAST);
        
        // Bouton "Appeler UNO!"
        DButton unoButton = new DButton("Call UNO!");
        unoButton.setBackground(new Color(0, 120, 255));
        unoButton.setForeground(Color.WHITE);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(unoButton.getComponent());
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Indicateur de tour
        DLabel turnLabel = new DLabel("C'est votre tour!");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setForeground(Color.WHITE);
        JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        turnPanel.setOpaque(false);
        turnPanel.add(turnLabel.getComponent());
        centerPanel.add(turnPanel, BorderLayout.NORTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Ajoute les cartes du joueur en bas de l'écran
     */
    private void addPlayerCards() {
        // Panneau pour les cartes du joueur
        DPanel playerPanel = new DPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel.getPanel(), BoxLayout.Y_AXIS));
        playerPanel.setOpaque(false);
        
        // Nom du joueur
        DLabel playerName = new DLabel("TheLegend27");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerName.getComponent().setAlignmentX(SwingConstants.CENTER);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setOpaque(false);
        namePanel.add(playerName.getComponent());
        playerPanel.add(namePanel);
        
        // Cartes du joueur
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -15, 5));
        cardsPanel.setOpaque(false);
        
        // Créer quelques cartes pour la démo
        Card[] cards = {
            new Card(CardColor.RED, CardValue.SEVEN),
            new Card(CardColor.GREEN, CardValue.FIVE),
            new Card(CardColor.YELLOW, CardValue.NINE),
            new Card(CardColor.BLUE, CardValue.DRAW_TWO),
            new Card(CardColor.RED, CardValue.REVERSE),
            new Card(CardColor.GREEN, CardValue.ONE),
            new Card(CardColor.WILD, CardValue.WILD_DRAW_FOUR)
        };
        
        for (Card card : cards) {
            DUnoCard unoCard = new DUnoCard(card);
            playerCards.add(unoCard);
            cardsPanel.add(unoCard.getComponent());
        }
        
        playerPanel.add(cardsPanel);
        mainPanel.add(playerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Ajoute les autres joueurs (IA) autour de la table
     */
    private void addOtherPlayers() {
        // Panneau pour le joueur de gauche
        DPanel leftPlayerPanel = createAIPlayer("Daenerys Targaryen", 7, BorderLayout.WEST);
        mainPanel.add(leftPlayerPanel, BorderLayout.WEST);
        
        // Panneau pour le joueur de droite
        DPanel rightPlayerPanel = createAIPlayer("Hannibal Lecter", 7, BorderLayout.EAST);
        mainPanel.add(rightPlayerPanel, BorderLayout.EAST);
        
        // Panneau pour le joueur du haut
        DPanel topPlayerPanel = createAIPlayer("Forrest Gump", 7, BorderLayout.NORTH);
        mainPanel.add(topPlayerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Crée un panneau pour un joueur IA
     */
    private DPanel createAIPlayer(String name, int cardCount, String position) {
        DPanel panel = new DPanel();
        panel.setOpaque(false);
        
        boolean isHorizontal = position.equals(BorderLayout.NORTH) || position.equals(BorderLayout.SOUTH);
        
        if (isHorizontal) {
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.Y_AXIS));
            
            // Nom du joueur
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            playerName.getComponent().setAlignmentX(SwingConstants.CENTER);
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            namePanel.setOpaque(false);
            namePanel.add(playerName.getComponent());
            panel.add(namePanel);
            
            // Cartes du joueur (faces cachées)
            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -25, 0));
            cardsPanel.setOpaque(false);
            
            for (int i = 0; i < cardCount; i++) {
                DUnoCard card = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
                card.setFaceUp(false);
                cardsPanel.add(card.getComponent());
            }
            
            panel.add(cardsPanel);
        } else {
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.X_AXIS));
            panel.setPreferredSize(new Dimension(100, 300));
            panel.getPanel().setBorder(new EmptyBorder(10, 10, 10, 10));
            
            // Organisation verticale pour les joueurs sur les côtés
            DPanel verticalPanel = new DPanel();
            verticalPanel.setOpaque(false);
            verticalPanel.setLayout(new BoxLayout(verticalPanel.getPanel(), BoxLayout.Y_AXIS));
            
            // Nom du joueur
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            
            JPanel namePanel = new JPanel();
            namePanel.setOpaque(false);
            namePanel.add(playerName.getComponent());
            verticalPanel.add(namePanel);
            
            // Cartes du joueur (empilées verticalement, faces cachées)
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setOpaque(false);
            
            for (int i = 0; i < cardCount; i++) {
                DUnoCard card = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD));
                card.setFaceUp(false);
                
                JPanel cardHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -10));
                cardHolder.setOpaque(false);
                cardHolder.add(card.getComponent());
                cardsPanel.add(cardHolder);
            }
            
            verticalPanel.add(cardsPanel);
            panel.add(verticalPanel);
        }
        
        return panel;
    }
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new WoodTableDemo();
        });
    }
}