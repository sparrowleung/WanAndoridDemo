package com.learning.common.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistoryBean(
    val dateTime: Long,
    @PrimaryKey
    val historyStr: String
)