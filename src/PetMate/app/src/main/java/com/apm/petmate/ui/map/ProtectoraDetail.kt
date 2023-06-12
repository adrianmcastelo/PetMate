package com.apm.petmate.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.apm.petmate.R
import com.apm.petmate.ui.protectoraDetail.ProtectoraDetailFragment


class ProtectoraDetail : AppCompatActivity() {

    var id: Int? = null
        get() = field

    var token:String? = null
        get() = field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protectora_detail)

        this.id = this.intent.extras?.getInt("idProtectora")
        this.token = this.intent.extras?.getString("token")

        println("ID :" + id)
        println("TOKEN :" + token)

        val fragment: Fragment = ProtectoraDetailFragment()
        moveToFragment(fragment)

    }

    private fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, fragment, fragment.javaClass.simpleName).commit()
    }
}