package com.apm.petmate

import androidx.recyclerview.widget.RecyclerView
import com.apm.petmate.databinding.CardCellBinding

class CardViewHolder(
    private val cardCellBinding: CardCellBinding
): RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindAnimal(animal: Animal) {
        cardCellBinding.animalImage.setImageResource(animal.photo)
        cardCellBinding.animalName.text = animal.name
        cardCellBinding.animalAge.text = animal.age.name
    }
}