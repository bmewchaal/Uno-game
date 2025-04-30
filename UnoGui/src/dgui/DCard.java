package dgui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom component for displaying a UNO card.
 */
public class DCard extends DComponent {
    private Color cardColor;
    private String cardValue;
    private boolean faceUp = true;
    private boolean selected = false;
    
    // Card dimensions
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 150;
    private static final int ARC_SIZE = 20;
    
    /**
     * Create a new DCard with the specified color and value
     */
    public DCard(Color cardColor, String cardValue) {
        super(new CardPanel());
        this.cardColor = cardColor;
        this.cardValue = cardValue;
        
        ((CardPanel) component).setCard(this);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Get the card's color
     */
    public Color getCardColor() {
        return cardColor;
    }
    
    /**
     * Set the card's color
     */
    public void setCardColor(Color cardColor) {
        this.cardColor = cardColor;
        component.repaint();
    }
    
    /**
     * Get the card's value
     */
    public String getCardValue() {
        return cardValue;
    }
    
    /**
     * Set the card's value
     */
    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
        component.repaint();
    }
    
    /**
     * Check if the card is face up
     */
    public boolean isFaceUp() {
        return faceUp;
    }
    
    /**
     * Set whether the card is face up
     */
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        component.repaint();
    }
    
    /**
     * Check if the card is selected
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Set whether the card is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            // Move the card up slightly when selected
            component.setLocation(component.getX(), component.getY() - 20);
        } else {
            // Move the card back down when deselected
            component.setLocation(component.getX(), component.getY() + 20);
        }
        component.repaint();
    }
    
    /**
     * Animate the card moving to a destination point
     * 
     * @param destX the x-coordinate of the destination
     * @param destY the y-coordinate of the destination
     */
    public void animateToPosition(int destX, int destY) {
        final int startX = component.getX();
        final int startY = component.getY();
        final int distanceX = destX - startX;
        final int distanceY = destY - startY;
        final int steps = 20;
        
        // Create a timer to update the card position
        final javax.swing.Timer timer = new javax.swing.Timer(20, null);
        final int[] step = {0}; // Using array to have a mutable integer
        
        timer.addActionListener(e -> {
            if (step[0] >= steps) {
                // Animation complete
                timer.stop();
                component.setLocation(destX, destY);
            } else {
                // Calculate new position using easing function for smoother movement
                float progress = (float)step[0] / steps;
                // Ease out function: progress = 1 - (1 - progress)^2
                float easedProgress = 1 - (1 - progress) * (1 - progress);
                
                int newX = startX + (int)(distanceX * easedProgress);
                int newY = startY + (int)(distanceY * easedProgress);
                
                component.setLocation(newX, newY);
                step[0]++;
            }
        });
        
        timer.start();
    }
    
    /**
     * Animate the card being played
     * 
     * @param destX the x-coordinate of the destination
     * @param destY the y-coordinate of the destination
     */
    public void animatePlay(int destX, int destY) {
        // First move up slightly
        final int startX = component.getX();
        final int startY = component.getY();
        final int moveUpY = startY - 30;
        
        // Create a timer to update the card position
        final javax.swing.Timer timer = new javax.swing.Timer(20, null);
        final int[] phase = {0}; // 0 = move up, 1 = move to destination
        final int[] step = {0};
        final int steps = 15;
        
        timer.addActionListener(e -> {
            if (phase[0] == 0) {
                // Phase 1: Move up
                if (step[0] >= steps) {
                    // Phase 1 complete, start phase 2
                    phase[0] = 1;
                    step[0] = 0;
                } else {
                    float progress = (float)step[0] / steps;
                    int newY = startY - (int)(30 * progress);
                    component.setLocation(startX, newY);
                    step[0]++;
                }
            } else {
                // Phase 2: Move to destination
                if (step[0] >= steps) {
                    // Animation complete
                    timer.stop();
                    component.setLocation(destX, destY);
                } else {
                    float progress = (float)step[0] / steps;
                    int newX = startX + (int)((destX - startX) * progress);
                    int newY = moveUpY + (int)((destY - moveUpY) * progress);
                    component.setLocation(newX, newY);
                    step[0]++;
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Custom panel for drawing a UNO card
     */
    private static class CardPanel extends JPanel {
        private DCard card;
        
        public CardPanel() {
            setOpaque(false);
        }
        
        public void setCard(DCard card) {
            this.card = card;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Draw card back if face down
            if (!card.isFaceUp()) {
                drawCardBack(g2d, width, height);
                g2d.dispose();
                return;
            }
            
            // Draw card outline
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, ARC_SIZE, ARC_SIZE));
            
            // Draw card background
            g2d.setColor(card.getCardColor());
            g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, ARC_SIZE, ARC_SIZE));
            
            // Draw white oval in the center
            g2d.setColor(Color.WHITE);
            g2d.fillOval(width / 6, height / 6, width * 2 / 3, height * 2 / 3);
            
            // Draw card value or special symbol
            g2d.setColor(card.getCardColor());
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            
            String value = card.getCardValue();
            // Calculate the center position
            int textX = width / 2 - g2d.getFontMetrics().stringWidth(value) / 2;
            int textY = height / 2 + g2d.getFontMetrics().getHeight() / 4;
            
            // For special cards, draw distinctive symbols
            if (value.equals("SKIP")) {
                // Draw a circle with a diagonal line (prohibition symbol)
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(width/4, height/3, width/2, width/2);
                g2d.drawLine(width/4, height/3 + width/2, width/4 + width/2, height/3);
                
                // Draw smaller text below
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String skipText = "SKIP";
                int skipX = width / 2 - g2d.getFontMetrics().stringWidth(skipText) / 2;
                g2d.drawString(skipText, skipX, height * 3/4);
            } else if (value.equals("REVERSE")) {
                // Draw circular arrows
                g2d.setStroke(new BasicStroke(3));
                // Top arrow
                g2d.drawArc(width/4, height/3, width/2, width/3, 0, 180);
                g2d.drawLine(width/4, height/3 + width/6, width/4 - 5, height/3 + width/6 - 5);
                g2d.drawLine(width/4, height/3 + width/6, width/4 - 5, height/3 + width/6 + 5);
                
                // Bottom arrow
                g2d.drawArc(width/4, height/3 + width/12, width/2, width/3, 180, 180);
                g2d.drawLine(width*3/4, height/3 + width/6 + width/12, width*3/4 + 5, height/3 + width/6 + width/12 - 5);
                g2d.drawLine(width*3/4, height/3 + width/6 + width/12, width*3/4 + 5, height/3 + width/6 + width/12 + 5);
                
                // Draw smaller text below
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                String reverseText = "REVERSE";
                int revX = width / 2 - g2d.getFontMetrics().stringWidth(reverseText) / 2;
                g2d.drawString(reverseText, revX, height * 3/4);
            } else if (value.equals("DRAW2")) {
                // Draw +2 symbol
                g2d.setFont(new Font("Arial", Font.BOLD, 28));
                g2d.drawString("+2", width/2 - g2d.getFontMetrics().stringWidth("+2")/2, height/2);
            } else if (value.equals("WILD")) {
                // Draw four colored quadrants for wild card
                g2d.setColor(Color.RED);
                g2d.fillArc(width/3, height/3, width/3, width/3, 0, 90);
                g2d.setColor(Color.BLUE);
                g2d.fillArc(width/3, height/3, width/3, width/3, 90, 90);
                g2d.setColor(Color.GREEN);
                g2d.fillArc(width/3, height/3, width/3, width/3, 180, 90);
                g2d.setColor(Color.YELLOW);
                g2d.fillArc(width/3, height/3, width/3, width/3, 270, 90);
                
                // Draw border around the color wheel
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(width/3, height/3, width/3, width/3);
                
                // Draw text below
                g2d.setColor(card.getCardColor());
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String wildText = "WILD";
                int wildX = width / 2 - g2d.getFontMetrics().stringWidth(wildText) / 2;
                g2d.drawString(wildText, wildX, height * 3/4);
            } else if (value.equals("WILD4")) {
                // Draw +4 symbol
                g2d.setFont(new Font("Arial", Font.BOLD, 28));
                g2d.drawString("+4", width/2 - g2d.getFontMetrics().stringWidth("+4")/2, height/2 - 10);
                
                // Draw four colored small rectangles for wild card
                int rectSize = width/10;
                g2d.setColor(Color.RED);
                g2d.fillRect(width/2 - rectSize*2, height/2 + 10, rectSize, rectSize);
                g2d.setColor(Color.BLUE);
                g2d.fillRect(width/2 - rectSize, height/2 + 10, rectSize, rectSize);
                g2d.setColor(Color.GREEN);
                g2d.fillRect(width/2, height/2 + 10, rectSize, rectSize);
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(width/2 + rectSize, height/2 + 10, rectSize, rectSize);
            } else {
                // For number cards, just draw the value
                g2d.drawString(value, textX, textY);
            }
            
            // Draw card value in upper left and lower right corners
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(card.getCardValue(), 5, 15);
            g2d.drawString(card.getCardValue(), width - 15, height - 5);
            
            // Draw selection highlight
            if (card.isSelected()) {
                g2d.setColor(new Color(255, 255, 0, 100)); // Semi-transparent yellow
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, ARC_SIZE, ARC_SIZE));
            }
            
            g2d.dispose();
        }
        
        private void drawCardBack(Graphics2D g2d, int width, int height) {
            // Draw card outline
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, ARC_SIZE, ARC_SIZE));
            
            // Draw card back (dark blue background)
            g2d.setColor(new Color(0, 0, 128));
            g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, ARC_SIZE, ARC_SIZE));
            
            // Draw UNO logo
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            int textX = width / 2 - g2d.getFontMetrics().stringWidth("UNO") / 2;
            int textY = height / 2 + g2d.getFontMetrics().getHeight() / 4;
            g2d.drawString("UNO", textX, textY);
            
            // Draw diagonal lines pattern
            g2d.setColor(new Color(0, 0, 180));
            g2d.setStroke(new BasicStroke(2));
            for (int i = -height; i < width + height; i += 20) {
                g2d.drawLine(i, 0, i + height, height);
            }
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
}
