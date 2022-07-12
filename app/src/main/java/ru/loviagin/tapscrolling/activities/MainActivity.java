package ru.loviagin.tapscrolling.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.adapters.VideoAdapter;
import ru.loviagin.tapscrolling.data.IntelligentVideo;
import ru.loviagin.tapscrolling.data.Video;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ViewPager2 viewPagerVideos;
    private ProgressBar progressBar;
    private TextView textViewForYou;
    private TextView textViewSubscriptions;

    private VideoAdapter adapter;
    private List<Video> videos;
    static int mPageLastScreen=0;
    private static boolean isFirst = true;

//    private int VIDEOS_COUNT = 10;

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
        progressBar = findViewById(R.id.progressBar);

        videos = new ArrayList<>();
        adapter = new VideoAdapter();

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
    }

    public static boolean isFirst() {
        return isFirst;
    }

    public static void setFirst(boolean first) {
        isFirst = first;
    }

    @Override
    public void onStart() {
        super.onStart();

        new Video(adapter, viewPagerVideos, progressBar);

        viewPagerVideos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(mPageLastScreen!=position)
                {
                    Log.d("page scrolled", "Page scrolled");
                    new Video(adapter, viewPagerVideos, progressBar);
                    mPageLastScreen=position;
                }
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Здесь мы зарегистрированы
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateVideoCount();
    }

//    class starting extends AsyncTaskLoader<Integer> {
//
//        public starting(@NonNull Context context) {
//            super(context);
//        }
//
//        @Nullable
//        @Override
//        public Integer loadInBackground() {
//
//            return null;
//        }
//    }
//
//    static class downloadData extends AsyncTask<Integer, Void, Video> {
//
//        @Override
//        protected Video doInBackground(Integer... integers) {
//            Video video = new Video(integers[0]);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return video;
//        }
//    }

    public void updateVideoCount(){
        db.collection("videos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int VIDEOS_COUNT = 0;
                for (DocumentSnapshot ignored : task.getResult()) {
                    VIDEOS_COUNT++;
                }
                IntelligentVideo.setVIDEOS_COUNT(VIDEOS_COUNT);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
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
        startActivity(new Intent(this, NotificationsActivity.class));
    }

    public void onProfileClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }


}