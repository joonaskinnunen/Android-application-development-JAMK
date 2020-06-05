package com.example.golfcoursesinamap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class GolfCourseItem : ClusterItem {
    private val mPosition: LatLng
    private val mSnippet: String = ""
    private val mType: String
    private val mTag: List<String>

    constructor(
        lat: Double,
        lng: Double,
        type: String,
        tag: List<String>
    ) {
        this.mPosition = LatLng(lat, lng)
        this.mType = type
        this.mTag = tag
    }

    override fun getSnippet(): String? {
        return mSnippet
    }

    override fun getTitle(): String? {
        return mTag[0]
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    fun getType(): String {
        return mType
    }

    fun getTag(): List<String> {
        return mTag
    }

}