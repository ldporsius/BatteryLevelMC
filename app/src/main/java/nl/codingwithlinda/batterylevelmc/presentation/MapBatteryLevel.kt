package nl.codingwithlinda.batterylevelmc.presentation

import androidx.compose.ui.graphics.Color
import nl.codingwithlinda.batterylevelmc.data.BatteryLevelIndicator
import nl.codingwithlinda.batterylevelmc.ui.theme.green
import nl.codingwithlinda.batterylevelmc.ui.theme.orange
import nl.codingwithlinda.batterylevelmc.ui.theme.red

fun mapBatteryLevelToIndicator(batteryLevel: Float): BatteryLevelIndicator {
    return when(batteryLevel){
        in 0f..20f -> BatteryLevelIndicator.LOW
        in 21f..80f -> BatteryLevelIndicator.MEDIUM
        else -> BatteryLevelIndicator.HIGH
    }
}

fun BatteryLevelIndicator.toColor(): Color {
    return when(this){
        BatteryLevelIndicator.LOW -> red
        BatteryLevelIndicator.MEDIUM -> orange
        BatteryLevelIndicator.HIGH -> green
    }
}