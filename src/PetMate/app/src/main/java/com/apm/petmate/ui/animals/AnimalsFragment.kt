package com.apm.petmate.ui.animals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.apm.petmate.AgeEnum
import com.apm.petmate.Animal
import com.apm.petmate.R
import com.apm.petmate.TypeEnum
import com.apm.petmate.animalList
import com.apm.petmate.databinding.ActivityMainBinding
import com.apm.petmate.databinding.FragmentAnimalsBinding

class AnimalsFragment : Fragment() {

    private var _binding: FragmentAnimalsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentAnimalsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        binding = FragmentAnimalsBinding.inflate(layoutInflater)

        poblateAnimals()

        val root: View = binding.root

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CardAdapter(animalList)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun poblateAnimals() {
        val animal1 = Animal(
            "Ejemplo1",
            AgeEnum.ADULT,
            TypeEnum.CAT,
            R.drawable.cat
        )
        val animal2 = Animal(
            "Ejemplo2",
            AgeEnum.YOUNG,
            TypeEnum.DOG,
            R.drawable.dalmata
        )

        animalList.add(animal1)
        animalList.add(animal2)
        animalList.add(animal1)
        animalList.add(animal1)
        animalList.add(animal2)
        animalList.add(animal2)
    }
}