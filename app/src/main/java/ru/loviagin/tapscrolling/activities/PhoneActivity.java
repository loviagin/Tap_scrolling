package ru.loviagin.tapscrolling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import ru.loviagin.tapscrolling.R;

public class PhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextPhone;
    private EditText editTextOTP;
    private Button buttonSend, buttonVerify;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextOTP = findViewById(R.id.editTextCode);
        buttonSend = findViewById(R.id.buttonGetOTP);
        buttonVerify = findViewById(R.id.buttonContinuePhone);
    }

    public void onClickSMSSend(View view) {
        if (!editTextPhone.getText().toString().isEmpty()) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(editTextPhone.getText().toString().trim())       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            Toast.makeText(this, "Введите номер телефона с +7", Toast.LENGTH_SHORT).show();
        }
        buttonSend.setVisibility(View.INVISIBLE);
        editTextOTP.setVisibility(View.VISIBLE);
        buttonVerify.setVisibility(View.VISIBLE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    final String code = credential.getSmsCode();

                    if (code != null) {
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(PhoneActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(s, token);
                    verificationId = s;
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signingByCredentials(credential);
    }

    private void signingByCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //success
                    startActivity(new Intent(PhoneActivity.this, RegisterContinueActivity.class));
                }
            }
        });
    }

    public void onClickPhoneContinue(View view) {
        if (!editTextOTP.getText().toString().isEmpty()) {
            verifyCode(editTextOTP.getText().toString().trim());
        } else {
            Toast.makeText(this, "Введите код из СМС", Toast.LENGTH_SHORT).show();
        }
        buttonSend.setVisibility(View.VISIBLE);
        editTextOTP.setVisibility(View.INVISIBLE);
        buttonVerify.setVisibility(View.INVISIBLE);
    }
}