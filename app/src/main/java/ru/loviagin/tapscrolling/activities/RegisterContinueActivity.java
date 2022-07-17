package ru.loviagin.tapscrolling.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.data.User;

public class RegisterContinueActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private ImageView imageView;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference reference;

    private String avatar_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_continue);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        editTextUsername = findViewById(R.id.editTextUsername);
        imageView = findViewById(R.id.imageViewUploadAvatar);
    }

    public void onClickRegister(View view) {
        Intent intent = getIntent();
        String email = "";
        String pass = "";
        if (intent.hasExtra("email") && intent.hasExtra("password")) {
            email = intent.getStringExtra("email");
            pass = intent.getStringExtra("password");
        }
        String finalEmail = email;
        String finalPass = pass;
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
                            if (!editTextUsername.getText().toString().trim().isEmpty() && avatar_url == null) {
                                createAccount(finalEmail, null, editTextUsername.getText().toString().trim(), finalPass);
                            } else if (editTextUsername.getText().toString().trim().isEmpty() && avatar_url != null) {
                                createAccount(finalEmail, avatar_url, "user", finalPass);
                            } else if (editTextUsername.getText().toString().trim().isEmpty() && avatar_url == null) {
                                createAccount(finalEmail, null, "user", finalPass);
                            } else {
                                createAccount(finalEmail, avatar_url, editTextUsername.getText().toString().trim(), finalPass);
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

    private void createAccount(String email, String avatar_url, String username, String pass) {
        db.collection("users").add(new User(email, avatar_url, username, pass)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });
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
                                    }
                                }
                            });
                        }
                    }
                }
            }
    );

    public void onAvatarUploadClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityGetContent.launch(intent);
    }
}