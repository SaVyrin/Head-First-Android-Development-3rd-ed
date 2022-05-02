package com.hfad.chapter10stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.hfad.chapter10stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var running = false
    var offset: Long = 0

    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        savedInstanceState?.let {
            offset = it.getLong(OFFSET_KEY)
            running = it.getBoolean(RUNNING_KEY)
            if (running) {
                binding.stopwatch.base = it.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        binding.startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }

        binding.pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }

        binding.resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }
}