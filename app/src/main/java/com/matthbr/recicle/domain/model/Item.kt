package com.matthbr.recicle.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val description : String,
    val quantity : Int
)