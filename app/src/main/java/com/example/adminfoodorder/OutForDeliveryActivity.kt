package com.example.adminfoodorder

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorder.adapters.DeliveryAdapter
import com.example.adminfoodorder.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodorder.models.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OutForDeliveryActivity : AppCompatActivity() {

    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database:FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.GoBackTOButton.setOnClickListener {
            finish()
        }

        // retrieve and display completed order
        retrieveCompleteOrderDetails()

    }

    private fun retrieveCompleteOrderDetails() {
        // Initializae Firebase db
        database = FirebaseDatabase.getInstance()
        val completeOrderReferencer = database.reference.child("CompletedOrder").orderByChild("currentTime")
        completeOrderReferencer.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear the list before populating it with new data
                listOfCompleteOrderList.clear() //mumsad

                for (orderSnapshot in snapshot.children){

                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }

                }
                //reverse the list tot display latest order first
                listOfCompleteOrderList.reverse()
                setDataInRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OutForDeliveryActivity,"Data Not Fetching ${error.message} ",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setDataInRecyclerView() {
        // Initialization list to hold customers name and payment status

        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }

            moneyStatus.add(order.paymentReceived)
        }

        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.outForDeliveryRecyclerView.adapter = adapter
        binding.outForDeliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}