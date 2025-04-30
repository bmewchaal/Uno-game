package dgui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Adaptateur pour les événements de souris 
 * Permet de gérer facilement les événements de souris dans la couche DGUI
 */
public class DMouseAdapter extends MouseAdapter {
    
    /**
     * Invoqué lorsque le bouton de la souris a été cliqué sur un composant
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque le bouton de la souris a été enfoncé sur un composant
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque le bouton de la souris a été relâché sur un composant
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque la souris entre dans les limites d'un composant
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque la souris sort des limites d'un composant
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque la souris a été déplacée sur un composant avec un bouton enfoncé
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // Comportement par défaut
    }

    /**
     * Invoqué lorsque la souris a été déplacée sur un composant sans bouton enfoncé
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Comportement par défaut
    }
}