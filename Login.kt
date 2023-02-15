package com.example.idsystemkoisk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.idsystemkoisk.databinding.ActivityLoginBinding
import com.example.idsystemkoisk.databinding.ActivityMainBinding

class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = resources.getString(R.string.login_url)
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val extras = intent.extras

        id = extras!!.getString("id", "")
        firstname = extras!!.getString("firstname", "")
        middlename = extras!!.getString("middlename", "")
        lastname = extras!!.getString("lastname", "")
        position = extras!!.getString("position", "")
        office = extras!!.getString("office", "")
        address = extras!!.getString("address", "")
        signature = extras!!.getString("signature", "")
        image = extras!!.getString("image", "")
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

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()
            checkLogin(url,username,password,firstname,middlename,lastname)
        }
    }

    fun intent()
    {
        val intent = Intent(this,PreviewIDFront::class.java)
        intent.putExtra("id", id)
        intent.putExtra("firstname", firstname)
        intent.putExtra("middlename", middlename)
        intent.putExtra("lastname", lastname)
        intent.putExtra("position", position)
        intent.putExtra("office", office)
        intent.putExtra("address", address)
        intent.putExtra("signature", signature)
        intent.putExtra("image", image)

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
        finish()
    }

    override fun onResume() {
        super.onResume()
        val isLogged = preferences.getBoolean("isLogged",false)
        if (isLogged)
            intent()
    }

    fun checkLogin(api_url:String,username:String,password:String,firstname:String,middlename:String,lastname:String)
    {
        val request = object : StringRequest(
            Request.Method.POST,api_url,
            { response->
                when(response.toString())
                {
                    "required"->displayErrors("Warning","Username and Password is required!")
                    "failed"->displayErrors("Error","Invalid Username or Password!")
                    else -> {
                        val editor = preferences.edit()
                        editor.putBoolean("isLogged",true)
                        editor.putString("username",username)
                        editor.commit()
                        intent()

                    }
                }
            },
            {
                    error ->
                displayErrors("Error",error.localizedMessage)
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params:MutableMap<String,String> = HashMap()
                params.put("username",username)
                params.put("password",password)
                params.put("firstname",firstname)
                params.put("middlename",middlename)
                params.put("lastname",lastname)
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)

    }

    fun displayErrors(title:String,message:String)
    {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK",null)
        alert.create().show()
    }

}