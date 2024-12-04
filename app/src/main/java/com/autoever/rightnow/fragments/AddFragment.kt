package com.autoever.rightnow.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        editTextName = view.findViewById(R.id.editTextName)
        button = view.findViewById(R.id.button)
        button.setOnClickListener {
            addCar()
        }

        return view
    }

    fun addCar() {
        val carName = editTextName.text.toString()
        if (carName.isNotEmpty()) {
            val car = Car(name = carName)

            firestore.collection("cars")
                .add(car)
                .addOnSuccessListener {
                    Toast.makeText(context, "차량이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    initInputs()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "차량 등록이 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "차량 별칭을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    fun initInputs() {
        editTextName.setText("")
    }
}