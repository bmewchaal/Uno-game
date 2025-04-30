package uno.gui;

import dgui.*;
import uno.core.SoundEffectManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class MainMenuPanel extends DPanel {
    private static final Color BACKGROUND_COLOR_TOP = new Color(50, 0, 100);
    private static final Color BACKGROUND_COLOR_BOTTOM = new Color(0, 0, 0);
    private static final Color BUTTON_COLOR = new Color(255, 50, 50);
    private static final Color BUTTON_HOVER_COLOR = new Color(255, 100, 100);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    
    private DActionListener startGameAction;
    private DActionListener settingsAction;
    private DActionListener exitAction;
    
    public MainMenuPanel() {
        super(new BorderLayout());
        
        DPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        DPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.CENTER);
        
        DLabel versionLabel = new DLabel("v1.0");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setTextAlignmentRight();
        add(versionLabel, BorderLayout.SOUTH);
        
        setBackground(BACKGROUND_COLOR_BOTTOM);
    }
    
    private DPanel createTitlePanel() {
        // Use a custom JPanel for drawing
        class TitlePanelContent extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw card-like shapes in the background
                drawCardShapes(g2d, width, height);
                
                g2d.dispose();
            }
            
            private void drawCardShapes(Graphics2D g2d, int width, int height) {
                // Draw some UNO-like card shapes in the background
                Color[] cardColors = {
                    new Color(220, 0, 0),    // Red
                    new Color(0, 180, 0),    // Green
                    new Color(0, 0, 220),    // Blue
                    new Color(220, 180, 0)   // Yellow
                };
                
                for (int i = 0; i < 6; i++) {
                    int x = (int) (Math.random() * width * 0.8);
                    int y = (int) (Math.random() * height * 0.7);
                    int size = 40 + (int) (Math.random() * 40);
                    double angle = Math.random() * Math.PI * 2;
                    
                    g2d.rotate(angle, x + size/2, y + size/2);
                    g2d.setColor(cardColors[i % cardColors.length]);
                    RoundRectangle2D.Double card = new RoundRectangle2D.Double(
                        x, y, size * 0.7, size, 10, 10);
                    g2d.fill(card);
                    g2d.setColor(Color.WHITE);
                    g2d.draw(card);
                    g2d.rotate(-angle, x + size/2, y + size/2);
                }
            }
        }
        
        // Create the panel with our custom content
        JPanel contentPanel = new TitlePanelContent();
        contentPanel.setPreferredSize(new Dimension(600, 150));
        contentPanel.setBackground(new Color(30, 30, 30));
        
        // Create a DPanel to wrap the content
        DPanel panel = new DPanel(new BorderLayout());
        
        // Create title label
        DLabel titleLabel = new DLabel("UNO Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setTextAlignmentCenter();
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private DPanel createButtonsPanel() {
        // Use a custom JPanel for drawing
        class ButtonsPanelContent extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw gradient background
                GradientPaint gp = new GradientPaint(
                    0, 0, BACKGROUND_COLOR_TOP,
                    0, height, BACKGROUND_COLOR_BOTTOM);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
                
                g2d.dispose();
            }
        }
        
        // Create the panel with our custom content
        JPanel contentPanel = new ButtonsPanelContent();
        
        // Create a DPanel to wrap the content
        DPanel panel = new DPanel(new BorderLayout());
        
        // Create a panel for the buttons
        DPanel buttonsSubPanel = new DPanel(new GridLayout(3, 1, 0, 20));
        
        // Create buttons
        DButton startButton = createMenuButton("New Game");
        DButton settingsButton = createMenuButton("Settings");
        DButton exitButton = createMenuButton("Exit");
        
        // Add action listeners
        startButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                if (startGameAction != null) {
                    SoundEffectManager.getInstance().playSound(SoundEffectManager.PLAY_CARD);
                    startGameAction.actionPerformed(e);
                }
            }
        });
        
        settingsButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                if (settingsAction != null) {
                    SoundEffectManager.getInstance().playSound(SoundEffectManager.PLAY_CARD);
                    settingsAction.actionPerformed(e);
                }
            }
        });
        
        exitButton.addActionListener(new DActionListener() {
            @Override
            public void actionPerformed(DActionEvent e) {
                if (exitAction != null) {
                    SoundEffectManager.getInstance().playSound(SoundEffectManager.PLAY_CARD);
                    exitAction.actionPerformed(e);
                }
            }
        });
        
        // Add buttons to the panel
        buttonsSubPanel.add(startButton);
        buttonsSubPanel.add(settingsButton);
        buttonsSubPanel.add(exitButton);
        
        // Center the buttons panel
        DPanel centerPanel = new DPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonsSubPanel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private DButton createMenuButton(String text) {
        DButton button = new DButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        return button;
    }
    
    public void setStartGameAction(DActionListener action) {
        this.startGameAction = action;
    }
    
    public void setSettingsAction(DActionListener action) {
        this.settingsAction = action;
    }
    
    public void setExitAction(DActionListener action) {
        this.exitAction = action;
    }
}