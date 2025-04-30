package dgui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Politique de traversée de focus personnalisée pour les composants DGUI
 * Cette classe permet d'éviter les ClassCastException dans le système de focus traversal de Swing
 * lorsqu'on utilise un mélange de composants DGUI et Swing
 */
public class DFocusTraversalPolicy extends FocusTraversalPolicy {
    private final List<Component> order = new ArrayList<>();
    
    /**
     * Ajoute un composant à l'ordre de traversée
     */
    public void addComponent(Component comp) {
        if (comp != null && !order.contains(comp)) {
            order.add(comp);
        }
    }
    
    /**
     * Ajoute un composant DGUI à l'ordre de traversée
     */
    public void addComponent(DComponent comp) {
        if (comp != null && comp.getComponent() != null) {
            addComponent(comp.getComponent());
        }
    }
    
    /**
     * Retire un composant de l'ordre de traversée
     */
    public void removeComponent(Component comp) {
        order.remove(comp);
    }
    
    /**
     * Retire un composant DGUI de l'ordre de traversée
     */
    public void removeComponent(DComponent comp) {
        if (comp != null && comp.getComponent() != null) {
            removeComponent(comp.getComponent());
        }
    }
    
    /**
     * Vide l'ordre de traversée
     */
    public void clear() {
        order.clear();
    }
    
    @Override
    public Component getComponentAfter(Container container, Component component) {
        int idx = order.indexOf(component);
        if (idx == -1 || idx >= order.size() - 1) {
            return order.isEmpty() ? null : order.get(0);
        } else {
            return order.get(idx + 1);
        }
    }
    
    @Override
    public Component getComponentBefore(Container container, Component component) {
        int idx = order.indexOf(component);
        if (idx <= 0) {
            return order.isEmpty() ? null : order.get(order.size() - 1);
        } else {
            return order.get(idx - 1);
        }
    }
    
    @Override
    public Component getFirstComponent(Container container) {
        return order.isEmpty() ? null : order.get(0);
    }
    
    @Override
    public Component getLastComponent(Container container) {
        return order.isEmpty() ? null : order.get(order.size() - 1);
    }
    
    @Override
    public Component getDefaultComponent(Container container) {
        return getFirstComponent(container);
    }
}