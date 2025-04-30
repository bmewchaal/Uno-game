package uno.gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utilitaires pour les sons
 */
public class SoundUtils {
    
    /**
     * Crée un fichier WAV vide
     * Utile pour créer des placeholders pour éviter les erreurs
     */
    public static void createEmptyWavFile(String filePath) throws IOException {
        // En-tête WAV minimale
        byte[] header = {
            // RIFF Header
            'R', 'I', 'F', 'F',  // RIFF chunk
            36, 0, 0, 0,  // Size of the entire file in bytes - 8
            'W', 'A', 'V', 'E',  // WAVE identifier
            
            // Format Chunk
            'f', 'm', 't', ' ',  // fmt chunk
            16, 0, 0, 0,  // Length of the format data
            1, 0,  // Type of format (1 = PCM)
            1, 0,  // Number of channels
            68, -84, 0, 0,  // Sample rate (44100Hz)
            68, -84, 0, 0,  // Byte rate (44100 * 1 * 1)
            1, 0,  // Block align
            8, 0,  // Bits per sample
            
            // Data Chunk
            'd', 'a', 't', 'a',  // data chunk
            0, 0, 0, 0   // Size of data chunk
        };
        
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Créer les répertoires parents si nécessaires
        
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(header);
        fos.close();
    }
    
    /**
     * Obtient un flux d'entrée pour un WAV vide
     */
    public static ByteArrayInputStream getEmptyWavStream() {
        // En-tête WAV minimale
        byte[] header = {
            // RIFF Header
            'R', 'I', 'F', 'F',  // RIFF chunk
            36, 0, 0, 0,  // Size of the entire file in bytes - 8
            'W', 'A', 'V', 'E',  // WAVE identifier
            
            // Format Chunk
            'f', 'm', 't', ' ',  // fmt chunk
            16, 0, 0, 0,  // Length of the format data
            1, 0,  // Type of format (1 = PCM)
            1, 0,  // Number of channels
            68, -84, 0, 0,  // Sample rate (44100Hz)
            68, -84, 0, 0,  // Byte rate (44100 * 1 * 1)
            1, 0,  // Block align
            8, 0,  // Bits per sample
            
            // Data Chunk
            'd', 'a', 't', 'a',  // data chunk
            0, 0, 0, 0   // Size of data chunk
        };
        
        return new ByteArrayInputStream(header);
    }
}