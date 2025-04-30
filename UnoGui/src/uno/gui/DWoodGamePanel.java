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
import uno.core.GameEventAdapter;
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

/**
 * Panneau principal du jeu UNO avec une interface en bois
 */
public class DWoodGamePanel extends DWoodPanel {
    private Game game;
    private List<DUnoCard> playerCards = new ArrayList<>();
    private Map<Player, List<DUnoCard>> aiPlayerCards = new HashMap<>();
    private Map<String, DPanel> playerPanels = new HashMap<>();
    
    // Cartes communes
    private DUnoCard topCard;
    private DUnoCard deckCard;
    
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
    private DUnoCard selectedCard = null;
    private CardColor selectedWildColor = null;
    
    /**
     * Crée un nouveau panneau de jeu UNO en bois
     */
    public DWoodGamePanel(Game game) {
        super(new BorderLayout(10, 10));
        this.game = game;
        this.game.addGameEventListener(new GameEventHandler());
        
        initUI();
        setupEventHandlers();
    }
    
    /**
     * Initialise l'interface utilisateur
     */
    private void initUI() {
        // Configurer le panneau principal avec une texture en bois
        createWoodTexture(LIGHT_WOOD);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Créer les panneaux d'organisation
        createCenterPanel();
        createPlayerPanel();
        createControlPanel();
        createColorSelectionPanel();
        
        // Ajouter les panneaux au panneau principal
        add(centerPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
        
        // Initialiser les cartes visibles
        updateTopCard();
        updateDeckCard();
    }
    
    /**
     * Crée le panneau central du jeu
     */
    private void createCenterPanel() {
        centerPanel = new DPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        
        // Carte jouée au centre
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardPanel.setOpaque(false);
        
        // Créer une carte initiale en attendant que le jeu soit configuré
        Card initialCard = game.getTopCard();
        if (initialCard == null) {
            initialCard = new Card(CardColor.RED, CardValue.ZERO);
        }
        topCard = new DUnoCard(initialCard);
        cardPanel.add(topCard.getComponent());
        
        // Pioche à côté
        JPanel deckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deckPanel.setOpaque(false);
        deckCard = new DUnoCard(new Card(CardColor.WILD, CardValue.WILD)); // Utiliser WILD au lieu de null
        deckCard.setFaceUp(false);
        deckPanel.add(deckCard.getComponent());
        
        // Indicateur de tour
        turnIndicator = new DLabel("C'est à vous de jouer!");
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        turnIndicator.setForeground(Color.WHITE);
        JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        turnPanel.setOpaque(false);
        turnPanel.add(turnIndicator.getComponent());
        
        // Status du jeu
        gameStatus = new DLabel("");
        gameStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        gameStatus.setForeground(Color.WHITE);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusPanel.add(gameStatus.getComponent());
        
        // Bouton UNO
        unoButton = new DButton("Call UNO!");
        unoButton.setBackground(new Color(0, 120, 255));
        unoButton.setForeground(Color.WHITE);
        JPanel unoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        unoPanel.setOpaque(false);
        unoPanel.add(unoButton.getComponent());
        
        // Organisation
        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setOpaque(false);
        topInfoPanel.setLayout(new BoxLayout(topInfoPanel, BoxLayout.Y_AXIS));
        topInfoPanel.add(turnPanel);
        topInfoPanel.add(statusPanel);
        
        JPanel centerCardPanel = new JPanel(new BorderLayout());
        centerCardPanel.setOpaque(false);
        centerCardPanel.add(deckPanel, BorderLayout.EAST);
        centerCardPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Ajout des panneaux au panneau central
        centerPanel.add(topInfoPanel, BorderLayout.NORTH);
        centerPanel.add(centerCardPanel, BorderLayout.CENTER);
        centerPanel.add(unoPanel, BorderLayout.SOUTH);
        
        // Ajouter des panneaux pour les autres joueurs
        createAIPlayerPanels();
    }
    
    /**
     * Crée les panneaux pour les joueurs IA
     */
    private void createAIPlayerPanels() {
        // Panneau pour le joueur du haut (en face)
        topPlayerPanel = createAIPlayerPanel("Joueur Nord", BorderLayout.NORTH);
        centerPanel.add(topPlayerPanel, BorderLayout.NORTH);
        
        // Panneau pour le joueur de gauche
        leftPlayerPanel = createAIPlayerPanel("Joueur Ouest", BorderLayout.WEST);
        centerPanel.add(leftPlayerPanel, BorderLayout.WEST);
        
        // Panneau pour le joueur de droite
        rightPlayerPanel = createAIPlayerPanel("Joueur Est", BorderLayout.EAST);
        centerPanel.add(rightPlayerPanel, BorderLayout.EAST);
        
        playerPanels.put("Nord", topPlayerPanel);
        playerPanels.put("Ouest", leftPlayerPanel);
        playerPanels.put("Est", rightPlayerPanel);
    }
    
    /**
     * Crée un panneau pour un joueur IA
     */
    private DPanel createAIPlayerPanel(String name, String position) {
        DPanel panel = new DPanel();
        panel.setOpaque(false);
        
        boolean isHorizontal = position.equals(BorderLayout.NORTH) || position.equals(BorderLayout.SOUTH);
        
        if (isHorizontal) {
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.Y_AXIS));
            
            // Nom du joueur
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            panel.add(playerName);
            
            // Place pour les cartes
            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -25, 0));
            cardsPanel.setOpaque(false);
            panel.add(cardsPanel);
            
        } else {
            panel.setLayout(new BoxLayout(panel.getPanel(), BoxLayout.X_AXIS));
            panel.setPreferredSize(new Dimension(100, 250));
            
            // Organisation verticale
            DPanel verticalPanel = new DPanel();
            verticalPanel.setOpaque(false);
            verticalPanel.setLayout(new BoxLayout(verticalPanel.getPanel(), BoxLayout.Y_AXIS));
            
            // Nom du joueur
            DLabel playerName = new DLabel(name);
            playerName.setFont(new Font("Arial", Font.BOLD, 14));
            playerName.setForeground(Color.WHITE);
            verticalPanel.add(playerName);
            
            // Place pour les cartes
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            cardsPanel.setOpaque(false);
            verticalPanel.add(cardsPanel);
            
            panel.add(verticalPanel);
        }
        
        return panel;
    }
    
    /**
     * Crée le panneau du joueur humain
     */
    private void createPlayerPanel() {
        playerPanel = new DPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new BoxLayout(playerPanel.getPanel(), BoxLayout.Y_AXIS));
        
        // Nom du joueur
        DLabel playerName = new DLabel("Vous");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerPanel.add(playerName);
        
        // Cartes du joueur
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -15, 5));
        cardsPanel.setOpaque(false);
        playerPanel.add(cardsPanel);
    }
    
    /**
     * Crée le panneau de contrôle
     */
    private void createControlPanel() {
        controlPanel = new DPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel.getPanel(), BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(150, 300));
        
        // Boutons de contrôle
        drawButton = new DButton("Piocher");
        drawButton.setBackground(new Color(60, 120, 60));
        drawButton.setForeground(Color.WHITE);
        
        playButton = new DButton("Jouer");
        playButton.setBackground(new Color(60, 60, 160));
        playButton.setForeground(Color.WHITE);
        
        // Ajouter les boutons au panneau
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(drawButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(playButton);
        controlPanel.add(Box.createVerticalStrut(20));
    }
    
    /**
     * Crée le panneau de sélection de couleur pour les cartes WILD
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
        
        // Ajouter les boutons au panneau
        colorSelectionPanel.add(colorRedButton);
        colorSelectionPanel.add(colorBlueButton);
        colorSelectionPanel.add(colorGreenButton);
        colorSelectionPanel.add(colorYellowButton);
        
        // Masquer initialement le panneau
        colorSelectionPanel.setVisible(false);
        controlPanel.add(colorSelectionPanel);
    }
    
    /**
     * Configure les gestionnaires d'événements
     */
    private void setupEventHandlers() {
        // Gestionnaire pour le bouton Piocher
        drawButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded()) {
                Card drawnCard = game.drawCard(game.getCurrentPlayer());
                updatePlayerHand();
                
                // Animations
                DUnoCard latestCard = playerCards.get(playerCards.size() - 1);
                Point deckPos = getComponentScreenPosition(deckCard);
                Point handPos = getComponentScreenPosition(latestCard);
                
                // Animer la carte depuis la pioche jusqu'à la main
                animateCardDraw(latestCard, deckPos, handPos);
                
                // Passer au tour suivant
                game.advanceToNextPlayer();
                updateTurnIndicator();
            }
        });
        
        // Gestionnaire pour le bouton Jouer
        playButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded() && selectedCard != null) {
                // Vérifier si la carte peut être jouée
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
        
        // Gestionnaire pour le bouton UNO
        unoButton.addActionListener(e -> {
            if (isPlayerTurn() && !game.isGameEnded()) {
                if (game.getCurrentPlayer().getHandSize() == 1) {
                    game.callUno(game.getCurrentPlayer());
                    showMessage("Vous avez appelé UNO!");
                } else {
                    showMessage("Vous ne pouvez appeler UNO que lorsqu'il vous reste une seule carte!");
                }
            }
        });
        
        // Gestionnaires pour les boutons de sélection de couleur
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
            
            // Mettre à jour la couleur de la carte
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
            
            // Animer la carte depuis la main jusqu'au centre
            Point handPos = getComponentScreenPosition(selectedCard);
            Point tablePos = getComponentScreenPosition(topCard);
            
            animateCardPlay(selectedCard, handPos, tablePos, () -> {
                // Jouer la carte et mettre à jour l'interface
                if (game.playCard(cardToPlay)) {
                    removeCardFromPlayerHand(selectedCard);
                    updateTopCard();
                    
                    // Vérifier la fin du jeu
                    if (game.getCurrentPlayer().getHandSize() == 0) {
                        showMessage("Vous avez gagné!");
                        return;
                    }
                    
                    // Passer au tour suivant
                    game.advanceToNextPlayer();
                    updateTurnIndicator();
                }
            });
        }
    }
    
    /**
     * Anime le tirage d'une carte
     */
    private void animateCardDraw(DUnoCard card, Point start, Point end) {
        // Cacher la carte initialement
        card.setVisible(false);
        
        // Animation de la carte depuis la pioche
        DAnimator.animateMove(card, start, end, DAnimator.DEFAULT_DURATION, (success) -> {
            // Afficher la carte une fois l'animation terminée
            card.setVisible(true);
            
            // Animation de retournement
            DAnimator.animateFlip(card, true, DAnimator.DEFAULT_DURATION, null);
        });
    }
    
    /**
     * Anime le jeu d'une carte
     */
    private void animateCardPlay(DUnoCard card, Point start, Point end, Runnable onComplete) {
        // Animation de la carte depuis la main jusqu'au centre
        DAnimator.animateMove(card, start, end, DAnimator.DEFAULT_DURATION, (success) -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }
    
    /**
     * Récupère la position d'un composant par rapport à l'écran
     */
    private Point getComponentScreenPosition(DComponent component) {
        Point p = component.getComponent().getLocationOnScreen();
        return new Point(p.x, p.y);
    }
    
    /**
     * Met à jour la carte du dessus de la pile
     */
    private void updateTopCard() {
        Card newTopCard = game.getTopCard();
        
        // Vérifier si la carte du dessus est null (peut arriver lors de l'initialisation)
        if (newTopCard == null) {
            // Créer une carte par défaut si aucune carte du dessus n'est disponible
            newTopCard = new Card(CardColor.RED, CardValue.ZERO);
            
            // Si nous sommes en train d'initialiser le jeu, ne rien faire d'autre
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
     * Met à jour la pioche
     */
    private void updateDeckCard() {
        deckCard.setFaceUp(false);
        deckCard.getComponent().repaint();
    }
    
    /**
     * Met à jour la main du joueur
     */
    private void updatePlayerHand() {
        // Vider la main actuelle
        playerCards.clear();
        JPanel cardsPanel = (JPanel) playerPanel.getComponent().getComponent(1);
        cardsPanel.removeAll();
        
        // Récupérer les cartes du joueur courant
        Player humanPlayer = getHumanPlayer();
        if (humanPlayer != null) {
            for (Card card : humanPlayer.getHand()) {
                DUnoCard unoCard = new DUnoCard(card);
                
                // Ajouter un écouteur de clic pour la sélection de carte
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
     * Retire une carte de la main du joueur
     */
    private void removeCardFromPlayerHand(DUnoCard card) {
        JPanel cardsPanel = (JPanel) playerPanel.getComponent().getComponent(1);
        cardsPanel.remove(card.getComponent());
        playerCards.remove(card);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        
        // Réinitialiser la sélection
        selectedCard = null;
    }
    
    /**
     * Met à jour les mains des joueurs IA
     */
    private void updateAIPlayersHands() {
        // TODO: Mettre à jour les cartes des joueurs IA
    }
    
    /**
     * Met à jour l'indicateur de tour
     */
    private void updateTurnIndicator() {
        if (isPlayerTurn()) {
            turnIndicator.setText("C'est à vous de jouer!");
            turnIndicator.setForeground(Color.WHITE);
        } else {
            String playerName = game.getCurrentPlayer().getName();
            turnIndicator.setText("Tour de " + playerName);
            turnIndicator.setForeground(Color.YELLOW);
        }
    }
    
    /**
     * Vérifie si c'est le tour du joueur humain
     */
    private boolean isPlayerTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer != null && !currentPlayer.isAI();
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
     * Affiche un message dans la zone de statut
     */
    private void showMessage(String message) {
        gameStatus.setText(message);
        
        // Créer un timer pour effacer le message après quelques secondes
        new javax.swing.Timer(5000, e -> {
            gameStatus.setText("");
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }
    
    /**
     * Initialise le jeu avec les joueurs
     */
    public void initializeGame(String playerName, int aiPlayerCount) {
        // Ajouter le joueur humain
        Player humanPlayer = new Player(playerName, false);
        game.addPlayer(humanPlayer);
        
        // Ajouter les joueurs IA
        String[] aiNames = {"Nord", "Est", "Ouest", "Sud"};
        for (int i = 0; i < Math.min(aiPlayerCount, aiNames.length); i++) {
            Player aiPlayer = new Player(aiNames[i], true);
            game.addPlayer(aiPlayer);
        }
        
        // Mettre à jour l'interface
        updatePlayerHand();
        updateAIPlayersHands();
        updateTurnIndicator();
    }
    
    /**
     * Démarre le jeu
     */
    public void startGame() {
        game.startGame();
        updatePlayerHand();
        updateAIPlayersHands();
        updateTopCard();
        updateDeckCard();
        updateTurnIndicator();
    }
    
    /**
     * Met à jour l'interface quand le jeu commence
     */
    public void onGameStarted() {
        // Mise à jour de l'interface quand le jeu commence
        updatePlayerHand();
        updateTopCard();
        updateTurnIndicator();
        showMessage("La partie commence!");
    }
    
    /**
     * Met à jour l'interface quand le tour change
     */
    public void onPlayerTurnChanged(Player player) {
        // Mise à jour de l'interface quand le tour change
        updateTurnIndicator();
    }
    
    /**
     * Met à jour l'interface quand une carte est jouée
     */
    public void onCardPlayed(Player player, Card card) {
        // Mise à jour de l'interface quand une carte est jouée
        updateTopCard();
        
        // Afficher un message approprié
        String cardDesc = card.getColor() + " " + card.getValue();
        if (player.isAI()) {
            showMessage(player.getName() + " a joué " + cardDesc);
        } else {
            showMessage("Vous avez joué " + cardDesc);
        }
    }
    
    /**
     * Met à jour l'interface quand une carte est piochée
     */
    public void onCardDrawn(Player player, Card card) {
        // Mise à jour de l'interface quand une carte est piochée
        if (player.isAI()) {
            showMessage(player.getName() + " a pioché une carte");
            // TODO: Mettre à jour la main du joueur IA
        } else {
            updatePlayerHand();
            showMessage("Vous avez pioché une carte");
        }
    }
    
    /**
     * Met à jour l'interface quand un joueur appelle UNO
     */
    public void onUnoCalledByPlayer(Player player) {
        // Mise à jour de l'interface quand un joueur appelle UNO
        if (player.isAI()) {
            showMessage(player.getName() + " a appelé UNO!");
        } else {
            showMessage("Vous avez appelé UNO!");
        }
    }
    
    /**
     * Met à jour l'interface quand le jeu se termine
     */
    public void onGameEnded(Player winner) {
        // Mise à jour de l'interface quand le jeu se termine
        if (winner.isAI()) {
            showMessage(winner.getName() + " a gagné la partie!");
        } else {
            showMessage("Félicitations! Vous avez gagné la partie!");
        }
    }
    
    /**
     * Met à jour le message de statut
     */
    private void updateStatusMessage(String message) {
        if (gameStatus != null) {
            gameStatus.setText(message);
            showMessage(message);
        }
    }
    
    /**
     * Met à jour l'interface utilisateur
     */
    private void updateUI() {
        updatePlayerHand();
        updateAIPlayersHands();
        updateTopCard();
        updateDeckCard();
        updateTurnIndicator();
    }
    
    /**
     * Retourne le nom lisible d'une couleur
     */
    private String getColorName(CardColor color) {
        switch (color) {
            case RED: return "Rouge";
            case BLUE: return "Bleu";
            case GREEN: return "Vert";
            case YELLOW: return "Jaune";
            case WILD: return "Multicolore";
            default: return "Inconnu";
        }
    }
    
    /**
     * Classe interne pour gérer les événements du jeu
     */
    private class GameEventHandler implements Game.GameEventListener {
        
        @Override
        public void onGameStarted(Game game) {
            updateStatusMessage("La partie commence!");
            updateUI();
        }
        
        @Override
        public void onGameEnded(Game game, Player winner) {
            updateStatusMessage("La partie est terminée! " + winner.getName() + " a gagné!");
            updateUI();
        }
        
        @Override
        public void onPlayerTurn(Game game, Player player) {
            updateTurnIndicator();
            updateStatusMessage("C'est au tour de " + player.getName());
            updateUI();
        }
        
        @Override
        public void onPlayerSkipped(Game game, Player player) {
            updateStatusMessage(player.getName() + " passe son tour!");
            updateUI();
        }
        
        @Override
        public void onDirectionChanged(Game game, boolean isClockwise) {
            updateStatusMessage("Changement de direction!");
            updateUI();
        }
        
        @Override
        public void onPlayerDrewCards(Game game, Player player, int count) {
            updateStatusMessage(player.getName() + " pioche " + count + " cartes");
            updateUI();
        }
        
        @Override
        public void onDeckReshuffled(Game game) {
            updateStatusMessage("Le deck a été remélangé");
            updateUI();
        }
        
        @Override
        public void onPlayerCalledUno(Game game, Player player) {
            updateStatusMessage(player.getName() + " a appelé UNO!");
            updateUI();
        }
        
        @Override
        public void onPlayerForgotUno(Game game, Player player) {
            updateStatusMessage(player.getName() + " a oublié d'appeler UNO et pioche 2 cartes!");
            updateUI();
        }
        
        @Override
        public void onColorChanged(Game game, CardColor color) {
            updateStatusMessage("La couleur change pour: " + getColorName(color));
            updateTopCard();
            updateUI();
        }
        
        @Override
        public void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged) {
            updateStatusMessage(challenger.getName() + " a contesté avec succès le +4 de " + challenged.getName() + "!");
            updateUI();
        }
        
        @Override
        public void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged) {
            updateStatusMessage(challenger.getName() + " a échoué à contester le +4 de " + challenged.getName() + "!");
            updateUI();
        }
    }
}