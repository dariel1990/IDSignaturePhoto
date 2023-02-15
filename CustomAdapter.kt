package com.example.idsystemkoisk

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley

class CustomAdapter(val allEmployees: AllEmployees, val context: Context): BaseAdapter() {
    override fun getCount(): Int {
        return allEmployees.employees.size
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun getEmployeeId (p0: Int) : String{
        return allEmployees.employees.get(p0).employeeId
    }

    fun getFirstname(p0: Int) : String {
        return allEmployees.employees.get(p0).fname
    }

    fun getMiddlename(p0: Int) : String {
        return allEmployees.employees.get(p0).mname
    }

    fun getLastname(p0: Int) : String {
        return allEmployees.employees.get(p0).lname
    }

    fun getFullname(p0: Int) : String {
        return allEmployees.employees.get(p0).fullname
    }

    fun getPosition(p0: Int) : String {
        return allEmployees.employees.get(p0).position
    }

    fun getOffice(p0: Int) : String {
        return allEmployees.employees.get(p0).office
    }

    fun getAddress(p0: Int) : String {
        return allEmployees.employees.get(p0).address
    }

    fun getPolicyNo(p0: Int) : String {
        return allEmployees.employees.get(p0).gsispo
    }

    fun getGsisNo(p0: Int) : String {
        return allEmployees.employees.get(p0).gsisno
    }

    fun getPagibig(p0: Int) : String {
        return allEmployees.employees.get(p0).pagibig
    }

    fun getTin(p0: Int) : String {
        return allEmployees.employees.get(p0).tin
    }

    fun getPhilhealth(p0: Int) : String {
        return allEmployees.employees.get(p0).philhealth
    }

    fun getBloodType(p0: Int) : String {
        return allEmployees.employees.get(p0).bloodtype
    }

    fun getBirthDate(p0: Int) : String {
        return allEmployees.employees.get(p0).dob
    }

    fun getEmerName(p0: Int) : String {
        return allEmployees.employees.get(p0).emername
    }

    fun getEmerAddress(p0: Int) : String {
        return allEmployees.employees.get(p0).emeraddress
    }

    fun getEmerContact(p0: Int) : String {
        return allEmployees.employees.get(p0).emercontact
    }

    fun getSignature(p0: Int) : String {
        val imageUrl = context.resources.getString(R.string.app_url)+"/getSignature/"+allEmployees.employees.get(p0).employeeId
        return imageUrl
    }

    fun getImage(p0: Int) : String {
        val imageUrl = context.resources.getString(R.string.app_url)+"/getIdPicture/"+allEmployees.employees.get(p0).employeeId
        return imageUrl
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.row, null)
        val imageUrl = context.resources.getString(R.string.app_url)+"/images/"

        val lblEmployeeID:TextView = view.findViewById(R.id.lblEmployeeID)
        val lblFullname:TextView = view.findViewById(R.id.lblFullname)
        val lblPosition:TextView = view.findViewById(R.id.lblPosition)
        val lblOffice:TextView = view.findViewById(R.id.lblOffice)

        val employee = allEmployees.employees.get(position)

        lblEmployeeID.text = employee.employeeId
        lblFullname.text = employee.fullname
        lblPosition.text = employee.position
        lblOffice.text = employee.office

        return view

    }
}