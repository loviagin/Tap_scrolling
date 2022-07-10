package ru.loviagin.tapscrolling.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    static int mPageLastScreen = 0;

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
        progressBar.setVisibility(View.VISIBLE);
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
//        viewPagerVideos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                try {
//                    Video video = new downloadData().execute(getRandomInt()).get();
//                    adapter.addVideo(video);
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(MainActivity.this, "i here", Toast.LENGTH_SHORT).show();
//            }
//        });
//                if (!viewPagerVideos.canScrollHorizontally(1)) {
//                    //adapter.addVideo(new downloadData().doInBackground(getRandomInt()));
//                }


        //  Log.i("TAG2365", "VIDEO COUNT " + VIDEOS_COUNT);
        //adapter.setVideos(videos);
        // adapter.notifyDataSetChanged();


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

        for (int i = 0; i < 3; i++) {
            Video video = new Video(getRandomInt());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            adapter.addVideo(video);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }
        progressBar.setVisibility(View.INVISIBLE);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Здесь мы зарегистрированы

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("videos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                VIDEOS_COUNT = 0;
                for (DocumentSnapshot document : task.getResult()) {
                    VIDEOS_COUNT++;
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    static class downloadData extends AsyncTask<Integer, Void, Video> {

        @Override
        protected Video doInBackground(Integer... integers) {
            Video video = new Video(integers[0]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return video;
        }
    }

    private int getRandomInt() {
        return (int) (Math.random() * VIDEOS_COUNT);
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