package com.test.fairmoney.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.fairmoney.App
import com.test.fairmoney.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}