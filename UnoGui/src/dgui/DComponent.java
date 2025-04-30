package dgui;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import dgui.themes.DTheme;

/**
 * Base component class for the custom GUI layer.
 * All custom components inherit from this class.
 */
public class DComponent {
    protected JComponent component;
    protected DTheme theme = DTheme.DEFAULT;
    protected BufferedImage backgroundImage;
    protected boolean useCustomBackground = false;
    
    public DComponent() {
        // Default constructor
    }
    
    public DComponent(JComponent component) {
        this.component = component;
    }
    
    /**
     * Définit le thème du composant
     */
    public void setTheme(DTheme theme) {
        this.theme = theme;
        applyTheme();
    }
    
    /**
     * Applique le thème au composant
     */
    protected void applyTheme() {
        if (component != null) {
            component.setBackground(theme.getBackgroundColor());
            component.setForeground(theme.getTextColor());
            component.setFont(theme.getBodyFont());
        }
    }
    
    /**
     * Définit une image de fond personnalisée
     */
    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        this.useCustomBackground = (image != null);
        if (component != null) {
            component.repaint();
        }
    }
    
    /**
     * Définit une texture de fond en bois
     */
    public void setWoodBackground(Color baseColor) {
        DImageManager imageManager = DImageManager.getInstance();
        if (component != null) {
            int width = Math.max(100, component.getWidth());
            int height = Math.max(100, component.getHeight());
            BufferedImage woodTexture = imageManager.createWoodTexture(width, height, baseColor);
            setBackgroundImage(woodTexture);
        }
    }
    
    /**
     * Get the underlying Swing component
     */
    public JComponent getComponent() {
        return component;
    }
    
    /**
     * Set the size of the component
     */
    public void setSize(int width, int height) {
        component.setSize(width, height);
        component.setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Get the size of the component
     */
    public Dimension getSize() {
        return component.getSize();
    }
    
    /**
     * Get the width of the component
     */
    public int getWidth() {
        return component.getWidth();
    }
    
    /**
     * Get the height of the component
     */
    public int getHeight() {
        return component.getHeight();
    }
    
    /**
     * Set the visibility of the component
     */
    public void setVisible(boolean visible) {
        component.setVisible(visible);
    }
    
    /**
     * Check if the component is visible
     */
    public boolean isVisible() {
        return component.isVisible();
    }
    
    /**
     * Set the background color of the component
     */
    public void setBackground(Color color) {
        component.setBackground(color);
    }
    
    /**
     * Get the background color of the component
     */
    public Color getBackground() {
        return component.getBackground();
    }
    
    /**
     * Set the foreground color of the component
     */
    public void setForeground(Color color) {
        component.setForeground(color);
    }
    
    /**
     * Get the foreground color of the component
     */
    public Color getForeground() {
        return component.getForeground();
    }
    
    /**
     * Enable or disable the component
     */
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }
    
    /**
     * Check if the component is enabled
     */
    public boolean isEnabled() {
        return component.isEnabled();
    }
    
    /**
     * Set the font of the component
     */
    public void setFont(Font font) {
        component.setFont(font);
    }
    
    /**
     * Get the font of the component
     */
    public Font getFont() {
        return component.getFont();
    }
    
    /**
     * Set the tool tip text of the component
     */
    public void setToolTipText(String text) {
        component.setToolTipText(text);
    }
    
    /**
     * Get the tool tip text of the component
     */
    public String getToolTipText() {
        return component.getToolTipText();
    }
    
    /**
     * Set the preferred size of the component
     */
    public void setPreferredSize(Dimension dimension) {
        component.setPreferredSize(dimension);
    }
    
    /**
     * Get the preferred size of the component
     */
    public Dimension getPreferredSize() {
        return component.getPreferredSize();
    }
    
    /**
     * Set the minimum size of the component
     */
    public void setMinimumSize(Dimension dimension) {
        component.setMinimumSize(dimension);
    }
    
    /**
     * Get the minimum size of the component
     */
    public Dimension getMinimumSize() {
        return component.getMinimumSize();
    }
    
    /**
     * Set the maximum size of the component
     */
    public void setMaximumSize(Dimension dimension) {
        component.setMaximumSize(dimension);
    }
    
    /**
     * Get the maximum size of the component
     */
    public Dimension getMaximumSize() {
        return component.getMaximumSize();
    }
    
    /**
     * Revalidate the component
     */
    public void revalidate() {
        component.revalidate();
    }
    
    /**
     * Repaint the component
     */
    public void repaint() {
        component.repaint();
    }
    
    /**
     * Set the position of the component
     */
    public void setPosition(int x, int y) {
        component.setLocation(x, y);
    }
    
    /**
     * Get the x position of the component
     */
    public int getX() {
        return component.getX();
    }
    
    /**
     * Get the y position of the component
     */
    public int getY() {
        return component.getY();
    }
    
    /**
     * Set the alpha transparency of the component
     * @param alpha Value between 0.0 (transparent) and 1.0 (opaque)
     */
    public void setAlpha(float alpha) {
        if (component != null) {
            component.setOpaque(alpha >= 0.99f);
            float currentAlpha = Math.max(0.0f, Math.min(1.0f, alpha));
            Color current = component.getBackground();
            component.setBackground(new Color(
                current.getRed(),
                current.getGreen(),
                current.getBlue(),
                (int)(255 * currentAlpha)
            ));
            component.repaint();
        }
    }
    
    /**
     * Get the alpha transparency of the component
     */
    public float getAlpha() {
        return component != null ? component.getBackground().getAlpha() / 255f : 1.0f;
    }
}
