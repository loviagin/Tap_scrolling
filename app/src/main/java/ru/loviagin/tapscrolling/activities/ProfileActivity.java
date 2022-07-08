package ru.loviagin.tapscrolling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.loviagin.tapscrolling.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }






    public void onHomeClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onDiscoverClick(View view) {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    public void onNotificationsClick(View view) {
        startActivity(new Intent(this, NotificationsActivity.class));
    }

    public void onProfileClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}