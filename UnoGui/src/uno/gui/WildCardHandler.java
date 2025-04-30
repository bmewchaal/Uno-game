package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.util.concurrent.CompletableFuture;

/**
 * Handler for managing wild card selection.
 */
public class WildCardHandler {
    private final DColorChooserDialog dialog;
    private Container parent;
    
    /**
     * Create a new wild card handler with the specified parent container.
     * 
     * @param parent the parent container where the dialog will be shown
     */
    public WildCardHandler(Container parent) {
        this.parent = parent;
        dialog = new DColorChooserDialog();
    }
    
    /**
     * Show the color chooser dialog and return a CompletableFuture that will be completed
     * when a color is selected.
     * 
     * @return a CompletableFuture that will be completed with the selected color
     */
    public CompletableFuture<CardColor> showColorChooser() {
        CompletableFuture<CardColor> future = new CompletableFuture<>();
        
        // Store the dialog's original parent component to restore it later
        Component originalParent = dialog.getComponent().getParent();
        
        // Set up listener to complete the future when a color is selected
        dialog.addColorSelectedListener(color -> {
            future.complete(color);
            
            // Remove the dialog from the parent after a color is selected
            if (dialog.getComponent().getParent() != null) {
                dialog.getComponent().getParent().remove(dialog.getComponent());
                parent.revalidate();
                parent.repaint();
            }
        });
        
        // Reset the dialog
        dialog.reset();
        
        // Add the dialog to the center of the parent container
        parent.add(dialog.getComponent(), BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
        
        return future;
    }
    
    /**
     * Hide the color chooser dialog.
     */
    public void hideColorChooser() {
        if (dialog.getComponent().getParent() != null) {
            dialog.getComponent().getParent().remove(dialog.getComponent());
            parent.revalidate();
            parent.repaint();
        }
    }
}