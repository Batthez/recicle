package com.matthbr.recicle.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Item(
    @PrimaryKey(autoGenerate = true)
    private val id : Int,
    private val description : String,
    private val quantity : Int
)