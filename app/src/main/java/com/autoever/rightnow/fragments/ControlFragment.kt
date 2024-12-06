package com.autoever.rightnow.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.autoever.rightnow.R
import com.autoever.rightnow.activities.BookActivity

import com.google.firebase.firestore.FirebaseFirestore


class ControlFragment : Fragment() {
    // Firestore 인스턴스 초기화
//    private val db: FirebaseFirestore = Firebase.firestore
    val firestore = FirebaseFirestore.getInstance()

    private var currentTemp = 27.0
    private lateinit var tempTextView: TextView

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
        val btnMinus = view.findViewById<FrameLayout>(R.id.btnMinus)
        val btnPlus = view.findViewById<FrameLayout>(R.id.btnPlus)
        tempTextView = view.findViewById(R.id.textViewTemp)
        val btnOff = view.findViewById<TextView>(R.id.btnOff)
        val temperatureLayout = view.findViewById<LinearLayout>(R.id.temperatureLayout)

//        //////////////////////////////////////////////////
//        // testBtn 추가
//        val testBtn = view.findViewById<TextView>(R.id.testBtn)
//        // testBtn 클릭 리스너 추가
//        testBtn.setOnClickListener {
//            val intent = Intent(activity, BookActivity::class.java)
//            startActivity(intent)
//        }
//        //////////////////////////////////////////////////
        // 현재 로그인된 사용자의 차량 ID
        val carId = "EeKNwezVJpnkhYW8ufDd"

        // 초기 전원 상태 확인 및 UI 업데이트
        firestore.collection("remoteControl").document(carId)
            .get()
            .addOnSuccessListener { document ->
                val powerState = document.getBoolean("power") ?: false
                updatePowerButtonUI(btnPower, powerState)
            }
        // 초기 온도 가져오기
        firestore.collection("remoteControl").document(carId)
            .get()
            .addOnSuccessListener { document ->
                currentTemp = document.getDouble("temperature") ?: 27.0
                updateTemperatureDisplay()
            }
        // 초기 공조 상태 확인 및 UI 업데이트
        firestore.collection("remoteControl").document(carId)
            .get()
            .addOnSuccessListener { document ->
                val acState = document.getBoolean("ac") ?: true
                updateAcUI(btnOff, temperatureLayout, acState)
            }

        // 온도 내리기 버튼
        btnMinus.setOnClickListener {
            currentTemp -= 0.5
            updateTemperature(carId)
        }

        // 온도 올리기 버튼
        btnPlus.setOnClickListener {
            currentTemp += 0.5
            updateTemperature(carId)
        }

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
                            updatePowerButtonUI(btnPower, newPowerState)
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


        // 끄기/켜기 버튼 동작
        btnOff.setOnClickListener {
            firestore.collection("remoteControl").document(carId)
                .get()
                .addOnSuccessListener { document ->
                    val currentAcState = document.getBoolean("temperaturePower") ?: true
                    val newAcState = !currentAcState

                    firestore.collection("remoteControl").document(carId)
                        .update("temperaturePower", newAcState)
                        .addOnSuccessListener {
                            updateAcUI(btnOff, temperatureLayout, newAcState)
                            if (newAcState) {
                                showToast("공조가 켜졌습니다")
                            } else {
                                showToast("공조가 꺼졌습니다")
                            }
                        }
                        .addOnFailureListener { e ->
                            showToast("상태 변경 실패: ${e.message}")
                        }
                }
        }

        return view
    }

    // 전원 버튼 ui 변경 함수
    private fun updatePowerButtonUI(powerButton: ImageView, isOn: Boolean) {
        if (isOn) {
            powerButton.setBackgroundResource(R.drawable.rounded_green)
            powerButton.setColorFilter(Color.WHITE)  // 아이콘 색상 변경
        } else {
            powerButton.setBackgroundResource(R.drawable.rounded_gray)
            powerButton.setColorFilter(Color.RED)
        }
    }

    // 온도 변경 함수
    private fun updateTemperature(carId: String) {
        firestore.collection("remoteControl").document(carId)
            .update("temperature", currentTemp)
            .addOnSuccessListener {
                updateTemperatureDisplay()
//                showToast("온도가 ${currentTemp}도로 설정되었습니다")
            }
            .addOnFailureListener { e ->
                showToast("온도 설정 실패: ${e.message}")
            }
    }

    private fun updateTemperatureDisplay() {
        tempTextView.text = String.format("%.1f°", currentTemp)
    }

    private fun updateAcUI(button: TextView, tempLayout: LinearLayout, isOn: Boolean) {
        if (isOn) {
            button.setBackgroundResource(R.drawable.primary_button)
            button.text = "끄기"
            button.setTextColor(Color.WHITE)
            tempLayout.visibility = View.VISIBLE
        } else {
            button.setBackgroundResource(R.drawable.secondary_button)
            button.text = "켜기"
            button.setTextColor(Color.BLACK)
            tempLayout.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}