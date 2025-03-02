package nl.codingwithlinda.batterylevelmc.data

enum class BatteryLevelIndicator {
    LOW {
        override fun lowIconSize() = 48f
        override fun highIconSize() = 32f
    },
    MEDIUM{
        override fun lowIconSize() = 32f
        override fun highIconSize() = 32f
    },
    HIGH{
        override fun lowIconSize() = 32f
        override fun highIconSize() = 48f
    };

    abstract fun lowIconSize(): Float
    abstract fun highIconSize(): Float
}