# Mehta Run

A fun and engaging endless runner game built with Kotlin and Jetpack Compose for Android.

## Overview

Mehta Run is a fast-paced mobile game where players control a character navigating through three lanes to avoid obstacles. The game features smooth animations, dynamic backgrounds, and background music for an immersive gaming experience.

## Features

- **Three-Lane Gameplay**: Navigate between three lanes to avoid obstacles
- **Smooth Animations**: Fluid jumping and movement mechanics
- **Dynamic Backgrounds**: Infinite scrolling background with 11 unique images
- **Obstacle Variety**: Three different obstacle types (rocks, cones, barrels)
- **Score Tracking**: Real-time score display
- **Background Music**: Immersive audio experience
- **Game Over Screen**: Replay functionality with final score display
- **Responsive Design**: Adapts to different screen sizes

## Controls

- **Swipe Up**: Jump to avoid obstacles
- **Swipe Right**: Move to the right lane
- **Swipe Left**: Move to the left lane

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: 24
- **Target SDK**: 35
- **Java Version**: 11

## Project Structure

```
MehtaRun/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mehta/mehtarun/
│   │   │   │   ├── MainActivity.kt          # Main activity with game logic
│   │   │   │   └── ui/theme/               # Theme configuration
│   │   │   ├── res/
│   │   │   │   ├── drawable/               # Game assets (obstacles, backgrounds)
│   │   │   │   ├── raw/                    # Background music
│   │   │   │   └── values/                 # Colors, strings, themes
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                    # Instrumented tests
│   │   └── test/                           # Unit tests
│   └── build.gradle.kts
├── gradle/                                  # Gradle wrapper
└── build.gradle.kts
```

## Key Components

### MainActivity
- Manages the game lifecycle and MediaPlayer for background music
- Handles splash screen display
- Initializes the game with Compose

### RunnerGame Composable
- Core game logic including:
  - Player movement and jumping mechanics
  - Obstacle detection and collision
  - Score calculation
  - Game over state management

### InfiniteBackgroundScroller Composable
- Renders infinite scrolling background
- Cycles through 11 background images
- Provides smooth visual transitions

## Game Mechanics

- **Jumping**: Smooth arc animation with 30 frames for natural motion
- **Lane Movement**: Instant lane switching with swipe gestures
- **Collision Detection**: Checks player position against obstacles
- **Scoring**: Increments score each time an obstacle passes safely
- **Difficulty**: Obstacles spawn randomly in any of the three lanes

## Dependencies

- androidx.core:core-ktx
- androidx.lifecycle:lifecycle-runtime-ktx
- androidx.activity:activity-compose
- androidx.compose.ui:ui
- androidx.compose.material3:material3
- androidx.test.espresso:espresso-core (testing)

## Building and Running

### Prerequisites
- Android Studio (latest version)
- Android SDK 35
- Gradle 8.11.1

### Build Steps

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Connect an Android device or start an emulator
5. Click "Run" or press `Shift + F10`

### Build Variants

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

## Game Flow

1. **Splash Screen**: 2.5-second intro with fade animation
2. **Game Start**: Background music begins, player appears in middle lane
3. **Gameplay**: Obstacles fall from top, player avoids them
4. **Game Over**: Triggered on collision, displays final score
5. **Replay**: Player can restart the game

## Performance Optimizations

- 12ms game loop update rate for smooth 60 FPS gameplay
- 8ms jump animation frames for fluid motion
- Efficient gesture detection with Compose's pointer input
- Lazy image loading with Compose's Image composable

## Future Enhancements

- Power-ups and special items
- Multiple difficulty levels
- Leaderboard system
- Sound effects for collisions and scoring
- Character customization
- Multiplayer mode

## License

This project is open source and available under the MIT License.

## Author

Developed by Chandan Kumar Yadav

## Support

For issues, suggestions, or contributions, please open an issue or submit a pull request.
