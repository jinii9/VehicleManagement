package com.autoever.rightnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.autoever.rightnow.R

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ControlFragment : Fragment() {
    // Firestore 인스턴스 초기화
//    private val db: FirebaseFirestore = Firebase.firestore
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_control, container, false)

        val btnPower = view.findViewById<ImageView>(R.id.btnPower)
        val btnUnlock = view.findViewById<ImageView>(R.id.btnUnlock)
        val btnLock = view.findViewById<ImageView>(R.id.btnLock)
        val btnReturn = view.findViewById<TextView>(R.id.btnReturn)


        // 현재 로그인된 사용자의 차량 ID
        val carId = "dNI86Olv5gZ1tLauLBT5"

        // 전원 버튼 동작
        btnPower.setOnClickListener {
            firestore.collection("remoteControl").document(carId)
                .get()
                .addOnSuccessListener { document ->
                    val currentPowerState = document.getBoolean("power") ?: true
                    val newPowerState = !currentPowerState

                    firestore.collection("remoteControl").document(carId)
                        .update("power", newPowerState)
                        .addOnSuccessListener {
                            if (newPowerState) {
                                showToast("시동이 켜졌습니다")
                            } else {
                                showToast("시동이 꺼졌습니다")
                            }
                        }
                        .addOnFailureListener { e ->
                            showToast("데이터 조회 실패: ${e.message}")
                        }
                }
        }

        // 잠금해제 버튼 동작
        btnUnlock.setOnClickListener {
            firestore.collection("remoteControl").document(carId)
                .update("lock", false)
                .addOnSuccessListener {
                    showToast("차량이 잠금해제되었습니다")
                }
        }

        // 잠금 버튼 동작
        btnLock.setOnClickListener {
            firestore.collection("remoteControl").document(carId)
                .update("lock", true)
                .addOnSuccessListener {
                    showToast("차량이 잠겼습니다")
                }
        }

        // 반납 버튼 동작
        btnReturn.setOnClickListener {
            firestore.collection("remoteControl").document(carId)
                .update("returned", true)
                .addOnSuccessListener {
                    showToast("반납이 완료되었습니다")
                }
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}