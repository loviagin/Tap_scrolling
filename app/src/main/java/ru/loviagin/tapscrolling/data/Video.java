package ru.loviagin.tapscrolling.data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.loviagin.tapscrolling.R;
import ru.loviagin.tapscrolling.activities.MainActivity;
import ru.loviagin.tapscrolling.adapters.VideoAdapter;

public class Video {
    private String video_url;
    private int video_id = 0;
    private int user_id = 1;
    private int replies_count = 0;
    private int likes_count = 0;
    private int video_views = 0;
    private String avatar_url = null;
    private List<Comment> comments_array;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    public void addVideoView() {
        video_views++;
        updateDocument("video_views", video_views);
    }

    public void addLike(ImageButton button) {
        likes_count++;
        button.setImageResource(R.drawable.ic_like_fill);
        button.setColorFilter(R.color.red);
        updateDocument("likes_count", likes_count);
    }

    public Video(VideoAdapter adapter, ViewPager2 viewPager) {
        for (int i = 0; i < 3; i++) {
            if (IntelligentVideo.getRandomInt() == -1){
                Toast.makeText(viewPager.getContext(), "Вы просмотрели все видео", Toast.LENGTH_SHORT).show();
            }else {
                this.video_id = IntelligentVideo.getRandomInt();
            }
            docRef = db.collection("videos").document(String.valueOf(video_id));
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        video_url = document.getData().get("video_url").toString();
                        try {
                            user_id = Integer.parseInt(document.getData().get("user_id").toString());
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
                            comments_array = (List<Comment>) document.getData().get("comments_array");
                        } catch (Exception ignored) {
                        }
                        try {
                            avatar_url = document.getData().get("avatar_url").toString();
                        }catch (Exception ignored){}
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
        if (viewPager.getAdapter() == null){
            viewPager.setAdapter(adapter);
        }
    }

    private void updateDocument(String field, int count) {
        DocumentReference document = db.collection("videos").document(String.valueOf(video_id));

        document
                .update(field, count)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public Video(String video_url, int video_id, int user_id, int replies_count, int likes_count, int video_views, List<Comment> comments_array, String avatar_url) {
        this.video_url = video_url;
        this.video_id = video_id;
        this.user_id = user_id;
        this.replies_count = replies_count;
        this.likes_count = likes_count;
        this.video_views = video_views;
        this.comments_array = comments_array;
        this.avatar_url = avatar_url;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getVideo_views() {
        return video_views;
    }

    public void setVideo_views(int video_views) {
        this.video_views = video_views;
    }

    public List<Comment> getComments_array() {
        return comments_array;
    }

    public void setComments_array(List<Comment> comments_array) {
        this.comments_array = comments_array;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
