# UnoGui - UNO Card Game with Custom GUI Framework

A feature-rich implementation of the classic UNO card game in Java, featuring a custom GUI framework (DGUI) with beautiful wood-themed graphics, animations, and sound effects.

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Status](https://img.shields.io/badge/status-Active-success.svg)

## 🎮 Features

### Game Features
- **Complete UNO Rules Implementation**
  - All standard UNO cards (0-9, Skip, Reverse, Draw Two, Wild, Wild Draw Four)
  - Full game logic with proper turn management
  - Direction changes and special card effects
  - UNO call system with penalties

### Visual Features
- **Custom DGUI Framework**: Lightweight GUI abstraction layer over Swing
- **Wood-Themed Interface**: Realistic wood texture backgrounds
- **Smooth Animations**: Card drawing, playing, and movement animations
- **Professional Card Design**: Custom-rendered UNO cards with gradients and effects
- **Responsive Layout**: Adaptive UI that works on different screen sizes

### Technical Features
- **AI Players**: Smart AI opponents with strategic card playing
- **Sound System**: Complete sound effect management (music, card sounds, UNO calls)
- **Event System**: Comprehensive game event listeners
- **Modular Architecture**: Clean separation of concerns (core, GUI, utils)

## 📋 Requirements

- **Java Development Kit (JDK)**: 17 or higher
- **Build Tool**: Maven or Ant (optional)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

## 🚀 Quick Start

### Option 1: Using Command Line

```bash
# Clone the repository
git clone https://github.com/bmewchaal/UnoGui.git
cd UnoGui

# Compile the project
javac -d bin -sourcepath src src/uno/*.java src/uno/**/*.java src/dgui/*.java src/dgui/**/*.java

# Run the game
java -cp bin uno.UnoCompleteDGUI
```

### Option 2: Using IDE

1. **Import Project**
   - Open your IDE (IntelliJ IDEA recommended)
   - File → Open → Select the `UnoGui` folder
   - Wait for indexing to complete

2. **Set Up JDK**
   - Ensure JDK 17+ is configured
   - File → Project Structure → Project SDK

3. **Run the Game**
   - Navigate to `src/uno/UnoCompleteDGUI.java`
   - Right-click → Run 'UnoCompleteDGUI.main()'

## 🎯 Game Controls

### During Your Turn
- **Click a Card**: Select a card from your hand
- **Play Card Button**: Play the selected card (if valid)
- **Draw Card Button**: Draw a card from the deck
- **Call UNO Button**: Call UNO when you have one card left

### Wild Cards
- When you play a Wild or Wild Draw Four card
- Color selection dialog appears
- Click the color you want to change to

### Navigation
- Cards are displayed in an overlapping fan layout
- Selected cards highlight in yellow
- Hover over cards for visual feedback

## 🏗️ Project Structure

```
UnoGui/
├── src/
│   ├── dgui/                      # Custom GUI Framework
│   │   ├── DComponent.java        # Base component wrapper
│   │   ├── DPanel.java            # Panel component
│   │   ├── DButton.java           # Button with custom styling
│   │   ├── DLabel.java            # Label component
│   │   ├── DFrame.java            # Frame wrapper
│   │   ├── DAnimator.java         # Animation system
│   │   ├── DImageManager.java     # Image/texture management
│   │   ├── themes/
│   │   │   └── DTheme.java        # Theme system
│   │   └── [other GUI components]
│   │
│   └── uno/
│       ├── core/                  # Game Logic
│       │   ├── Card.java          # Card model
│       │   ├── CardColor.java     # Card colors enum
│       │   ├── CardValue.java     # Card values enum
│       │   ├── Player.java        # Player model
│       │   ├── Game.java          # Main game engine
│       │   └── GameEventListener.java
│       │
│       ├── gui/                   # Game GUI Components
│       │   ├── DUnoCard.java      # Visual card component
│       │   ├── DWoodPanel.java    # Wood-textured panel
│       │   ├── DWoodGamePanel.java # Main game board
│       │   ├── MainMenuPanel.java # Menu screen
│       │   ├── SoundManager.java  # Sound effects
│       │   └── [other GUI panels]
│       │
│       └── UnoCompleteDGUI.java   # Main entry point
│
├── README.md                      # This file
├── LICENSE                        # MIT License
├── .gitignore                     # Git ignore rules
└── CONTRIBUTING.md                # Contribution guidelines
```

## 🎨 Architecture Overview

### Core Layer (`uno.core`)
- **Game Logic**: Turn management, rule enforcement, win conditions
- **Data Models**: Card, Player, Deck representations
- **Event System**: Observer pattern for game state changes

### GUI Layer (`dgui`)
- **Component System**: Lightweight wrappers over Swing components
- **Theme Support**: Consistent styling across the application
- **Animation Framework**: Smooth transitions and effects
- **Layout Managers**: Custom layouts for card hands

### Integration Layer (`uno.gui`)
- **Game Components**: Specialized UI for UNO gameplay
- **Sound Management**: Audio playback and control
- **Visual Effects**: Card animations, wood textures

## 🔧 Configuration

### Themes
Change the game theme in `DWoodPanel.java`:

```java
// Light wood (default)
mainPanel.createWoodTexture(DWoodPanel.LIGHT_WOOD);

// Dark wood
mainPanel.createWoodTexture(DWoodPanel.DARK_WOOD);

// Red wood
mainPanel.createWoodTexture(DWoodPanel.RED_WOOD);
```

### Sound Settings
Adjust sound in `SoundManager.java`:

```java
SoundManager soundManager = new SoundManager();
soundManager.setVolume(0.7f);  // 70% volume
soundManager.setMuted(false);
```

### Game Rules
Modify game settings in `Game.java`:

```java
// Number of starting cards (default: 7)
private static final int STARTING_CARDS = 7;

// Enable/disable special rules
private boolean allowStackingDrawCards = true;
private boolean playDrawnCardImmediately = false;
```

## 🎮 Gameplay Rules

### Standard Rules
1. **Starting**: Each player gets 7 cards
2. **Turns**: Play proceeds clockwise (or counter-clockwise after Reverse)
3. **Valid Plays**: Match color, number, or play a Wild card
4. **Drawing**: If you can't play, draw a card
5. **UNO**: Call "UNO" when you have one card left
6. **Winning**: First player to play all cards wins

### Special Cards
- **Skip**: Next player loses their turn
- **Reverse**: Direction of play reverses
- **Draw Two (+2)**: Next player draws 2 cards and loses turn
- **Wild**: Choose any color
- **Wild Draw Four (+4)**: Choose color, next player draws 4 and loses turn

## 🛠️ Development

### Building from Source

```bash
# Create bin directory
mkdir -p bin

# Compile all Java files
find src -name "*.java" | xargs javac -d bin

# Create JAR file (optional)
jar cfe UnoGui.jar uno.UnoCompleteDGUI -C bin .

# Run the JAR
java -jar UnoGui.jar
```

### Running Tests

```bash
# Compile test files
javac -d bin -cp bin:junit.jar test/**/*.java

# Run tests
java -cp bin:junit.jar org.junit.runner.JUnitCore uno.core.GameTest
```

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Comment complex logic
- Keep methods focused and short (<50 lines)

## 🐛 Known Issues

1. **Sound System**: Currently uses placeholder sounds (silent mode)
   - Solution: Add actual .wav files to resources folder

2. **AI Intelligence**: Basic AI implementation
   - Future: Implement more strategic decision-making

3. **Network Play**: Not yet implemented
   - Future: Add multiplayer over network support

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Write clean, readable code
- Add comments for complex logic
- Test your changes thoroughly
- Update documentation as needed

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **bmewchaal** - *Initial work* - [GitHub Profile](https://github.com/bmewchaal)

## 🙏 Acknowledgments

- Mattel Inc. for the original UNO card game
- Java Swing team for the GUI framework
- Contributors and testers

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/bmewchaal/UnoGui/issues)
- **Discussions**: [GitHub Discussions](https://github.com/bmewchaal/UnoGui/discussions)
- **Email**: [Contact](mailto:bmewchaal@example.com)

## 🗺️ Roadmap

### Version 2.0
- [ ] Online multiplayer support
- [ ] Tournament mode
- [ ] Player statistics and leaderboards
- [ ] Custom card skins
- [ ] Mobile version (Android/iOS)

### Version 1.5
- [ ] Improved AI difficulty levels
- [ ] Save/load game state
- [ ] Replay system
- [ ] More sound effects and music
- [ ] Achievements system

### Current Version (1.0)
- [x] Complete UNO rules
- [x] AI players
- [x] Custom GUI framework
- [x] Animations
- [x] Sound system
- [x] Wood-themed interface

## 📊 Statistics

- **Lines of Code**: ~10,000+
- **Classes**: 50+
- **Development Time**: 3+ months
- **Language**: Java 17

## 🎓 Learning Resources

- [Official UNO Rules](https://www.unorules.com/)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Game Development Patterns](https://gameprogrammingpatterns.com/)

---

**Enjoy playing UNO! 🎉**

*Made with ❤️ by bmewchaal*
