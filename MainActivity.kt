package com.example.idsystemkoisk

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.idsystemkoisk.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.file.Files.delete

data class Employees(val employeeId:String,
                     val fname:String,
                     val mname:String,
                     val lname:String,
                     val fullname:String,
                     val position:String,
                     val office:String,
                     val address:String,
                     val gsispo:String,
                     val gsisno:String,
                     val pagibig:String,
                     val tin:String,
                     val philhealth:String,
                     val bloodtype:String,
                     val dob:String,
                     val emername:String,
                     val emeraddress:String,
                     val emercontact:String,
                     val signature:String,
                     val employee_image:String)

data class AllEmployees(val employees:ArrayList<Employees>)

class MainActivity : AppCompatActivity() {
    lateinit var customAdapter: CustomAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var allEmployees: AllEmployees
    lateinit var url:String
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = resources.getString(R.string.app_url)

        preferences = getSharedPreferences("login", MODE_PRIVATE)

        viewAllEmployees("$url/getAllEmployees", null)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewEmployeesBySearchTerm("$url/getAllEmployees/$p0", null)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewEmployeesBySearchTerm("$url/getAllEmployees/$p0", null)
                return false
            }

        })

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, Login::class.java)

            intent.putExtra("id", customAdapter.getEmployeeId(i))
            intent.putExtra("firstname", customAdapter.getFirstname(i))
            intent.putExtra("middlename", customAdapter.getMiddlename(i))
            intent.putExtra("lastname", customAdapter.getLastname(i))
            intent.putExtra("position", customAdapter.getPosition(i))
            intent.putExtra("office", customAdapter.getOffice(i))
            intent.putExtra("address", customAdapter.getAddress(i))
            intent.putExtra("signature", customAdapter.getSignature(i))
            intent.putExtra("image", customAdapter.getImage(i))

            intent.putExtra("gsispo", customAdapter.getPolicyNo(i))
            intent.putExtra("gsisno", customAdapter.getGsisNo(i))
            intent.putExtra("pagibig", customAdapter.getPagibig(i))
            intent.putExtra("tin", customAdapter.getTin(i))
            intent.putExtra("philhealth", customAdapter.getPhilhealth(i))
            intent.putExtra("bloodtype", customAdapter.getBloodType(i))
            intent.putExtra("dob", customAdapter.getBirthDate(i))
            intent.putExtra("emername", customAdapter.getEmerName(i))
            intent.putExtra("emeraddress", customAdapter.getEmerAddress(i))
            intent.putExtra("emercontact", customAdapter.getEmerContact(i))

            startActivity(intent)
            false
        }

        val closeBtnId = binding.search.context.resources
            .getIdentifier("android:id/search_close_btn", null, null)
        val closeBtn = binding.search.findViewById<ImageView>(closeBtnId)
        closeBtn?.setOnClickListener {
            binding.search.onActionViewCollapsed()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.search.setFocusable(false);
        binding.search.clearFocus();

        val editor = preferences.edit()
        editor.clear()
        editor.remove("isLogged")
        editor.commit()
        viewAllEmployees("$url/getAllEmployees", null)
    }

    private fun jsonToGson(json:String){
        val gson = Gson()
        allEmployees = gson.fromJson(json, AllEmployees::class.java)
        customAdapter = CustomAdapter(allEmployees, this)
        binding.listView.adapter = customAdapter

    }

    private fun viewAllEmployees(api_url:String, keyword: JSONObject?){
        val TAG = "json_obj_req"

        val request = JsonObjectRequest(Request.Method.GET, api_url, keyword, {
                jsonobj ->
            jsonToGson(jsonobj.toString())
        },{
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        })
        AppController.instance!!.addToRequestQueue(request, TAG)
    }

    private fun viewEmployeesBySearchTerm(api_url:String, keyword: JSONObject?){
        val TAG = "json_obj_req"

        val request = JsonObjectRequest(Request.Method.GET, api_url, keyword, {
                jsonobj ->
            jsonToGson(jsonobj.toString())
        },{
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        })
        AppController.instance!!.addToRequestQueue(request, TAG)
    }
}