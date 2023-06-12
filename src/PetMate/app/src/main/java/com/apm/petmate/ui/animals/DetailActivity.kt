package com.apm.petmate.ui.animals

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.databinding.ActivityDetailBinding
import com.apm.petmate.ui.map.ProtectoraDetail
import com.apm.petmate.utils.VolleyApi
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var id: Int? = null
    private var token: String? = null
    private var isProtectora: Boolean? = false
    private var animal: Animal? = null
    private var isFavourite: Boolean? = false
    private var animalId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.id = this.intent.extras?.getInt("id")
        this.token = this.intent.extras?.getString("token")
        this.isProtectora = this.intent.extras?.getBoolean("isProtectora")
        println("ID en DETALLE ANIMAL:" + id)
        println("TOKEN en DETALLE ANIMAL:" + token)
        println("isProtectora en DETALLE ANIMAL:" + isProtectora)

        this.animalId = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        println("ID ANIMAL : " + animalId)

        this.animal = animalFromId(animalId)
        if(this.animal != null)
        {
            binding.animalImage.setImageBitmap(animal?.imagen)
            binding.animalName.text = animal?.name
            binding.animalDescription.text = animal?.descripcion
            binding.animalType.text = animal?.type
            when (animal?.estado) {
                "AD" -> {
                    binding.animalState.text = "Adoptado"
                    binding.iconState.setImageResource(R.drawable.animal_shelter)
                }
                "DP" -> {
                    binding.animalState.text = "En adopción"
                    binding.iconState.setImageResource(R.drawable.adopcion)
                }
            }
            when (animal?.age) {
                "CA" -> binding.animalAge.text = "Cachorro"
                "AD" -> binding.animalAge.text = "Adulto"
                "SN" -> binding.animalAge.text = "Senior"
            }
            binding.animalBornDate.text = animal?.fechaNacimiento
        }

        if (isProtectora == false) {
            checkIfFavourite(animalId)
        } else {
            changeIcon(animal)
        }

        binding.button.setOnClickListener {
            if (isProtectora == false) {
                setFavourite()
            } else {
                setAdoptedOrNot()
            }
        }

        binding.goToProtectoraButton.setOnClickListener {
            val intent = Intent(this, ProtectoraDetail::class.java)
            intent.putExtra("idProtectora", animal?.protectora)
            intent.putExtra("token", token)
            println("ID PROT: " + animal?.protectora)
            startActivity(intent)
        }
    }

    private fun checkIfFavourite(animalId: Int) {
        val url = "http://10.0.2.2:8000/petmate/animal/isfav?idUser=" + this.id + "&idAnimal=" +this.animalId
        val request = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                this.isFavourite = response.getBoolean("isFav")
                changeIcon(animal)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token " + token
                return headers
            }
        }
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    private fun changeIcon(animal: Animal?) {
        if (isProtectora == false){
            if (isFavourite == false){
                binding.button.setIconResource(R.drawable.baseline_favorite_border_24)
            } else {
                binding.button.setIconResource(R.drawable.baseline_favorite_24)
            }
        } else {
            when (animal?.estado) {
                "AD" -> {
                    binding.button.setIconResource(R.drawable.adopcion)
                    binding.animalState.text = "Adoptado"
                    binding.iconState.setImageResource(R.drawable.animal_shelter)
                }
                "DP" -> {
                    binding.button.setIconResource(R.drawable.animal_shelter)
                    binding.animalState.text = "En adopción"
                    binding.iconState.setImageResource(R.drawable.adopcion)
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

    private fun setFavourite() {

        val url = "http://10.0.2.2:8000/petmate/animal/fav?idUser=" + this.id + "&idAnimal=" + this.animalId
        val jsonO = JSONObject()

        val request = object: JsonObjectRequest(
            Request.Method.POST,url, jsonO,
            { response ->
                println(response.getString("msg"))
                checkIfFavourite(animalId)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token " + token
                return headers
            }
        }
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    private fun setAdoptedOrNot() {

        val url = "http://10.0.2.2:8000/petmate/animal/state?idAnimal=" + this.animalId + "&estado=" + setEstado()
        val jsonO = JSONObject()

        val request = object: JsonObjectRequest(
            Request.Method.PUT,url, jsonO,
            { response ->
                println(response.getString("msg"))
                animal?.estado = setEstado()
                changeIcon(animal)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token " + token
                return headers
            }
        }
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    private fun setEstado(): String {
        when (animal?.estado) {
            "AD" -> {
                return "DP"
            }
            "DP" -> {
                return "AD"
            }
            else -> {
                return ""
            }
        }
    }
}