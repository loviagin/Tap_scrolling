package ru.loviagin.tapscrolling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ru.loviagin.tapscrolling.R;

public class AuthorizeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void onEmailRegisterClick(View view) {
        startActivity(new Intent(this, EmailActivity.class));
    }

    public void onGoogleRegisterClick(View view) {
    }

    public void onPhoneRegisterClick(View view) {
        startActivity(new Intent(this, PhoneActivity.class));
    }
}