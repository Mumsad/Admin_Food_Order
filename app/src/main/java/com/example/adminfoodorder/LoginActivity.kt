package com.example.adminfoodorder

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfoodorder.databinding.ActivityLoginBinding
import com.example.adminfoodorder.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class LoginActivity : AppCompatActivity() {

    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        database = Firebase.database.reference
        // Initialize Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        // Goto SignUpActivity
        binding.textViewCreateNewAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Login Button
        binding.loginButton.setOnClickListener {
            email = binding.editTextLoginTextEmailAddress.text.toString().trim()
            password = binding.editTextLoginTextPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
            } else {
                // Use FirebaseAuth to sign in the user
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI if needed
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            // Here, you can navigate to another activity or perform other actions
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }

//                createUserAccount(email, password)
            }
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
        }

        // Login with Google Account
        binding.googleLoginbutton.setOnClickListener {

            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }

    // Create a new User Account function / login the existing user

    private fun createUserAccount(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // User login is successful
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                // Authentication failed
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "LoginUser: Authentication Failed", task.exception)
            }
        }
    }




    // Save User Data with RealTime DB
//    private fun saveUserData() {
//        // get text form edittext
//        email = binding.editTextLoginTextEmailAddress.text.toString().trim()
//        password = binding.editTextLoginTextPassword.text.toString().trim()
//
//        val user = UserModel(userName, nameOfRestaurant, email, password)
//
//        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
//
//        userId?.let {
//            database.child("user").child(it).setValue(user)
//        }
//    }

    // Check if user is already logged in
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null)
            updateUI(currentUser)
    }

    // Launcher Google Sign in
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode ==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask->
                    if (authTask.isSuccessful){
                        // Successfully sign in with Google
                        Toast.makeText(this,"Successfully Sign-in with Google", Toast.LENGTH_SHORT).show()
                        updateUI(authTask.result?.user)
                        finish()
                    }
                    else{
                        //
                        Toast.makeText(this,"Google Sign-in Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this,"Google Sign-in Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // GOTO MainActivity with updateUI
    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}