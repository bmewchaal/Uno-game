package uno.gui;

import dgui.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class SettingsPanel extends DPanel {
    private DActionListener backAction;
    private SoundControlPanel soundControlPanel;
    
    public SettingsPanel() {
        super(new BorderLayout(10, 10));
        
        // Create title panel
        DPanel titlePanel = new DPanel(new BorderLayout());
        DLabel titleLabel = new DLabel("Game Settings");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setTextAlignmentCenter();
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBackground(new Color(50, 50, 100));
        
        // Create settings content panel
        DPanel contentPanel = new DPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBackground(new Color(30, 30, 50));
        
        // Add sound control panel
        soundControlPanel = new SoundControlPanel();
        contentPanel.add(soundControlPanel);
        
        // Add game rules panel
        DPanel rulesPanel = createRulesPanel();
        contentPanel.add(rulesPanel);
        
        // Create button panel
        DPanel buttonPanel = new DPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(30, 30, 50));
        
        DButton backButton = new DButton("Back to Menu");
        backButton.setBackground(new Color(80, 80, 150));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                if (backAction != null) {
                    backAction.actionPerformed(e);
                }
            }
        });
        
        buttonPanel.add(backButton, BorderLayout.EAST);
        
        // Add all panels to the main panel
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set background color
        setBackground(new Color(30, 30, 50));
        
        // Set preferred size
        setPreferredSize(new Dimension(600, 400));
    }
    
    private DPanel createRulesPanel() {
        DPanel panel = new DPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(40, 40, 60));
        
        // Create title
        DLabel titleLabel = new DLabel("Game Rules");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setTextAlignmentCenter();
        
        // Create checkboxes for various rules
        DPanel checkboxPanel = new DPanel(new GridLayout(2, 2, 5, 5));
        checkboxPanel.setBackground(new Color(40, 40, 60));
        
        // Create and add checkboxes with custom styling
        checkboxPanel.add(createStyledCheckBox("Draw until playable card"));
        checkboxPanel.add(createStyledCheckBox("Play drawn card immediately"));
        checkboxPanel.add(createStyledCheckBox("Stack +2 cards"));
        checkboxPanel.add(createStyledCheckBox("Stack +4 cards"));
        
        // Add components to the panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(checkboxPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private DComponent createStyledCheckBox(String text) {
        class StyledCheckBox extends DComponent {
            public StyledCheckBox(String text) {
                super(new javax.swing.JCheckBox(text));
                javax.swing.JCheckBox checkBox = (javax.swing.JCheckBox) component;
                
                checkBox.setForeground(Color.WHITE);
                checkBox.setBackground(new Color(40, 40, 60));
                checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
                checkBox.setFocusPainted(false);
            }
        }
        
        return new StyledCheckBox(text);
    }
    
    public void setBackAction(DActionListener action) {
        this.backAction = action;
    }
}