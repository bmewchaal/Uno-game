package uno;

import uno.gui.WoodTableDemo;

/**
 * Programme de démonstration pour l'interface UNO avec thème en bois
 */
public class UnoWoodDemo {
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new WoodTableDemo();
        });
    }
}