package com.solecloth7.cosmeticsPluginOPL.util

object ColorUtil {
    fun gradientName(text: String, hexColors: List<String>, bold: Boolean = false): String {
        if (text.isEmpty() || hexColors.isEmpty()) return text
        if (hexColors.size == 1) {
            val hex = hexColors.first()
            return toColorCode(hex) + (if (bold) "§l" else "") + text
        }
        val chars = text.toCharArray()
        val colors = createGradient(hexColors, chars.size)
        val sb = StringBuilder()
        for (i in chars.indices) {
            sb.append(toColorCode(colors[i]))
            if (bold) sb.append("§l")
            sb.append(chars[i])
        }
        return sb.toString()
    }

    private fun toColorCode(hex: String): String {
        val cleanHex = hex.removePrefix("#")
        return "§x" + cleanHex.toCharArray().joinToString("") { "§$it" }
    }

    private fun createGradient(hexColors: List<String>, steps: Int): List<String> {
        val rgbList = hexColors.map { hexToRgb(it) }
        val segments = rgbList.size - 1
        val stepsPerSegment = steps / segments
        val colors = mutableListOf<String>()
        var leftover = steps - (stepsPerSegment * segments)
        for (i in 0 until segments) {
            val start = rgbList[i]
            val end = rgbList[i + 1]
            val count = stepsPerSegment + if (leftover > 0) 1 else 0
            if (leftover > 0) leftover--
            for (s in 0 until count) {
                val ratio = if (count == 1) 0.0 else s.toDouble() / (count - 1)
                val r = lerp(start[0], end[0], ratio).toInt()
                val g = lerp(start[1], end[1], ratio).toInt()
                val b = lerp(start[2], end[2], ratio).toInt()
                colors.add(String.format("#%02X%02X%02X", r, g, b))
            }
        }
        return colors.take(steps)
    }

    private fun hexToRgb(hex: String): IntArray {
        val clean = hex.removePrefix("#")
        require(clean.length == 6) { "Hex color $hex must be exactly 6 digits." }
        return intArrayOf(
            Integer.valueOf(clean.substring(0, 2), 16),
            Integer.valueOf(clean.substring(2, 4), 16),
            Integer.valueOf(clean.substring(4, 6), 16)
        )
    }


    private fun lerp(a: Int, b: Int, t: Double): Double = a + (b - a) * t
}
