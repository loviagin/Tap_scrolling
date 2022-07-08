package ru.loviagin.tapscrolling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.adapters.VideoAdapter;
import ru.loviagin.tapscrolling.data.Comment;
import ru.loviagin.tapscrolling.data.Video;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //private DocumentReference docRef;

    private ViewPager2 viewPagerVideos;
    private ProgressBar progressBar;

    private VideoAdapter adapter;
    private List<Video> videos;

    private int VIDEOS_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //docRef = db.collection("videos").document("fef");

        viewPagerVideos = findViewById(R.id.viewPagerVideos);

        videos = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comments.add(comment);
        adapter = new VideoAdapter();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        db.collection("videos").orderBy("id").addSnapshotListener((value, error) -> {
            if (value != null) {
                for (int i = 0; i < 10; i++) {
                    Video video = new Video(value.getDocuments().get(i).get("video_url").toString());
                    Log.i("MYTAG5", "THERE is here");
                    adapter.addVideo(video);
                    Log.i("MYTAG2", value.getDocuments().get(i).get("video_url").toString());
                   // Log.i("MYTAG3", String.valueOf(Integer.parseInt(value.getDocuments().get(i).get("id").toString())));
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        //adapter.setVideos(videos);

        viewPagerVideos.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Здесь мы зарегистрированы
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("videos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    VIDEOS_COUNT = value.size();
                }
                //Toast.makeText(MainActivity.this, "count + " + VIDEOS_COUNT, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRandomInt() {
        return (int) (Math.random() * ++VIDEOS_COUNT);
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