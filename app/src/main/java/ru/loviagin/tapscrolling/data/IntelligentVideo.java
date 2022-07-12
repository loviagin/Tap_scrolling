package ru.loviagin.tapscrolling.data;

public class IntelligentVideo {

    private static int VIDEOS_COUNT = 10;

    public static void setVIDEOS_COUNT(int videos_count) {
        VIDEOS_COUNT = videos_count;
    }

    public static int getRandomInt() {
        return (int) (Math.random() * VIDEOS_COUNT);
    }
}
