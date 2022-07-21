package ru.loviagin.tapscrolling.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.objects.User;

public class RegisterContinueActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private ImageView imageView;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference reference;
    private FirebaseAuth mAuth;

    private String avatar_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_continue);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        imageView = findViewById(R.id.imageViewUploadAvatar);
        progressBar = findViewById(R.id.progressBarAuthCon);
    }

    public void onClickRegister(View view) {
        Intent intent = getIntent();
        String email = "";
        String phone = "";
        String name = "";
        String idToken = null;
        if (intent.hasExtra("email")) {
            email = intent.getStringExtra("email");
        }
        if (intent.hasExtra("email")) {
            phone = intent.getStringExtra("phone");
        }
        if (intent.hasExtra("idToken")) {
            idToken = intent.getStringExtra("idToken");
        }
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name");
        }
        String finalEmail = email;
        String finalPhone = phone;
        String finalIdToken = idToken;
        String finalName = name;
        db.collection("users")
                .whereEqualTo("username", editTextUsername.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = 0;
                        //  Log.i("TAG3545", "" + count);
                        for (DocumentSnapshot document : task.getResult()) {
                            count++;
                            //       Log.i("TAG3545", "" + document);
                        }
                        if (count == 0) {
                            if (editTextUsername.getText().toString().trim().isEmpty()) {
                                createAccount(finalEmail, avatar_url, "user", finalPhone, finalIdToken, finalName);
                            } else {
                                createAccount(finalEmail, avatar_url, editTextUsername.getText().toString().trim(), finalPhone, finalIdToken, finalName);
                            }

                            startActivity(new Intent(RegisterContinueActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegisterContinueActivity.this, "Этот ник уже занят", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    private void createAccount(String email, String avatar_url, String username, String phone, String idToken, String name) {
        User user;
        if (idToken != null) {
            user = new User(email, avatar_url, username, phone, idToken, name);
        } else {
            user = new User(email, avatar_url, username, phone, name);
        }

        Log.i("TAG3545", "" + user);
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user).addOnCompleteListener(task -> Log.i("exc", "Created account"));
    }

    private final ActivityResultLauncher<Intent> startActivityGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == RESULT_OK && intent != null) {
                        final Uri uri = intent.getData();
                        if (uri != null) {
                            final StorageReference referenceImages = reference.child("images/" + uri.getLastPathSegment());
                            referenceImages.putFile(uri).continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return referenceImages.getDownloadUrl();
                            }).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    if (downloadUri != null) {
                                        avatar_url = downloadUri.toString();
                                        Picasso.get().load(downloadUri).into(imageView);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                }
            }
    );

    public void onAvatarUploadClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityGetContent.launch(intent);
    }
}