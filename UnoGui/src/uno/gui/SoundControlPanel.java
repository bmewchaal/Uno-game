package uno.gui;

import dgui.*;
import uno.core.SoundEffectManager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSlider;
import javax.swing.JCheckBox;

public class SoundControlPanel extends DPanel {
    private final SoundEffectManager soundManager = SoundEffectManager.getInstance();
    private DComponent volumeSlider;
    private DComponent muteCheckBox;
    
    public SoundControlPanel() {
        super(new BorderLayout(5, 5));
        
        DPanel controlsPanel = new DPanel(new GridLayout(2, 2, 5, 5));
        
        DLabel volumeLabel = new DLabel("Volume:");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        DLabel muteLabel = new DLabel("Mute:");
        muteLabel.setForeground(Color.WHITE);
        muteLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        volumeSlider = createVolumeSlider();
        muteCheckBox = createMuteCheckBox();
        
        controlsPanel.add(volumeLabel);
        controlsPanel.add(volumeSlider);
        controlsPanel.add(muteLabel);
        controlsPanel.add(muteCheckBox);
        
        DPanel titlePanel = new DPanel(new BorderLayout());
        DLabel titleLabel = new DLabel("Sound Controls");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setTextAlignmentCenter();
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(controlsPanel, BorderLayout.CENTER);
        
        setBackground(new Color(50, 50, 50));
        titlePanel.setBackground(new Color(50, 50, 50));
        controlsPanel.setBackground(new Color(50, 50, 50));
        
        setPreferredSize(new Dimension(200, 100));
    }
    
    private DComponent createVolumeSlider() {
        class VolumeSlider extends DComponent {
            private final ChangeListener changeListener;
            
            public VolumeSlider(int min, int max, int value) {
                super(new JSlider(min, max, value));
                JSlider slider = (JSlider) component;
                
                slider.setMajorTickSpacing(25);
                slider.setMinorTickSpacing(5);
                slider.setPaintTicks(true);
                slider.setBackground(new Color(50, 50, 50));
                slider.setForeground(Color.WHITE);
                
                changeListener = new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        float volume = slider.getValue() / 100.0f;
                        soundManager.setVolume(volume);
                        
                        if (!slider.getValueIsAdjusting()) {
                            soundManager.playSound(SoundEffectManager.PLAY_CARD);
                        }
                    }
                };
                
                slider.addChangeListener(changeListener);
            }
            
            public void removeChangeListener() {
                ((JSlider) component).removeChangeListener(changeListener);
            }
        }
        
        int volumePercent = (int) (soundManager.getVolume() * 100);
        return new VolumeSlider(0, 100, volumePercent);
    }
    
    private DComponent createMuteCheckBox() {
        class MuteCheckBox extends DComponent {
            public MuteCheckBox(String text, boolean selected) {
                super(new JCheckBox(text, selected));
                JCheckBox checkBox = (JCheckBox) component;
                
                checkBox.setBackground(new Color(50, 50, 50));
                checkBox.setForeground(Color.WHITE);
                
                checkBox.addActionListener(e -> {
                    boolean muted = checkBox.isSelected();
                    soundManager.setMuted(muted);
                    
                    if (!muted) {
                        soundManager.playSound(SoundEffectManager.PLAY_CARD);
                    }
                });
            }
            
            public boolean isSelected() {
                return ((JCheckBox) component).isSelected();
            }
            
            public void setSelected(boolean selected) {
                ((JCheckBox) component).setSelected(selected);
            }
        }
        
        return new MuteCheckBox("", soundManager.isMuted());
    }
    
    public void playTestSound() {
        soundManager.playSound(SoundEffectManager.PLAY_CARD);
    }
    
    public void dispose() {
        // Remove listeners if needed
    }
}