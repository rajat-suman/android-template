package com.template.extensions

fun Float?.formatPrice(uptoDecimal: Int) = String.format("%.${uptoDecimal}f", this ?: 0.00f)
fun Double?.formatPrice(uptoDecimal: Int) = String.format("%.${uptoDecimal}f", this ?: 0.00)