package com.example.idsystemkoisk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.idsystemkoisk.databinding.ActivityPreviewIdbackBinding
import com.squareup.picasso.Picasso

class PreviewIDBack : AppCompatActivity() {

    lateinit var binding : ActivityPreviewIdbackBinding
    lateinit var gsispo:String
    lateinit var gsisno:String
    lateinit var pagibig:String
    lateinit var tin:String
    lateinit var philhealth:String
    lateinit var bloodtype:String
    lateinit var dob:String
    lateinit var url:String
    lateinit var emername:String
    lateinit var emeraddress:String
    lateinit var emercontact:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewIdbackBinding.inflate(layoutInflater)
        url = resources.getString(R.string.app_url)
        setContentView(binding.root)
        getDetails()

        binding.backLayout.setOnTouchListener(object : OnSwipeTouchListener(this@PreviewIDBack) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    fun getDetails(){
        val extras = intent.extras
        gsispo = extras!!.getString("gsispo", "")
        gsisno = extras!!.getString("gsisno", "")
        pagibig = extras!!.getString("pagibig", "")
        tin = extras!!.getString("tin", "")
        philhealth = extras!!.getString("philhealth", "")
        bloodtype = extras!!.getString("bloodtype", "")
        dob = extras!!.getString("dob", "")
        emername = extras!!.getString("emername", "")
        emeraddress = extras!!.getString("emeraddress", "")
        emercontact = extras!!.getString("emercontact", "")

        binding.lblGsisPolicy.text = gsispo
        binding.lblGsisUmid.text = gsisno
        binding.lblPagibig.text = pagibig
        binding.lblTin.text = tin
        binding.lblPhilhealth.text = philhealth
        binding.lblBloodType.text = bloodtype
        binding.lblDob.text = dob
        binding.lblEmerName.text = emername
        binding.lblEmerAddress.text = emeraddress
        binding.lblEmerContact.text = emercontact

    }
}