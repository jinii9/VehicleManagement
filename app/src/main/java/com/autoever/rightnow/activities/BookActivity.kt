package com.autoever.rightnow.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.autoever.rightnow.R
import com.autoever.rightnow.databinding.ActivityBookBinding
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookBinding
    private val CAMERA_PERMISSION_REQUEST = 100
    private val CAMERA_REQUEST_CODE_1 = 102
    private val CAMERA_REQUEST_CODE_2 = 103
    private val CAMERA_REQUEST_CODE_3 = 104
    private val CAMERA_REQUEST_CODE_4 = 105
    private var photoUri: Uri? = null
    private var currentRequestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // System bars padding 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // btnBook 클릭 리스너 설정
        binding.btnBook.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // 현재 Activity 종료
        }

        // 각 버튼에 클릭 리스너 설정
        binding.btnPlus1.setOnClickListener {
            currentRequestCode = CAMERA_REQUEST_CODE_1
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnPlus2.setOnClickListener {
            currentRequestCode = CAMERA_REQUEST_CODE_2
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnPlus3.setOnClickListener {
            currentRequestCode = CAMERA_REQUEST_CODE_3
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.btnPlus4.setOnClickListener {
            currentRequestCode = CAMERA_REQUEST_CODE_4
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, currentRequestCode)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val targetLayout = when (requestCode) {
                CAMERA_REQUEST_CODE_1 -> binding.btnPlus1
                CAMERA_REQUEST_CODE_2 -> binding.btnPlus2
                CAMERA_REQUEST_CODE_3 -> binding.btnPlus3
                CAMERA_REQUEST_CODE_4 -> binding.btnPlus4
                else -> null
            }

            targetLayout?.let { layout ->
                // 이미지뷰 생성 및 설정
                val imageView = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        70.dpToPx(),
                        70.dpToPx()
                    ).apply {
                        marginEnd = 8.dpToPx()
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }

                // 촬영한 사진을 이미지뷰에 설정
                Glide.with(this)
                    .load(photoUri)
                    .centerCrop()
                    .into(imageView)

                // 기존 레이아웃에 이미지뷰 추가
                layout.addView(imageView, 0)
            }
        }
    }

    // dp를 px로 변환하는 확장 함수
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}