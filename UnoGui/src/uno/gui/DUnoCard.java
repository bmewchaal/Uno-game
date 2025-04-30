package uno.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.BasicStroke;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dgui.DComponent;
import dgui.DActionEvent;
import dgui.DActionListener;
import uno.core.Card;
import uno.core.CardColor;
import uno.core.CardValue;

/**
 * Enhanced UNO card component with improved visuals
 */
public class DUnoCard extends DComponent {
    private Card card;
    private boolean faceUp = true;
    private boolean isHovered = false;
    private boolean selected = false;
    private int cornerRadius = 12;
    private DActionListener clickListener;
    
    // Card dimensions
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    
    /**
     * Create a new UNO card
     */
    public DUnoCard(Card card) {
        super(createCardPanel());
        this.card = card;
        initCard();
    }
    
    /**
     * Create a custom JPanel for the card
     */
    private static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            private DUnoCard parent;
            
            @Override
            protected void paintComponent(Graphics g) {
                if (parent != null) {
                    parent.paintCard(g, getWidth(), getHeight());
                } else {
                    super.paintComponent(g);
                }
            }
            
            public void setParent(DUnoCard parent) {
                this.parent = parent;
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        return panel;
    }
    
    /**
     * Initialize the card
     */
    private void initCard() {
        // Set default size
        setSize(CARD_WIDTH, CARD_HEIGHT);
        
        // Add mouse listeners
        ((JPanel)component).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                component.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                component.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.actionPerformed(new DActionEvent(DUnoCard.this));
                }
            }
        });
        
        // Set parent reference
        try {
            ((JPanel)component).getClass().getMethod("setParent", DUnoCard.class).invoke(component, this);
        } catch (Exception e) {
            System.err.println("Error initializing card: " + e.getMessage());
        }
    }
    
    /**
     * Paint the card with enhanced visuals
     */
    public void paintCard(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw drop shadow
        drawCardShadow(g2d, width, height);
        
        // Draw card background (black border)
        g2d.setColor(Color.BLACK);
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius + 2, cornerRadius + 2));
        
        if (faceUp) {
            // Get card color
            Color cardColor = getCardAwtColor(card.getColor());
            
            // Draw gradient background
            drawCardBackground(g2d, width, height, cardColor);
            
            // Draw white oval
            drawCardOval(g2d, width, height);
            
            // Draw symbol based on card value
            drawCardSymbol(g2d, width, height);
            
            // Draw corners
            drawCardCorners(g2d, width, height);
        } else {
            // Draw card back
            drawCardBack(g2d, width, height);
        }
        
        // Draw hover and selection effects
        drawCardEffects(g2d, width, height);
        
        g2d.dispose();
    }
    
    /**
     * Draw card shadow
     */
    private void drawCardShadow(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 40));
        for (int i = 0; i < 3; i++) {
            g2d.fill(new RoundRectangle2D.Float(i + 2, i + 2, width, height, cornerRadius + 2, cornerRadius + 2));
        }
    }
    
    /**
     * Draw card background with gradient
     */
    private void drawCardBackground(Graphics2D g2d, int width, int height, Color cardColor) {
        // Create slightly darker color for gradient
        Color darkerColor = new Color(
            Math.max(0, cardColor.getRed() - 30),
            Math.max(0, cardColor.getGreen() - 30),
            Math.max(0, cardColor.getBlue() - 30)
        );
        
        // Create gradient paint
        LinearGradientPaint gradient = new LinearGradientPaint(
            new Point2D.Float(0, 0),
            new Point2D.Float(width, height),
            new float[] {0.0f, 1.0f},
            new Color[] {cardColor, darkerColor}
        );
        
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, cornerRadius, cornerRadius));
    }
    
    /**
     * Draw card oval
     */
    private void drawCardOval(Graphics2D g2d, int width, int height) {
        // Draw white oval with slight gradient
        RadialGradientPaint ovalGradient = new RadialGradientPaint(
            new Point2D.Float(width / 2, height / 2),
            width / 2,
            new float[] {0.0f, 1.0f},
            new Color[] {Color.WHITE, new Color(240, 240, 240)}
        );
        
        g2d.setPaint(ovalGradient);
        g2d.fill(new Ellipse2D.Float(10, 20, width - 20, height - 40));
        
        // Add subtle border to oval
        g2d.setColor(new Color(200, 200, 200));
        g2d.draw(new Ellipse2D.Float(10, 20, width - 20, height - 40));
    }
    
    /**
     * Draw card symbol
     */
    private void drawCardSymbol(Graphics2D g2d, int width, int height) {
        // Get symbol and card color
        String symbol = getCardSymbol();
        Color cardColor = getCardAwtColor(card.getColor());
        
        // Draw value or special symbol
        if (card.getValue() == CardValue.SKIP) {
            drawSkipSymbol(g2d, width, height, cardColor);
        } else if (card.getValue() == CardValue.REVERSE) {
            drawReverseSymbol(g2d, width, height, cardColor);
        } else if (card.getValue() == CardValue.DRAW_TWO) {
            drawDrawTwoSymbol(g2d, width, height, cardColor);
        } else if (card.getValue() == CardValue.WILD) {
            drawWildSymbol(g2d, width, height);
        } else if (card.getValue() == CardValue.WILD_DRAW_FOUR) {
            drawWildDrawFourSymbol(g2d, width, height);
        } else {
            // Draw number
            g2d.setColor(cardColor);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            int xCenter = width / 2 - g2d.getFontMetrics().stringWidth(symbol) / 2;
            int yCenter = height / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
            g2d.drawString(symbol, xCenter, yCenter);
        }
    }
    
    /**
     * Draw Skip symbol
     */
    private void drawSkipSymbol(Graphics2D g2d, int width, int height, Color cardColor) {
        g2d.setColor(cardColor);
        g2d.setStroke(new BasicStroke(3));
        
        // Draw circle
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = width / 3;
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Draw diagonal line
        g2d.drawLine(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        
        // Draw "SKIP" text
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String text = "SKIP";
        int textX = centerX - g2d.getFontMetrics().stringWidth(text) / 2;
        g2d.drawString(text, textX, centerY + radius + 20);
    }
    
    /**
     * Draw Reverse symbol
     */
    private void drawReverseSymbol(Graphics2D g2d, int width, int height, Color cardColor) {
        g2d.setColor(cardColor);
        g2d.setStroke(new BasicStroke(3));
        
        int centerX = width / 2;
        int centerY = height / 2;
        int arrowWidth = width / 3;
        
        // Draw top arrow (left to right)
        g2d.drawArc(centerX - arrowWidth/2, centerY - 15, arrowWidth, 15, 0, 180);
        // Draw arrowhead
        g2d.drawLine(centerX + arrowWidth/2, centerY - 15, centerX + arrowWidth/2 - 5, centerY - 20);
        g2d.drawLine(centerX + arrowWidth/2, centerY - 15, centerX + arrowWidth/2 - 5, centerY - 10);
        
        // Draw bottom arrow (right to left)
        g2d.drawArc(centerX - arrowWidth/2, centerY, arrowWidth, 15, 180, 180);
        // Draw arrowhead
        g2d.drawLine(centerX - arrowWidth/2, centerY + 15, centerX - arrowWidth/2 + 5, centerY + 10);
        g2d.drawLine(centerX - arrowWidth/2, centerY + 15, centerX - arrowWidth/2 + 5, centerY + 20);
        
        // Draw "REV" text
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String text = "REV";
        int textX = centerX - g2d.getFontMetrics().stringWidth(text) / 2;
        g2d.drawString(text, textX, centerY + 30);
    }
    
    /**
     * Draw Draw Two symbol
     */
    private void drawDrawTwoSymbol(Graphics2D g2d, int width, int height, Color cardColor) {
        g2d.setColor(cardColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Draw +2 symbol
        String text = "+2";
        int textX = centerX - g2d.getFontMetrics().stringWidth(text) / 2;
        int textY = centerY + g2d.getFontMetrics().getAscent() / 2 - 5;
        g2d.drawString(text, textX, textY);
    }
    
    /**
     * Draw Wild symbol
     */
    private void drawWildSymbol(Graphics2D g2d, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = width / 4;
        
        // Draw color quadrants
        g2d.setColor(Color.RED);
        g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 90);
        
        g2d.setColor(Color.BLUE);
        g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 90, 90);
        
        g2d.setColor(Color.GREEN);
        g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 180, 90);
        
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 270, 90);
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Draw "WILD" text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String text = "WILD";
        int textX = centerX - g2d.getFontMetrics().stringWidth(text) / 2;
        g2d.drawString(text, textX, centerY + radius + 20);
    }
    
    /**
     * Draw Wild Draw Four symbol
     */
    private void drawWildDrawFourSymbol(Graphics2D g2d, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Draw +4 text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String text = "+4";
        int textX = centerX - g2d.getFontMetrics().stringWidth(text) / 2;
        g2d.drawString(text, textX, centerY - 10);
        
        // Draw color squares
        int squareSize = 10;
        int margin = 3;
        int totalWidth = 4 * squareSize + 3 * margin;
        int startX = centerX - totalWidth / 2;
        int startY = centerY + 5;
        
        // Red square
        g2d.setColor(Color.RED);
        g2d.fillRect(startX, startY, squareSize, squareSize);
        
        // Blue square
        g2d.setColor(Color.BLUE);
        g2d.fillRect(startX + squareSize + margin, startY, squareSize, squareSize);
        
        // Green square
        g2d.setColor(Color.GREEN);
        g2d.fillRect(startX + 2 * (squareSize + margin), startY, squareSize, squareSize);
        
        // Yellow square
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(startX + 3 * (squareSize + margin), startY, squareSize, squareSize);
        
        // Draw "WILD" text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String wildText = "WILD";
        int wildX = centerX - g2d.getFontMetrics().stringWidth(wildText) / 2;
        g2d.drawString(wildText, wildX, startY + squareSize + 15);
    }
    
    /**
     * Draw card corners with value/symbol
     */
    private void drawCardCorners(Graphics2D g2d, int width, int height) {
        String symbol = getCardSymbol();
        Color cardColor = getCardAwtColor(card.getColor());
        
        // Top-left corner
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(5, 5, 15, 15, 5, 5);
        
        g2d.setColor(cardColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(symbol, 8, 17);
        
        // Bottom-right corner (rotated)
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(width - 20, height - 20, 15, 15, 5, 5);
        
        // Save current transform
        java.awt.geom.AffineTransform originalTransform = g2d.getTransform();
        
        // Translate and rotate for bottom-right corner
        g2d.translate(width - 8, height - 8);
        g2d.rotate(Math.PI);
        
        g2d.setColor(cardColor);
        g2d.drawString(symbol, -7, 9);
        
        // Restore original transform
        g2d.setTransform(originalTransform);
    }
    
    /**
     * Draw card back (when face down)
     */
    private void drawCardBack(Graphics2D g2d, int width, int height) {
        // Dark blue background
        Color darkBlue = new Color(0, 0, 100);
        Color lighterBlue = new Color(30, 30, 150);
        
        LinearGradientPaint gradient = new LinearGradientPaint(
            new Point2D.Float(0, 0),
            new Point2D.Float(width, height),
            new float[] {0.0f, 1.0f},
            new Color[] {darkBlue, lighterBlue}
        );
        
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, cornerRadius, cornerRadius));
        
        // UNO logo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String logo = "UNO";
        int logoX = width / 2 - g2d.getFontMetrics().stringWidth(logo) / 2;
        int logoY = height / 2 + g2d.getFontMetrics().getAscent() / 4;
        
        // Draw logo with shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(logo, logoX + 1, logoY + 1);
        g2d.setColor(Color.WHITE);
        g2d.drawString(logo, logoX, logoY);
        
        // Draw diagonal pattern
        g2d.setColor(new Color(70, 70, 200));
        g2d.setStroke(new BasicStroke(1.5f));
        
        for (int i = -height; i < width + height; i += 15) {
            g2d.drawLine(i, 0, i + height, height);
        }
    }
    
    /**
     * Draw hover and selection effects
     */
    private void drawCardEffects(Graphics2D g2d, int width, int height) {
        // Hover effect
        if (isHovered) {
            // Glow effect
            g2d.setColor(new Color(255, 255, 200, 100));
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, cornerRadius, cornerRadius));
        }
        
        // Selection effect
        if (selected) {
            // Yellow border
            g2d.setColor(new Color(255, 255, 0, 200));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius + 1, cornerRadius + 1));
        }
    }
    
    /**
     * Get card symbol string
     */
    private String getCardSymbol() {
        if (card.getValue() == null) {
            return "?";
        }
        
        switch (card.getValue()) {
            case ZERO:
            case ONE:
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
                return String.valueOf(card.getValue().ordinal());
            case SKIP:
                return "⊘";
            case REVERSE:
                return "⇄";
            case DRAW_TWO:
                return "+2";
            case WILD:
                return "W";
            case WILD_DRAW_FOUR:
                return "+4";
            default:
                return "?";
        }
    }
    
    /**
     * Convert CardColor to AWT Color
     */
    private Color getCardAwtColor(CardColor cardColor) {
        if (cardColor == null) {
            return Color.GRAY;
        }
        
        switch (cardColor) {
            case RED:
                return new Color(220, 40, 30);
            case BLUE:
                return new Color(30, 80, 200);
            case GREEN:
                return new Color(30, 180, 50);
            case YELLOW:
                return new Color(240, 200, 40);
            case WILD:
                return Color.BLACK;
            default:
                return Color.GRAY;
        }
    }
    
    /**
     * Get the card
     */
    public Card getCard() {
        return card;
    }
    
    /**
     * Set whether the card is face up
     */
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        component.repaint();
    }
    
    /**
     * Check if card is face up
     */
    public boolean isFaceUp() {
        return faceUp;
    }
    
    /**
     * Set whether the card is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        component.repaint();
    }
    
    /**
     * Check if card is selected
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Set click listener
     */
    public void setClickListener(DActionListener listener) {
        this.clickListener = listener;
    }
}