package uno.gui;

import dgui.DButton;
import dgui.DComponentAdapter;
import dgui.DLabel;
import dgui.DPanel;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Panneau principal du jeu UNO avec une interface en bois
 * Version pure DGUI sans dépendances directes à Swing
 * Version simplifiée pour faciliter le développement
 */
public class DWoodGamePanelPure extends DWoodPanel {
    private Game game;
    private List<DUnoCardPure> playerCards = new ArrayList<>();
    private Map<Player, List<DUnoCardPure>> aiPlayerCards = new HashMap<>();
    private Map<String, DPanel> playerPanels = new HashMap<>();
    
    // Cartes communes
    private DUnoCardPure topCard;
    private DUnoCardPure deckCard;
    
    // Panneaux d'organisation
    private DPanel centerPanel;
    private DPanel playerPanel;
    private DPanel controlPanel;
    private DPanel topPlayerPanel;
    private DPanel leftPlayerPanel;
    private DPanel rightPlayerPanel;
    
    // Contrôles du jeu
    private DButton drawButton;
    private DButton playButton;
    private DButton unoButton;
    private DButton colorRedButton;
    private DButton colorBlueButton;
    private DButton colorGreenButton;
    private DButton colorYellowButton;
    private DPanel colorSelectionPanel;
    
    // Indicateurs
    private DLabel turnIndicator;
    private DLabel gameStatus;
    
    // État du jeu
    private boolean waitingForColorSelection = false;
    private DUnoCardPure selectedCard = null;
    private CardColor selectedWildColor = null;
    
    /**
     * Crée un nouveau panneau de jeu UNO en bois
     */
    public DWoodGamePanelPure(Game game) {
        super(new BorderLayout(10, 10));
        this.game = game;
        
        // Adapter notre gestionnaire d'événements à l'interface GameEventListener interne de Game
        Game.GameEventListener eventListener = new Game.GameEventListener() {
            @Override
            public void onGameStarted(Game game) {
                updatePlayerHand();
                updateAIPlayerHands();
                updateTurnIndicator();
            }
            
            @Override
            public void onGameEnded(Game game, Player winner) {
                showMessage(winner.getName() + " a gagné la partie!");
            }
            
            @Override
            public void onPlayerTurn(Game game, Player player) {
                updateTurnIndicator();
            }
            
            @Override
            public void onPlayerSkipped(Game game, Player player) {
                // Ne rien faire
            }
            
            @Override
            public void onDirectionChanged(Game game, boolean isClockwise) {
                // Ne rien faire
            }
            
            @Override
            public void onPlayerDrewCards(Game game, Player player, int count) {
                if (player.isAI()) {
                    updateAIPlayerHands();
                } else {
                    updatePlayerHand();
                }
            }
            
            @Override
            public void onDeckReshuffled(Game game) {
                // Ne rien faire
            }
            
            @Override
            public void onPlayerCalledUno(Game game, Player player) {
                showMessage(player.getName() + " a appelé UNO!");
            }
            
            @Override
            public void onPlayerForgotUno(Game game, Player player) {
                // Ne rien faire
            }
            
            @Override
            public void onColorChanged(Game game, CardColor color) {
                // Ne rien faire
            }
            
            @Override
            public void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged) {
                // Ne rien faire
            }
            
            @Override
            public void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged) {
                // Ne rien faire
            }
        };
        
        this.game.addGameEventListener(eventListener);
        
        initUI();
        setupEventHandlers();
    }
    
    /**
     * Initialise l'interface utilisateur
     */
    private void initUI() {
        // Configurer le panneau principal avec une texture en bois
        createWoodTexture(LIGHT_WOOD);
        setBorder(10, 10, 10, 10);
        
        // Créer les panneaux d'organisation
        createCenterPanel();
        createPlayerPanel();
        createControlPanel();
        createColorSelectionPanel();
        
        // Ajouter les panneaux au panneau principal
        add(centerPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
    }
    
    /**
     * Crée le panneau central du jeu
     */
    private void createCenterPanel() {
        centerPanel = new DPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        
        // Carte jouée au centre
        DPanel cardPanel = new DPanel(new FlowLayout(FlowLayout.CENTER));
        cardPanel.setOpaque(false);
        
        // Créer une carte initiale en attendant que le jeu soit configuré
        Card initialCard = game.getTopCard();
        if (initialCard == null) {
            initialCard = new Card(CardColor.RED, CardValue.ZERO);
        }
        topCard = new DUnoCardPure(initialCard);
        cardPanel.add(topCard);
        
        // Pioche à côté
        DPanel deckPanel = new DPanel(new FlowLayout(FlowLayout.CENTER));
        deckPanel.setOpaque(false);
        deckCard = new DUnoCardPure(new Card(CardColor.WILD, CardValue.WILD));
        deckCard.setFaceUp(false);
        deckPanel.add(deckCard);
        
        // Indicateur de tour
        turnIndicator = new DLabel("C'est à vous de jouer!");
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        turnIndicator.setForeground(Color.WHITE);
        
        // Status du jeu
        gameStatus = new DLabel("");
        gameStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        gameStatus.setForeground(Color.WHITE);
        
        // Organisation verticale
        DPanel infoPanel = new DPanel();
        infoPanel.setOpaque(false);
        infoPanel.add(turnIndicator);
        infoPanel.add(gameStatus);
        
        // Organisation au centre
        DPanel cardsPanel = new DPanel(new BorderLayout());
        cardsPanel.setOpaque(false);
        cardsPanel.add(cardPanel, BorderLayout.CENTER);
        cardsPanel.add(deckPanel, BorderLayout.EAST);
        
        // Bouton UNO
        unoButton = new DButton("Call UNO!");
        unoButton.setBackground(new Color(0, 120, 255));
        unoButton.setForeground(Color.WHITE);
        DPanel unoPanel = new DPanel();
        unoPanel.setOpaque(false);
        unoPanel.add(unoButton);
        
        // Ajouter au panneau central
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(cardsPanel, BorderLayout.CENTER);
        centerPanel.add(unoPanel, BorderLayout.SOUTH);
        
        // Ajouter les joueurs IA
        createAIPlayerPanels();
    }
    
    /**
     * Crée les panneaux pour les joueurs IA
     */
    private void createAIPlayerPanels() {
        // Joueur du haut
        topPlayerPanel = new DPanel();
        topPlayerPanel.setOpaque(false);
        DLabel topName = new DLabel("Forrest Gump");
        topName.setForeground(Color.WHITE);
        topPlayerPanel.add(topName);
        
        // Joueur de gauche
        leftPlayerPanel = new DPanel();
        leftPlayerPanel.setOpaque(false);
        DLabel leftName = new DLabel("Daenerys Targaryen");
        leftName.setForeground(Color.WHITE);
        leftPlayerPanel.add(leftName);
        
        // Joueur de droite
        rightPlayerPanel = new DPanel();
        rightPlayerPanel.setOpaque(false);
        DLabel rightName = new DLabel("Hannibal Lecter");
        rightName.setForeground(Color.WHITE);
        rightPlayerPanel.add(rightName);
        
        // Ajouter au panneau central
        centerPanel.add(topPlayerPanel, BorderLayout.NORTH);
        centerPanel.add(leftPlayerPanel, BorderLayout.WEST);
        centerPanel.add(rightPlayerPanel, BorderLayout.EAST);
        
        // Stocker les références
        playerPanels.put("Forrest Gump", topPlayerPanel);
        playerPanels.put("Daenerys Targaryen", leftPlayerPanel);
        playerPanels.put("Hannibal Lecter", rightPlayerPanel);
    }
    
    /**
     * Crée le panneau du joueur humain
     */
    private void createPlayerPanel() {
        playerPanel = new DPanel();
        playerPanel.setOpaque(false);
        
        // Nom du joueur
        DLabel playerName = new DLabel("TheLegend27");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerPanel.add(playerName);
        
        // Zone pour les cartes du joueur
        DPanel cardsPanel = new DPanel(new FlowLayout(FlowLayout.CENTER));
        cardsPanel.setOpaque(false);
        playerPanel.add(cardsPanel);
    }
    
    /**
     * Crée le panneau de contrôle
     */
    private void createControlPanel() {
        controlPanel = new DPanel();
        controlPanel.setOpaque(false);
        controlPanel.setPreferredSize(new Dimension(150, 300));
        
        // Boutons
        drawButton = new DButton("Piocher");
        drawButton.setBackground(new Color(60, 120, 60));
        drawButton.setForeground(Color.WHITE);
        
        playButton = new DButton("Jouer");
        playButton.setBackground(new Color(60, 60, 160));
        playButton.setForeground(Color.WHITE);
        
        controlPanel.add(drawButton);
        controlPanel.add(playButton);
    }
    
    /**
     * Crée le panneau de sélection de couleur
     */
    private void createColorSelectionPanel() {
        colorSelectionPanel = new DPanel(new GridLayout(2, 2, 10, 10));
        colorSelectionPanel.setOpaque(false);
        
        // Boutons de couleur
        colorRedButton = new DButton("Rouge");
        colorRedButton.setBackground(new Color(220, 40, 30));
        colorRedButton.setForeground(Color.WHITE);
        
        colorBlueButton = new DButton("Bleu");
        colorBlueButton.setBackground(new Color(30, 80, 200));
        colorBlueButton.setForeground(Color.WHITE);
        
        colorGreenButton = new DButton("Vert");
        colorGreenButton.setBackground(new Color(30, 180, 50));
        colorGreenButton.setForeground(Color.WHITE);
        
        colorYellowButton = new DButton("Jaune");
        colorYellowButton.setBackground(new Color(240, 200, 40));
        colorYellowButton.setForeground(Color.BLACK);
        
        colorSelectionPanel.add(colorRedButton);
        colorSelectionPanel.add(colorBlueButton);
        colorSelectionPanel.add(colorGreenButton);
        colorSelectionPanel.add(colorYellowButton);
        
        colorSelectionPanel.setVisible(false);
        controlPanel.add(colorSelectionPanel);
    }
    
    /**
     * Configure les gestionnaires d'événements
     */
    private void setupEventHandlers() {
        // Pioche
        drawButton.addActionListener(e -> {
            if (game.getCurrentPlayer() == getHumanPlayer()) {
                Card drawnCard = game.drawCard(game.getCurrentPlayer());
                updatePlayerHand();
                game.advanceToNextPlayer();
                updateTurnIndicator();
            }
        });
        
        // Jouer une carte
        playButton.addActionListener(e -> {
            if (game.getCurrentPlayer() == getHumanPlayer() && selectedCard != null) {
                Card cardToPlay = selectedCard.getCard();
                
                if (cardToPlay.canPlayOn(game.getTopCard())) {
                    if (cardToPlay.isWild()) {
                        waitingForColorSelection = true;
                        colorSelectionPanel.setVisible(true);
                        return;
                    }
                    
                    playSelectedCard();
                } else {
                    showMessage("Cette carte ne peut pas être jouée!");
                }
            }
        });
        
        // UNO
        unoButton.addActionListener(e -> {
            if (game.getCurrentPlayer() == getHumanPlayer()) {
                if (game.getCurrentPlayer().getHandSize() == 1) {
                    game.callUno(game.getCurrentPlayer());
                    showMessage("Vous avez appelé UNO!");
                }
            }
        });
        
        // Sélection de couleur
        colorRedButton.addActionListener(e -> selectWildColor(CardColor.RED));
        colorBlueButton.addActionListener(e -> selectWildColor(CardColor.BLUE));
        colorGreenButton.addActionListener(e -> selectWildColor(CardColor.GREEN));
        colorYellowButton.addActionListener(e -> selectWildColor(CardColor.YELLOW));
    }
    
    /**
     * Sélectionne une couleur pour une carte WILD
     */
    private void selectWildColor(CardColor color) {
        if (waitingForColorSelection && selectedCard != null) {
            selectedWildColor = color;
            colorSelectionPanel.setVisible(false);
            waitingForColorSelection = false;
            
            Card cardToPlay = selectedCard.getCard();
            cardToPlay.setChosenColor(color);
            
            playSelectedCard();
        }
    }
    
    /**
     * Joue la carte sélectionnée
     */
    private void playSelectedCard() {
        if (selectedCard != null) {
            Card cardToPlay = selectedCard.getCard();
            
            // Jouer la carte (cela va l'ajouter à la pile de défausse)
            if (game.playCard(cardToPlay)) {
                // Mise à jour de l'interface après avoir joué la carte
                updateTopCard();
                
                // Retirer la carte de la main
                playerCards.remove(selectedCard);
                
                // Mettre à jour l'interface de la main du joueur
                updatePlayerHand();
                
                // Vérifier fin de partie
                if (game.getCurrentPlayer().getHandSize() == 0) {
                    showMessage("Vous avez gagné!");
                    return;
                }
                
                // Joueur suivant
                game.advanceToNextPlayer();
                updateTurnIndicator();
            }
            
            selectedCard = null;
        }
    }
    
    /**
     * Met à jour la carte du dessus
     */
    private void updateTopCard() {
        Card topCardModel = game.getTopCard();
        if (topCardModel != null) {
            topCard.getCard().setColor(topCardModel.getColor());
            topCard.getCard().setValue(topCardModel.getValue());
            topCard.getCard().setChosenColor(topCardModel.getChosenColor());
            topCard.repaint();
        }
    }
    
    /**
     * Met à jour la main du joueur
     */
    private void updatePlayerHand() {
        Player humanPlayer = getHumanPlayer();
        if (humanPlayer == null) return;
        
        // Effacer les cartes actuelles
        playerCards.clear();
        
        // Récupérer le conteneur des cartes (dans notre cas, il s'agit du second composant ajouté au playerPanel)
        // Mais comme nous ne pouvons pas utiliser getComponent(index), on doit stocker une référence
        DPanel cardsPanel = new DPanel(new FlowLayout(FlowLayout.CENTER));
        cardsPanel.setOpaque(false);
        
        // Recréer le panneau du joueur
        playerPanel.removeAll();
        
        // Recréer le nom
        DLabel playerName = new DLabel("TheLegend27");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerPanel.add(playerName);
        
        // Ajouter le nouveau panneau des cartes
        playerPanel.add(cardsPanel);
        
        // Ajouter les cartes du joueur
        for (Card card : humanPlayer.getHand()) {
            DUnoCardPure cardComponent = new DUnoCardPure(card);
            cardComponent.setFaceUp(true);
            
            // Ajouter l'écouteur de clic
            cardComponent.setClickListener(e -> selectCard(cardComponent));
            
            playerCards.add(cardComponent);
            cardsPanel.add(cardComponent);
        }
        
        cardsPanel.revalidate();
        cardsPanel.repaint();
        playerPanel.revalidate();
        playerPanel.repaint();
    }
    
    /**
     * Met à jour les mains des joueurs IA
     */
    private void updateAIPlayerHands() {
        for (Player player : game.getPlayers()) {
            if (player.isAI()) {
                DPanel panel = playerPanels.get(player.getName());
                if (panel != null) {
                    panel.removeAll();
                    
                    // Nom du joueur
                    DLabel name = new DLabel(player.getName());
                    name.setForeground(Color.WHITE);
                    panel.add(name);
                    
                    // Cartes
                    DPanel cardsPanel = new DPanel();
                    cardsPanel.setOpaque(false);
                    
                    for (int i = 0; i < player.getHandSize(); i++) {
                        DUnoCardPure card = new DUnoCardPure(new Card(CardColor.WILD, CardValue.WILD));
                        card.setFaceUp(false);
                        cardsPanel.add(card);
                    }
                    
                    panel.add(cardsPanel);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        }
    }
    
    /**
     * Sélectionne une carte
     */
    private void selectCard(DUnoCardPure card) {
        if (selectedCard != null) {
            selectedCard.setSelected(false);
        }
        
        selectedCard = card;
        selectedCard.setSelected(true);
    }
    
    /**
     * Met à jour l'indicateur de tour
     */
    private void updateTurnIndicator() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            if (currentPlayer.isAI()) {
                turnIndicator.setText("C'est au tour de " + currentPlayer.getName());
                
                // Simuler le tour IA
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        playAITurn(currentPlayer);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                turnIndicator.setText("C'est à votre tour!");
            }
        }
    }
    
    /**
     * Joue un tour pour un joueur IA
     */
    private void playAITurn(Player aiPlayer) {
        if (game.isGameEnded()) return;
        
        // Jouer une carte ou piocher
        Card cardToPlay = null;
        
        // Chercher une carte jouable
        for (Card card : aiPlayer.getHand()) {
            if (card.canPlayOn(game.getTopCard())) {
                cardToPlay = card;
                break;
            }
        }
        
        if (cardToPlay != null) {
            // Si WILD, choisir une couleur
            if (cardToPlay.isWild()) {
                CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};
                cardToPlay.setChosenColor(colors[(int)(Math.random() * 4)]);
            }
            
            // Jouer la carte
            game.playCard(cardToPlay);
            
            // UNO
            if (aiPlayer.getHandSize() == 1) {
                game.callUno(aiPlayer);
                showMessage(aiPlayer.getName() + " a appelé UNO!");
            }
            
            // Fin de partie
            if (aiPlayer.getHandSize() == 0) {
                showMessage(aiPlayer.getName() + " a gagné!");
                return;
            }
        } else {
            // Piocher
            game.drawCard(aiPlayer);
        }
        
        // Mettre à jour
        updateTopCard();
        updateAIPlayerHands();
        game.advanceToNextPlayer();
        updateTurnIndicator();
    }
    
    /**
     * Récupère le joueur humain
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
     * Définit la bordure du panneau
     */

    
    /**
     * Affiche un message
     */
    private void showMessage(String message) {
        gameStatus.setText(message);
        
        // Effacer après un délai
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                gameStatus.setText("");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Initialise le jeu
     */
    public void initializeGame(String playerName, int aiCount) {
        // Déjà initialisé dans le constructeur
    }
    
    /**
     * Démarre le jeu
     */
    public void startGame() {
        updatePlayerHand();
        updateAIPlayerHands();
        updateTurnIndicator();
    }
    
    // Le gestionnaire d'événements a été déplacé sous forme de classe anonyme dans le constructeur
}