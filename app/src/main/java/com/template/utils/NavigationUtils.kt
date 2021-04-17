package com.template.utils

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController

/**
 * Navigate using destination ID
 * */
fun View.navigateWithId(id: Int, bundle: Bundle?=null) = try {
    this.findNavController().navigate(id, bundle)
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * Navigate using Nav Directions
 * */
fun View.navigateWithAction(action: NavDirections) = try {
    this.findNavController().navigate(action)
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * Navigate to previous screen
 * */
fun View.navigateBack() = try {
    this.findNavController().navigateUp()
} catch (e: Exception) {
    e.printStackTrace()
}
