package ru.loviagin.tapscrolling.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.loviagin.tapscrolling.R;

public class EmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPass;
    //private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPassword);
      //  button = findViewById(R.id.buttonContinueEmail);

        mAuth = FirebaseAuth.getInstance();

//        button.setOnClickListener(view -> {
//
//        });

    }


    public void onClickEmailContinue(View view) {
        if (!editTextEmail.getText().toString().isEmpty() &&
                !editTextPass.getText().toString().isEmpty() &&
                editTextEmail.getText().toString().contains("@")) {

            mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPass.getText().toString().trim())
                    .addOnCompleteListener(EmailActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG2345", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(EmailActivity.this, RegisterContinueActivity.class);
                            intent.putExtra("email", editTextEmail.getText().toString().trim());
                            intent.putExtra("password", editTextPass.getText().toString().trim());
                            startActivity(intent);
                            Toast.makeText(EmailActivity.this, "SUCCESS",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPass.getText().toString().trim())
                                        .addOnCompleteListener(this, task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                startActivity(new Intent(EmailActivity.this, MainActivity.class));
                                            } else {
                                                Log.w(TAG, "signInWithEmail:failure", task1.getException());
                                                Toast.makeText(EmailActivity.this, "Ошибка",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        });
                        }
                    });
        } else if (!editTextEmail.getText().toString().contains("@")) {
            Toast.makeText(EmailActivity.this, "Введите корректный email",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EmailActivity.this, getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT).show();
        }
    }
}