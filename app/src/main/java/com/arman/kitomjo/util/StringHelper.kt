package com.arman.kitomjo.util

fun String.processInput(): String {
    var baseCost = this
    baseCost = if (this.isEmpty()) {
        this
    } else {
        when (this.toDoubleOrNull()) {
            null -> baseCost
            else -> this
        }
    }
    return baseCost
}

fun String.matchesDouble(): Boolean {
    val pattern = Regex("^\\d+\$")
    return matches(pattern)
}