package uno.gui;

import dgui.*;
import uno.core.*;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Dialog for choosing a color when playing a Wild card.
 */
public class DColorChooserDialog extends DPanel {
    private CardColor selectedColor = null;
    private boolean colorSelected = false;
    
    private DButton redButton;
    private DButton blueButton;
    private DButton greenButton;
    private DButton yellowButton;
    
    /**
     * Create a new color chooser dialog
     */
    public DColorChooserDialog() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(200, 180));
        
        // Create a title label
        DLabel titleLabel = new DLabel("Choose a color:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setTextAlignmentCenter();
        
        // Create color buttons panel
        DPanel buttonsPanel = new DPanel(new GridLayout(2, 2, 10, 10));
        
        // Create color buttons
        redButton = new DButton("Red");
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        redButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        blueButton = new DButton("Blue");
        blueButton.setBackground(Color.BLUE);
        blueButton.setForeground(Color.WHITE);
        blueButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        greenButton = new DButton("Green");
        greenButton.setBackground(Color.GREEN);
        greenButton.setForeground(Color.BLACK);
        greenButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        yellowButton = new DButton("Yellow");
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setForeground(Color.BLACK);
        yellowButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add action listeners to buttons
        redButton.addActionListener(e -> {
            selectedColor = CardColor.RED;
            colorSelected = true;
            notifyListeners();
        });
        
        blueButton.addActionListener(e -> {
            selectedColor = CardColor.BLUE;
            colorSelected = true;
            notifyListeners();
        });
        
        greenButton.addActionListener(e -> {
            selectedColor = CardColor.GREEN;
            colorSelected = true;
            notifyListeners();
        });
        
        yellowButton.addActionListener(e -> {
            selectedColor = CardColor.YELLOW;
            colorSelected = true;
            notifyListeners();
        });
        
        // Add buttons to the panel
        buttonsPanel.add(redButton);
        buttonsPanel.add(blueButton);
        buttonsPanel.add(greenButton);
        buttonsPanel.add(yellowButton);
        
        // Add components to the dialog
        add(titleLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        
        // Add padding
        DPanel paddingPanel = new DPanel();
        paddingPanel.setPreferredSize(new Dimension(10, 10));
        add(paddingPanel, BorderLayout.SOUTH);
        
        // Set background
        setBackground(new Color(50, 50, 50));
        titleLabel.setForeground(Color.WHITE);
    }
    
    /**
     * Get the selected color
     * 
     * @return the selected color, or null if no color has been selected
     */
    public CardColor getSelectedColor() {
        return selectedColor;
    }
    
    /**
     * Check if a color has been selected
     * 
     * @return true if a color has been selected, false otherwise
     */
    public boolean isColorSelected() {
        return colorSelected;
    }
    
    /**
     * Reset the dialog
     */
    public void reset() {
        selectedColor = null;
        colorSelected = false;
    }
    
    // List of listeners to notify when a color is selected
    private final java.util.List<ColorSelectedListener> listeners = new java.util.ArrayList<>();
    
    /**
     * Add a listener to be notified when a color is selected
     * 
     * @param listener the listener to add
     */
    public void addColorSelectedListener(ColorSelectedListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove a listener
     * 
     * @param listener the listener to remove
     */
    public void removeColorSelectedListener(ColorSelectedListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners that a color has been selected
     */
    private void notifyListeners() {
        for (ColorSelectedListener listener : listeners) {
            listener.colorSelected(selectedColor);
        }
    }
    
    /**
     * Interface for objects that want to be notified when a color is selected
     */
    public interface ColorSelectedListener {
        /**
         * Called when a color is selected
         * 
         * @param color the selected color
         */
        void colorSelected(CardColor color);
    }
}