package com.example.firebasetest.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.R
import com.example.firebasetest.utils.UsersAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val TAG = "DatabaseActivity"
   lateinit var rvData:RecyclerView ;
    lateinit var adapter: UsersAdapter;
    private val userList: ArrayList<User> = ArrayList();
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvData = findViewById(R.id.rv_data)

        val db = Firebase.firestore
        val usersCollection = db.collection("Users")
        adapter=UsersAdapter(this,userList)
        rvData.adapter=adapter;
      //  val userId = 20221145
          val userId = 23839

        usersCollection.document(userId.toString()).get().addOnSuccessListener {
            val user = it.toObject<User>()
            if (user != null)
                Log.i(TAG, user.toString())
            else Log.e(TAG, "User $userId is null")
        }.addOnFailureListener {
            Log.e(TAG, "Failure")
        }

        //get All Users
        usersCollection.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        val user = document.toObject<User>()
                        if (user != null)
                        {
                            Log.i(TAG, user.toString())

                            userList.add(user);
                            adapter.notifyDataSetChanged()
                        }


                        else Log.e(TAG, "User $userId is null")
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }




    }
        }

class User(

    var id: String = "",

    //this I put it year2 instead of year because there was wrong data written by a colleague with String instead of int and it causes the application to crash
    var year2: Int = -1,
    var course: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",



    ) {
    override fun toString(): String {
        return "$name: $course (Year $year2)"
    }
}