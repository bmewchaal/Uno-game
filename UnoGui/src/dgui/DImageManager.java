package dgui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Gestionnaire d'images pour la couche DGUI
 * Permet de charger, créer et manipuler des images
 */
public class DImageManager {
    private static DImageManager instance;
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private Random random = new Random();
    
    /**
     * Constructeur privé (singleton)
     */
    private DImageManager() {
        // Constructeur privé
    }
    
    /**
     * Récupère l'instance unique du gestionnaire d'images
     */
    public static DImageManager getInstance() {
        if (instance == null) {
            instance = new DImageManager();
        }
        return instance;
    }
    
    /**
     * Charge une image depuis un fichier
     */
    public BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        try {
            BufferedImage image = ImageIO.read(new File(path));
            imageCache.put(path, image);
            return image;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image: " + path);
            return createErrorImage(100, 100);
        }
    }
    
    /**
     * Crée une image d'erreur
     */
    private BufferedImage createErrorImage(int width, int height) {
        BufferedImage errorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = errorImage.createGraphics();
        
        // Fond rouge
        g2d.setColor(new Color(255, 0, 0, 150));
        g2d.fillRect(0, 0, width, height);
        
        // Croix
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, 0, width, height);
        g2d.drawLine(0, height, width, 0);
        
        g2d.dispose();
        return errorImage;
    }
    
    /**
     * Crée une texture de bois
     */
    public BufferedImage createWoodTexture(int width, int height, Color baseColor) {
        BufferedImage texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = texture.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fond de base
        g2d.setColor(baseColor);
        g2d.fill(new Rectangle2D.Float(0, 0, width, height));
        
        // Ajouter des veines de bois
        int numVeins = width / 20;
        
        for (int i = 0; i < numVeins; i++) {
            float x = random.nextFloat() * width;
            int veinWidth = 1 + random.nextInt(3);
            
            // Variation de couleur pour la veine
            Color veinColor = varyColor(baseColor, 0.15f);
            g2d.setColor(veinColor);
            
            // Forme ondulée
            float lastX = x;
            for (int y = 0; y < height; y += 5) {
                float newX = lastX + (random.nextFloat() * 6 - 3);
                g2d.fillOval((int)newX - veinWidth/2, y, veinWidth, 10);
                lastX = newX;
            }
        }
        
        // Ajouter des nœuds de bois
        int numKnots = 2 + random.nextInt(4);
        for (int i = 0; i < numKnots; i++) {
            int knotX = random.nextInt(width);
            int knotY = random.nextInt(height);
            int knotSize = 5 + random.nextInt(15);
            
            Color knotColor = darkenColor(baseColor, 0.3f);
            g2d.setColor(knotColor);
            g2d.fillOval(knotX - knotSize/2, knotY - knotSize/2, knotSize, knotSize);
            
            // Cercles autour du nœud
            for (int r = knotSize/2 + 2; r < knotSize * 2; r += 2) {
                g2d.setColor(varyColor(baseColor, 0.1f));
                g2d.drawOval(knotX - r, knotY - r, r*2, r*2);
            }
        }
        
        g2d.dispose();
        return texture;
    }
    
    /**
     * Modifie légèrement une couleur
     */
    private Color varyColor(Color base, float amount) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        hsb[1] *= (1.0f + (random.nextFloat() * amount * 2 - amount));
        hsb[2] *= (1.0f + (random.nextFloat() * amount * 2 - amount));
        
        return new Color(Color.HSBtoRGB(hsb[0], 
                Math.max(0f, Math.min(1f, hsb[1])), 
                Math.max(0f, Math.min(1f, hsb[2]))));
    }
    
    /**
     * Assombrit une couleur
     */
    private Color darkenColor(Color base, float amount) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        hsb[2] *= (1.0f - amount);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    /**
     * Éclaircit une couleur
     */
    private Color lightenColor(Color base, float amount) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        hsb[2] = Math.min(1.0f, hsb[2] * (1.0f + amount));
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    /**
     * Redimensionne une image
     */
    public BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }
    
    /**
     * Crée une image avec des coins arrondis
     */
    public BufferedImage createRoundedImage(BufferedImage original, int radius) {
        int width = original.getWidth();
        int height = original.getHeight();
        
        BufferedImage rounded = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rounded.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, radius, radius));
        g2d.drawImage(original, 0, 0, null);
        
        g2d.dispose();
        return rounded;
    }
}