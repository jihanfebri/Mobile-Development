package com.skinective.network.responses

import com.google.android.gms.maps.model.LatLng

data class Hospital(
    val name: String,
    val address: String,
    val location: LatLng,
    val openingHours: String, // Example property
    val distance: String, // Example property
    var travelTime: String // Travel time property
)
