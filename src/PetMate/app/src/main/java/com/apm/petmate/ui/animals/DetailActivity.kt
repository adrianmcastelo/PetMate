package com.apm.petmate.ui.animals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apm.petmate.R
import com.apm.petmate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var idProtectora:Int? = null
    private var token:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.idProtectora = this.intent.extras?.getInt("idProtectora")
        this.token = this.intent.extras?.getString("token")

        val animalId = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        val animal = animalFromId(animalId)
        if(animal != null)
        {
            binding.animalImage.setImageBitmap(animal.imagen)
            binding.animalName.text = animal.name
            binding.animalDescription.text = animal.descripcion
            binding.animalType.text = animal.type
            when (animal.estado) {
                "AD" -> {
                    binding.animalState.text = "Adoptado"
                    binding.iconState.setImageResource(R.drawable.animal_shelter)
                }
                "DP" -> {
                    binding.animalState.text = "En adopción"
                    binding.iconState.setImageResource(R.drawable.adopcion)
                }
            }
            when (animal.age) {
                "CA" -> binding.animalAge.text = "Cachorro"
                "AD" -> binding.animalAge.text = "Adulto"
                "SN" -> binding.animalAge.text = "Senior"
            }
            binding.animalBornDate.text = animal.fechaNacimiento
        }

        when (idProtectora){
            (0) -> {
                binding.button.setIconResource(R.drawable.baseline_favorite_border_24)
            }
            else -> {
                when (animal?.estado) {
                    "AD" -> {
                        binding.button.setIconResource(R.drawable.adopcion)
                    }
                    "DP" -> {
                        binding.button.setIconResource(R.drawable.animal_shelter)
                    }
                }
            }
        }

        binding.button.setOnClickListener {
            when (idProtectora){
                (0) -> {
                    //TODO hacer favorito o no
                }
                else -> {
                    //TODO poner a adoptado o en adopción
                }
            }
        }
    }

    private fun animalFromId(animalId: Int): Animal?
    {
        for(animal in animalList)
        {
            if(animal.id == animalId)
                return animal
        }
        return null
    }
}