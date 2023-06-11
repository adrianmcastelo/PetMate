package com.apm.petmate.authentication

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.apm.petmate.MainActivity
import com.apm.petmate.R
import com.apm.petmate.utils.VolleyApi
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


class RegisterProtectora : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_protectora)

        var user = this.intent.extras?.getString("user")
        var pass = this.intent.extras?.getString("pass")

        findViewById<ImageView>(R.id.RegisterProtectoraImagen).setOnClickListener{
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

        var btnReister = findViewById<Button>(R.id.RegisterProtectoraButton)

        btnReister.setOnClickListener{
            var imagen = getStringImage(findViewById<ImageView>(R.id.RegisterProtectoraImagen).drawable.toBitmap())
            println(imagen)
            var nombre = findViewById<EditText>(R.id.RegisterProtectoraNombre).text.toString()
            var dir = findViewById<EditText>(R.id.RegisterProtectoraDireccion).text.toString()
            var ubi = findViewById<EditText>(R.id.RegisterProtectoraUbicacion).text.toString()
            var tlf = findViewById<EditText>(R.id.RegisterProtectoraTelefono).text.toString()
            println(tlf)
            var url = findViewById<EditText>(R.id.RegisterProtectoraURL).text.toString()
            var correo = findViewById<EditText>(R.id.RegisterProtectoraCorreo).text.toString()
            var descripcion = findViewById<EditText>(R.id.RegisterProtectoraDescripcion).text.toString()

            register(imagen!!, user!!, pass!!, nombre, dir, ubi, tlf, url, correo, descripcion)
        }
    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun register(imagen: String?, user: String, pass: String, nombre: String, dir: String, ubicacion: String,
                 telefono: String, urlP: String, correo: String, descripcion: String) {

        val url = "http://10.0.2.2:8000/petmate/protectora/register"

        val jsonO = JSONObject()
            .put("username", user)
            .put("pass", pass)
            .put("name", nombre)
            .put("direccion", dir)
            .put("ubicacion", ubicacion)
            .put("url", urlP)
            .put("correo", correo)
            .put("descripcion", descripcion)
            .put("telefono", telefono)
            .put("imagen", imagen)

        println(jsonO.toString())

        val request = JsonObjectRequest(
            Request.Method.POST,url, jsonO,
            { response ->
                println("La respuesta es : $response")
                var token = JSONObject(response.toString()).getString("token")
                var id = JSONObject(response.toString()).getInt("Id")
                var isProtectora = JSONObject(response.toString()).getBoolean("isProtectora")
                println(id)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", token)
                intent.putExtra("id", id)
                intent.putExtra("isProtectora", isProtectora)
                startActivity(intent)
            },
            { error -> error.printStackTrace() }
        )
        VolleyApi.getInstance(this).addToRequestQueue(request)
    }

    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@RegisterProtectora,
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

                object: MultiplePermissionsListener {
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
                    findViewById<ImageView>(R.id.RegisterProtectoraImagen).setImageBitmap(bitmap)
                }
                GALLERY_REQUEST_CODE->{

                    findViewById<ImageView>(R.id.RegisterProtectoraImagen).setImageURI(data?.data)
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