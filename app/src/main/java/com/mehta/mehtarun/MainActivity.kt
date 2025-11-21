package com.mehta.mehtarun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mehta.mehtarun.ui.theme.MehtaRunTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random
import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer?.isLooping = true
        
        setContent {
            MehtaRunTheme {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    SplashScreen {
                        showSplash = false
                    }
                } else {
                    RunnerGame()
                    mediaPlayer?.start()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }
}

@Composable
fun SplashScreen(onSplashScreenFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        onSplashScreenFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.admin),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mehta Run",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}

@Composable
fun RunnerGame() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val lanePositions = listOf(30f, screenWidth / 2f - 75f, screenWidth - 180f) // Adjusted for larger character

    val obstacleImages = listOf(
        R.drawable.rock,
        R.drawable.cone,
        R.drawable.barrel
    )
    var currentObstacleImage by remember { mutableStateOf(obstacleImages.random()) }

    var laneIndex by remember { mutableStateOf(1) }

    var isJumping by remember { mutableStateOf(false) }
    var jumpOffset by remember { mutableStateOf(0f) }

    val playerY = screenHeight - 350f // Adjusted position for larger character

    var obstacleY by remember { mutableStateOf(0f) }
    var obstacleLane by remember { mutableStateOf(Random.nextInt(0, 3)) }

    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    val fallSpeed = 10f

    val gestureModifier = Modifier
        .pointerInput(Unit) {
            detectVerticalDragGestures { _, dragAmount ->
                if (dragAmount < -50 && !isJumping && !gameOver) {
                    isJumping = true
                }
            }
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (!gameOver) {
                    if (dragAmount > 50 && laneIndex < 2) {
                        laneIndex++
                    } else if (dragAmount < -50 && laneIndex > 0) {
                        laneIndex--
                    }
                }
            }
        }

    // Improved jumping animation
    LaunchedEffect(isJumping) {
        if (isJumping) {
            for (i in 0..30) { // Increased steps for smoother animation
                jumpOffset = -i * 6f // Adjusted jump height
                delay(8) // Reduced delay for smoother animation
            }
            for (i in 30 downTo 0) {
                jumpOffset = -i * 6f
                delay(8)
            }
            jumpOffset = 0f
            isJumping = false
        }
    }

    // Faster game loop for smoother gameplay
    LaunchedEffect(gameOver) {
        if (!gameOver) {
            while (true) {
                obstacleY += fallSpeed

                if (obstacleY > screenHeight) {
                    score += 1
                    obstacleY = 0f
                    obstacleLane = Random.nextInt(0, 3)
                    currentObstacleImage = obstacleImages.random()
                }

                if (abs(obstacleY - playerY) < 80 &&
                    obstacleLane == laneIndex &&
                    !isJumping
                ) {
                    gameOver = true
                    break
                }

                delay(12L) // Reduced delay for smoother movement
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.DarkGray)
            .then(gestureModifier)
    ) {
        InfiniteBackgroundScroller(screenHeight = screenHeight)
        // Draw 3 lane background
        Row(modifier = Modifier.fillMaxSize()) {
//            repeat(3) { index ->
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight()
//                        .background(if (index % 2 == 0) Color(0xFF333333) else Color(0xFF444444))
//                )
//            }
        }

        // Score
        Text(
            text = "Score: $score",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Admin (player)
        if (!gameOver) {
            Image(
                painter = painterResource(id = R.drawable.admin),
                contentDescription = "Admin",
                modifier = Modifier
                    .size(150.dp) // Increased size from 100.dp to 150.dp
                    .offset(x = lanePositions[laneIndex].dp, y = (playerY + jumpOffset).dp)
            )
        }

        // Obstacle as PNG Image
        if (!gameOver) {
            Image(
                painter = painterResource(id = currentObstacleImage),
                contentDescription = "Obstacle",
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = lanePositions[obstacleLane].dp, y = obstacleY.dp)
            )
        }

        // Game Over UI
        if (gameOver) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Game Over",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Final Score: $score",
                    color = Color.White,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    gameOver = false
                    obstacleY = 0f
                    obstacleLane = Random.nextInt(0, 3)
                    laneIndex = 1
                    jumpOffset = 0f
                    isJumping = false
                    score = 0
                }) {
                    Text("Replay")
                }
            }
        }
    }
}

@Composable
fun InfiniteBackgroundScroller(screenHeight: Int) {
    val bgImages = listOf(
        R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4,
        R.drawable.bg5, R.drawable.bg6, R.drawable.bg7, R.drawable.bg8,
        R.drawable.bg9, R.drawable.bg10, R.drawable.bg11
    )

    var scrollOffset by remember { mutableStateOf(0f) }
    val scrollSpeed = 5f
    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            scrollOffset += scrollSpeed  // Changed back to += for downward scrolling
            if (scrollOffset >= screenHeight) {
                scrollOffset = 0f
                currentImageIndex = (currentImageIndex + 1) % bgImages.size
            }
            delay(16L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show current and previous image for smooth transition
        for (offset in -1..0) {  // Changed from 0..1 to -1..0
            val index = ((currentImageIndex + offset + bgImages.size) % bgImages.size)
            Image(
                painter = painterResource(id = bgImages[index]),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight.dp)
                    .offset(y = (scrollOffset + (offset * screenHeight)).dp)
            )
        }
    }
}

