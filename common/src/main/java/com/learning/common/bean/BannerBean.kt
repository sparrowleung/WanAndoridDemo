package com.learning.common.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BannerBean(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var url: String,
    var desc: String,
    var title: String,
    var imagePath: String
)