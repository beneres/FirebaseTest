package com.example.firebasetest.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.firebasetest.R
import com.example.firebasetest.utils.UsersAdapter

class ShowUserActivity : AppCompatActivity() {

   lateinit var id: TextView ;
   lateinit var year: TextView ;
   lateinit var course: TextView ;
   lateinit var date: TextView ;
   lateinit var name: TextView ;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user)

        id = findViewById(R.id.id)
        name = findViewById(R.id.name)
        year = findViewById(R.id.year)
        date = findViewById(R.id.data)
        course = findViewById(R.id.course)
        var user =UsersAdapter.currentUser

        id.text=user.id
        name.text=user.name
        course.text=user.course
        year.text= user.year2.toString()


    }
}