package uno.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import dgui.DPanel;
import dgui.DImageManager;
import dgui.DComponentAdapter;

/**
 * Enhanced wood panel for UNO game with improved rendering
 */
public class DWoodPanel extends DPanel {
    // Wood color constants
    public static final Color LIGHT_WOOD = new Color(210, 180, 140);
    public static final Color DARK_WOOD = new Color(120, 80, 30);
    public static final Color RED_WOOD = new Color(170, 80, 60);
    
    // Texture image
    private BufferedImage woodTexture;
    private Color woodColor = LIGHT_WOOD;
    
    /**
     * Create a new wood panel
     */
    public DWoodPanel() {
        super();
        init();
    }
    
    /**
     * Create a new wood panel with the specified layout
     */
    public DWoodPanel(LayoutManager layout) {
        super(layout);
        init();
    }
    
    /**
     * Shared initialization
     */
    private void init() {
        setOpaque(true);
        
        // Add a component listener to detect size changes
        getComponent().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Regenerate the texture when the size changes significantly
                int width = getWidth();
                int height = getHeight();
                
                if (woodTexture == null || 
                    Math.abs(width - woodTexture.getWidth()) > 50 ||
                    Math.abs(height - woodTexture.getHeight()) > 50) {
                    createWoodTexture(woodColor);
                }
            }
        });
        
        // Create initial texture
        createWoodTexture(LIGHT_WOOD);
    }
    
    /**
     * Create a wood texture with the specified color
     */
    public void createWoodTexture(Color baseColor) {
        this.woodColor = baseColor;
        
        // Get current dimensions
        int width = Math.max(100, getWidth());
        int height = Math.max(100, getHeight());
        
        // If dimensions are too small, use default size
        if (width <= 0) width = 800;
        if (height <= 0) height = 600;
        
        // Use DImageManager to create texture
        DImageManager imageManager = DImageManager.getInstance();
        woodTexture = imageManager.createWoodTexture(width, height, baseColor);
        
        // Apply the texture as background
        setBackgroundImage(woodTexture);
    }
    
    /**
     * Set border with specified insets
     */
    public void setBorder(int top, int left, int bottom, int right) {
        getComponent().setBorder(javax.swing.BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
    
    /**
     * Paint component with wood texture
     */
    @Override
    public void repaint() {
        super.repaint();
        
        // Ensure texture is created if needed
        if (woodTexture == null) {
            createWoodTexture(woodColor);
        }
    }
}