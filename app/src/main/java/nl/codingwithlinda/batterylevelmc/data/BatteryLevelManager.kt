package nl.codingwithlinda.batterylevelmc.data

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class BatteryLevelManager (
    private val context: Context
){


   /* val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL

    // How are we charging?
    val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
    val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
*/
    fun batteryPct(): Float?  {
        val intent: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        intent ?: return null
        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level * 100 / scale.toFloat()
    }

}