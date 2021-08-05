package com.example.wgutscheduler.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wgutscheduler.DB.DataBase
import com.example.wgutscheduler.Entity.CourseMentor
import com.example.wgutscheduler.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MentorDetails : AppCompatActivity() {
    lateinit var db: DataBase
    var termID = 0
    var courseID = 0
    private var mentorID = 0
    private lateinit var mdName: TextView
    private lateinit var mdPhone: TextView
    private lateinit var mdEmail: TextView
    private lateinit var mdEditFAB: ExtendedFloatingActionButton
    private var mentorDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        db = DataBase.getInstance(applicationContext)!!
        termID = intent.getIntExtra("termID", -1)
        courseID = intent.getIntExtra("courseID", -1)
        mentorID = intent.getIntExtra("mentorID", -1)
        mdName = findViewById(R.id.mdName)
        mdPhone = findViewById(R.id.mdPhone)
        mdEmail = findViewById(R.id.mdEmail)
        mdEditFAB = findViewById(R.id.mdEditFAB)
        setValues()
        mdEditFAB.setOnClickListener {
            val intent = Intent(applicationContext, EditMentor::class.java)
            intent.putExtra("termID", termID)
            intent.putExtra("courseID", courseID)
            intent.putExtra("mentorID", mentorID)
            startActivity(intent)
        }
    }

    private fun deleteMentor() {
        val mentor: CourseMentor? = db.MentorDao()?.getMentor(courseID, mentorID)
        db.MentorDao()?.deleteMentor(mentor)
        Toast.makeText(this, "Mentor has been deleted", Toast.LENGTH_SHORT).show()
        mentorDeleted = true
    }

    private fun setValues() {
        val mentor: CourseMentor? = db.MentorDao()?.getMentor(courseID, mentorID)
        val name = mentor?.mentor_name
        val phone = mentor?.mentor_phone
        val email = mentor?.mentor_email
        mdName.text = name
        mdPhone.text = phone
        mdEmail.text = email
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.delete_mentor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteMentorIC) {
            deleteMentor()
            val intent = Intent(applicationContext, CourseDetails::class.java)
            intent.putExtra("termID", termID)
            intent.putExtra("courseID", courseID)
            startActivity(intent)
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}