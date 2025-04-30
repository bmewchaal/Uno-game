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

import dgui.DComponentAdapter;
import dgui.DPanel;
import dgui.DActionEvent;
import dgui.DActionListener;
import uno.core.Card;
import uno.core.CardColor;
import uno.core.CardValue;

/**
 * Composant graphique représentant une carte UNO
 * Version pure DGUI sans dépendances directes à Swing
 */
public class DUnoCardPure extends DPanel {
    private Card card;
    private boolean faceUp = true;
    private boolean isHovered = false;
    private boolean selected = false;
    private int cornerRadius = 12;
    private DActionListener clickListener;
    
    /**
     * Crée une nouvelle carte UNO avec la carte spécifiée
     */
    public DUnoCardPure(Card card) {
        super();
        this.card = card;
        initCard();
    }
    
    /**
     * Initialise la carte après sa création
     */
    private void initCard() {
        // Définir la taille par défaut
        setSize(80, 120);
        
        // Ajouter les écouteurs d'événements de la souris
        DComponentAdapter.addMouseListener(this, new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.actionPerformed(new DActionEvent(DUnoCardPure.this));
                }
            }
        });
        
        // Définir le mode opaque à false pour que la texture de fond soit visible
        setOpaque(false);
    }
    
    /**
     * Méthode qui sera appelée pour dessiner le composant
     * Elle est utilisée par le JPanel sous-jacent
     */
    public void paintComponent(Graphics g) {
        paintCard(g, getWidth(), getHeight());
    }
    
    /**
     * Dessine la carte
     */
    public void paintCard(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner le fond de la carte (noir)
        g2d.setColor(Color.BLACK);
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius + 2, cornerRadius + 2));
        
        if (faceUp) {
            // Dessiner le fond de la carte selon la couleur
            Color cardColor;
            switch (card.getColor()) {
                case RED:
                    cardColor = new Color(220, 40, 30);
                    break;
                case BLUE:
                    cardColor = new Color(30, 80, 200);
                    break;
                case GREEN:
                    cardColor = new Color(30, 180, 50);
                    break;
                case YELLOW:
                    cardColor = new Color(240, 200, 40);
                    break;
                case WILD:
                    cardColor = Color.BLACK;
                    break;
                default:
                    cardColor = Color.GRAY;
                    break;
            }
            
            // Dessiner le fond coloré
            g2d.setColor(cardColor);
            g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, cornerRadius, cornerRadius));
            
            // Dessiner le symbole ovale blanc
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Float(10, 20, width - 20, height - 40));
            
            // Dessiner la valeur ou le symbole de la carte
            String symbol;
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
                    symbol = String.valueOf(card.getValue().ordinal());
                    break;
                case SKIP:
                    symbol = "⊘";
                    break;
                case REVERSE:
                    symbol = "⇄";
                    break;
                case DRAW_TWO:
                    symbol = "+2";
                    break;
                case WILD:
                    symbol = "W";
                    break;
                case WILD_DRAW_FOUR:
                    symbol = "+4";
                    break;
                default:
                    symbol = "?";
                    break;
            }
            
            // Dessiner le petit numéro en haut à gauche
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(symbol, 5, 15);
            
            // Dessiner le grand symbole au centre
            g2d.setColor(cardColor);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            int xCenter = width / 2 - g2d.getFontMetrics().stringWidth(symbol) / 2;
            int yCenter = height / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
            g2d.drawString(symbol, xCenter, yCenter);
            
            // Si c'est une carte WILD multicolore, dessiner les 4 coins colorés
            if (card.getValue() == CardValue.WILD || card.getValue() == CardValue.WILD_DRAW_FOUR) {
                int cornerSize = 15;
                
                // Coin rouge (en haut à gauche)
                g2d.setColor(new Color(220, 40, 30));
                g2d.fillRect(15, 35, cornerSize, cornerSize);
                
                // Coin bleu (en haut à droite)
                g2d.setColor(new Color(30, 80, 200));
                g2d.fillRect(width - 15 - cornerSize, 35, cornerSize, cornerSize);
                
                // Coin vert (en bas à gauche)
                g2d.setColor(new Color(30, 180, 50));
                g2d.fillRect(15, height - 35 - cornerSize, cornerSize, cornerSize);
                
                // Coin jaune (en bas à droite)
                g2d.setColor(new Color(240, 200, 40));
                g2d.fillRect(width - 15 - cornerSize, height - 35 - cornerSize, cornerSize, cornerSize);
            }
        } else {
            // Dessiner le dos de la carte (logo UNO)
            g2d.setColor(new Color(180, 30, 20)); // Rouge UNO
            g2d.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, cornerRadius, cornerRadius));
            
            // Ellipse centrale
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Float(10, 20, width - 20, height - 40));
            
            // Texte "UNO"
            g2d.setColor(new Color(180, 30, 20));
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            String unoText = "UNO";
            int unoX = width / 2 - g2d.getFontMetrics().stringWidth(unoText) / 2;
            int unoY = height / 2 + g2d.getFontMetrics().getAscent() / 2 - 5;
            g2d.drawString(unoText, unoX, unoY);
        }
        
        // Effets visuels pour les cartes
        if (isHovered) {
            // Lueur autour de la carte quand survolée
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, cornerRadius, cornerRadius));
            g2d.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius + 1, cornerRadius + 1));
        }
        
        if (selected) {
            // Bordure pour les cartes sélectionnées
            g2d.setColor(new Color(255, 255, 100, 200));
            g2d.setStroke(new java.awt.BasicStroke(2.0f));
            g2d.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius + 1, cornerRadius + 1));
        }
        
        g2d.dispose();
    }
    
    /**
     * Récupère la carte
     */
    public Card getCard() {
        return card;
    }
    
    /**
     * Définit si la carte est face visible
     */
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        repaint();
    }
    
    /**
     * Définit si la carte est sélectionnée
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
    
    /**
     * Récupère si la carte est sélectionnée
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Ajoute un écouteur de clic à la carte
     */
    public void setClickListener(DActionListener listener) {
        this.clickListener = listener;
    }
}