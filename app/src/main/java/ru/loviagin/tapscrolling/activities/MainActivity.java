package ru.loviagin.tapscrolling.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.adapters.VideoAdapter;
import ru.loviagin.tapscrolling.data.Video;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //private DocumentReference docRef;

    private ViewPager2 viewPagerVideos;
    private ProgressBar progressBar;
    private TextView textViewForYou, textViewSubscriptions;

    private VideoAdapter adapter;
    private List<Video> videos;

    private int VIDEOS_COUNT = 10;
    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        viewPagerVideos = findViewById(R.id.viewPagerVideos);
        textViewForYou = findViewById(R.id.textViewForYou);
        textViewSubscriptions = findViewById(R.id.textViewSubscriptions);

        videos = new ArrayList<>();
        adapter = new VideoAdapter();
        progressBar = findViewById(R.id.progressBar);
        viewPagerVideos.setAdapter(adapter);
//
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.INVISIBLE);

        //здесь Для Вас и Подписки
//        textViewSubscriptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Subscriptions", Toast.LENGTH_SHORT).show();
//            }
//        });
//        textViewForYou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "For you", Toast.LENGTH_SHORT).show();
//            }
//        });
        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < 10; i++) {
            Video video = new Video(getRandomInt());
            Toast.makeText(this, "Int  " + i + ", Video " + video.getVideo_url(), Toast.LENGTH_SHORT).show();
//            Log.i("TAG2365", "Int  " + i + ", Video " + video.getVideo_url());
            /*while (video.isReady() != 1){

            }*/
            adapter.addVideo(video);
            adapter.notifyDataSetChanged();
            //adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }
        progressBar.setVisibility(View.INVISIBLE);
        Log.i("TAG2365", "VIDEO COUNT " + VIDEOS_COUNT);
        //adapter.setVideos(videos);

    }

    //    private void addVideo(String video_url){
//        Video video = new Video(video_url);
//        Log.i("MYTAG5", "THERE is here");
//        adapter.addVideo(video);
//        Log.i("MYTAG2", video_url);
//    }

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
                VIDEOS_COUNT = 0;
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    try {
                        throw new Exception("Ошибка обновления количества видео");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<Integer> integers = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("name") != null) {
                        VIDEOS_COUNT++;
                        integers.add(Integer.parseInt(doc.get("likes_count").toString()));
                    }
                }
                Log.i("integers", String.valueOf(integers));

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