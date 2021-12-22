package com.example.scraping.repository.mappers

import kotlin.math.roundToInt

fun String.statsToInt(): Int {
    var stats = this

    if(this.contains(",")) {
        stats = this.replace(",", ".")
    }

    if (stats.contains("K")) {
        val quantityWithoutK = stats.replace("K", "")
        return quantityWithoutK.toDouble().roundToInt() * 1000
    }

    if (stats.contains("M")) {
        val quantityWithoutK = stats.replace("M", "")
        return quantityWithoutK.toDouble().roundToInt() * 1000000
    }

    return this.toInt()
}

