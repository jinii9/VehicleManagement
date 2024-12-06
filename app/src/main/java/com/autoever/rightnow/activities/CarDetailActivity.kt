package com.autoever.rightnow.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.autoever.rightnow.R
import com.autoever.rightnow.models.Car
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class CarDetailActivity : AppCompatActivity() {

    private lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val car = intent.getSerializableExtra("car") as? Car

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewCarType = findViewById<TextView>(R.id.textViewCarType)
        val textView15 = findViewById<TextView>(R.id.textViewmanufacturer)
        val textView16 = findViewById<TextView>(R.id.textViewcarPerson)
        val textViewcarDescription = findViewById<TextView>(R.id.textView21)
        val textViewcarNumber = findViewById<TextView>(R.id.textViewcarNumber)
        val textViewpricePerHour = findViewById<TextView>(R.id.textViewpricePerHour)
        val btnUse = findViewById<TextView>(R.id.btnUse)

        Log.d("제발",car.toString())

        car?.let { it ->
            textViewCarType.text = it.carType
            textViewcarDescription.text = it.carDescription
            textViewcarNumber.text = "${it.carNumber}"

            textView15.text = it.manufacturer
            textView16.text = it.carPerson.toString() + "명"
            textViewpricePerHour.text = it.pricePerHour.toString() + "원"
            // 이미지 로드 (Glide 사용)

            val imageUri = Uri.parse(it.image) // String을 URI로 변환
            imageView.setImageURI(imageUri)
            Glide.with(this)
                .load(it.image)
//                .placeholder(R.drawable.placeholder_image) // 기본 이미지
                .into(imageView)

            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
                }

            mapFragment.getMapAsync { naverMap ->
                this.naverMap = naverMap

                // 지도 중심을 차량 위치로 이동
                val carPosition = LatLng(it.carLocationLat.toDouble(), it.carLocationLong.toDouble())
                val cameraPosition = CameraPosition(carPosition, 12.0) // 줌 레벨 설정
                naverMap.cameraPosition = cameraPosition

                // 마커 추가
                val marker = Marker()
                marker.position = carPosition
                marker.map = naverMap

                // 지도 이동/확대/축소 비활성화
                naverMap.uiSettings.isScrollGesturesEnabled = false
                naverMap.uiSettings.isZoomGesturesEnabled = false
                naverMap.uiSettings.isTiltGesturesEnabled = false
                naverMap.uiSettings.isRotateGesturesEnabled = false
            }
        } ?: Log.e("CarDetailActivity", "Car object is null")

//        btnUse.setOnClickListener {
//            val intent = Intent(this, BookActivity::class.java)
//            startActivity(intent)
//        }
    }
}