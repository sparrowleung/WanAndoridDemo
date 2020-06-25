package com.learning.common.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class SystemChildBean(
    @Ignore
    var children: ArrayList<SystemChildBean>,
    var courseId: Int,
    var id: Int,
    var name: String,
    @PrimaryKey(autoGenerate = true)
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: String,
    var visible: Int
){
    constructor():this(ArrayList<SystemChildBean>(),0,0,"",0,0,"",0)
}
