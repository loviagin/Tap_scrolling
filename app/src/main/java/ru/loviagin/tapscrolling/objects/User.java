package ru.loviagin.tapscrolling.objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
    //private int Uid;
    private String email;
    private String phone;
    private String avatar_url;
    private String username;
    private String name;
    private String idToken;
    private List<Integer> likes_videos;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User(String email, String avatar_url, String username, String phone, String name) {
        this.email = email;
        this.avatar_url = avatar_url;
        this.username = username;
        this.phone = phone;
        this.name = name;
        this.likes_videos = new ArrayList<>();
    }

    public User(String email, String avatar_url, String username, String phone, String idToken, String name) {
        this.email = email;
        this.avatar_url = avatar_url;
        this.username = username;
        this.phone = phone;
        this.name = name;
        this.idToken = idToken;
        this.likes_videos = new ArrayList<>();
    }

//    private void setup(int video_id) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//                DocumentReference usrRef = db.collection("users").document(currentUser.getUid());
//                usrRef.get()
//                        .addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot documentSnapshot = task.getResult();
//                                        User user = documentSnapshot.toObject(User.class);
//                                        if (user.get(0).getLikes_videos().contains(video_id)){
//
//                                        }
//                                    } else {
//                                        Log.d(TAG, "Error getting documents: ");
//                                    }
//                                }
//                        );
//            }
//    }

    public List<Integer> getLikes_videos() {
        return likes_videos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
