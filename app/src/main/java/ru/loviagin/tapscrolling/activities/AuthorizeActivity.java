package ru.loviagin.tapscrolling.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ru.loviagin.tapscrolling.R;

public class AuthorizeActivity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                String password = credential.getPassword();
                if (idToken != null) {
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    db.collection("users")
                                            .whereEqualTo("idToken", idToken)
                                            .get()
                                            .addOnCompleteListener(task0 -> {
                                                        if (task0.isSuccessful()) {
                                                            int count = 0;
                                                            for (DocumentSnapshot ignored : task0.getResult()) {
                                                                count++;
                                                            }
                                                            if (count == 0) {
                                                                startActivity(new Intent(AuthorizeActivity.this, RegisterContinueActivity.class)
                                                                        .putExtra("idToken", idToken).putExtra("name", credential.getDisplayName()));
                                                            } else {
                                                                startActivity(new Intent(AuthorizeActivity.this, MainActivity.class));
                                                                Toast.makeText(AuthorizeActivity.this, "С возвращением!", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Log.d(MotionEffect.TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                            );
                                } else {
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    Toast.makeText(this, "Ошибка получения аккаунта", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (password != null) {
                    Log.d(TAG, "Got password.");
                }
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case CommonStatusCodes.CANCELED:
                        Log.d(TAG, "One-tap dialog was closed.");
                        Toast.makeText(this, "Не найден Google аккаунт", Toast.LENGTH_SHORT).show();
                        boolean showOneTapUI = false;
                        break;
                    case CommonStatusCodes.NETWORK_ERROR:
                        Log.d(TAG, "One-tap encountered a network error.");
                        Toast.makeText(this, "Не найден Google аккаунт", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d(TAG, "Couldn't get credential from result."
                                + e.getLocalizedMessage());
                        Toast.makeText(this, "Не найден Google аккаунт", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false)
                .build();
    }

    public void onEmailRegisterClick(View view) {
        startActivity(new Intent(this, EmailActivity.class));
    }

    public void onGoogleRegisterClick(View view) {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(this, e -> Log.d(TAG, e.getLocalizedMessage()));
    }

    public void onPhoneRegisterClick(View view) {
        startActivity(new Intent(this, PhoneActivity.class));
    }
}