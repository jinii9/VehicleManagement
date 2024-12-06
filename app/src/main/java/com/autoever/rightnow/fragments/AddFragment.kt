package com.autoever.rightnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autoever.rightnow.R
import com.autoever.rightnow.models.Car
import com.google.firebase.firestore.FirebaseFirestore

class AddFragment : Fragment() {
    val firestore = FirebaseFirestore.getInstance()

    lateinit var editTextName: EditText
    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)


        val registerCarButton = view.findViewById<Button>(R.id.registerCarButton)
        registerCarButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_to_fragment_register_car1)
        }

        return view
    }
}
