package dgui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Custom frame component wrapping JFrame.
 */
public class DFrame extends DComponent {
    private JFrame frame;
    
    /**
     * Create a new DFrame with default title
     */
    public DFrame() {
        this("DFrame");
    }
    
    /**
     * Create a new DFrame with the specified title
     */
    public DFrame(String title) {
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.component = new JPanel(); // Create a component for the parent class
        frame.add((JPanel) super.component, BorderLayout.CENTER);
    }
    
    /**
     * Set the title of the frame
     */
    public void setTitle(String title) {
        frame.setTitle(title);
    }
    
    /**
     * Get the content pane of the frame
     */
    public java.awt.Container getContentPane() {
        return frame.getContentPane();
    }
    
    /**
     * Get the title of the frame
     */
    public String getTitle() {
        return frame.getTitle();
    }
    
    /**
     * Set the size of the frame
     */
    @Override
    public void setSize(int width, int height) {
        frame.setSize(width, height);
        frame.setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Get the size of the frame
     */
    @Override
    public Dimension getSize() {
        return frame.getSize();
    }
    
    /**
     * Set the visibility of the frame
     */
    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
    
    /**
     * Add a component to the frame
     */
    public void add(DComponent component) {
        frame.add(component.getComponent());
    }
    
    /**
     * Add a component to the frame with the specified layout constraint
     */
    public void add(DComponent component, Object constraints) {
        frame.add(component.getComponent(), constraints);
    }
    
    /**
     * Pack the frame to fit its contents
     */
    public void pack() {
        frame.pack();
    }
    
    /**
     * Set whether the frame is resizable
     */
    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }
    
    /**
     * Center the frame on the screen
     */
    public void centerOnScreen() {
        frame.setLocationRelativeTo(null);
    }
    
    /**
     * Set the default close operation
     */
    public void setDefaultCloseOperation(int operation) {
        frame.setDefaultCloseOperation(operation);
    }
    
    /**
     * Get the underlying JFrame
     */
    public JFrame getFrame() {
        return frame;
    }
}
