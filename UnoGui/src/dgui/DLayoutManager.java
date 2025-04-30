package dgui;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

/**
 * Base class for custom layout managers.
 */
public abstract class DLayoutManager implements LayoutManager {
    
    /**
     * Create a new DLayoutManager
     */
    public DLayoutManager() {
        // Default constructor
    }
    
    /**
     * Adds the specified component with the specified name to the layout.
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Default implementation does nothing
    }
    
    /**
     * Removes the specified component from the layout.
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        // Default implementation does nothing
    }
    
    /**
     * Returns the preferred dimensions for this layout given the components in the specified container.
     */
    @Override
    public abstract Dimension preferredLayoutSize(Container parent);
    
    /**
     * Returns the minimum dimensions for this layout given the components in the specified container.
     */
    @Override
    public abstract Dimension minimumLayoutSize(Container parent);
    
    /**
     * Lays out the container in the specified container.
     */
    @Override
    public abstract void layoutContainer(Container parent);
}
