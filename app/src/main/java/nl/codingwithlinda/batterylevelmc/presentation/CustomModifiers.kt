package nl.codingwithlinda.batterylevelmc.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.applyIf(condition: Boolean, modifier: Modifier): Modifier {

    return if (condition) {
        this.then(modifier)
    } else {
        this
    }
}