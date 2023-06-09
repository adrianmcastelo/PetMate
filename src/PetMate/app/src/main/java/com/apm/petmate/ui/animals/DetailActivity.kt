package com.apm.petmate.ui.animals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apm.petmate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animalId = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        val animal = animalFromId(animalId)
        if(animal != null)
        {
            binding.animalImage.setImageResource(animal.photo)
            binding.animalName.text = animal.name
            binding.animalAge.text = animal.age.name
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