package nl.codingwithlinda.batterylevelmc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import nl.codingwithlinda.batterylevelmc.data.BatteryLevelManager
import nl.codingwithlinda.batterylevelmc.ui.theme.BatteryLevelMCTheme
import nl.codingwithlinda.batterylevelmc.ui.theme.orange
import nl.codingwithlinda.batterylevelmc.ui.theme.surface
import nl.codingwithlinda.batterylevelmc.ui.theme.surfaceHigh
import nl.codingwithlinda.batterylevelmc.ui.theme.surfaceLow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val batteryLevelManager = BatteryLevelManager(this)

            var batteryLevel: Float? by remember {
                mutableStateOf(0f)
            }

            LaunchedEffect(true) {
                while (true){
                    batteryLevelManager.batteryPct().also {level ->
                        println("Battery level is $level")
                        batteryLevel = level
                    }
                    delay(1000)
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
                                .height(200.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check, contentDescription = null
                            )
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(color = surfaceHigh,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .weight(1f)
                                .padding(2.dp)
                                .drawBehind {
                                    val maxSize = this.size

                                    val indicatorBlockScaleFactor  = batteryLevel?.div(100) ?: 0f

                                    drawRoundRect(
                                        color = orange,
                                        size = Size(
                                            width = maxSize.width * indicatorBlockScaleFactor,
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

                            Icon(
                                imageVector = Icons.Default.Settings, contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

