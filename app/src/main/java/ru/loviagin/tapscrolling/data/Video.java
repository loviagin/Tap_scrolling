package ru.loviagin.tapscrolling.data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ru.loviagin.tapscrolling.activities.MainActivity;

public class Video {
    private String video_url;
    private int video_id;
    private int user_id = 0;
    private int reply_count = 0;
    private int likes_count = 0;
    private int video_views = 0;
    private List<Comment> comments_array;
    private boolean isReady;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

//    public Video(String video_url, int video_id, int user_id, int reply_count, int likes_count, int video_views, List<Comment> comments_array) {
//        this.video_url = video_url;
//        this.video_id = video_id;
//        this.user_id = user_id;
//        this.reply_count = reply_count;
//        this.likes_count = likes_count;
//        this.video_views = video_views;
//        this.comments_array = comments_array;
//    }

    public void addVideoView(){
        video_views++;
    }

    public Video(int video_id) {
        isReady = false;
        this.video_id = video_id;
        docRef = db.collection("videos").document(String.valueOf(video_id));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    video_url = document.getData().get("video_url").toString();
                    try{
                        user_id = Integer.parseInt(document.getData().get("user_id").toString());
                    } catch (Exception ignored){}
                    try{
                        reply_count = Integer.parseInt(document.getData().get("reply_count").toString());
                    } catch (Exception ignored){}
                    try{
                        likes_count = Integer.parseInt(document.getData().get("likes_count").toString());
                    } catch (Exception ignored){}
                    try{
                        video_views = Integer.parseInt(document.getData().get("video_views").toString());
                    } catch (Exception ignored){}
                    try{
                        comments_array = (List<Comment>) document.getData().get("comments_array");
                    } catch (Exception ignored){}
                    isReady = true;
                    Log.i("TAG2365", "Ready");
                } else {
                    Log.i("TAG2365", "No such document");
                }
            } else {
                Log.i("TAG2365", "get failed with ", task.getException());
            }
        });

    }

    public int isReady(){
        if (isReady){
            return 1;
        } else {
            return 0;
        }
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

//    public Video() {
//        comments_array = new ArrayList<>();
//    }

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

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
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
}
