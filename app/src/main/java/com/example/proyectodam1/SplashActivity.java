package com.example.proyectodam1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam1.activities.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //barra de estado, ocultar inicio
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //barra de estado, ocultar inicio

        //agregamos inicio de controlador
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
        //agregamos inicio de controlador
    }
}
