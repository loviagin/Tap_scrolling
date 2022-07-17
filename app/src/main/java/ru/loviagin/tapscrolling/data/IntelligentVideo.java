package ru.loviagin.tapscrolling.data;

import java.util.ArrayList;

public class IntelligentVideo {

    private static int VIDEOS_COUNT = 10;
    private static final ArrayList<Integer> videos = new ArrayList<>();

    public static void setVIDEOS_COUNT(int videos_count) {
        VIDEOS_COUNT = videos_count;
    }

    public static int getRandomInt() {
        int i = 0;
        int random;
        do {
            random = (int) (Math.random() * VIDEOS_COUNT);
            i++;
        } while (videos.contains(random) && i < 50);
//        if (videos.size() == VIDEOS_COUNT){
//            return -1;
//        }
        videos.add(random);
        return random;
    }
}
