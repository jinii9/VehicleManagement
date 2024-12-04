package com.autoever.rightnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.rightnow.R
import com.autoever.rightnow.models.Car
import com.google.firebase.firestore.FirebaseFirestore

class FindFragment : Fragment() {
    val firestore = FirebaseFirestore.getInstance()

    val cars = mutableListOf<Car>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        carAdapter = CarAdapter(cars)
        recyclerView.adapter = carAdapter

        getCars()

        return view
    }

    fun getCars() {
        cars.clear()
        firestore.collection("cars")
            .get() // get() 메서드를 사용하여 데이터를 가져옴
            .addOnSuccessListener { result ->
                // 가져온 결과를 반복문으로 처리
                for (document in result) {
                    val car = document.toObject(Car::class.java) // 각 문서를 User 객체로 변환
                    cars.add(car)
                }
                carAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 실패했을 때 처리
                println("Error getting documents: $exception")
            }
    }
}

class CarAdapter(private val cars: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    // ViewHolder 정의
    class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carName: TextView = view.findViewById(R.id.textView)
    }

    // onCreateViewHolder: 아이템 레이아웃을 뷰로 변환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    // onBindViewHolder: 데이터를 뷰에 바인딩
    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.carName.text = car.name
    }

    // getItemCount: 아이템의 개수 반환
    override fun getItemCount(): Int {
        return cars.size
    }
}
