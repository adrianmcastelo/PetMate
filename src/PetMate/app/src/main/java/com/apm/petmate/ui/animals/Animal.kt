package com.apm.petmate.ui.animals

import android.graphics.Bitmap

var animalList = mutableListOf<Animal>()

val ANIMAL_ID_EXTRA = "animalExtra"
class Animal {
    var id:Int = 0
        get() = field
        set(value) { field = value }

    var name:String = ""
        get() = field
        set(value) { field = value }

    var descripcion:String = ""
        get() = field
        set(value) { field = value }

    var age:String = ""
        get() = field
        set(value) { field = value }

    var type:String = ""
        get() = field
        set(value) { field = value }

    var imagen:Bitmap? = null
        get() = field
        set(value) { field = value }

    var fechaNacimiento:String = ""
        get() = field
        set(value) { field = value }

    var protectora:Int? = null
        get() = field
        set(value) { field = value }

    var estado:String = ""
        get() = field
        set(value) { field = value }
}