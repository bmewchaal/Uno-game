package dgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface for handling action events in the custom GUI layer.
 * Étend ActionListener pour assurer la compatibilité avec les composants Swing.
 */
public interface DActionListener extends ActionListener {
    
    /**
     * Invoked when an action occurs.
     * 
     * @param event the event to be processed
     */
    void actionPerformed(DActionEvent event);
    
    /**
     * Implémentation par défaut de la méthode ActionListener.actionPerformed
     * qui convertit l'ActionEvent en DActionEvent pour maintenir la compatibilité.
     */
    @Override
    default void actionPerformed(ActionEvent e) {
        // Convertir l'ActionEvent en DActionEvent
        DActionEvent dEvent = new DActionEvent(e.getSource(), e.getID(), e.getActionCommand());
        // Appeler notre méthode avec le DActionEvent
        actionPerformed(dEvent);
    }
}
