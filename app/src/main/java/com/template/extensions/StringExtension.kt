package com.template.extensions

import java.util.*

fun String.makeCap() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
fun String.attachedPriceTag() = "$AED $this"
fun String.makeAllCaps() = this.uppercase(Locale.getDefault())
fun String.withPriceTagAndKGInEnd() = "$this $AED_PER_KG"