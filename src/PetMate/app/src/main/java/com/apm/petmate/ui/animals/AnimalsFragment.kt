package com.apm.petmate.ui.animals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.FragmentAnimalsBinding

class AnimalsFragment : Fragment(), AnimalClickListener {

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

        val animalFragment = this
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CardAdapter(animalList, animalFragment)
        }


        var id = (activity as? MainActivity)?.getId()
        println(id + "animals_fragment")
        if (id === null) {

            binding.addButton.visibility = View.VISIBLE;

            binding.addButton.setOnClickListener{
                var intent = Intent(context, CreateAnimalActivity::class.java)
                startActivity(intent);
            }
        } else {

            val scale = requireContext().resources.displayMetrics.density

            val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.topMargin = (10 * scale + 0.5f).toInt()
            lp.leftMargin = (10 * scale + 0.5f).toInt()
            lp.rightMargin = (10 * scale + 0.5f).toInt()
            binding.filterButton.layoutParams = lp
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(animal: Animal) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(ANIMAL_ID_EXTRA, animal.id)
        startActivity(intent)
    }

    fun poblateAnimals() {
        val animal1 = Animal(
            "Ejemplo1",
            AgeEnum.Adulto,
            TypeEnum.Gato,
            R.drawable.cat
        )
        val animal2 = Animal(
            "Ejemplo2",
            AgeEnum.Joven,
            TypeEnum.Perro,
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