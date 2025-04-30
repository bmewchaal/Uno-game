package dgui;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

/**
 * Custom label component wrapping JLabel.
 */
public class DLabel extends DComponent {
    
    /**
     * Create a new DLabel with no text
     */
    public DLabel() {
        this("");
    }
    
    /**
     * Create a new DLabel with the specified text
     */
    public DLabel(String text) {
        super(new JLabel(text));
    }
    
    /**
     * Create a new DLabel with the specified icon
     */
    public DLabel(ImageIcon icon) {
        super(new JLabel(icon));
    }
    
    /**
     * Create a new DLabel with the specified text and icon
     */
    public DLabel(String text, ImageIcon icon) {
        super(new JLabel(text, icon, SwingConstants.LEFT));
    }
    
    /**
     * Create a new DLabel with the specified text and horizontal alignment
     */
    public DLabel(String text, int horizontalAlignment) {
        super(new JLabel(text, horizontalAlignment));
    }
    
    /**
     * Create a new DLabel with the specified text, icon, and horizontal alignment
     */
    public DLabel(String text, ImageIcon icon, int horizontalAlignment) {
        super(new JLabel(text, icon, horizontalAlignment));
    }
    
    /**
     * Set the text of the label
     */
    public void setText(String text) {
        ((JLabel) component).setText(text);
    }
    
    /**
     * Get the text of the label
     */
    public String getText() {
        return ((JLabel) component).getText();
    }
    
    /**
     * Set the icon of the label
     */
    public void setIcon(ImageIcon icon) {
        ((JLabel) component).setIcon(icon);
    }
    
    /**
     * Get the icon of the label
     */
    public ImageIcon getIcon() {
        return (ImageIcon) ((JLabel) component).getIcon();
    }
    
    /**
     * Set the horizontal alignment of the label
     */
    public void setHorizontalAlignment(int alignment) {
        ((JLabel) component).setHorizontalAlignment(alignment);
    }
    
    /**
     * Get the horizontal alignment of the label
     */
    public int getHorizontalAlignment() {
        return ((JLabel) component).getHorizontalAlignment();
    }
    
    /**
     * Set the vertical alignment of the label
     */
    public void setVerticalAlignment(int alignment) {
        ((JLabel) component).setVerticalAlignment(alignment);
    }
    
    /**
     * Get the vertical alignment of the label
     */
    public int getVerticalAlignment() {
        return ((JLabel) component).getVerticalAlignment();
    }
    
    /**
     * Set the text alignment to center
     */
    public void setTextAlignmentCenter() {
        ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    /**
     * Set the text alignment to right
     */
    public void setTextAlignmentRight() {
        ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
    }
    
    /**
     * Set the text alignment to left
     */
    public void setTextAlignmentLeft() {
        ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
    }
    
    /**
     * Get the underlying JLabel
     */
    public JLabel getLabel() {
        return (JLabel) component;
    }
}
