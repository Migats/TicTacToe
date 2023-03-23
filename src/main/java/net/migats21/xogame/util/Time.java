package net.migats21.xogame.util;

public class Time {
    private static final float STARTING_TIME = System.nanoTime();
    private static float lastTime = getTime();
    public static float getTime() {
        return (float) ((System.nanoTime() - STARTING_TIME) * 1E-9);
    }
    public static float getDeltaTime() {
        float time = getTime();
        float deltaTime = time - lastTime;
        lastTime = time;
        return deltaTime;
    }
}
