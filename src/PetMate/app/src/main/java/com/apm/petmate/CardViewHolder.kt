package com.apm.petmate

import androidx.recyclerview.widget.RecyclerView
import com.apm.petmate.databinding.CardCellBinding
import com.apm.petmate.ui.animals.AnimalClickListener

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val clickListener: AnimalClickListener
): RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindAnimal(animal: Animal) {
        cardCellBinding.animalImage.setImageResource(animal.photo)
        cardCellBinding.animalName.text = animal.name
        cardCellBinding.animalAge.text = animal.age.name
        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(animal)
        }
    }
}