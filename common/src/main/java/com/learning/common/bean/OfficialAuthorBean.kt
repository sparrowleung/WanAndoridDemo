package com.learning.common.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class OfficialAuthorBean(
    var courseId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: String,
    var visible: Int
)