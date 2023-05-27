package com.apm.petmate

var animalList = mutableListOf<Animal>()

class Animal (
    var name: String,
    var age: Enum<AgeEnum>,
    var type: Enum<TypeEnum>,
    var photo: Int
    )