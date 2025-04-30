package dgui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;

/**
 * Classe utilitaire qui facilite l'ajout d'écouteurs d'événements aux composants DGUI
 * Permet d'éviter d'accéder directement au composant Swing sous-jacent
 */
public class DComponentAdapter {
    
    /**
     * Ajoute un écouteur de souris à un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de souris à ajouter
     */
    public static void addMouseListener(DComponent component, MouseListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().addMouseListener(listener);
        }
    }
    
    /**
     * Ajoute un écouteur de mouvement de souris à un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de mouvement de souris à ajouter
     */
    public static void addMouseMotionListener(DComponent component, MouseMotionListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().addMouseMotionListener(listener);
        }
    }
    
    /**
     * Ajoute un écouteur de clavier à un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de clavier à ajouter
     */
    public static void addKeyListener(DComponent component, KeyListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().addKeyListener(listener);
        }
    }
    
    /**
     * Retire un écouteur de souris d'un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de souris à retirer
     */
    public static void removeMouseListener(DComponent component, MouseListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().removeMouseListener(listener);
        }
    }
    
    /**
     * Retire un écouteur de mouvement de souris d'un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de mouvement de souris à retirer
     */
    public static void removeMouseMotionListener(DComponent component, MouseMotionListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().removeMouseMotionListener(listener);
        }
    }
    
    /**
     * Retire un écouteur de clavier d'un composant DGUI
     * 
     * @param component Le composant DGUI
     * @param listener L'écouteur de clavier à retirer
     */
    public static void removeKeyListener(DComponent component, KeyListener listener) {
        if (component != null && component.getComponent() != null) {
            component.getComponent().removeKeyListener(listener);
        }
    }
}