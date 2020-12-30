package com.matthbr.recicle.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Item(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var description : String,
    var quantity : Int
)