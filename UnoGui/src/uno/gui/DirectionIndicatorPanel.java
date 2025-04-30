package uno.gui;

import dgui.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.BorderLayout;

/**
 * A panel that shows the current direction of play with an animated indicator.
 */
public class DirectionIndicatorPanel extends DPanel {
    private boolean clockwise = true;
    private float animationProgress = 0.0f;
    private boolean animating = false;
    private javax.swing.Timer animationTimer;
    
    // Create a custom panel for drawing
    private class DirectionPanel extends javax.swing.JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintIndicator((Graphics2D) g);
        }
    }
    
    /**
     * Create a new direction indicator panel.
     */
    public DirectionIndicatorPanel() {
        super(new BorderLayout());
        
        // Create the custom panel and add it
        javax.swing.JPanel drawingPanel = new DirectionPanel();
        this.component = drawingPanel;
        
        setPreferredSize(new Dimension(100, 100));
        
        // Initialize the animation timer
        animationTimer = new javax.swing.Timer(16, e -> {
            if (animating) {
                // Increment the animation progress
                animationProgress += 0.05f;
                
                // Check if the animation is complete
                if (animationProgress >= 1.0f) {
                    animationProgress = 0.0f;
                    animating = false;
                    ((javax.swing.Timer) e.getSource()).stop();
                }
                
                // Repaint to show the animation
                component.repaint();
            }
        });
    }
    
    /**
     * Set the direction of play.
     * 
     * @param clockwise true for clockwise, false for counter-clockwise
     * @param animate whether to animate the change
     */
    public void setDirection(boolean clockwise, boolean animate) {
        // Only animate if the direction is actually changing
        if (this.clockwise != clockwise) {
            this.clockwise = clockwise;
            
            if (animate) {
                // Start the animation
                animationProgress = 0.0f;
                animating = true;
                animationTimer.start();
            } else {
                // Just repaint with the new direction
                component.repaint();
            }
        }
    }
    
    /**
     * Check if the direction is clockwise.
     * 
     * @return true if clockwise, false if counter-clockwise
     */
    public boolean isClockwise() {
        return clockwise;
    }
    
    /**
     * Paint the direction indicator.
     * 
     * @param g2d the graphics context
     */
    private void paintIndicator(Graphics2D g2d) {
        int width = component.getWidth();
        int height = component.getHeight();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the circle
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillOval(10, 10, width - 20, height - 20);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(10, 10, width - 20, height - 20);
        
        // Calculate the center and radius
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2 - 15;
        
        // Draw the arrows
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        
        // Calculate the start and end angles based on animation
        float startAngle, endAngle;
        
        if (animating) {
            // When animating, we swap from one direction to the other
            // by gradually shrinking the current arrows and growing the new ones
            if (clockwise) {
                // Transitioning to clockwise
                startAngle = -90;
                endAngle = startAngle + 270 * animationProgress;
            } else {
                // Transitioning to counter-clockwise
                startAngle = 0;
                endAngle = startAngle - 270 * animationProgress;
            }
        } else {
            // When not animating, we show full arrows in the current direction
            if (clockwise) {
                startAngle = -90;
                endAngle = 180;
            } else {
                startAngle = 90;
                endAngle = -180;
            }
        }
        
        // Draw the arc
        g2d.drawArc(
            centerX - radius, 
            centerY - radius, 
            radius * 2, 
            radius * 2, 
            (int) startAngle, 
            (int) (endAngle - startAngle)
        );
        
        // Draw the arrowhead
        double arrowAngle = Math.toRadians(endAngle);
        int arrowX = centerX + (int) (radius * Math.cos(arrowAngle));
        int arrowY = centerY + (int) (radius * Math.sin(arrowAngle));
        
        // Calculate the direction of the arrowhead
        double arrowDirAngle = arrowAngle + (clockwise ? -Math.PI/2 : Math.PI/2);
        
        // Draw the arrowhead lines
        int arrowSize = 8;
        g2d.drawLine(
            arrowX, 
            arrowY, 
            arrowX + (int) (arrowSize * Math.cos(arrowDirAngle + Math.PI/4)), 
            arrowY + (int) (arrowSize * Math.sin(arrowDirAngle + Math.PI/4))
        );
        
        g2d.drawLine(
            arrowX, 
            arrowY, 
            arrowX + (int) (arrowSize * Math.cos(arrowDirAngle - Math.PI/4)), 
            arrowY + (int) (arrowSize * Math.sin(arrowDirAngle - Math.PI/4))
        );
    }
}