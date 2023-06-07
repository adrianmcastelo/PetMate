package com.apm.petmate.ui.animals

var animalList = mutableListOf<Animal>()

val ANIMAL_ID_EXTRA = "animalExtra"
class Animal (
    var name: String,
    var age: Enum<AgeEnum>,
    var type: Enum<TypeEnum>,
    var photo: Int,
    var id: Int? = animalList.size
    )