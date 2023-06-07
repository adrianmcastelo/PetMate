package com.apm.petmate.ui.animals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apm.petmate.databinding.CardCellBinding

class CardAdapter(
    private val animals: List<Animal>,
    private val clickListener: AnimalClickListener
    )
    : RecyclerView.Adapter<CardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = animals.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindAnimal(animals[position])
    }
}