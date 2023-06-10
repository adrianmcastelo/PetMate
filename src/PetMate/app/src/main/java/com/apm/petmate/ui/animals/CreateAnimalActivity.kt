package com.apm.petmate.ui.animals

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.databinding.ActivityCreateAnimalBinding
import com.apm.petmate.utils.VolleyApi
import com.google.android.material.chip.Chip
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class CreateAnimalActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    private lateinit var binding: ActivityCreateAnimalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animalImage.setOnClickListener{
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Seleccione una foto")
            val pictureDialogItem = arrayOf("Seleccionar de la galería",
                "Seleccionar de la cámara")
            pictureDialog.setItems(pictureDialogItem){dialog, which ->

                when(which){
                    0 -> galleryCheckPermission()
                    1 -> cameraCheckPermission()
                }
            }
            pictureDialog.show()
        }

        binding.addAnimalButton.setOnClickListener {
            var animalImage = getStringImage(binding.animalImage.drawable.toBitmap())
            var name = binding.animalName.text.toString()
            var descripcion = binding.animalDescription.text.toString()
            var fechaNacimiento = binding.animalBornDate.year.toString() + '-' + binding.animalBornDate.month.toString() + '-' + binding.animalBornDate.dayOfMonth.toString()

            var ageChip = binding.ageChipGroup
            val ageChipId = ageChip.checkedChipId
            val selectedAgeChip = ageChip.findViewById<Chip>(ageChipId).text.toString()

            var typeChip = binding.typeChipGroup
            val typeChipId = typeChip.checkedChipId
            val selectedTypeChip = typeChip.findViewById<Chip>(typeChipId).text.toString()

            var stateChip = binding.estadoChipGroup
            val stateChipId = stateChip.checkedChipId
            val selectedStateChip = stateChip.findViewById<Chip>(stateChipId).text.toString()

            var protectora = 7;

            register(animalImage, name, fechaNacimiento, descripcion, selectedTypeChip,
                selectedAgeChip, protectora, selectedStateChip)
        }
    }

    fun register(imagen: String?, name: String, fechaNacimiento: String, descripcion: String,
                 tipo: String, edad: String, protectora: Int, estado: String) {

        val url = "http://10.0.2.2:8000/petmate/animal"

        val jsonO = JSONObject()
            .put("name", name)
            .put("fechaNacimiento", fechaNacimiento)
            .put("descripcion", descripcion)
            .put("tipo", tipo)
            .put("edad", "SN")
            .put("protectora", protectora)
            .put("estado", "AD")
            .put("imagen", imagen)

        println(jsonO.toString())

        val request = object: JsonObjectRequest(
            Request.Method.POST,url, jsonO,
            { response ->
                println("La respuesta es : $response")
                var token = JSONObject(response.toString()).getString("token")
                var id = JSONObject(response.toString()).getString("IdProtectora")
                println(id)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", token)
                intent.putExtra("IdProtectora", id)
                startActivity(intent)
            },
            { error -> error.printStackTrace() }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token c44f4a4bbe7bd9435af0ac2f74f99f72e0fbbeed"
                return headers
            }
        }
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@CreateAnimalActivity,
                    "",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }

        }).onSameThread().check()
    }

    private fun gallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraCheckPermission(){
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).withListener(

                object: MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                camera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                CAMERA_REQUEST_CODE->{

                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.animalImage.setImageBitmap(bitmap)
                }
                GALLERY_REQUEST_CODE->{

                    binding.animalImage.setImageURI(data?.data)
                }
            }
        }

    }

    private fun showRotationalDialogForPermission(){
    AlertDialog.Builder(this).setMessage("No tiene permiso para acceder a esta funcionalidad" +
            "Actívelos en el apartado de ajustes")
        .setPositiveButton("Ir a AJUSTES"){_,_ ->
            try{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

            } catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }
        }

        .setNegativeButton("CANCELAR"){dialog, _->
            dialog.dismiss()
        }.show()
    }

}