package dgui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.awt.geom.Point2D;
import javax.swing.Timer;

/**
 * Gestionnaire d'animations pour les composants DGui
 */
public class DAnimator {
    // Constantes pour les animations
    public static final int DEFAULT_DURATION = 300; // durée par défaut en ms
    public static final int QUICK_DURATION = 150;
    public static final int SLOW_DURATION = 600;
    
    // Types d'animation
    public enum AnimationType {
        MOVE,    // Déplacer un composant
        FADE,    // Fade in/out d'un composant
        SCALE,   // Changement d'échelle 
        FLIP     // Retournement de carte
    }
    
    /**
     * Animation de déplacement d'un composant d'un point A à un point B
     */
    public static void animateMove(DComponent component, Point startPoint, Point endPoint, 
                                 int duration, Consumer<Boolean> onComplete) {
        final int steps = Math.max(10, duration / 15); // ~60fps
        final int delay = duration / steps;
        final int[] currentStep = {0};
        
        // Position initiale
        component.setPosition(startPoint.x, startPoint.y);
        
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                // Fonction d'interpolation pour un mouvement plus naturel (easeInOutQuad)
                float easedProgress = progress < 0.5f ? 
                        2 * progress * progress : 
                        1 - (float)Math.pow(-2 * progress + 2, 2) / 2;
                
                int x = startPoint.x + (int)(easedProgress * (endPoint.x - startPoint.x));
                int y = startPoint.y + (int)(easedProgress * (endPoint.y - startPoint.y));
                
                component.setPosition(x, y);
                
                if (currentStep[0] >= steps) {
                    ((Timer)e.getSource()).stop();
                    component.setPosition(endPoint.x, endPoint.y);
                    if (onComplete != null) {
                        onComplete.accept(true);
                    }
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animation de fade in/out d'un composant
     */
    public static void animateFade(DComponent component, float startAlpha, float endAlpha, 
                                 int duration, Consumer<Boolean> onComplete) {
        final int steps = Math.max(10, duration / 15);
        final int delay = duration / steps;
        final int[] currentStep = {0};
        
        // Alpha initial
        component.setAlpha(startAlpha);
        
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                // Fonction d'interpolation linéaire
                float currentAlpha = startAlpha + (endAlpha - startAlpha) * progress;
                component.setAlpha(currentAlpha);
                
                if (currentStep[0] >= steps) {
                    ((Timer)e.getSource()).stop();
                    component.setAlpha(endAlpha);
                    if (onComplete != null) {
                        onComplete.accept(true);
                    }
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animation d'échelle d'un composant
     */
    public static void animateScale(DComponent component, float startScale, float endScale, 
                                  int duration, Consumer<Boolean> onComplete) {
        final int steps = Math.max(10, duration / 15);
        final int delay = duration / steps;
        final int[] currentStep = {0};
        
        // Taille originale
        final int originalWidth = component.getWidth();
        final int originalHeight = component.getHeight();
        
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                // Fonction d'interpolation (easeInOutQuad)
                float easedProgress = progress < 0.5f ? 
                        2 * progress * progress : 
                        1 - (float)Math.pow(-2 * progress + 2, 2) / 2;
                
                float currentScale = startScale + (endScale - startScale) * easedProgress;
                int newWidth = (int)(originalWidth * currentScale);
                int newHeight = (int)(originalHeight * currentScale);
                
                component.setSize(newWidth, newHeight);
                
                if (currentStep[0] >= steps) {
                    ((Timer)e.getSource()).stop();
                    component.setSize((int)(originalWidth * endScale), (int)(originalHeight * endScale));
                    if (onComplete != null) {
                        onComplete.accept(true);
                    }
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animation de retournement d'une carte (DUnoCard)
     * Cette méthode est spécifique aux cartes UNO
     */
    public static void animateFlip(DComponent component, boolean toFaceUp, 
                                 int duration, Consumer<Boolean> onComplete) {
        final int steps = Math.max(10, duration / 15);
        final int delay = duration / steps;
        final int[] currentStep = {0};
        
        // Largeur originale
        final int originalWidth = component.getWidth();
        
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                // Phase 1: rétrécir horizontalement jusqu'à zéro
                if (progress <= 0.5f) {
                    float phase1Progress = progress * 2; // 0->1 pendant la première moitié
                    int currentWidth = (int)(originalWidth * (1 - phase1Progress));
                    component.setSize(Math.max(1, currentWidth), component.getHeight());
                } 
                // Phase 2: Retourner et élargir
                else if (currentStep[0] == steps / 2 + 1) {
                    // Au milieu de l'animation, retournez la carte
                    if (component instanceof uno.gui.DUnoCard) {
                        ((uno.gui.DUnoCard) component).setFaceUp(toFaceUp);
                    }
                } 
                // Phase 3: agrandir horizontalement jusqu'à la taille originale
                else {
                    float phase2Progress = (progress - 0.5f) * 2; // 0->1 pendant la seconde moitié
                    int currentWidth = (int)(originalWidth * phase2Progress);
                    component.setSize(Math.max(1, currentWidth), component.getHeight());
                }
                
                if (currentStep[0] >= steps) {
                    ((Timer)e.getSource()).stop();
                    component.setSize(originalWidth, component.getHeight());
                    if (onComplete != null) {
                        onComplete.accept(true);
                    }
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animer une séquence d'animations à la suite
     */
    public static void sequence(Runnable... animations) {
        if (animations.length == 0) return;
        
        // Exécuter la première animation
        animations[0].run();
        
        // Si c'est la seule, on s'arrête là
        if (animations.length == 1) return;
        
        // Sinon, on programme les animations suivantes avec un délai
        for (int i = 1; i < animations.length; i++) {
            final int index = i;
            new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animations[index].run();
                    ((Timer)e.getSource()).stop();
                }
            }).start();
        }
    }
}