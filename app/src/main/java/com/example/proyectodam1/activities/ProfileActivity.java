package com.example.proyectodam1.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam1.R;
import com.example.proyectodam1.fragments.BottomSheetSelectImage;
import com.example.proyectodam1.utils.MyToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity {

    FloatingActionButton mSelectImage;
    BottomSheetSelectImage mBottomSheetSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyToolbar.show(this, "Perfil", true);

        mSelectImage = findViewById(R.id.fabSelectImage);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetSelectImage();
            }
        });
    }

    private void openBottomSheetSelectImage() {
        mBottomSheetSelectImage = BottomSheetSelectImage.newInstance();
        mBottomSheetSelectImage.show(getSupportFragmentManager(), mBottomSheetSelectImage.getTag());
    }
}
