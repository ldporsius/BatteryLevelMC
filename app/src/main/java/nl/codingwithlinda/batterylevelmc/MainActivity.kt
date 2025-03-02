package nl.codingwithlinda.batterylevelmc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import nl.codingwithlinda.batterylevelmc.data.BatteryLevelIndicator
import nl.codingwithlinda.batterylevelmc.data.BatteryLevelManager
import nl.codingwithlinda.batterylevelmc.presentation.applyIf
import nl.codingwithlinda.batterylevelmc.presentation.mapBatteryLevelToIndicator
import nl.codingwithlinda.batterylevelmc.presentation.toColor
import nl.codingwithlinda.batterylevelmc.presentation.toIconHighColor
import nl.codingwithlinda.batterylevelmc.presentation.toIconLowColor
import nl.codingwithlinda.batterylevelmc.ui.theme.BatteryLevelMCTheme
import nl.codingwithlinda.batterylevelmc.ui.theme.red
import nl.codingwithlinda.batterylevelmc.ui.theme.redAlternative
import nl.codingwithlinda.batterylevelmc.ui.theme.surface
import nl.codingwithlinda.batterylevelmc.ui.theme.surfaceHigh

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val batteryLevelManager = BatteryLevelManager(this)

            var batteryLevel: Float? by remember {
                mutableStateOf(0f)
            }
            val batteryLevelIndicator: BatteryLevelIndicator by remember {
                derivedStateOf {
                    mapBatteryLevelToIndicator(batteryLevel ?: 0f)
                }
            }
            val batteryLevelIndicatorColor = batteryLevelIndicator.toColor()
            val batteryLevelIconLowColor = batteryLevelIndicator.toIconLowColor()
            val batteryLevelIconHighColor = batteryLevelIndicator.toIconHighColor()

            val animatedLevelWidth = animateFloatAsState(
                targetValue = batteryLevel?.div(100) ?: 0f,
                animationSpec = spring(
                    dampingRatio = 0.3f,
                    stiffness = 100f
                ),
                label = "animatedLevelWidth"
            )

            val infiniteAnimation = rememberInfiniteTransition(
                label = "infiniteIconLowAnimation"
            )
            val animatedIconLowSize by infiniteAnimation.animateFloat(
                initialValue = .9f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                ),
                label = "animatedIconLowSize"
            )

            val animatedIconLowColor by infiniteAnimation.animateColor(
                initialValue = red,
                targetValue = redAlternative,
                animationSpec = infiniteRepeatable(
                    animation = tween(500),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                ), label = "animatedIconLowColor"
            )
            val iconLowColor = if (batteryLevelIndicator == BatteryLevelIndicator.LOW) {
                animatedIconLowColor
            } else {
                batteryLevelIconLowColor
            }

            val animateLowIconToMedium by animateFloatAsState(
                targetValue = batteryLevelIndicator.lowIconSize(),
                animationSpec = tween(1000),
                label = "animatedIconLowSize"
            )

            val animatedIconHighSize by animateFloatAsState(
                targetValue = batteryLevelIndicator.highIconSize(),
                animationSpec = tween(1000),
                label = "animatedIconHighSize"
            )

            //probably hoist this in a viewmodel
            LaunchedEffect(true) {
                while (true){
                    batteryLevelManager.batteryPct().also {level ->
                        batteryLevel = level
                    }
                    delay(100)
                }
            }
            BatteryLevelMCTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = surface
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        
                        Row(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .padding(16.dp)
                            ,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.width(48.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.vector),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .applyIf(
                                            batteryLevelIndicator == BatteryLevelIndicator.LOW,
                                            Modifier.scale(animatedIconLowSize)
                                        )
                                        .size(animateLowIconToMedium.dp),
                                    tint = iconLowColor
                                )
                            }

                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(color = surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .weight(1f)
                                .padding(2.dp)
                                .drawBehind {
                                    val maxSize = this.size
                                    val sizeBattery = maxSize.width * 0.97f

                                    drawRoundRect(
                                        color = surfaceHigh,
                                        size = Size(
                                            width = maxSize.width,
                                            height = maxSize.height / 2f
                                        ),
                                        topLeft = Offset(
                                            x = 0f, y = maxSize.height / 2f / 2
                                        ),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                            16.dp.toPx(), 16.dp.toPx()
                                        )
                                    )

                                    drawRoundRect(
                                        color = surfaceHigh,
                                        size = Size(
                                            width = sizeBattery,
                                            height = maxSize.height
                                        ),
                                        topLeft = Offset(
                                            x = 0f, y = 0f
                                        ),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                            16.dp.toPx(), 16.dp.toPx()
                                        )
                                    )

                                    drawRoundRect(
                                        color = batteryLevelIndicatorColor,
                                        size = Size(
                                            width = sizeBattery * animatedLevelWidth.value,
                                            height = maxSize.height
                                        ),
                                        topLeft = Offset(
                                            x = 0f, y = 0f
                                        ),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                            16.dp.toPx(), 16.dp.toPx()
                                        )
                                    )
                                    for (i in 1..4) {
                                        drawRoundRect(
                                            color = surfaceHigh,
                                            topLeft = Offset(
                                                x = (maxSize.width/5) * i,
                                                y = 0f
                                            ),
                                            size = Size(
                                                width =  2.dp.toPx(),
                                                height = maxSize.height
                                            )
                                            ,
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                                16.dp.toPx(), 16.dp.toPx()
                                            )
                                        )
                                    }
                                }

                            ) {
                                Spacer(Modifier.fillMaxSize())
                            }

                            Box(modifier = Modifier.width(48.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.union),
                                    contentDescription = null,
                                    tint = batteryLevelIconHighColor,
                                    modifier = Modifier
                                        .size(
                                            animatedIconHighSize.dp
                                        )


                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

