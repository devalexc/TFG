package com.correro.alejandro.tfg.patient.patientfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.R


/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient, container, false)
    }

}// Required empty public constructor
