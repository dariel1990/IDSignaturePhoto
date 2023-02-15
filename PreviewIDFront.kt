package com.example.idsystemkoisk

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import com.example.idsystemkoisk.databinding.ActivityPreviewIdfrontBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class PreviewIDFront : AppCompatActivity() {

    lateinit var binding : ActivityPreviewIdfrontBinding
    lateinit var id:String
    lateinit var firstname:String
    lateinit var middlename:String
    lateinit var lastname:String
    lateinit var position:String
    lateinit var office:String
    lateinit var address:String
    lateinit var signature:String
    lateinit var image:String
    lateinit var gsispo:String
    lateinit var gsisno:String
    lateinit var pagibig:String
    lateinit var tin:String
    lateinit var philhealth:String
    lateinit var bloodtype:String
    lateinit var dob:String
    lateinit var emername:String
    lateinit var emeraddress:String
    lateinit var emercontact:String
    lateinit var url:String
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewIdfrontBinding.inflate(layoutInflater)
        url = resources.getString(R.string.app_url)
        setContentView(binding.root)

        preferences = getSharedPreferences("login", MODE_PRIVATE)

        getDetails()

        binding.frontLayout.setOnTouchListener(object : OnSwipeTouchListener(this@PreviewIDFront) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()

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

                val intent = Intent(this@PreviewIDFront, PreviewIDBack::class.java)
                intent.putExtra("gsispo", gsispo)
                intent.putExtra("gsisno", gsisno)
                intent.putExtra("pagibig", pagibig)
                intent.putExtra("tin", tin)
                intent.putExtra("philhealth", philhealth)
                intent.putExtra("bloodtype", bloodtype)
                intent.putExtra("dob", dob)
                intent.putExtra("emername", emername)
                intent.putExtra("emeraddress", emeraddress)
                intent.putExtra("emercontact", emercontact)
                startActivity(intent)

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })

        binding.btnChangePhoto.setOnClickListener{
            val intent = Intent(this, ChangeIDPhoto::class.java)
            intent.putExtra("id", id)
            startActivity(intent)

            false
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.imageView.setOnClickListener{
            reloadImages()
        }


    }

    override fun onResume() {
        super.onResume()
        getDetails()
    }


    @Override
    override fun onBackPressed() {
        super.onBackPressed()

        val editor = preferences.edit()
        editor.clear()
        editor.remove("isLogged")
        editor.commit()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    fun getIdPhoto(): String {
        val extras = intent.extras
        val id = extras!!.getString("id", "").toInt()

        val imageUrl = url+"/getIdPicture/"+id
        return imageUrl
    }

    fun getSignaturePhoto(): String {
        val extras = intent.extras
        val id = extras!!.getString("id", "").toInt()

        val imageUrl = url+"/getSignature/"+id
        return imageUrl
    }

    fun getDetails(){
        val extras = intent.extras
        id = extras!!.getString("id", "")
        firstname = extras!!.getString("firstname", "")
        middlename = extras!!.getString("middlename", "").substring(0, 1)
        lastname = extras!!.getString("lastname", "")
        position = extras!!.getString("position", "")
        office = extras!!.getString("office", "")
        address = extras!!.getString("address", "")
        binding.lblEmployeeID.text = id
        binding.lblFullname.text = "${firstname} ${middlename}. ${lastname}"
        binding.lblPosition.text = position
        binding.lblOffice.text = office
        binding.lblAddress.text = address

        Picasso.with(this).load(getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(binding.imageView)
        Picasso.with(this).load(getSignaturePhoto()).placeholder(R.drawable.ic_launcher_foreground).into(binding.signature)

    }

    fun reloadImages(){
        val extras = intent.extras
        id = extras!!.getString("id", "")

        Picasso.with(getApplicationContext()).invalidate("");

        Picasso.with(this)
            .load(getIdPhoto())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(binding.imageView);

        Picasso.with(this)
            .load(getSignaturePhoto())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(binding.signature);

    }

}