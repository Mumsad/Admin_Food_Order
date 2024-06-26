package com.example.adminfoodorder


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.adminfoodorder.adapters.AllItemAdapter
import com.example.adminfoodorder.adapters.MenuItemAdapter
import com.example.adminfoodorder.databinding.ActivityAllItemsBinding
import com.example.adminfoodorder.models.AllMenu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AllItemsActivity : AppCompatActivity(), MenuItemAdapter.OnItemClicked {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems : ArrayList<AllMenu> = ArrayList()

    private val binding: ActivityAllItemsBinding by lazy {
        ActivityAllItemsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().reference

        retrieveMenuItem()
        binding.BackButton.setOnClickListener {
            finish()
        }

        /*val foodName =
            listOf("Burger", "Sandwich", "momo", "Herbal Pancake", "Mixing", "Burger")
        val cartPrice = listOf("$10", "$8", "$15", "$99", "$50", "$12")
        val foodImages = listOf(
            R.drawable.photo,
            R.drawable.photo,
            R.drawable.photo,
            R.drawable.photo,
            R.drawable.photo,
            R.drawable.photo
        )

        val adapter =
            MenuItemAdapter(ArrayList(foodName), ArrayList(cartPrice), ArrayList(foodImages))
        binding.allItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allItemRecyclerView.adapter = adapter*/
    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")

        // fetch data from data base
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear exiting data before populating
                menuItems.clear()

                // loop for through each food item
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
                Toast.makeText(this@AllItemsActivity,"Data Not Fetching Error",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemsActivity, menuItems,databaseReference,this)
        binding.allItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allItemRecyclerView.adapter = adapter
    }

    override fun onItemDeleteClicked(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener {
                task->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.allItemRecyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Item NOt Deleted",Toast.LENGTH_SHORT).show()
            }
        }
    }
}