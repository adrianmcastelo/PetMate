package com.apm.petmate.ui.favs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.FragmentFavsBinding
import com.apm.petmate.ui.animals.ANIMAL_ID_EXTRA
import com.apm.petmate.ui.animals.Animal
import com.apm.petmate.ui.animals.AnimalClickListener
import com.apm.petmate.ui.animals.CardAdapter
import com.apm.petmate.ui.animals.DetailActivity
import com.apm.petmate.ui.animals.animalList
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject
import com.apm.petmate.utils.AnimalUtils

class FavsFragment : Fragment(), AnimalClickListener {

    private var _binding: FragmentFavsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var id:Int? = null
    private var token:String? = null
    private var isProtectora:Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        this.id = (activity as? MainActivity)?.id
        this.token = (activity as? MainActivity)?.token
        this.isProtectora = (activity as? MainActivity)?.isProtectora
        println("ID en FAVS:" + id)
        println("TOKEN en FAVS:" + token)
        println("isProtectora en FAVS:" + isProtectora)

        _binding = FragmentFavsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getAnimalsFav(this.id!!)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAnimalsFav(idUser: Int) {
        val url = "http://10.0.2.2:8000/petmate/animal/fav?idUser=$idUser"

        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                animalList = AnimalUtils().parseAnimals(response)
                createRecyclerView(animalList)
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

    private fun createRecyclerView(animalList: List<Animal>) {
        val animalFragment = this
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CardAdapter(com.apm.petmate.ui.animals.animalList, animalFragment)
        }
    }
    override fun onClick(animal: Animal) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(ANIMAL_ID_EXTRA, animal.id)
        intent.putExtra("id", this.id)
        intent.putExtra("token", this.token)
        intent.putExtra("isProtectora", this.isProtectora)
        startActivity(intent)
    }
}