package com.apm.petmate.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.recyclerview.widget.GridLayoutManager
import com.apm.petmate.R
import com.apm.petmate.ui.animals.Animal
import com.apm.petmate.ui.animals.CardAdapter
import com.apm.petmate.ui.animals.animalList
import org.json.JSONObject

class AnimalUtils {

    fun parseAnimals(response: JSONObject):ArrayList<Animal>{
        var animalsJSON = JSONObject(response.toString()).getJSONArray("data")
        var animals = ArrayList<Animal>()

        for (i in 0 until animalsJSON.length()) {
            var animalJSON: JSONObject = animalsJSON.getJSONObject(i)
            var animal: Animal = Animal()
            animal.id = animalJSON.getInt("id")
            animal.name = animalJSON.getString("name").toString()
            animal.age = animalJSON.getString("edad").toString()
            animal.type = animalJSON.getString("tipo").toString()
            animal.imagen = base64ToBitmap(animalJSON.getString("imagen").toString())
            animal.protectora = animalJSON.getInt("protectora")
            animal.descripcion = animalJSON.getString("descripcion").toString()
            animal.fechaNacimiento = animalJSON.getString("fechaNacimiento").toString()
            animal.estado = animalJSON.getString("estado").toString()
            when (animal.estado) {
                "AD" -> animal.iconoEstado = R.drawable.animal_shelter
                "DP" -> animal.iconoEstado = R.drawable.adopcion
            }
            when (animal.age) {
                "CA" -> animal.translateAge = "Cachorro"
                "AD" -> animal.translateAge = "Adulto"
                "SN" -> animal.translateAge = "Senior"
            }

            animals.add(animal)
        }

        println("Animals list:" + animals.size)
        animalList = animals
        return animals
    }

    fun base64ToBitmap(image: String): Bitmap? {
        val imageBytes = Base64.decode(image, 0)
        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return imageBitmap
    }


}