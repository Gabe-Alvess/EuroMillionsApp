package com.example.euromillionsapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var lastResult: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextNum: EditText = findViewById(R.id.edit_number)
        val editTextStar: EditText = findViewById(R.id.edit_star)
        val txtNumResult: TextView = findViewById(R.id.txt_num_res)
        val txtStarResult: TextView = findViewById(R.id.txt_star_res)
        val drawBtn: Button = findViewById(R.id.draw_btn)

        lastResult = getSharedPreferences("db", Context.MODE_PRIVATE)
        val numResult = lastResult.getString("numResult", null)
        val starResult = lastResult.getString("starResult", null)

        numResult?.let {
            txtNumResult.text = "Last numbers: $it"
        }

        starResult?.let {
            txtStarResult.text = "Last stars: $it"
        }

        drawBtn.setOnClickListener {
            val numText = editTextNum.text.toString()
            val starText = editTextStar.text.toString()

            generateNumbers(numText, starText, txtNumResult, txtStarResult)
        }
    }

    private fun generateNumbers(
        numText: String,
        starText: String,
        numResult: TextView,
        starResult: TextView
    ) {
        // Check if fields are empty
        if (numText.isEmpty()) {
            Toast.makeText(this, "Please enter how much numbers!", Toast.LENGTH_LONG).show()
            return
        } else if (starText.isEmpty()) {
            Toast.makeText(this, "Please enter how much stars!", Toast.LENGTH_LONG).show()
            return
        }

        // Check if numbers are correct
        val numValue = numText.toInt()
        val starValue = starText.toInt()

        if (numValue < 5 || numValue > 30) {
            Toast.makeText(this, "Enter a number between 5 and 30!", Toast.LENGTH_LONG).show()
            return
        } else if (starValue < 2 || starValue > 12) {
            Toast.makeText(this, "Enter a number between 2 and 12!", Toast.LENGTH_LONG).show()
            return
        }

        // Generate random numbers
        val random = Random()
        val luckyNumbers = mutableSetOf<Int>()
        val luckyStars = mutableSetOf<Int>()

        while (true) {
            val num = random.nextInt(50) // 0...49
            luckyNumbers.add(num + 1)

            if (luckyNumbers.size == numValue) {
                break
            }
        }

        while (true) {
            val star = random.nextInt(12) // 0...11
            luckyStars.add(star + 1)

            if (luckyStars.size == starValue) {
                break
            }
        }

        numResult.text = luckyNumbers.joinToString(" | ")
        starResult.text = luckyStars.joinToString(" * ")

        lastResult.edit().apply {
            putString("numResult", numResult.text.toString())
            putString("starResult", starResult.text.toString())
            apply()
        }
    }

}