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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.apm.petmate.R
import com.apm.petmate.databinding.ActivityCreateAnimalBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

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