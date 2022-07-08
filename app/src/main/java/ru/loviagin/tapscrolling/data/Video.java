package ru.loviagin.tapscrolling.data;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String video_url;
    private int video_id, user_id, reply_count, likes_count, video_views;
    private List<Comment> comments_array;

    public Video(String video_url, int video_id, int user_id, int reply_count, int likes_count, int video_views, List<Comment> comments_array) {
        this.video_url = video_url;
        this.video_id = video_id;
        this.user_id = user_id;
        this.reply_count = reply_count;
        this.likes_count = likes_count;
        this.video_views = video_views;
        this.comments_array = comments_array;
    }

    public void addVideoView(){
        video_views++;
    }

    public Video(String video_url) {
        this.video_url = video_url;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public Video() {
        comments_array = new ArrayList<>();
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
