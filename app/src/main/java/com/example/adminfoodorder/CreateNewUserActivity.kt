package com.example.adminfoodorder


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adminfoodorder.databinding.ActivityCreateNewUserBinding
import com.example.adminfoodorder.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class CreateNewUserActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
//    private lateinit var nameOfRestaurant: String
    // Firebase var
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    private val binding: ActivityCreateNewUserBinding by lazy {
        ActivityCreateNewUserBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        database = Firebase.database.reference

        binding.createNewUserButton.setOnClickListener {
            // Get Text form EditText
            userName = binding.createNewUserName.text.toString().trim()
//            nameOfRestaurant = binding.editTextSignUpNameOfRestaurant.text.toString().trim()
            email = binding.createNewUserEmail.text.toString().trim()
            password = binding.createNewUserPassword.text.toString().trim()

            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill All Details", Toast.LENGTH_LONG).show()
            }
            else {
                // Create new Account
                createAccount(email, password)
            }
        }

        binding.createNewUserBackButton.setOnClickListener {
            finish()
        }

    }

    // Create new Account function with Firebase Auth
    private fun createAccount(email: String, password: String) {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    saveUserData()
                    finish()
                } else {
                    Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                    Log.d("Account", "createAccount: Failed", task.exception)
                }

            }
    }

    // Save data into Firebase RealTime Database DB

    private fun saveUserData() {
            // get Text form EditText
            userName = binding.createNewUserName.text.toString().trim()
//            nameOfRestaurant = binding.editTextSignUpNameOfRestaurant.text.toString().trim()
            email = binding.createNewUserEmail.text.toString().trim()
            password = binding.createNewUserPassword.text.toString().trim()

            val user = UserModel(userName, email, password)
            val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
            // Save user data Firebase Database
            database.child("user").child(userId).setValue(user)
        }


    }


