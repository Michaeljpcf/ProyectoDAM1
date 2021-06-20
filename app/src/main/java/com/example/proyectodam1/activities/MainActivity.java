package com.example.proyectodam1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodam1.R;
import com.example.proyectodam1.providers.AuthProvider;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    Button mbtnSendCode;
    EditText medtCel;
    CountryCodePicker mccp;

    AuthProvider mAuthProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbtnSendCode = findViewById(R.id.btnSendCode);
        medtCel = findViewById(R.id.edtTextCel);
        mccp = findViewById(R.id.ccp);

        mAuthProvider = new AuthProvider();

        mbtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToCodeVerificationActivity();
                getData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthProvider.getSessionUser() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void getData(){

        String code = mccp.getSelectedCountryCodeWithPlus();
        String cel = medtCel.getText().toString();

        if(cel.equals("")){
            Toast.makeText(this, "Ingresar número de celular", Toast.LENGTH_SHORT).show();
        }
        else {
            goToCodeVerificationActivity(code + cel);
        }

    }

    private void goToCodeVerificationActivity(String cel){
        Intent intent = new Intent(MainActivity.this, CodeVerificationActivity.class);
        intent.putExtra("cel", cel);
        startActivity(intent);

    }
}
