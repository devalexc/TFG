package com.correro.alejandro.tfg.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.ui.medic.MainMedicActivity
import com.correro.alejandro.tfg.ui.patient.MainActivityPatient
import com.correro.alejandro.tfg.utils.errorRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mviewmodel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mviewmodel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        btnLogin.setOnClickListener { loginButton() }
        txtDni.setText("12345678G")
        txtPassword.setText("1234")

        /*setSupportActionBar(toolbar.findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
    }

    private fun loginButton() {
        mviewmodel.login(txtDni.text.toString(), txtPassword.text.toString())
        mviewmodel.userResponse.observe(this, Observer { t -> responseCall(t!!) })
        mviewmodel.errorCode.observe(this, Observer<Int> { errorResponse -> errorRequest(errorResponse!!, this) })
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false
    }

    private fun responseCall(userResponse: UserResponse) {

        if (userResponse.type == 1) {
            mviewmodel.getValues().observe(this, Observer { initPatient(it) })

        } else
            startActivity(Intent(this, MainMedicActivity::class.java))
        //Toast.makeText(this, if (userResponse.type == 1) "SOY UN PACIENTE" else "SOY UN MEDICO", Toast.LENGTH_LONG).show()


    }

    private fun initPatient(it: Boolean?) {
        if (it == true) {
            MainActivityPatient.start(this, mviewmodel.userResponse.value!!.user, mviewmodel.historicalResponse, mviewmodel.chronicsResponse)

        }
    }


}




