package dgui.themes;

import java.awt.Color;
import java.awt.Font;

/**
 * Classe de base pour les thèmes de l'interface DGui.
 * Permet de définir des couleurs et polices cohérentes pour toute l'application.
 */
public class DTheme {
    // Couleurs principales
    private Color primaryColor;
    private Color secondaryColor;
    private Color accentColor;
    private Color backgroundColor;
    private Color textColor;
    
    // Couleurs spécifiques
    private Color buttonColor;
    private Color buttonHoverColor;
    private Color buttonTextColor;
    private Color panelColor;
    private Color cardBackgroundColor;
    private Color cardBorderColor;
    
    // Polices
    private Font titleFont;
    private Font bodyFont;
    private Font buttonFont;
    private Font cardFont;
    
    // Thème par défaut
    public static final DTheme DEFAULT = new DTheme(
        new Color(0, 100, 0),     // Vert foncé pour couleur primaire
        new Color(50, 150, 50),   // Vert clair pour couleur secondaire
        new Color(0, 100, 255),   // Bleu pour l'accent
        new Color(240, 230, 210), // Beige pour le fond
        Color.BLACK,              // Noir pour le texte
        
        new Color(30, 100, 200),  // Bleu pour les boutons
        new Color(60, 130, 230),  // Bleu clair pour le hover
        Color.WHITE,              // Blanc pour le texte des boutons
        new Color(210, 210, 180), // Beige clair pour les panneaux
        Color.WHITE,              // Blanc pour le fond des cartes
        Color.BLACK,              // Noir pour la bordure des cartes
        
        new Font("Arial", Font.BOLD, 24),   // Police des titres
        new Font("Arial", Font.PLAIN, 14),  // Police du texte
        new Font("Arial", Font.BOLD, 14),   // Police des boutons
        new Font("Arial", Font.BOLD, 18)    // Police des cartes
    );
    
    // Thème bois
    public static final DTheme WOOD = new DTheme(
        new Color(120, 80, 20),   // Marron pour couleur primaire
        new Color(160, 120, 60),  // Marron clair pour couleur secondaire
        new Color(0, 120, 255),   // Bleu pour l'accent
        new Color(225, 195, 150), // Beige bois pour le fond
        new Color(60, 40, 10),    // Marron foncé pour le texte
        
        new Color(0, 120, 255),   // Bleu pour les boutons
        new Color(40, 160, 255),  // Bleu clair pour le hover
        Color.WHITE,              // Blanc pour le texte des boutons
        new Color(200, 170, 130), // Beige bois clair pour les panneaux
        Color.WHITE,              // Blanc pour le fond des cartes
        Color.BLACK,              // Noir pour la bordure des cartes
        
        new Font("Georgia", Font.BOLD, 24),   // Police des titres
        new Font("Georgia", Font.PLAIN, 14),  // Police du texte
        new Font("Arial", Font.BOLD, 14),     // Police des boutons
        new Font("Arial", Font.BOLD, 18)      // Police des cartes
    );
    
    public DTheme(
            Color primaryColor, 
            Color secondaryColor, 
            Color accentColor, 
            Color backgroundColor, 
            Color textColor,
            Color buttonColor,
            Color buttonHoverColor,
            Color buttonTextColor,
            Color panelColor,
            Color cardBackgroundColor,
            Color cardBorderColor,
            Font titleFont,
            Font bodyFont,
            Font buttonFont,
            Font cardFont) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.accentColor = accentColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.buttonColor = buttonColor;
        this.buttonHoverColor = buttonHoverColor;
        this.buttonTextColor = buttonTextColor;
        this.panelColor = panelColor;
        this.cardBackgroundColor = cardBackgroundColor;
        this.cardBorderColor = cardBorderColor;
        this.titleFont = titleFont;
        this.bodyFont = bodyFont;
        this.buttonFont = buttonFont;
        this.cardFont = cardFont;
    }
    
    // Getters pour toutes les propriétés
    public Color getPrimaryColor() { return primaryColor; }
    public Color getSecondaryColor() { return secondaryColor; }
    public Color getAccentColor() { return accentColor; }
    public Color getBackgroundColor() { return backgroundColor; }
    public Color getTextColor() { return textColor; }
    public Color getButtonColor() { return buttonColor; }
    public Color getButtonHoverColor() { return buttonHoverColor; }
    public Color getButtonTextColor() { return buttonTextColor; }
    public Color getPanelColor() { return panelColor; }
    public Color getCardBackgroundColor() { return cardBackgroundColor; }
    public Color getCardBorderColor() { return cardBorderColor; }
    public Font getTitleFont() { return titleFont; }
    public Font getBodyFont() { return bodyFont; }
    public Font getButtonFont() { return buttonFont; }
    public Font getCardFont() { return cardFont; }
}