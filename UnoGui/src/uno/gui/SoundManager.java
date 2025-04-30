package uno.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire de sons pour le jeu UNO
 * Permet de charger et jouer des effets sonores
 */
public class SoundManager {
    
    // Constantes pour les noms des sons
    public static final String CARD_SHUFFLE = "card_shuffle";
    public static final String CARD_PLACE = "card_place";
    public static final String CARD_DRAW = "card_draw";
    public static final String GAME_START = "game_start";
    public static final String GAME_WIN = "game_win";
    public static final String UNO_CALL = "uno_call";
    public static final String CARD_FLIP = "card_flip";
    
    // Stockage des clips audio
    private Map<String, Clip> sounds;
    
    // Volume global (0.0 à 1.0)
    private float volume = 1.0f;
    
    // État de mute
    private boolean muted = false;
    
    /**
     * Constructeur
     */
    public SoundManager() {
        sounds = new HashMap<>();
        
        // Nous utilisons une version silencieuse des sons pour éviter les erreurs
        // Dans une version complète, ces sons seraient chargés depuis des fichiers
        try {
            // Créer des sons vides en mémoire
            createEmptySound(CARD_SHUFFLE);
            createEmptySound(CARD_PLACE);
            createEmptySound(CARD_DRAW);
            createEmptySound(GAME_START);
            createEmptySound(GAME_WIN);
            createEmptySound(UNO_CALL);
            createEmptySound(CARD_FLIP);
            
            System.out.println("Sons initialisés avec succès (mode silencieux)");
        } catch (Exception e) {
            System.err.println("Error initializing sound effects: " + e.getMessage());
        }
    }
    
    /**
     * Crée un son vide en mémoire
     * Cette version n'essaie pas d'initialiser un vrai clip pour éviter les erreurs
     */
    private void createEmptySound(String name) {
        // Au lieu de créer un vrai clip qui peut causer des erreurs,
        // nous utilisons un objet null comme placeholder
        // Les méthodes playSound et stopSound gèrent le cas où le clip est null
        sounds.put(name, null);
    }
    
    /**
     * Charger un son depuis un fichier
     */
    private void loadSound(String name, String path) throws IOException, 
            UnsupportedAudioFileException, LineUnavailableException {
        
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Sound file not found: " + path);
            return;
        }
        
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        
        // Ajuster le volume initial
        setClipVolume(clip, volume);
        
        sounds.put(name, clip);
    }
    
    /**
     * Jouer un son par son nom
     */
    public void playSound(String name) {
        if (muted) return;
        
        try {
            Clip clip = sounds.get(name);
            if (clip != null && clip.isOpen()) {
                // Si le son est déjà en cours, on le remet au début
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
            } else {
                // Gérer silencieusement le cas où le clip n'est pas initialisé
                System.out.println("Son '" + name + "' joué (mode silencieux)");
            }
        } catch (Exception e) {
            // Ignorer les erreurs de son - ne pas interrompre le jeu pour des sons
            System.out.println("Erreur lors de la lecture du son '" + name + "': " + e.getMessage());
        }
    }
    
    /**
     * Arrêter un son
     */
    public void stopSound(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            clip.stop();
        }
    }
    
    /**
     * Définir le volume global (0.0 à 1.0)
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        
        // Mettre à jour le volume de tous les clips
        for (Clip clip : sounds.values()) {
            if (clip != null) {
                setClipVolume(clip, this.volume);
            }
        }
    }
    
    /**
     * Définir le volume d'un clip individuel
     */
    private void setClipVolume(Clip clip, float volume) {
        try {
            if (clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log10(volume) * 20.0);
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            // Ignorer les erreurs de volume
        }
    }
    
    /**
     * Activer/désactiver le son
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
        
        // Si on reactive le son et qu'un son était en cours, on ne le reprend pas
        if (!muted) {
            // Optionnellement, on pourrait reprendre les sons qui étaient en cours
        }
    }
    
    /**
     * Vérifier si le son est coupé
     */
    public boolean isMuted() {
        return muted;
    }
    
    /**
     * Obtenir le volume actuel
     */
    public float getVolume() {
        return volume;
    }
    
    /**
     * Libérer les ressources
     */
    public void dispose() {
        for (Clip clip : sounds.values()) {
            if (clip != null) {
                try {
                    clip.close();
                } catch (Exception e) {
                    // Ignorer les erreurs de fermeture
                }
            }
        }
        sounds.clear();
    }
}