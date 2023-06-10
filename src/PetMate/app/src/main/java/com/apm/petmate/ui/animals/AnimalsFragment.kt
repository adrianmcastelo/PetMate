package com.apm.petmate.ui.animals

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.FragmentAnimalsBinding
import com.apm.petmate.utils.Protectora
import com.apm.petmate.utils.VolleyApi
import com.google.android.gms.maps.SupportMapFragment
import org.json.JSONObject

class AnimalsFragment : Fragment(), AnimalClickListener {

    private var _binding: FragmentAnimalsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentAnimalsBinding

    private var token:String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentAnimalsBinding.inflate(layoutInflater)

        getAnimals()

        val root: View = binding.root

        val animalFragment = this
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CardAdapter(animalList, animalFragment)
        }

        var id = (activity as? MainActivity)?.id
        this.token = (activity as? MainActivity)?.token
        println("ID en ANIMALES:" + id)
        println("TOKEN en ANIMALES:" + token)

        if (id !== null) {
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

    private fun getAnimals() {
        val url = "http://10.0.2.2:8000/petmate/animal/search?tipo=" + "&edad=" + "&estado="

        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                animalList = parseAnimals(response)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token " + token
                return headers
            }
        }
        VolleyApi.getInstance(getActivity() as Activity).addToRequestQueue(request)
    }

    private fun parseAnimals(response: JSONObject):ArrayList<Animal> {
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

            animals.add(animal)
        }

        println("Animals list:" + animals.size)
        animalList = animals
        return animals
    }

    private fun base64ToBitmap(image: String): Bitmap? {
        val imageBytes = Base64.decode(image, 0)
        val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return imageBitmap
    }

}