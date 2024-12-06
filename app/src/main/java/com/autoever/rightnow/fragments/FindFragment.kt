package com.autoever.rightnow.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.autoever.rightnow.KAKAO_MAP_KEY
//import com.autoever.rightnow.NAVER_MAP_KEY
import com.autoever.rightnow.R
import com.autoever.rightnow.activities.CarDetailActivity
import com.autoever.rightnow.databinding.FragmentFindBinding
import com.autoever.rightnow.models.Car
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.util.FusedLocationSource


class FindFragment : Fragment() {
    val firestore = FirebaseFirestore.getInstance()

    val cars = mutableListOf<Car>()

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var carAdapter: CarAdapter

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find, container, false)

//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        carAdapter = CarAdapter(cars)
//        recyclerView.adapter = carAdapter

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        // 네이버 지도 객체 가져오기
        mapFragment.getMapAsync { naverMap ->
            this.naverMap = naverMap // naverMap 초기화
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            getCars()
        }
        return view
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted
            Toast.makeText(requireContext(), "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT)
        }
        }
    }

    fun getCars() {
        cars.clear() // 기존 데이터 초기화
        firestore.collection("cars")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val car = document.toObject(Car::class.java)
                    cars.add(car)

                    // 지도에 마커 추가
                    if (::naverMap.isInitialized) {
                        addMarker(car)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FindFragment", "Error getting documents: $exception")
            }

        Log.d("차들 개수",cars.toString())
    }

    private fun addMarker(car: Car) {
        Log.d("자동차 자동차", car.toString())
        val marker = com.naver.maps.map.overlay.Marker()
        marker.position = com.naver.maps.geometry.LatLng(car.carLocationLat.toDouble(), car.carLocationLong.toDouble())
        marker.map = naverMap
//        marker.captionText = car.carType // 마커 위에 이름 표시

        marker.setOnClickListener {
            val intent = Intent(requireContext(), CarDetailActivity::class.java).apply {
                putExtra("car", car) // Car 객체 전달
            }
            startActivity(intent)
            true
        }
    }
}

//class CarAdapter(private val cars: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {
//
//    // ViewHolder 정의
//    class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val carName: TextView = view.findViewById(R.id.textView)
//    }
//
//    // onCreateViewHolder: 아이템 레이아웃을 뷰로 변환
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
//        return CarViewHolder(view)
//    }
//
//    // onBindViewHolder: 데이터를 뷰에 바인딩
//    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
//        val car = cars[position]
//        holder.carName.text = car.name
//    }
//
//    // getItemCount: 아이템의 개수 반환
//    override fun getItemCount(): Int {
//        return cars.size
//    }
//}
