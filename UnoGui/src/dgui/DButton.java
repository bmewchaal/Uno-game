package dgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import dgui.themes.DTheme;

/**
 * Un bouton personnalisé avec des effets visuels avancés.
 */
public class DButton extends DComponent {
    private String text;
    private boolean isRounded = true;
    private int cornerRadius = 10;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    
    /**
     * Crée un nouveau bouton avec le texte spécifié
     */
    public DButton(String text) {
        super(createCustomButton());
        this.text = text;
        initButton();
    }
    
    /**
     * Crée un JButton personnalisé avec un rendu avancé
     */
    private static JButton createCustomButton() {
        JButton button = new JButton() {
            private DButton parent;
            
            @Override
            protected void paintComponent(Graphics g) {
                if (parent != null) {
                    parent.paintButton(g, getWidth(), getHeight());
                } else {
                    super.paintComponent(g);
                }
            }
            
            public void setParent(DButton parent) {
                this.parent = parent;
            }
        };
        
        // Désactiver le rendu par défaut pour utiliser notre propre rendu
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        return button;
    }
    
    /**
     * Initialise le bouton après sa création
     */
    private void initButton() {
        applyTheme();
        setText(text);
        
        // Attacher les écouteurs d'événements de la souris
        ((JButton)component).addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                component.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                component.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                component.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                component.repaint();
            }
        });
        
        // Attache this comme parent au JButton personnalisé
        try {
            ((JButton)component).getClass().getMethod("setParent", DButton.class).invoke(component, this);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du bouton: " + e.getMessage());
        }
    }
    
    /**
     * Dessine le bouton avec des effets visuels avancés
     */
    public void paintButton(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Déterminer la couleur de fond en fonction de l'état du bouton
        Color bgColor;
        if (isPressed) {
            bgColor = pressedColor;
        } else if (isHovered) {
            bgColor = hoverColor;
        } else {
            bgColor = normalColor;
        }
        
        // Dessiner le fond du bouton
        if (useCustomBackground && backgroundImage != null) {
            // Utiliser l'image de fond si elle existe
            if (isRounded) {
                g2d.setClip(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
            }
            g2d.drawImage(backgroundImage, 0, 0, width, height, null);
            
            // Ajouter une superposition de couleur pour les effets
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.setColor(bgColor);
            if (isRounded) {
                g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
            } else {
                g2d.fillRect(0, 0, width, height);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // Dessiner un fond uni
            g2d.setColor(bgColor);
            if (isRounded) {
                g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
            } else {
                g2d.fillRect(0, 0, width, height);
            }
        }
        
        // Dessiner une bordure
        g2d.setColor(new Color(0, 0, 0, 50));
        if (isRounded) {
            g2d.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius));
        } else {
            g2d.drawRect(0, 0, width - 1, height - 1);
        }
        
        // Dessiner l'ombre intérieure lorsque le bouton est enfoncé
        if (isPressed) {
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.drawLine(0, 0, width, 0);
            g2d.drawLine(0, 0, 0, height);
        }
        
        // Dessiner le texte
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D textBounds = fm.getStringBounds(text, g2d);
        
        int textX = (int) (width - textBounds.getWidth()) / 2;
        int textY = (int) (height - textBounds.getHeight()) / 2 + fm.getAscent();
        
        // Ajouter un effet d'ombre légère au texte
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawString(text, textX + 1, textY + 1);
        
        // Dessiner le texte principal
        g2d.setColor(textColor);
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
    }
    
    @Override
    protected void applyTheme() {
        if (component != null) {
            normalColor = theme.getButtonColor();
            hoverColor = theme.getButtonHoverColor();
            pressedColor = new Color(
                Math.max(0, theme.getButtonColor().getRed() - 30),
                Math.max(0, theme.getButtonColor().getGreen() - 30),
                Math.max(0, theme.getButtonColor().getBlue() - 30)
            );
            textColor = theme.getButtonTextColor();
            component.setFont(theme.getButtonFont());
            component.repaint();
        }
    }
    
    /**
     * Définit le texte du bouton
     */
    public void setText(String text) {
        this.text = text;
        ((JButton)component).setText(text);
    }
    
    /**
     * Récupère le texte du bouton
     */
    public String getText() {
        return text;
    }
    
    /**
     * Définit si le bouton a des coins arrondis
     */
    public void setRounded(boolean rounded) {
        this.isRounded = rounded;
        component.repaint();
    }
    
    /**
     * Définit le rayon des coins du bouton
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        component.repaint();
    }
    
    /**
     * Ajoute un écouteur d'action au bouton
     */
    public void addActionListener(ActionListener l) {
        ((AbstractButton)component).addActionListener(l);
    }
    
    /**
     * Déclenche un clic sur le bouton
     */
    public void doClick() {
        ((JButton)component).doClick();
    }
    
    /**
     * Récupère le JButton sous-jacent
     */
    public JButton getButton() {
        return (JButton)component;
    }
}