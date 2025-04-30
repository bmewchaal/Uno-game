package uno.core;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffectManager {
    private static SoundEffectManager instance;
    private final ExecutorService soundPool = Executors.newCachedThreadPool();
    private final Map<String, Clip> soundClips = new HashMap<>();
    
    public static final String PLAY_CARD = "play_card";
    public static final String DRAW_CARD = "draw_card";
    public static final String UNO_CALL = "uno_call";
    public static final String GAME_WIN = "game_win";
    public static final String SPECIAL_CARD = "special_card";
    public static final String DIRECTION_CHANGE = "direction_change";
    
    private float volume = 1.0f;
    private boolean muted = false;
    
    private SoundEffectManager() {
        loadSoundEffects();
    }
    
    public static synchronized SoundEffectManager getInstance() {
        if (instance == null) {
            instance = new SoundEffectManager();
        }
        return instance;
    }
    
    private void loadSoundEffects() {
        try {
            soundClips.put(PLAY_CARD, createToneClip(800, 150));
            soundClips.put(DRAW_CARD, createToneClip(500, 100));
            soundClips.put(UNO_CALL, createToneClip(1200, 300));
            soundClips.put(GAME_WIN, createFanfareClip());
            soundClips.put(SPECIAL_CARD, createToneClip(950, 200));
            soundClips.put(DIRECTION_CHANGE, createSwooshClip());
        } catch (Exception e) {
            System.err.println("Error loading sound effects: " + e.getMessage());
        }
    }
    
    private Clip createToneClip(int frequency, int duration) throws LineUnavailableException {
        float sampleRate = 44100.0f;
        int sampleSize = 16;
        
        byte[] data = new byte[(int)(sampleRate * duration / 1000)];
        double period = sampleRate / frequency;
        
        for (int i = 0; i < data.length; i++) {
            double angle = 2.0 * Math.PI * i / period;
            data[i] = (byte)(Math.sin(angle) * 127.0);
        }
        
        AudioFormat format = new AudioFormat(
            sampleRate,
            sampleSize,
            1,  // Mono
            true,  // Signed
            false  // Little endian
        );
        
        Clip clip = AudioSystem.getClip();
        clip.open(format, data, 0, data.length);
        
        return clip;
    }
    
    private Clip createFanfareClip() throws LineUnavailableException {
        float sampleRate = 44100.0f;
        int sampleSize = 16;
        
        int[] frequencies = {440, 550, 660};
        int toneDuration = 150;
        
        byte[] data = new byte[(int)(sampleRate * toneDuration * 3 / 1000)];
        
        for (int i = 0; i < 3; i++) {
            double period = sampleRate / frequencies[i];
            int offset = (int)(sampleRate * toneDuration * i / 1000);
            int length = (int)(sampleRate * toneDuration / 1000);
            
            for (int j = 0; j < length; j++) {
                double angle = 2.0 * Math.PI * j / period;
                data[offset + j] = (byte)(Math.sin(angle) * 127.0);
            }
        }
        
        AudioFormat format = new AudioFormat(
            sampleRate,
            sampleSize,
            1,
            true,
            false
        );
        
        Clip clip = AudioSystem.getClip();
        clip.open(format, data, 0, data.length);
        
        return clip;
    }
    
    private Clip createSwooshClip() throws LineUnavailableException {
        float sampleRate = 44100.0f;
        int sampleSize = 16;
        
        int startFreq = 2000;
        int endFreq = 500;
        int duration = 200;
        
        byte[] data = new byte[(int)(sampleRate * duration / 1000)];
        
        for (int i = 0; i < data.length; i++) {
            double progress = (double)i / data.length;
            double currentFreq = startFreq + (endFreq - startFreq) * progress;
            double period = sampleRate / currentFreq;
            double angle = 2.0 * Math.PI * i / period;
            
            double envelope = Math.sin(Math.PI * progress);
            data[i] = (byte)(Math.sin(angle) * 127.0 * envelope);
        }
        
        AudioFormat format = new AudioFormat(
            sampleRate,
            sampleSize,
            1,
            true,
            false
        );
        
        Clip clip = AudioSystem.getClip();
        clip.open(format, data, 0, data.length);
        
        return clip;
    }
    
    public void playSound(String soundName) {
        if (muted) {
            return;
        }
        
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            soundPool.execute(() -> {
                try {
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float range = gainControl.getMaximum() - gainControl.getMinimum();
                        float gain = (range * volume) + gainControl.getMinimum();
                        gainControl.setValue(gain);
                    }
                    
                    clip.setFramePosition(0);
                    clip.start();
                } catch (Exception e) {
                    System.err.println("Error playing sound: " + e.getMessage());
                }
            });
        }
    }
    
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    public float getVolume() {
        return volume;
    }
    
    public void setMuted(boolean muted) {
        this.muted = muted;
    }
    
    public boolean isMuted() {
        return muted;
    }
    
    public void dispose() {
        for (Clip clip : soundClips.values()) {
            clip.close();
        }
        soundClips.clear();
        soundPool.shutdown();
    }
}