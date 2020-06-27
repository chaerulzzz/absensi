package com.absensi.alpa.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.absensi.alpa.R;
import com.absensi.alpa.module.home.HomeActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText tietUsername, tietPassword;
    private TextInputLayout tilUsername, tielPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        this.init();
        this.setData();
    }

    private void init() {
        this.tietUsername = findViewById(R.id.tietUsername);
        this.tietPassword = findViewById(R.id.tietPassword);

        this.btnLogin = findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);
    }

    private void setData() {

    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            if (tietUsername.getText() != null && tietPassword.getText() != null){
                if (!tietUsername.getText().toString().isEmpty() && !tietPassword.getText().toString().isEmpty()) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    Toast.makeText(this, "Username atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Username atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
