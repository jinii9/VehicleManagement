package com.autoever.rightnow.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.autoever.rightnow.R
import com.autoever.rightnow.models.RemoteControlStatus
import com.autoever.rightnow.viewmodels.RegisterCarViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore

class RegisterCar2Fragment : Fragment(R.layout.fragment_register_car2) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var registerCarViewModel: RegisterCarViewModel
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var latitude = ""
    private var longitude = ""
    val firestore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_car2, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        registerCarViewModel =
            ViewModelProvider(requireActivity()).get(RegisterCarViewModel::class.java)

        val carNumberEditText = view.findViewById<EditText>(R.id.carNumberEditText)
        val pricePerHourEditText = view.findViewById<EditText>(R.id.pricePerHourEditText)
        val carDescriptionEditText = view.findViewById<EditText>(R.id.carDescriptionEditText)
        val currentPosButton = view.findViewById<Button>(R.id.currentPosButton)
        val registerButton = view.findViewById<Button>(R.id.RegisterButton)

        // 위치 정보 가져오기
        currentPosButton.setOnClickListener {
            if (checkLocationPermission()) {
                getCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        // 등록 버튼 클릭 시 ViewModel에 데이터 저장
        registerButton.setOnClickListener {
            // EditText 값들을 ViewModel에 저장
            val carNumber = carNumberEditText.text.toString()
            val pricePerHour = pricePerHourEditText.text.toString().toLong()
            val carDescription = carDescriptionEditText.text.toString()

            // Firestore에 데이터 저장
            saveCarInfoToFirestore(carNumber, pricePerHour, carDescription)

            Toast.makeText(requireContext(), "차량 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    // Firebase Firestore에 차량 정보 저장
    private fun saveCarInfoToFirestore(
        carNumber: String,
        pricePerHour: Long,
        carDescription: String
    ) {
        // Firestore에 저장할 데이터 구성
        val carInfo = hashMapOf(
            "manufacturer" to registerCarViewModel.manufacturer,
            "carType" to registerCarViewModel.carType,
            "carPerson" to registerCarViewModel.carPerson,
            "year" to registerCarViewModel.year,
            "carNumber" to carNumber,
            "pricePerHour" to pricePerHour,
            "carDescription" to carDescription,
            "carLocationLat" to latitude,
            "carLocationLong" to longitude,
            "image" to registerCarViewModel.image // 이미지 URI 저장
        )

        // 새로운 문서 ID 생성
        val newDocumentId = firestore.collection("cars").document().id

        // Firestore에 저장
        firestore.collection("cars").document(newDocumentId)
            .set(carInfo)
            .addOnSuccessListener {
                saveRemoteControlInfoToFirestore(newDocumentId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "차량 등록 실패: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    // 리모컨 데이터 firebase에 전송
    private fun saveRemoteControlInfoToFirestore(documentId:String) {
        val remoteControl = RemoteControlStatus()

        val carStatusMap = hashMapOf(
            "lock" to remoteControl.lock,
            "power" to remoteControl.power,
            "returned" to remoteControl.returned,
            "temperature" to remoteControl.temperature,
            "temperaturePower" to remoteControl.temperaturePower
        )

        firestore.collection("remoteControl").document(documentId)
            .set(carStatusMap)
            .addOnSuccessListener {
                // 성공 시, 차량 검색 페이지로 이동
                findNavController().navigate(R.id.findFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "차량 등록 실패: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    // 위치 정보 가져오는 함수
    private fun getCurrentLocation() {
        // 위치 권한이 있는지 확인
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val location: Location? = task.result

                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()

                    // 현재 위치를 화면에 출력 (예시)
                    Toast.makeText(
                        requireContext(),
                        "성공! 현재 위치: $latitude, $longitude",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(requireContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // 권한이 없으면 권한 요청
            requestLocationPermission()
        }
    }

    // 위치 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 위치 권한 확인 함수
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 위치 권한 요청 함수
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}
