package dgui;

import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import dgui.themes.DTheme;

/**
 * Custom panel component wrapping JPanel.
 */
public class DPanel extends DComponent {
    
    /**
     * Create a new DPanel with the default layout
     */
    public DPanel() {
        this(null);
    }
    
    /**
     * Create a new DPanel with the specified layout
     */
    public DPanel(LayoutManager layout) {
        super(createCustomPanel(layout));
        initPanel();
        applyTheme();
    }
    
    /**
     * Crée un JPanel personnalisé qui peut afficher des images de fond
     */
    private static JPanel createCustomPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout) {
            private DPanel parent;
            
            @Override
            protected void paintComponent(Graphics g) {
                if (parent != null && parent.useCustomBackground && parent.backgroundImage != null) {
                    // Dessiner l'image de fond personnalisée
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(parent.backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Dessiner le fond standard
                    super.paintComponent(g);
                }
            }
            
            public void setParent(DPanel parent) {
                this.parent = parent;
            }
        };
        
        return panel;
    }
    
    @Override
    public void setBackgroundImage(BufferedImage image) {
        super.setBackgroundImage(image);
        ((JPanel)component).setOpaque(!useCustomBackground);
    }
    
    /**
     * Initialise le panel après sa création
     */
    private void initPanel() {
        ((JPanel)component).setOpaque(true);
        // Attache this comme parent au JPanel personnalisé
        try {
            ((JPanel)component).getClass().getMethod("setParent", DPanel.class).invoke(component, this);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du panel: " + e.getMessage());
        }
    }
    
    /**
     * Add a component to the panel
     */
    public void add(DComponent component) {
        ((JPanel) this.component).add(component.getComponent());
    }
    
    /**
     * Add a JPanel to the panel
     */
    public void add(JPanel panel) {
        ((JPanel) this.component).add(panel);
    }
    
    /**
     * Add a standard Component to the panel (for compatibility with Swing components)
     */
    public void add(java.awt.Component component) {
        ((JPanel) this.component).add(component);
    }
    
    /**
     * Add a component to the panel with the specified constraints
     */
    public void add(DComponent component, Object constraints) {
        ((JPanel) this.component).add(component.getComponent(), constraints);
    }
    
    /**
     * Add a JPanel to the panel with the specified constraints
     */
    public void add(JPanel panel, Object constraints) {
        ((JPanel) this.component).add(panel, constraints);
    }
    
    /**
     * Add a standard Component to the panel with the specified constraints
     */
    public void add(java.awt.Component component, Object constraints) {
        ((JPanel) this.component).add(component, constraints);
    }
    
    /**
     * Remove a component from the panel
     */
    public void remove(DComponent component) {
        ((JPanel) this.component).remove(component.getComponent());
    }
    
    /**
     * Remove all components from the panel
     */
    public void removeAll() {
        ((JPanel) this.component).removeAll();
    }
    
    /**
     * Set the layout manager for the panel
     */
    public void setLayout(LayoutManager layout) {
        ((JPanel) this.component).setLayout(layout);
    }
    
    /**
     * Get the layout manager for the panel
     */
    public LayoutManager getLayout() {
        return ((JPanel) this.component).getLayout();
    }
    
    /**
     * Set the border for the panel
     */
    public void setBorder(Border border) {
        ((JPanel) this.component).setBorder(border);
    }
    
    /**
     * Set the opacity of the panel
     */
    public void setOpaque(boolean isOpaque) {
        ((JPanel) this.component).setOpaque(isOpaque);
    }
    
    /**
     * Check if the panel is opaque
     */
    public boolean isOpaque() {
        return ((JPanel) this.component).isOpaque();
    }
    
    /**
     * Revalidate the panel
     */
    public void revalidate() {
        ((JPanel) this.component).revalidate();
    }
    
    /**
     * Repaint the panel
     */
    public void repaint() {
        ((JPanel) this.component).repaint();
    }
    
    /**
     * Get the underlying JPanel
     */
    public JPanel getPanel() {
        return (JPanel) component;
    }
}
