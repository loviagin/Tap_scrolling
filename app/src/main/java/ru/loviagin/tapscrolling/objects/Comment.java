package ru.loviagin.tapscrolling.objects;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private int countOfComments;
    private Map<String, String> comments;

    public Comment(int countOfComments, Map<String, String> comments) {
        this.countOfComments = countOfComments;
        this.comments = comments;
    }

    public Comment(){
        countOfComments = 0;
        comments = new HashMap<>();
        comments.put("key0", "vvuirjed");
    }

    public int getCountOfComments() {
        return countOfComments;
    }

    public void setCountOfComments(int countOfComments) {
        this.countOfComments = countOfComments;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }
}
