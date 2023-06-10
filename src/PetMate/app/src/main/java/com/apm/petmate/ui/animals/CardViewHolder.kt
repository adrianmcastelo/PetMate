package com.apm.petmate.ui.animals

import androidx.recyclerview.widget.RecyclerView
import com.apm.petmate.databinding.CardCellBinding

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val clickListener: AnimalClickListener
): RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindAnimal(animal: Animal) {
        cardCellBinding.animalImage.setImageBitmap(animal.imagen)
        cardCellBinding.animalName.text = animal.name
        cardCellBinding.animalAge.text = animal.age
        cardCellBinding.iconAdopted.setImageResource(animal.iconoEstado)
        cardCellBinding.cardView.setOnClickListener{
            clickListener.onClick(animal)
        }
    }
}