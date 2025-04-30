package uno.core;

/**
 * Interface pour les écouteurs d'événements du jeu UNO
 * Simplifiée pour notre implémentation DGUI
 */
public interface GameEventListener {
    
    /**
     * Appelé lorsqu'une carte est jouée
     */
    void onCardPlayed(Player player, Card card);
    
    /**
     * Appelé lorsqu'une carte est piochée
     */
    void onCardDrawn(Player player, Card card);
    
    /**
     * Appelé lorsqu'un joueur appelle UNO
     */
    void onUnoCall(Player player);
    
    /**
     * Appelé lorsque le tour change
     */
    void onTurnChanged(Player player);
    
    /**
     * Appelé lorsque le jeu se termine
     */
    void onGameEnded(Player winner);
    
    /**
     * Appelé lorsque le jeu démarre
     */
    void onGameStarted();
    
    /**
     * Appelé lorsqu'un joueur est sauté
     */
    default void onPlayerSkipped(Game game, Player player) {}
    
    /**
     * Appelé lorsque la direction change
     */
    default void onDirectionChanged(Game game, boolean isClockwise) {}
    
    /**
     * Appelé lorsqu'un joueur pioche des cartes
     */
    default void onPlayerDrewCards(Game game, Player player, int count) {}
    
    /**
     * Appelé lorsque le deck est mélangé
     */
    default void onDeckReshuffled(Game game) {}
    
    /**
     * Appelé lorsqu'un joueur a appelé UNO
     */
    default void onPlayerCalledUno(Game game, Player player) {}
    
    /**
     * Appelé lorsqu'un joueur a oublié d'appeler UNO
     */
    default void onPlayerForgotUno(Game game, Player player) {}
    
    /**
     * Appelé lorsque la couleur change
     */
    default void onColorChanged(Game game, CardColor color) {}
    
    /**
     * Appelé lorsqu'un défi Wild Draw Four réussit
     */
    default void onWildDrawFourChallengeSucceeded(Game game, Player challenger, Player challenged) {}
    
    /**
     * Appelé lorsqu'un défi Wild Draw Four échoue
     */
    default void onWildDrawFourChallengeFailed(Game game, Player challenger, Player challenged) {}
    
    /**
     * Appelé lorsque c'est le tour d'un joueur
     */
    default void onPlayerTurn(Game game, Player player) {}
}