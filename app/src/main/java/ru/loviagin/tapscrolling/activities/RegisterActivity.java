package ru.loviagin.tapscrolling.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import ru.loviagin.tapscrolling.R;

public class RegisterActivity extends AppCompatActivity {

    private GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button button = findViewById(R.id.buttonGoogle);

        //      FirebaseAuth mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);

        ActivityResultLauncher<Intent> googleActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                });

        button.setOnClickListener(view -> {
            Intent i = client.getSignInIntent();
            googleActivityResultLauncher.launch(i);
        });
    }

    public void onEmailRegisterClick(View view) {
        startActivity(new Intent(this, EmailRegisterActivity.class));
    }
}