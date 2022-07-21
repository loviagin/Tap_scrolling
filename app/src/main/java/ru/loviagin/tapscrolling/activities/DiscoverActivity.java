package ru.loviagin.tapscrolling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ru.loviagin.tapscrolling.R;

public class DiscoverActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SearchView searchView;
    private ListView listViewSearchResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> searches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        searchView = findViewById(R.id.searchView);
        searches = new ArrayList<>();
        listViewSearchResults = findViewById(R.id.listViewSearchResults);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searches);
        listViewSearchResults.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery("", false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                db.collection("users")
                        .whereEqualTo("username", s)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                int count = 0;
                                searches.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                    try {
                                        searches.add(String.format("%s - %s", document.get("username").toString(), document.get("name").toString()));
                                    } catch (Exception ignored) {
                                    }
                                    Log.i("TAG3545", "" + document);
                                }
                                listViewSearchResults.setAdapter(adapter);
                                Log.i("TAG3545", "" + count);
                            } else {
                                Log.d("TAG3545", "Error getting documents: ", task.getException());
                            }
                        });
                return false;
            }
        });
    }


    public void onHomeClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onDiscoverClick(View view) {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    public void onNotificationsClick(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, NotificationsActivity.class));
        } else {
            startActivity(new Intent(this, AuthorizeActivity.class));
        }
    }

    public void onRecordButtonClick(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, RecordActivity.class));
        } else {
            startActivity(new Intent(this, AuthorizeActivity.class));
        }
    }

    public void onProfileClick(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else {
            startActivity(new Intent(this, AuthorizeActivity.class));
        }
    }
}