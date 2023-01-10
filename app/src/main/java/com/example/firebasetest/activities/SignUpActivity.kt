package com.example.firebasetest.activities


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.R
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null
    var name: TextInputEditText? = null
    var courseName: TextInputEditText? = null
    var year: TextInputEditText? = null
    var email: TextInputEditText? = null
    var password: TextInputEditText? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        name = findViewById(R.id.et_full_name)
        courseName = findViewById(R.id.et_course_name)
        year = findViewById(R.id.et_year_name)
        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)
        progressDialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser
        findViewById<View>(R.id.sign_up_btn).setOnClickListener { view: View? -> registerUser() }
    }

    private fun registerUser() {
        val fullName = name!!.text.toString()
        val emailString = email!!.text.toString()
        val yearString = year!!.text.toString()
        val courseString = courseName!!.text.toString()
        val passwordString = password!!.text.toString()
        if (fullName.isEmpty()) Toast.makeText(this, "Please set Full name", Toast.LENGTH_SHORT)
            .show() else if (yearString.isEmpty()) Toast.makeText(
            this,
            "Please set Year",
            Toast.LENGTH_SHORT
        ).show() else if (courseString.isEmpty()) Toast.makeText(
            this,
            "Please set your City ",
            Toast.LENGTH_SHORT
        ).show() else if (emailString.isEmpty()) Toast.makeText(
            this,
            "Please set email address",
            Toast.LENGTH_SHORT
        ).show() else if (passwordString.isEmpty() || passwordString.length < 6) Toast.makeText(
            this,
            "Please set the password and is greater than 6 characters",
            Toast.LENGTH_SHORT
        ).show() else signUp(
            User("23839", yearString.toInt(), courseString, fullName, emailString, passwordString)
        )
    }

    private fun signUp(user: User) {
        progressDialog!!.setMessage("Password Wait While Registration...")
        progressDialog!!.setTitle("Registration")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
        mAuth!!.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    progressDialog!!.dismiss()
                    addDataToFirestore(user)
                    // get user data and store
                    Toast.makeText(
                        this@SignUpActivity,
                        " User has been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(this@SignUpActivity, MainActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    finish()
                } else {
                    progressDialog!!.dismiss()
                    Log.e("getException", "" + task.exception!!.message)
                    if (task.exception!!.message!!.contains("use by another account")) {
                        // SignIn
                        mAuth!!.signInWithEmailAndPassword(user.email, user.password)
                            .addOnCompleteListener { task2: Task<AuthResult?> ->
                                if (task2.isSuccessful) {
                                    addDataToFirestore(user)
                                    progressDialog!!.dismiss()
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            MainActivity::class.java
                                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                    finish()
                                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT)
                                        .show()
                                    return@addOnCompleteListener
                                } else {
                                    progressDialog!!.dismiss()
                                    Toast.makeText(
                                        this,
                                        "Failed: " + task2.exception,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e("ErrorLogin", "" + task2.exception)
                                }
                            }
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "" + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog!!.dismiss()
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
       if (mAuth!!.currentUser != null)
        {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
    }

    private fun addDataToFirestore(
       user :User
    ) {

        // creating a collection reference
        // for our Firebase Firestore database.
        val db = Firebase.firestore
        val dbUsers: CollectionReference = db.collection("Users")

        // adding our data to our courses object class.


        // below method is use to add data to Firebase Firestore.
        dbUsers.document(user.id).set(user).addOnSuccessListener { // after the data addition is successful
            // we are displaying a success toast message.
            Toast.makeText(
                this@SignUpActivity,
                "Data has been Update to Firebase Firestore",
                Toast.LENGTH_SHORT
            ).show()
        }
            .addOnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(this@SignUpActivity, "Fail to add user \n$e", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}