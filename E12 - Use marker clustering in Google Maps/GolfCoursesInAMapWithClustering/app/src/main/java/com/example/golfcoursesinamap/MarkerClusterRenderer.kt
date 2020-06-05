package com.example.golfcoursesinamap

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MarkerClusterRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<GolfCourseItem>?
) :
    DefaultClusterRenderer<GolfCourseItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: GolfCourseItem,
        markerOptions: MarkerOptions
    ) {
        val courseTypes: Map<String, Float> = mapOf(
            "?" to BitmapDescriptorFactory.HUE_VIOLET,
            "Etu" to BitmapDescriptorFactory.HUE_BLUE,
            "Kulta" to BitmapDescriptorFactory.HUE_GREEN,
            "Kulta/Etu" to BitmapDescriptorFactory.HUE_YELLOW
        )
        val iconColor = courseTypes.get(item.getType())
        markerOptions.icon((iconColor?.let { BitmapDescriptorFactory.defaultMarker(it) }))
    }
}