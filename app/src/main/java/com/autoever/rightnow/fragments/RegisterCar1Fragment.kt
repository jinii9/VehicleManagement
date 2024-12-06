package com.autoever.rightnow.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.autoever.rightnow.R
import com.autoever.rightnow.viewmodels.RegisterCarViewModel

class RegisterCar1Fragment : Fragment(R.layout.fragment_register_car1) {
    private lateinit var registerCarViewModel: RegisterCarViewModel
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_car1, container, false)
        registerCarViewModel = ViewModelProvider(requireActivity()).get(RegisterCarViewModel::class.java)

        val manufacturerEditText = view.findViewById<EditText>(R.id.manufacturerEditText)
        val carTypeEditText = view.findViewById<EditText>(R.id.carTypeEditText)
        val carPersonNumberPicker = view.findViewById<com.travijuu.numberpicker.library.NumberPicker>(R.id.carPersonNumberPicker)
        val npYearPicker = view.findViewById<NumberPicker>(R.id.npYearPicker)
        val emptycarImageButton = view.findViewById<ImageButton>(R.id.emptycarImageButton)
        val registerCarButton = view.findViewById<Button>(R.id.nextStepRegisterButton)

        npYearPicker.minValue = 2000
        npYearPicker.maxValue = 2025

        // 이미지 버튼 클릭 시 이미지 선택
        emptycarImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        registerCarButton.setOnClickListener {
            // 사용자 입력값을 ViewModel에 저장
            registerCarViewModel.manufacturer = manufacturerEditText.text.toString()
            registerCarViewModel.carType = carTypeEditText.text.toString()
            registerCarViewModel.carPerson = carPersonNumberPicker.value
            registerCarViewModel.year = npYearPicker.value


            imageUri?.let {
                registerCarViewModel.image = it.toString()  // URI를 String으로 저장
            }

            // 다음 단계로 이동
            findNavController().navigate(R.id.action_fragment_register_car1_to_fragment_register_car2)
        }

        return view
    }

    // 이미지를 선택한 후 처리하는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val selectedImageUri = data?.data
            val emptycarImageButton = requireView().findViewById<ImageButton>(R.id.emptycarImageButton)
            val uploadedImageView = requireView().findViewById<ImageView>(R.id.uploadedImageView)

            // 이미지 업로드 후 이미지 버튼을 숨기고, 업로드된 이미지를 ImageView에 표시
            emptycarImageButton.visibility = View.GONE  // 이미지 버튼 숨기기
            uploadedImageView.visibility = View.VISIBLE  // 업로드된 이미지 보이기
            uploadedImageView.setImageURI(selectedImageUri)  // 선택된 이미지를 ImageView에 설정

            // 이미지 URI를 저장
            imageUri = selectedImageUri
        }
    }
}
