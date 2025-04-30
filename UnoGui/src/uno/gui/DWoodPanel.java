package uno.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import dgui.DPanel;
import dgui.DImageManager;
import dgui.DComponentAdapter;

/**
 * Panneau avec fond en bois pour le jeu UNO
 * Version pure DGUI sans dépendances directes à Swing
 */
public class DWoodPanel extends DPanel {
    // Constantes des couleurs de bois
    public static final Color LIGHT_WOOD = new Color(210, 180, 140);
    public static final Color DARK_WOOD = new Color(160, 120, 80);
    public static final Color RED_WOOD = new Color(170, 80, 60);
    
    /**
     * Crée un nouveau panneau en bois
     */
    public DWoodPanel() {
        super();
        init();
    }
    
    /**
     * Crée un nouveau panneau en bois avec un layout spécifié
     */
    public DWoodPanel(LayoutManager layout) {
        super(layout);
        init();
    }
    
    /**
     * Initialisation commune aux constructeurs
     */
    private void init() {
        setOpaque(true);
        createWoodTexture(LIGHT_WOOD);
    }
    
    /**
     * Crée une texture de bois pour le fond du panneau
     */
    public void createWoodTexture(Color baseColor) {
        // Utiliser l'outil de génération de textures DImageManager
        DImageManager imageManager = DImageManager.getInstance();
        
        int width = Math.max(100, getWidth());
        int height = Math.max(100, getHeight());
        
        // Si les dimensions sont trop petites, on utilise des valeurs par défaut
        if (width <= 0) width = 800;
        if (height <= 0) height = 600;
        
        BufferedImage woodTexture = imageManager.createWoodTexture(width, height, baseColor);
        setBackgroundImage(woodTexture);
    }
}