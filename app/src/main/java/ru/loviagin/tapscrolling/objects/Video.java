package ru.loviagin.tapscrolling.objects;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Build;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.platforminfo.KotlinDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.adapters.VideoAdapter;
import ru.loviagin.tapscrolling.data.IntelligentVideo;

public class Video {
    private String video_url;
    private int video_id = 0;
    private String user_id;
    private int replies_count = 0;
    private int likes_count = 0;
    private int video_views = 0;
    private String avatar_url = null;
    private List<Comment> comments_array;
    private List<Integer> likes_videos = new ArrayList<>();
    private boolean isLike = false;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void addVideoView() {
        video_views++;
        updateDocument("video_views", video_views);
    }

    public void addLike(ImageButton button) {
        if (isLike) {
            likes_count--;
            button.setImageResource(R.drawable.ic_like);
            button.clearColorFilter();
            isLike = false;
            delLike_video();
        } else {
            likes_count++;
            button.setImageResource(R.drawable.ic_like_fill);
            button.setColorFilter(R.color.red);
            isLike = true;
            setLike_video();
        }
        updateDocument("likes_count", likes_count);
        updateDocument("likes_videos", likes_videos);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Video(VideoAdapter adapter, ViewPager2 viewPager) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user_id = mAuth.getCurrentUser().getUid();
        for (int i = 0; i < 5; i++) {
            int random = IntelligentVideo.getRandomInt();
            if (random == -1) {
                Toast.makeText(viewPager.getContext(), "Вы просмотрели все видео", Toast.LENGTH_SHORT).show();
            } else {
                this.video_id = random;
            }
            DocumentReference docRef = db.collection("videos").document(String.valueOf(video_id));
            if (currentUser != null) {

                KotlinDetector kotlinDetector = {
                    for(document in snapshot){
                        val userData=document.toObject(Users::class.java)
                        userData.userId=document.id
                        arrayList.add(userData)
                    }
                }
                    .addOnFailureListener { exception->
                        Toast.makeText(fragment.context, exception.message, Toast.LENGTH_SHORT).show()
                }}

                db.collection("users").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                            }
                        })
                        .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        try {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            likes_videos.addAll((List<Integer>) documentSnapshot.get("likes_videos"));
                                            Log.i("TAG3500", "" + likes_videos);
                                            if (likes_videos.contains(video_id)) {
                                                isLike = true;
                                            }
                                            Log.i("TAGABA", "el: 1 - " + likes_videos + "---" + video_id);
                                            for (int i1 : likes_videos) {
                                                Log.i("TAGABA", "el: " + i1);
                                            }


                                        } catch (Exception ignored) {
                                            Log.i("TAG3500", "err");
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ");
                                    }
                                }
                        );
            }

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        video_url = document.getData().get("video_url").toString();
                        try {
                            user_id = document.getData().get("user_id").toString();
                        } catch (Exception ignored) {
                        }
                        try {
                            replies_count = Integer.parseInt(document.getData().get("replies_count").toString());
                        } catch (Exception ignored) {
                        }
                        try {
                            likes_count = Integer.parseInt(document.getData().get("likes_count").toString());
                        } catch (Exception ignored) {
                        }
                        try {
                            video_views = Integer.parseInt(document.getData().get("video_views").toString());
                        } catch (Exception ignored) {
                        }
                        try {
                            comments_array.addAll((List<Comment>) document.getData().get("comments_array"));
                        } catch (Exception ignored) {
                        }
                        try {
                            avatar_url = document.getData().get("avatar_url").toString();
                        } catch (Exception ignored) {
                        }
                        adapter.addVideo(video_id, video_url, video_id, user_id, replies_count, likes_count, video_views, comments_array, avatar_url);

                        Log.i("TAG2365", "Ready");
                    } else {
                        Log.i("TAG2365", "No such document");
                    }
                } else {
                    Log.i("TAG2365", "get failed with ", task.getException());
                }
            });
        }
        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(adapter);
        }
    }

    private void updateDocument(String field, int count) {
        DocumentReference document = db.collection("videos").document(String.valueOf(video_id));
        Map<String, Object> docData = new HashMap<>();
        docData.put(field, count);

        document
                .update(docData)
                .addOnSuccessListener(aVoid -> Log.d("TAG0000000000", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    private void updateDocument(String field, List<Integer> count) {
        DocumentReference document = db.collection("users").document(String.valueOf(user_id));
        Map<String, Object> docData = new HashMap<>();
        docData.put(field, count);

        document
                .update(docData)
                .addOnSuccessListener(aVoid -> Log.d("TAG2022", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("TAG2022", "Error updating document", e));
    }

    public Video(String video_url, int video_id, String user_id, int replies_count, int likes_count, int video_views, List<Comment> comments_array, String avatar_url) {
        this.video_url = video_url;
        this.video_id = video_id;
        this.user_id = user_id;
        this.replies_count = replies_count;
        this.likes_count = likes_count;
        this.video_views = video_views;
        this.comments_array = comments_array;
        this.avatar_url = avatar_url;
    }

    private void delLike_video(){
        Iterator<Integer> itr = likes_videos.iterator();
        int strElement = 0;
        while (itr.hasNext()) {
            strElement = (int) itr.next();
            if (strElement == video_id) {
                itr.remove();
            }
        }
    }

    private void setLike_video(){
        likes_videos.add(video_id);
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getVideo_url() {
        return video_url;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public List<Comment> getComments_array() {
        return comments_array;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

}
