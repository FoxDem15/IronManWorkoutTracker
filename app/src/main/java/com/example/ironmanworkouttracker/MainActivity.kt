package com.example.ironmanworkouttracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var exerciseName: EditText
    private lateinit var setsInput: EditText
    private lateinit var repsInput: EditText
    private lateinit var submitButton: Button
    private lateinit var resultText: TextView
    private lateinit var exerciseCount: TextView
    private lateinit var completedExercises: TextView

    private var totalExercises = 0
    private val exerciseList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkMemory()
    }

    private fun checkMemory() {
        val runtime = Runtime.getRuntime()
        val memory = runtime.totalMemory() - runtime.freeMemory()
        Log.d("MemoryCheck", "Used memory: ${memory / 1024} KB")
        exerciseName = findViewById(R.id.exerciseName)
        setsInput = findViewById(R.id.setsInput)
        repsInput = findViewById(R.id.repsInput)
        submitButton = findViewById(R.id.submitButton)
        resultText = findViewById(R.id.resultText)
        exerciseCount = findViewById(R.id.exerciseCount)
        completedExercises = findViewById(R.id.completedExercises)

        loadExercises() // Загружаем упражнения при запуске

        submitButton.setOnClickListener { saveExercise() }
    }

    private fun loadExercises() {
        val sharedPreferences = getSharedPreferences("ExercisePrefs", Context.MODE_PRIVATE)
        val savedExercises = sharedPreferences.getStringSet("exerciseList", emptySet())

        if (savedExercises != null) {
            exerciseList.addAll(savedExercises)
            totalExercises = exerciseList.size
            updateUI()
        }
    }

    private fun saveExercise() {
        val exercise = exerciseName.text.toString()
        val sets = setsInput.text.toString()
        val reps = repsInput.text.toString()

        if (exercise.isNotEmpty() && sets.isNotEmpty() && reps.isNotEmpty()) {
            val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
            val result = "Упражнение: $exercise\nПодходы: $sets\nПовторения: $reps\nДата: $currentDate"

            exerciseList.add(result)
            totalExercises++
            updateUI()
            saveToPreferences()
            clearInputs()
        } else {
            resultText.text = "Пожалуйста, заполните все поля."
        }
    }

    private fun updateUI() {
        resultText.text = exerciseList.lastOrNull() ?: "Нет записей."
        exerciseCount.text = "Общая сумма упражнений: $totalExercises"
        completedExercises.text = exerciseList.joinToString("\n\n")
    }

    private fun saveToPreferences() {
        val sharedPreferences = getSharedPreferences("ExercisePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putStringSet("exerciseList", exerciseList.toSet())
        editor.apply()
    }

    private fun clearInputs() {
        exerciseName.text.clear()
        setsInput.text.clear()
        repsInput.text.clear()
    }
}