package uno.core;

/**
 * Adaptateur pour les événements du jeu UNO
 * Implémente GameEventListener avec des méthodes vides par défaut
 */
public class GameEventAdapter implements GameEventListener {
    
    /**
     * Appelé lorsqu'une carte est jouée
     */
    @Override
    public void onCardPlayed(Player player, Card card) {
        // Implémentation par défaut ne fait rien
    }
    
    /**
     * Appelé lorsqu'une carte est piochée
     */
    @Override
    public void onCardDrawn(Player player, Card card) {
        // Implémentation par défaut ne fait rien
    }
    
    /**
     * Appelé lorsqu'un joueur appelle UNO
     */
    @Override
    public void onUnoCall(Player player) {
        // Implémentation par défaut ne fait rien
    }
    
    /**
     * Appelé lorsque le tour change
     */
    @Override
    public void onTurnChanged(Player player) {
        // Implémentation par défaut ne fait rien
    }
    
    /**
     * Appelé lorsque le jeu se termine
     */
    @Override
    public void onGameEnded(Player winner) {
        // Implémentation par défaut ne fait rien
    }
    
    /**
     * Appelé lorsque le jeu démarre
     */
    @Override
    public void onGameStarted() {
        // Implémentation par défaut ne fait rien
    }
    
    // Les autres méthodes ont déjà une implémentation par défaut via l'interface
}